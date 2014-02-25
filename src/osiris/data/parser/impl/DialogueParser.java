package osiris.data.parser.impl;

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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import osiris.Main;
import osiris.data.parser.Parser;
import osiris.game.model.dialogues.Dialogue;
import osiris.game.model.dialogues.Dialogue.Type;

// TODO: Auto-generated Javadoc
/**
 * The Class DialogueParser.
 * 
 * @author samuraiblood2
 * 
 */
public class DialogueParser extends Parser {

	/**
	 * Instantiates a new dialogue parser.
	 * 
	 * @param file
	 *            the file
	 */
	public DialogueParser(String file) {
		super(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.data.parser.Parser#onParse(org.w3c.dom.Document)
	 */
	@Override
	public void onParse(Document doc) throws Exception {
		NodeList dialogueList = (NodeList) doc.getElementsByTagName("dialogue");
		if (dialogueList == null) {
			return;
		}

		for (int i = 0; i < dialogueList.getLength(); i++) {
			Element dialogueElement = (Element) dialogueList.item(i);

			Type type = Type.valueOf(dialogueElement.getAttribute("type").toUpperCase());
			int id = Integer.parseInt(dialogueElement.getAttribute("id"));
			Dialogue dialogue = new Dialogue(type, id);

			String[] params = getTextValue(dialogueElement, "npc").split(",");
			for (int l = 0; l < params.length; l++) {
				Dialogue clone = new Dialogue(dialogue);
				clone.setNpc(Integer.parseInt(params[l]));

				Element linesElement = (Element) dialogueElement.getElementsByTagName("lines").item(0);
				clone.setTitle(linesElement.getAttribute("title"));

				NodeList lineList = (NodeList) linesElement.getElementsByTagName("line");
				List<String> lines = new ArrayList<String>();
				for (int j = 0; j < lineList.getLength(); j++) {
					Element lineElement = (Element) lineList.item(j);
					lines.add(lineElement.getFirstChild().getNodeValue());
				}

				if (clone.getType() == Type.OPTION && lines.size() < 2) {
					lines.add("");
				}
				clone.setLines(lines);
				clone.setNext(Integer.parseInt(getTextValue(dialogueElement, "next")));
				Main.getDialogues().add(clone);
			}
		}
	}
}
