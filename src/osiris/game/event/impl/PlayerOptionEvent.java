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
import osiris.game.action.impl.CombatAction;
import osiris.game.action.impl.TradeAction;
import osiris.game.event.GameEvent;
import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.item.Trade;
import osiris.io.ByteForm;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.ValueType;

// TODO: Auto-generated Javadoc
/**
 * The Class PlayerOptionEvent.
 * 
 * @author Boomer
 */
public class PlayerOptionEvent extends GameEvent {
	/**
	 * Instantiates a new game event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public PlayerOptionEvent(Player player, Packet packet) {
		super(player, packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.event.GameEvent#process()
	 */
	@Override
	public void process() throws Exception {
		switch (getPacket().getHeader().getOpcode()) {
		case 160:
			int playerSlot = new PacketReader(getPacket()).readShort(false, ByteForm.LITTLE);
			Player character = null;
			if (playerSlot <= Main.getPlayers().size())
				character = (Player) Main.getPlayers().get((playerSlot));
			if (character == null)
				return;
			Character attacking = getPlayer().getInteractingCharacter();
			if (getPlayer().getCombatManager().combatEnabled() && attacking != null && attacking.equals(character))
				return;
			getPlayer().getCombatManager().setCombatAction(new CombatAction(getPlayer(), character));
			break;
		case 253:
			int pIndex = new PacketReader(getPacket()).readShort(false, ValueType.A, ByteForm.LITTLE);
			Player other = (Player) Main.getPlayers().get(pIndex);
			if (getPlayer().getCurrentAction() != null)
				getPlayer().getCurrentAction().cancel();
			if (getPlayer().getTradeRequest() != null && getPlayer().getTradeRequest().equals(other))
				return;
			if (other.getCurrentAction() != null && other.getCurrentAction() instanceof TradeAction) {
				getPlayer().getEventWriter().sendMessage("That player is busy right now.");
				return;
			} else if (other.getTradeRequest() != null && other.getTradeRequest().equals(getPlayer())) {
				Trade trade = new Trade(new Player[] { other, getPlayer() });
				new TradeAction(getPlayer(), trade);
				new TradeAction(other, trade);

			} else {
				getPlayer().getEventWriter().sendMessage("Sending trade request...");
				other.getEventWriter().sendMessage(getPlayer().getUsername() + ":tradereq:");
				getPlayer().setTradeRequest(other);
			}
			break;
		}
	}
}
