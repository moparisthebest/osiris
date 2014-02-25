package osiris.game.update;

/*
 * Osiris Emulator
 * Copyright (C) 2011  Garrett Woodard, Blake Beaupain, Travis Burtrum
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import osiris.Main;
import osiris.game.model.Direction;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.game.update.UpdateFlags.UpdateFlag;
import osiris.game.update.block.AppearanceBlock;
import osiris.io.ByteForm;
import osiris.io.PacketHeader;
import osiris.io.PacketWriter;
import osiris.io.PacketHeader.LengthType;
import osiris.io.PacketWriter.AccessType;

/**
 * Updates a Player.
 * 
 * @author Blake
 * 
 */
public class PlayerUpdater implements Runnable {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Instantiates a new player updater.
	 * 
	 * @param player
	 *            the player
	 */
	public PlayerUpdater(Player player) {
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#cycle()
	 */

	@Override
	public void run() {
		try {
			// Update the new map region if necessary.
			Position current = player.getPosition();
			Position last = player.getLastRegion();
			int deltaX = current.getX() - (last.getRegionX() - 6) * 8;
			int deltaY = current.getY() - (last.getRegionY() - 6) * 8;
			if (!(deltaX >= 16 && deltaX < 88 && deltaY >= 16 && deltaY < 88)) {
				player.getEventWriter().sendLandscape();
				player.getUpdateFlags().flag(UpdateFlag.TELEPORTED);
				player.getUpdateFlags().flag(UpdateFlag.NO_MOVEMENT_QUEUE_RESET);
				player.setLastRegion(current);
			}

			PacketWriter writer = new PacketWriter(new PacketHeader(216, LengthType.VARIABLE_SHORT));
			PacketWriter blockWriter = new PacketWriter();
			UpdateFlags f = player.getUpdateFlags();

			writer.setAccessType(AccessType.BIT_ACCESS);

			// Update this player.
			if (!f.isFlagged(UpdateFlag.UPDATE)) {
				writer.writeBit(false);
			} else {
				writer.writeBit(true);
				updateMyMovement(player, writer);
				if (player.hasUpdateBlocks()) {
					updateBlocks(player, blockWriter, false);
				}
			}

			// Update local players.
			writer.writeBits(8, player.getLocalPlayers().size());
			for (Iterator<Player> it = player.getLocalPlayers().iterator(); it.hasNext();) {
				Player other = it.next();
				if (Main.getPlayers().contains(other) && player.isInSight(other) && !other.getUpdateFlags().isFlagged(UpdateFlag.TELEPORTED) && other.isVisible()) {
					UpdateFlags oF = other.getUpdateFlags();
					if (!oF.isFlagged(UpdateFlag.UPDATE)) {
						writer.writeBit(false);
					} else {
						writer.writeBit(true);
						updateMovement(other, writer);
						if (other.hasUpdateBlocks()) {
							updateBlocks(other, blockWriter, false);
						}
					}
				} else {
					it.remove();
					writer.writeBit(true);
					writer.writeBits(2, 3);
				}
			}

			int addedCount = 0;
			for (Player other : Main.getPlayers()) {
				if (addedCount == 15 || player.getLocalPlayers().size() >= 255) {
					break;
				}
				if (player.getLocalPlayers().contains(other) || other == player || !other.isVisible()) {
					continue;
				}
				if (player.isInSight(other)) {
					addedCount++;
					player.getLocalPlayers().add(other);
					addPlayer(player, other, writer);
					updateBlocks(other, blockWriter, true);
				}
			}

			if (blockWriter.getPacket().getBuffer().writerIndex() > 0) {
				writer.writeBits(11, 2047);
				writer.setAccessType(AccessType.BYTE_ACCESS);
				writer.getPacket().getBuffer().writeBytes(blockWriter.getPacket().getBuffer());
			} else {
				writer.setAccessType(AccessType.BYTE_ACCESS);
			}

			player.getChannel().write(writer.getPacket());
			writer = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Adds the player.
	 * 
	 * @param player
	 *            the player
	 * @param other
	 *            the other
	 * @param writer
	 *            the writer
	 */
	private void addPlayer(Player player, Player other, PacketWriter writer) {
		int deltaX = other.getPosition().getX() - player.getPosition().getX();
		int deltaY = other.getPosition().getY() - player.getPosition().getY();
		if (deltaX < 0) {
			deltaX += 32;
		}
		if (deltaY < 0) {
			deltaY += 32;
		}
		writer.writeBits(11, other.getSlot());
		writer.writeBits(5, deltaX);
		writer.writeBit(true);
		writer.writeBits(3, 1);
		writer.writeBit(true);
		writer.writeBits(5, deltaY);
	}

	/**
	 * Update blocks.
	 * 
	 * @param player
	 *            the player
	 * @param writer
	 *            the writer
	 * @param forceAppearance
	 *            the force appearance
	 * @throws Exception
	 *             the exception
	 */
	private void updateBlocks(Player player, PacketWriter writer, boolean forceAppearance) throws Exception {
		List<UpdateBlock> blocks = player.getUpdateBlocks();

		// Check if we need to force appearance.
		if (forceAppearance) {
			boolean needsAppearanceBlock = true;
			for (UpdateBlock block : blocks) {
				if (block instanceof AppearanceBlock) {
					needsAppearanceBlock = false;
					break;
				}
			}

			// Force the block if it's not present.
			if (needsAppearanceBlock) {
				// / Rebuild a new list to keep updating read-only and thread
				// safe.
				blocks = new LinkedList<UpdateBlock>();
				blocks.addAll(player.getUpdateBlocks());
				blocks.add(new AppearanceBlock(player));
			}
		}

		// Sort the update blocks.
		Collections.sort(blocks, UpdateBlockComparator.getSingleton());

		// Compile the mask value.
		int mask = 0;
		for (UpdateBlock block : blocks) {
			mask |= block.getMask();
		}

		// Write the mask value.
		if (mask >= 0x100) {
			mask |= 0x10;
			writer.writeShort(mask, ByteForm.LITTLE);
		} else {
			writer.writeByte(mask);
		}

		// Append each block.
		for (UpdateBlock block : blocks) {
			// System.out.println("Appending update block: " + block);
			block.append(writer);
		}
		// System.out.println("END OF UPDATE BLOCKS");
	}

	/**
	 * Update movement.
	 * 
	 * @param player
	 *            the player
	 * @param writer
	 *            the writer
	 */
	private void updateMovement(Player player, PacketWriter writer) {
		if (player.getPrimaryDirection() == Direction.NONE) {
			writer.writeBits(2, 0);
		} else if (player.getSecondaryDirection() == Direction.NONE) {
			writer.writeBits(2, 1);
			writer.writeBits(3, player.getPrimaryDirection().toInteger());
			writer.writeBit(player.hasUpdateBlocks());
		} else {
			writer.writeBits(2, 2);
			writer.writeBits(3, player.getPrimaryDirection().toInteger());
			writer.writeBits(3, player.getSecondaryDirection().toInteger());
			writer.writeBit(player.hasUpdateBlocks());
		}
	}

	/**
	 * Update my movement.
	 * 
	 * @param player
	 *            the player
	 * @param writer
	 *            the writer
	 */
	private void updateMyMovement(Player player, PacketWriter writer) {
		UpdateFlags f = player.getUpdateFlags();
		if (f.isFlagged(UpdateFlag.TELEPORTED)) {
			writer.writeBits(2, 3);
			writer.writeBits(7, player.getPosition().getLocalX(player.getLastRegion()));
			writer.writeBit(!player.getUpdateFlags().isFlagged(UpdateFlag.NO_MOVEMENT_QUEUE_RESET));
			writer.writeBits(2, player.getPosition().getZ());
			writer.writeBit(player.hasUpdateBlocks());
			writer.writeBits(7, player.getPosition().getLocalY(player.getLastRegion()));
		} else {
			if (player.getPrimaryDirection() == Direction.NONE) {
				writer.writeBits(2, 0);
			} else if (player.getSecondaryDirection() == Direction.NONE) {
				writer.writeBits(2, 1);
				writer.writeBits(3, player.getPrimaryDirection().toInteger());
				writer.writeBit(player.hasUpdateBlocks());
			} else {
				writer.writeBits(2, 2);
				writer.writeBits(3, player.getPrimaryDirection().toInteger());
				writer.writeBits(3, player.getSecondaryDirection().toInteger());
				writer.writeBit(player.hasUpdateBlocks());
			}
		}
	}

	/**
	 * Gets the player.
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

}
