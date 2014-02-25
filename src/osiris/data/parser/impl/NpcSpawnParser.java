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

import static osiris.util.Settings.DEFAULT_X;
import static osiris.util.Settings.DEFAULT_Y;
import static osiris.util.Settings.DEFAULT_Z;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import osiris.Main;
import osiris.data.parser.Parser;
import osiris.game.model.Npc;
import osiris.game.model.Position;

// TODO: Auto-generated Javadoc

/**
 * The Class NpcSpawnParser.
 * 
 * @author Blake Beaupain
 * @author samuraiblood2
 * @author Boomer
 * 
 */
public class NpcSpawnParser extends Parser {

	/**
	 * Instantiates a new npc spawn parser.
	 * 
	 * @param file
	 *            the file
	 */
	public NpcSpawnParser(String file) {
		super(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.data.parser.Parser#onParse(org.w3c.dom.Document)
	 */
	@Override
	public void onParse(Document doc) throws Exception {
		NodeList npcList = (NodeList) doc.getElementsByTagName("npc");
		for (int i = 0; i < npcList.getLength(); i++) {
			int npcID = 1;
			int posX = DEFAULT_X, posY = DEFAULT_Y, posZ = DEFAULT_Z;
			int minX = DEFAULT_X, minY = DEFAULT_Y, maxX = DEFAULT_Z, maxY = DEFAULT_Y;
			Position defaultFacing = null;
			@SuppressWarnings("unused")
			int health = 0, maxHit = 1, accuracy = 0, defence = 0, attackSpeed = 3;
			// Load the NPC ID.
			Element npcElement = (Element) npcList.item(i);
			npcID = Integer.parseInt(npcElement.getAttribute("id"));

			// Load the position.
			Element posElement = (Element) npcElement.getElementsByTagName("position").item(0);
			if (posElement != null) {
				posX = Integer.parseInt(posElement.getAttribute("x"));
				posY = Integer.parseInt(posElement.getAttribute("y"));
				posZ = Integer.parseInt(posElement.getAttribute("z"));
			}

			// Load the movement missile.
			Element rangeElement = (Element) npcElement.getElementsByTagName("movementRange").item(0);
			if (rangeElement != null) {
				minX = Integer.parseInt(rangeElement.getAttribute("minX"));
				minY = Integer.parseInt(rangeElement.getAttribute("minY"));
				maxX = Integer.parseInt(rangeElement.getAttribute("maxX"));
				maxY = Integer.parseInt(rangeElement.getAttribute("maxY"));
			}

			Element faceElement = (Element) npcElement.getElementsByTagName("face").item(0);
			if (faceElement != null) {
				int faceX = posX;
				int faceY = posY;
				String facing = faceElement.getTextContent().toLowerCase();
				if (facing.contains("north"))
					faceY += 1;
				else if (facing.contains("south"))
					faceY -= 1;
				if (facing.contains("west"))
					faceX -= 1;
				else if (facing.contains("east"))
					faceX += 1;
				defaultFacing = new Position(faceX, faceY, posZ);
			}

			// Make the NPC object and register it with the world.
			Npc npc = new Npc(npcID);
			npc.getPosition().set(posX, posY, posZ);
			npc.setDefaultPosition(npc.getPosition());
			npc.setWalkingRange(minX, minY, maxX, maxY);
			npc.setDefaultFacing(defaultFacing);
			Main.getNpcs().add(npc);
		}
	}
}
