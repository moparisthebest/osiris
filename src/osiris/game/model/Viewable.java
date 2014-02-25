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

import osiris.game.model.ground.GroundItem;

// TODO: Auto-generated Javadoc
/**
 * The Class Viewable.
 * 
 * @author Boomer
 */
public class Viewable {

	/** The position. */
	private Position position = new Position();

	/**
	 * Checks if is in sight.
	 * 
	 * @param other
	 *            the other
	 * @return true, if is in sight
	 */
	public boolean isInSight(Viewable other) {
		int radius = 15;
		if (this instanceof GroundItem || other instanceof GroundItem || this instanceof WorldObject || other instanceof WorldObject)
			radius = 32;
		return isInSight(other.getPosition(), radius);
	}

	/**
	 * Checks if is in sight.
	 * 
	 * @param position
	 *            the position
	 * @param radius
	 *            the radius
	 * @return true, if is in sight
	 */
	public boolean isInSight(Position position, int radius) {
		return position.inSight(this.getPosition(), radius);
	}

	/**
	 * Gets the position.
	 * 
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 * 
	 * @param position
	 *            the new position
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
}
