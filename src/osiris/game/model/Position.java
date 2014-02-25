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

import static osiris.util.Settings.DEFAULT_X;
import static osiris.util.Settings.DEFAULT_Y;
import static osiris.util.Settings.DEFAULT_Z;

// TODO: Auto-generated Javadoc

/**
 * A position with methods for getting region and other coordinate data.
 * 
 * @author Blake
 * 
 */
public class Position {

	/**
	 * The x.
	 */
	private int x;

	/**
	 * The y.
	 */
	private int y;

	/**
	 * The z.
	 */
	private int z;

	/**
	 * Instantiates a new position.
	 */
	public Position() {
		this(DEFAULT_X, DEFAULT_Y);
	}

	/**
	 * Instantiates a new position.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public Position(int x, int y) {
		this(x, y, DEFAULT_Z);
	}

	/**
	 * Instantiates a new position.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 */
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Instantiates a new position.
	 * 
	 * @param position
	 *            the position
	 */
	public Position(Position position) {
		this.x = position.x;
		this.y = position.y;
		this.z = position.z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Position[absolute=" + x + ", " + y + ", " + z + "]" + "[local=" + getLocalX() + ", " + getLocalY() + "]" + "[region=" + getRegionX() + ", " + getRegionY() + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Position) {
			Position p = (Position) other;
			return (p.x == x && p.y == y && p.z == z);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Position clone() {
		return new Position(x, y, z);
	}

	/**
	 * Sets the.
	 * 
	 * @param position
	 *            the position
	 */
	public void set(Position position) {
		this.x = position.x;
		this.y = position.y;
		this.z = position.z;
	}

	/**
	 * Sets the.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void set(int x, int y) {
		set(x, y, z);
	}

	/**
	 * Sets the.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 */
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Checks if a Position is in sight.
	 * 
	 * @param other
	 *            the other
	 * @return true, if the argued Position is in sight
	 */
	public boolean inSight(Position other, int radius) {
		if (other.z == z) {
			int deltaX = other.x - x;
			int deltaY = other.y - y;
			return deltaX < radius && deltaX >= -radius && deltaY < radius && deltaY >= -radius;
		}
		return false;
	}

	/**
	 * Gets the region x.
	 * 
	 * @return the region x
	 */
	public int getRegionX() {
		return x >> 3;
	}

	/**
	 * Gets the region y.
	 * 
	 * @return the region y
	 */
	public int getRegionY() {
		return y >> 3;
	}

	/**
	 * Gets the local x.
	 * 
	 * @return the local x
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the local y.
	 * 
	 * @return the local y
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Gets the local x.
	 * 
	 * @param relative
	 *            the relative
	 * @return the local x
	 */
	public int getLocalX(Position relative) {
		return x - 8 * (relative.getRegionX() - 6);
	}

	/**
	 * Gets the local y.
	 * 
	 * @param relative
	 *            the relative
	 * @return the local y
	 */
	public int getLocalY(Position relative) {
		return y - 8 * (relative.getRegionY() - 6);
	}

	/**
	 * Sets the x.
	 * 
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the x.
	 * 
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the y.
	 * 
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the y.
	 * 
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the z.
	 * 
	 * @param z
	 *            the z to set
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * Gets the z.
	 * 
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

}
