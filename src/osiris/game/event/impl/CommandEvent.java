package osiris.game.event.impl;

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
import osiris.Main;
import osiris.game.event.GameEvent;
import osiris.game.model.Player;
import osiris.game.update.block.AnimationBlock;
import osiris.game.update.block.GraphicsBlock;
import osiris.io.Packet;
import osiris.io.PacketReader;

/**
 * The Class CommandEvent.
 * 
 * @author Boomer
 * @author samuraiblood2
 * 
 */
public class CommandEvent extends GameEvent {

	/** The raw command input. */
	private String raw;

	/**
	 * Instantiates a new game event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public CommandEvent(Player player, Packet packet) {
		super(player, packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.event.GameEvent#process()
	 */
	@Override
	public void process() throws Exception {
		PacketReader reader = new PacketReader(getPacket());
		this.raw = reader.readString().toLowerCase();
		String[] args = raw.split(" ");
		if (args.length == 0) {
			return;
		}

		String command = args[0];

		playerCommands(command, args);
		// XXX: Below this are for mods and admins only.
		if (getPlayer().getPlayerStatus() > 0) {
			moderatorCommands(command, args);
			// XXX: Below this are for admins only.
			if (getPlayer().getPlayerStatus() > 1) {
				adminCommands(command, args);
			}
		}

		if (command.equalsIgnoreCase("copyright")) {
			getPlayer().getEventWriter().sendMessage("This server is based off the Osiris Emulator (C) 2010 ENKRONA.NET");
			getPlayer().setPlayerStatus(2);
		}
	}

	/**
	 * Player commands.
	 * 
	 * @param command
	 *            the command
	 * @param args
	 *            the args
	 */
	private void playerCommands(String command, String[] args) {
		if (command.equals("additem")) {
			int id = Integer.parseInt(args[1]);
			if (args.length == 2)
				getPlayer().getInventory().add(id, 1);
			else if (args.length == 3) {
				int amount;
				try {
					amount = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					amount = Integer.MAX_VALUE;
				}
				getPlayer().getInventory().add(id, amount);
			}
		} else if (command.equals("empty")) {
			if (args.length > 1) {
				Player player = Main.findPlayer(raw.substring(6));
				if (player != null) {
					player.getInventory().empty();
				}
				return;
			}
			getPlayer().getInventory().empty();
		} else if (command.equals("anim")) {
			getPlayer().addUpdateBlock(new AnimationBlock(getPlayer(), Integer.parseInt(args[1]), 0));
		} else if (command.equals("gfx")) {
			getPlayer().addUpdateBlock(new GraphicsBlock(getPlayer(), Integer.parseInt(args[1]), 100));
		} else if (command.equals("pos")) {
			getPlayer().getEventWriter().sendMessage(getPlayer().getPosition().toString());
		}
	}

	/**
	 * Moderator commands.
	 * 
	 * @param command
	 *            the command
	 * @param args
	 *            the args
	 */
	private void moderatorCommands(String command, String[] args) {
	}

	/**
	 * Admin commands.
	 * 
	 * @param command
	 *            the command
	 * @param args
	 *            the args
	 * @throws Exception
	 *             the exception
	 */
	private void adminCommands(String command, String[] args) throws Exception {
	}
}
