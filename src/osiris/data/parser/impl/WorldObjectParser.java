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

import osiris.data.parser.Parser;
import osiris.game.model.Position;
import osiris.game.model.WorldObject;
import osiris.game.model.WorldObjects;

// TODO: Auto-generated Javadoc
/**
 * The Class WorldObjectParser.
 * 
 * @author samuraiblood2
 * 
 */
public class WorldObjectParser extends Parser {

	/**
	 * Instantiates a new world object parser.
	 * 
	 * @param file
	 *            the file
	 */
	public WorldObjectParser(String file) {
		super(file);
	}

	/**
	 * The Enum Face.
	 * 
	 * @author samuraiblood2
	 * 
	 */
	public enum Face {

		NORTH(2), SOUTH(0), EAST(3), WEST(1);

		/** The id. */
		private int id;

		/**
		 * Instantiates a new face.
		 * 
		 * @param id
		 *            the id
		 */
		Face(int id) {
			this.id = id;
		}

		/**
		 * Gets the id.
		 * 
		 * @return the id
		 */
		public int getId() {
			return id;
		}
	}

	/**
	 * The Enum Type.
	 * 
	 * @author samuraiblood2
	 * 
	 */
	public enum Type {

		/** The WALL. */
		WALL(0), /** The WAL l_ decor. */
		WALL_DECOR(4), /** The DIA g_ wall. */
		DIAG_WALL(9), /** The WORLD. */
		WORLD(10), /** The ROOF. */
		ROOF(12), /** The FLOOR. */
		FLOOR(22);

		/** The id. */
		private int id;

		/**
		 * Instantiates a new type.
		 * 
		 * @param id
		 *            the id
		 */
		Type(int id) {
			this.id = id;
		}

		/**
		 * Gets the id.
		 * 
		 * @return the id
		 */
		public int getId() {
			return id;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.data.parser.Parser#onParse(org.w3c.dom.Document)
	 */
	@Override
	public void onParse(Document doc) throws Exception {
		NodeList objectList = (NodeList) doc.getElementsByTagName("object");
		if (objectList == null) {
			return;
		}

		List<WorldObject> list = new ArrayList<WorldObject>();
		for (int i = 0; i < objectList.getLength(); i++) {
			Element objectElement = (Element) objectList.item(i);
			String idAttribute = objectElement.getAttribute("id");
			int id = 0;
			if (idAttribute.equalsIgnoreCase("remove")) {
				id = 6951;
			} else {
				id = Integer.parseInt(idAttribute);
			}

			int x = Integer.parseInt(objectElement.getAttribute("x"));
			int y = Integer.parseInt(objectElement.getAttribute("y"));
			int z = 0;
			if (!objectElement.getAttribute("z").contains("")) {
				z = Integer.parseInt(objectElement.getAttribute("z"));
			}
			Face face = Face.WEST;
			if (id != 6951) {
				face = Face.valueOf(getTextValue(objectElement, "face").toUpperCase().replaceAll(" ", "_").trim());
			}

			String value = getTextValue(objectElement, "type");
			Type type = Type.WORLD;
			if (value.equalsIgnoreCase("all")) {
				for (int l = 0; l < 22; l++) {
					list.add(new WorldObject(id, face.getId(), l, new Position(x, y, z)));
				}
				break;
			} else {
				type = Type.valueOf(value.toUpperCase().replaceAll(" ", "_").trim());
			}
			list.add(new WorldObject(id, face.getId(), type.getId(), new Position(x, y, z)));
		}
		WorldObjects.getObjects().addAll(list);
	}
}
