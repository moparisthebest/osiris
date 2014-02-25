package osiris.game.action.impl;

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

import osiris.game.action.Action;
import osiris.game.model.Character;
import osiris.game.model.dialogues.Dialogue;
import osiris.game.model.dialogues.Dialogue.Type;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class DialogueAction.
 * 
 * @author samuraiblood2
 * 
 */
public class DialogueAction extends Action {

	/** The dialogue. */
	private Dialogue dialogue;

	/**
	 * Instantiates a new dialogue action.
	 * 
	 * @param character
	 *            the character
	 * @param dialogue
	 *            the dialogue
	 */
	public DialogueAction(Character character, Dialogue dialogue) {
		super(character);
		this.dialogue = dialogue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
		getPlayer().getEventWriter().sendCloseChatboxInterface();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
		int inter = 241;
		switch (dialogue.getType()) {
		case OPTION:
			int start = ((dialogue.getLines().length > 2) ? 228 : 227);
			inter = start + dialogue.getLines().length;
			getPlayer().getEventWriter().sendChatboxInterface(inter);
			break;

		case NPC:
			inter = 240 + dialogue.getLines().length;
			getPlayer().getEventWriter().sendChatboxInterface(inter);
			getPlayer().getEventWriter().sendInterfaceAnimation(inter, 2, 9847);
			getPlayer().getEventWriter().sendInterfaceNpc(inter, 2, dialogue.getNpc());
			getPlayer().getEventWriter().sendString(dialogue.getTitle(), inter, 3);
			break;

		case PLAYER:
			break;
		}

		int child = dialogue.getType() == Type.OPTION ? 2 : 4;
		for (int i = 0; i < dialogue.getLines().length; i++) {
			String line = dialogue.getLines()[i];
			if (line == null) {
				continue;
			}

			if (line.contains("NAME")) {
				line = line.replaceAll("NAME", Utilities.toProperCase(getPlayer().getUsername()));
			}
			getPlayer().getEventWriter().sendString(line, inter, child++);
		}
		getPlayer().setCurrentlyOpenDialogue(dialogue);
	}
}
