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
import osiris.game.action.impl.BankAction;
import osiris.game.action.impl.DialogueAction;
import osiris.game.event.GameEvent;
import osiris.game.model.Npc;
import osiris.game.model.Player;
import osiris.game.model.dialogues.Dialogue;
import osiris.game.model.skills.Fishing;
import osiris.io.ByteForm;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.ValueType;

// TODO: Auto-generated Javadoc
/**
 * The Class NpcOptionEvent.
 * 
 * @author Blake Beaupain
 */
public class NpcOptionEvent extends GameEvent {

	/**
	 * Instantiates a new game event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public NpcOptionEvent(Player player, Packet packet) {
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
		int npcSlot = -1;
		switch (getPacket().getHeader().getOpcode()) {
		case 7: // First option
			npcSlot = reader.readShort(false, ValueType.A);
			Npc npc = (Npc) Main.getNpcs().get(npcSlot);
			if (npc == null) {
				break;
			}
			Fishing.FishingSpot spot = Fishing.getFishingSpot(npc.getId(), 0);
			if (spot != null) {
				Fishing.startFishing(getPlayer(), npc.getPosition(), spot);
				return;
			}
			for (Dialogue dialogue : Main.getDialogues()) {
				if (dialogue != null && dialogue.getNpc() == npc.getId() && dialogue.getId() == 1) {
					new DialogueAction(getPlayer(), dialogue).run();
					break;
				}
			}

			break;
		case 199:
			npcSlot = reader.readShort(false, ByteForm.LITTLE);
			npc = (Npc) Main.getNpcs().get(npcSlot);
			if (npc == null)
				break;
			break;
		case 52: // Second option
			npcSlot = reader.readShort(false, ValueType.A, ByteForm.LITTLE);
			npc = (Npc) Main.getNpcs().get(npcSlot);
			if (npc == null)
				break;
			spot = Fishing.getFishingSpot(npc.getId(), 1);
			if (spot != null) {
				Fishing.startFishing(getPlayer(), npc.getPosition(), spot);
				return;
			}
			break;
		}
	}

}
