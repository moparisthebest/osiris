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
import java.util.List;

import osiris.Main;
import osiris.game.model.Direction;
import osiris.game.model.Npc;
import osiris.game.model.Player;
import osiris.game.update.UpdateFlags.UpdateFlag;
import osiris.io.PacketHeader;
import osiris.io.PacketWriter;
import osiris.io.PacketHeader.LengthType;
import osiris.io.PacketWriter.AccessType;

// TODO: Auto-generated Javadoc

/**
 * Updates NPCs for a player.
 * 
 * @author Blake
 * 
 */
public class NpcUpdater extends PlayerUpdater {

	/**
	 * Instantiates a new npc updater.
	 * 
	 * @param player
	 *            the player
	 */
	public NpcUpdater(Player player) {
		super(player);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.update.PlayerUpdater#cycle()
	 */

	@Override
	public void run() {
		try {
			Player player = getPlayer();
			PacketWriter writer = new PacketWriter(new PacketHeader(222, LengthType.VARIABLE_SHORT));
			PacketWriter blockWriter = new PacketWriter();

			writer.setAccessType(AccessType.BIT_ACCESS);

			// Update local NPCs.
			writer.writeBits(8, player.getLocalNpcs().size());
			for (Iterator<Npc> it = player.getLocalNpcs().iterator(); it.hasNext();) {
				Npc other = it.next();
				if (Main.getNpcs().contains(other) && player.isInSight(other) && !other.getUpdateFlags().isFlagged(UpdateFlag.TELEPORTED) && other.isVisible()) {
					UpdateFlags f = other.getUpdateFlags();
					if (!f.isFlagged(UpdateFlag.UPDATE)) {
						writer.writeBit(false);
					} else {
						writer.writeBit(true);
						updateMovement(other, writer);
						if (other.hasUpdateBlocks()) {
							updateBlocks(other, blockWriter);
						}
					}
				} else {
					it.remove();
					writer.writeBit(true);
					writer.writeBits(2, 3);
					other.getLocalPlayers().remove(player);
				}
			}

			// Update the local NPC list itself.
			int addedCount = 0;
			for (Npc npc : Main.getNpcs()) {
				if (addedCount == 15 || player.getLocalNpcs().size() >= 255) {
					break;
				}
				if (!npc.isVisible()) {
					continue;
				}
				if (player.getLocalNpcs().contains(npc)) {
					continue;
				}
				if (player.isInSight(npc)) {
					addedCount++;
					player.getLocalNpcs().add(npc);
					addNpc(player, npc, writer);
					npc.getLocalPlayers().add(player);
					if (npc.hasUpdateBlocks()) {
						updateBlocks(npc, blockWriter);
					}
				}
			}

			if (blockWriter.getPacket().getBuffer().writerIndex() > 0) {
				writer.writeBits(15, 32767);
				writer.setAccessType(AccessType.BYTE_ACCESS);
				writer.getPacket().getBuffer().writeBytes(blockWriter.getPacket().getBuffer());
			} else {
				writer.setAccessType(AccessType.BYTE_ACCESS);
			}

			player.getChannel().write(writer.getPacket());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Adds the npc.
	 * 
	 * @param player
	 *            the player
	 * @param npc
	 *            the npc
	 * @param writer
	 *            the writer
	 */
	private void addNpc(Player player, Npc npc, PacketWriter writer) {
		int deltaX = npc.getPosition().getX() - player.getPosition().getX();
		int deltaY = npc.getPosition().getY() - player.getPosition().getY();
		if (deltaX < 0) {
			deltaX += 32;
		}
		if (deltaY < 0) {
			deltaY += 32;
		}

		writer.writeBits(15, npc.getSlot());
		writer.writeBits(14, npc.getId());
		writer.writeBit(npc.hasUpdateBlocks());
		writer.writeBits(5, deltaY);
		writer.writeBits(5, deltaX);
		writer.writeBits(3, 0);
		writer.writeBit(true);
	}

	/**
	 * Update movement.
	 * 
	 * @param npc
	 *            the npc
	 * @param writer
	 *            the writer
	 */
	private void updateMovement(Npc npc, PacketWriter writer) {
		if (npc.getPrimaryDirection() == Direction.NONE) {
			writer.writeBits(2, 0);
		} else {
			writer.writeBits(2, 1);
			writer.writeBits(3, npc.getPrimaryDirection().toInteger());
			writer.writeBit(npc.hasUpdateBlocks());
		}
	}

	/**
	 * Update blocks.
	 * 
	 * @param npc
	 *            the npc
	 * @param writer
	 *            the writer
	 * @throws Exception
	 *             the exception
	 */
	private void updateBlocks(Npc npc, PacketWriter writer) throws Exception {
		List<UpdateBlock> blocks = npc.getUpdateBlocks();

		// Sort the update blocks.
		Collections.sort(blocks, UpdateBlockComparator.getSingleton());

		// Compile the mask value.
		int mask = 0;
		for (UpdateBlock block : blocks) {
			mask |= block.getMask();
		}

		// Write the mask value.
		writer.writeByte(mask);

		// Append each block.
		for (UpdateBlock block : blocks) {
			block.append(writer);
		}
	}

}
