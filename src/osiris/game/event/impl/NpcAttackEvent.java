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
import osiris.game.event.GameEvent;
import osiris.game.model.Character;
import osiris.game.model.Npc;
import osiris.game.model.Player;
import osiris.io.Packet;
import osiris.io.PacketReader;

// TODO: Auto-generated Javadoc
/**
 * The Class NpcAttackEvent.
 * 
 * @author Boomer
 */
public class NpcAttackEvent extends GameEvent {

	/**
	 * Instantiates a new game event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public NpcAttackEvent(Player player, Packet packet) {
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
		case 123:
			int npcSlot = new PacketReader(getPacket()).readShort(false);
			final Npc npc = (Npc) Main.getNpcs().get((npcSlot));
			if (npc == null)
				return;
			Character attacking = getPlayer().getInteractingCharacter();
			if (getPlayer().getCombatManager().combatEnabled() && attacking != null && attacking.equals(npc))
				return;
			getPlayer().getCombatManager().setCombatAction(new CombatAction(getPlayer(), npc));
			break;
		}
	}

}
