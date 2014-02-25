package osiris.game.model;

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

/**
 * The Class WorldObject.
 * 
 * @author Blake Beaupain
 */
public class WorldObject extends Viewable {

	/** The object id. */
	private int objectID;

	/** The object face. */
	private final int objectFace;

	/** The object type. */
	private final int objectType;

	/**
	 * Instantiates a new world object.
	 * 
	 * @param objectID
	 *            the object id
	 * @param objectFace
	 *            the object face
	 * @param objectType
	 *            the object type
	 * @param objectPosition
	 *            the object position
	 */
	public WorldObject(int objectID, int objectFace, int objectType, Position objectPosition) {
		this.objectID = objectID;
		this.objectFace = objectFace;
		this.objectType = objectType;
		setPosition(objectPosition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WorldObject[id=" + objectID + ", face=" + objectFace + ", type=" + objectType + ", position=" + getPosition();
	}

	/**
	 * Gets the object id.
	 * 
	 * @return the object id
	 */
	public int getObjectID() {
		return objectID;
	}

	/**
	 * Gets the object face.
	 * 
	 * @return the object face
	 */
	public int getObjectFace() {
		return objectFace;
	}

	/**
	 * Gets the object type.
	 * 
	 * @return the object type
	 */
	public int getObjectType() {
		return objectType;
	}

	/**
	 * Gets the object position.
	 * 
	 * @return the object position
	 */
	public Position getObjectPosition() {
		return this.getPosition();
	}

	/**
	 * Sets the object id.
	 * 
	 * @param id
	 *            the new object id
	 */
	public void setObjectId(int id) {
		this.objectID = id;
	}

	public WorldObject clone() {
		return new WorldObject(objectID, objectFace, objectType, getPosition());
	}

}