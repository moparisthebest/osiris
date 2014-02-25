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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import osiris.Main;
import osiris.data.parser.Parser;
import osiris.game.model.Position;

// TODO: Auto-generated Javadoc
/**
 * I The Class PositionChangeParser.
 * 
 * @author samuraiblood2
 * 
 */
public class PositionChangeParser extends Parser {

	/**
	 * Instantiates a new position change parser.
	 * 
	 * @param file
	 *            the file
	 */
	public PositionChangeParser(String file) {
		super(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.data.parser.Parser#onParse(org.w3c.dom.Document)
	 */
	@Override
	public void onParse(Document doc) throws Exception {
		NodeList positionList = (NodeList) doc.getElementsByTagName("pos");
		if (positionList == null) {
			return;
		}

		for (int i = 0; i < positionList.getLength(); i++) {
			Element positionElement = (Element) positionList.item(i);
			int id = Integer.parseInt(positionElement.getAttribute("id"));
			if (id < 0) {
				throw new Exception("Object ID must be higher than 0.");
			}

			int x = Integer.parseInt(positionElement.getAttribute("x"));
			int y = Integer.parseInt(positionElement.getAttribute("y"));
			int z = 0;
			if (!positionElement.getAttribute("z").matches("\\s*")) {
				z = Integer.parseInt(positionElement.getAttribute("z"));
			}
			PositionChangeInfo positionInfo = new PositionChangeInfo(id, new Position(x, y, z));

			Element door = (Element) positionElement.getElementsByTagName("swing").item(0);
			if (door != null) {
				String currentFace = door.getAttribute("current").toUpperCase();
				WorldObjectParser.Face current = WorldObjectParser.Face.valueOf(currentFace);
				if (current == null) {
					throw new Exception("Incorrect face value.");
				}
				positionInfo.setCurrent(current);
				positionInfo.setFrom(current);

				String toFace = door.getAttribute("to").toUpperCase();
				WorldObjectParser.Face to = WorldObjectParser.Face.valueOf(toFace);
				if (to == null) {
					throw new Exception("Incorrect face value.");
				}
				positionInfo.setTo(to);
				positionInfo.setType(Type.DOOR);
			} else {
				for (int i2 = 0; i2 < 2; i2++) {
					Element direction = (Element) positionElement.getElementsByTagName(i2 == 0 ? "up" : "down").item(0);
					if (direction == null) {
						continue;
					}

					Position pos = createPositionFromElement(direction);
					if (i2 == 0) {
						positionInfo.setMove(0, pos);
					} else {
						positionInfo.setMove(1, pos);
					}
				}

				if (positionInfo.getMove(0) == null && positionInfo.getMove(1) == null) {
					throw new Exception("Must have the up or down tag present.");
				}
				positionInfo.setType(Type.STAIR);
			}

			Main.getStairs().add(positionInfo);
		}
	}

	/**
	 * Creates the position from element.
	 * 
	 * @param element
	 *            the element
	 * @return the position
	 */
	private Position createPositionFromElement(Element element) {
		int moveX = Integer.parseInt(element.getAttribute("x"));
		int moveY = Integer.parseInt(element.getAttribute("y"));
		int moveZ = 0;
		if (!element.getAttribute("z").matches("\\s*")) {
			moveZ = Integer.parseInt(element.getAttribute("z"));
		}

		return new Position(moveX, moveY, moveZ);
	}

	public enum Type {
		DOOR, STAIR
	}

	/**
	 * The Class PositionChangeInfo.
	 * 
	 * @author samuraiblood2
	 * 
	 */
	public class PositionChangeInfo {

		/** The id. */
		private int id;

		/** The location. */
		private Position location;

		/** The move. */
		private Position[] move;

		/** The face. */
		private WorldObjectParser.Face current, from, to;

		private Type type;

		/**
		 * Instantiates a new position change info.
		 * 
		 * @param id
		 *            the id
		 * @param location
		 *            the location
		 */
		public PositionChangeInfo(int id, Position location) {
			this.id = id;
			this.location = location;
			move = new Position[2];
		}

		public PositionChangeInfo setType(Type type) {
			this.type = type;
			return this;
		}

		/**
		 * Gets the id.
		 * 
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		public Type getType() {
			return type;
		}

		/**
		 * Sets the lcation.
		 */
		public void setLocation(Position location) {
			this.location = location;
		}

		/**
		 * Gets the location.
		 * 
		 * @return the location
		 */
		public Position getLocation() {
			return location;
		}

		public void setMove(int index, Position position) {
			move[index] = position;
		}

		public Position getMove(int index) {
			return move[index];
		}

		/**
		 * Sets the face.
		 * 
		 * @param current
		 *            the new face
		 */
		public void setCurrent(WorldObjectParser.Face current) {
			this.current = current;
		}

		/**
		 * Gets the face.
		 * 
		 * @return the face
		 */
		public WorldObjectParser.Face getCurrent() {
			return current;
		}

		public void setTo(WorldObjectParser.Face to) {
			this.to = to;
		}

		public WorldObjectParser.Face getTo() {
			return to;
		}

		public void setFrom(WorldObjectParser.Face from) {
			this.from = from;
		}

		public WorldObjectParser.Face getFrom() {
			return from;
		}
	}
}