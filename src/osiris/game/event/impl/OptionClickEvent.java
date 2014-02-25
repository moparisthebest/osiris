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
import osiris.game.action.impl.DialogueAction;
import osiris.game.event.GameEvent;
import osiris.game.model.Player;
import osiris.game.model.dialogues.Dialogue;
import osiris.game.model.skills.Fletching;
import osiris.io.ByteForm;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.util.RubyInterpreter;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionClickEvent.
 * 
 * @author samuraiblood2
 * 
 */
public class OptionClickEvent extends GameEvent {

	/**
	 * Instantiates a new option click event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public OptionClickEvent(Player player, Packet packet) {
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
		reader.readShort();
		int id = reader.readShort(ByteForm.LITTLE);
		reader.readShort();
		switch (id) {

		case 4:
		case 3:
		case 2:
			doDialogueOption(id);
			break;

		// Option 1
		case 8: // 1
			Fletching.fletchBow(getPlayer(), 0, 1);
			break;

		case 7: // 5
			if (dialogueHook()) {
				break;
			} else {
				Fletching.fletchBow(getPlayer(), 0, 5);
			}
			break;

		case 6: // 10
			if (dialogueHook()) {
				break;
			} else {
				Fletching.fletchBow(getPlayer(), 0, 10);
			}
			break;

		case 5: // X
			if (dialogueHook()) {
				break;
			} else {
				// Fletching.fletchBow(getPlayer(),
				// Item.create(Fletching.getLog().getGive()[0]), );
			}
			break;

		// Option 2
		case 12: // 1
			Fletching.fletchBow(getPlayer(), 1, 1);
			break;

		case 11: // 5
			Fletching.fletchBow(getPlayer(), 1, 5);
			break;

		case 10: // 10
			Fletching.fletchBow(getPlayer(), 1, 10);
			break;

		case 9: // X
			// Fletching.fletchBow(getPlayer(),
			// Item.create(Fletching.getLog().getGive()[1]), );
			break;

		// Option 3
		case 16: // 1
			Fletching.fletchBow(getPlayer(), 2, 1);
			break;

		case 15: // 5
			Fletching.fletchBow(getPlayer(), 2, 5);
			break;

		case 14: // 10
			Fletching.fletchBow(getPlayer(), 2, 10);
			break;

		case 13: // X
			// Fletching.fletchBow(getPlayer(),
			// Item.create(Fletching.getLog().getGive()[2]), );
			break;
		}
	}

	/**
	 * Dialogue hook.
	 * 
	 * @return true, if successful
	 */
	private boolean dialogueHook() {
		if (getPlayer().getCurrentlyOpenDialogue() != null) {
			Dialogue current = getPlayer().getCurrentlyOpenDialogue();
			if (current.getLines().length > 3) {
				doDialogueOption(5);
				return true;
			}

			if (current.getNext() == -1) {
				getPlayer().getCurrentAction().cancel();
			}
			for (Dialogue dialogue : Main.getDialogues()) {
				if (current.getNext() == dialogue.getId() && current.getNpc() == dialogue.getNpc()) {
					new DialogueAction(getPlayer(), dialogue).run();
					return true;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Do dialogue option.
	 * 
	 * @param id
	 *            the id
	 */
	private void doDialogueOption(int id) {
		RubyInterpreter.invoke("dialogueOption", getPlayer(), id);
	}
}
