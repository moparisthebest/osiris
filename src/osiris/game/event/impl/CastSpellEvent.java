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
import osiris.io.ByteForm;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.ValueType;

// TODO: Auto-generated Javadoc
/**
 * The Class CastSpellEvent.
 * 
 * @author Boomer
 */
public class CastSpellEvent extends GameEvent {
	/**
	 * Instantiates a new game event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public CastSpellEvent(Player player, Packet packet) {
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
		int characterSlot, buttonId, interfaceId;
		Character character;
		if (getPacket().getHeader().getOpcode() == 24) {
			characterSlot = reader.readShort(false, ValueType.A);
			buttonId = reader.readShort(false);
			interfaceId = reader.readShort(false);
			character = Main.getNpcs().get(characterSlot);
		} else {
			reader.readShort(ByteForm.LITTLE);
			characterSlot = reader.readShort(ByteForm.LITTLE);
			interfaceId = reader.readShort(false);
			buttonId = reader.readShort(true);
			character = Main.getPlayers().get(characterSlot);
		}
		getPlayer().getMovementQueue().reset();
		if (getPlayer().getSpellBook().getInterfaceId() != interfaceId)
			return;
		if (character == null)
			return;

		if (character instanceof Npc) {
			if (((Npc) character).getCombatDef().getMaxHp() == 0)
				return;
		}
		Character attacking = getPlayer().getInteractingCharacter();
		if (getPlayer().getCombatManager().combatEnabled() && getPlayer().getCombatManager().getTempSpell() != null && attacking != null && attacking.equals(character))
			return;
		getPlayer().getCombatManager().setCombatAction(new CombatAction(getPlayer(), character, buttonId));
	}
}