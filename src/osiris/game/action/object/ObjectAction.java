package osiris.game.action.object;

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

import osiris.game.action.DistancedAction;
import osiris.game.action.ObjectActionListeners;
import osiris.game.model.Character;
import osiris.game.model.Position;

/**
 * The Class ObjectAction.
 * 
 * @author Blake
 * 
 */
public class ObjectAction extends DistancedAction {

	/**
	 * The Enum ObjectActionType.
	 */
	public enum ObjectActionType {

		/**
		 * The FIRST.
		 */
		FIRST,

		/**
		 * The SECOND.
		 */
		SECOND,

		/**
		 * The THIRD.
		 */
		THIRD

	}

	/**
	 * The type.
	 */
	private final ObjectActionType type;

	/**
	 * The object id.
	 */
	private final int objectID;

	/**
	 * The object x.
	 */
	private final Position objectPosition;

	/**
	 * Instantiates a new object action.
	 * 
	 * @param character
	 *            the character
	 * @param position
	 *            the position
	 * @param distance
	 *            the distance
	 * @param type
	 *            the type
	 * @param objectID
	 *            the object id
	 * @param objectX
	 *            the object x
	 * @param objectY
	 *            the object y
	 */
	public ObjectAction(Character character, Position position, int distance, ObjectActionType type, int objectID, int objectX, int objectY) {
		super(character, position, distance);
		this.type = type;
		this.objectID = objectID;
		this.objectPosition = new Position(objectX, objectY, character.getPosition().getZ());
	}

	@Override
	public void execute() {
		getCharacter().facePosition(objectPosition);
		ObjectActionListeners.fireObjectAction(this);
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public ObjectActionType getType() {
		return type;
	}

	/**
	 * Gets the object id.
	 * 
	 * @return the objectID
	 */
	public int getObjectID() {
		return objectID;
	}

	/**
	 * Gets the object position.
	 * 
	 * @return the object position
	 */
	public Position getObjectPosition() {
		return objectPosition;
	}
}