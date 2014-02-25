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
 * Represents a single movement direction.
 * 
 * @author Graham Edgecombe
 * 
 */
public enum Direction {

	/**
	 * North movement.
	 */
	NORTH(1),

	/**
	 * North east movement.
	 */
	NORTH_EAST(2),

	/**
	 * East movement.
	 */
	EAST(4),

	/**
	 * South east movement.
	 */
	SOUTH_EAST(7),

	/**
	 * South movement.
	 */
	SOUTH(6),

	/**
	 * South west movement.
	 */
	SOUTH_WEST(5),

	/**
	 * West movement.
	 */
	WEST(3),

	/**
	 * North west movement.
	 */
	NORTH_WEST(0),

	/**
	 * No movement.
	 */
	NONE(-1);

	/**
	 * An empty direction array.
	 */
	public static final Direction[] EMPTY_DIRECTION_ARRAY = new Direction[0];

	/**
	 * Checks if the direction represented by the two delta values can connect
	 * two points together in a single direction.
	 * 
	 * @param deltaX
	 *            The difference in X coordinates.
	 * @param deltaY
	 *            The difference in X coordinates.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public static boolean isConnectable(int deltaX, int deltaY) {
		return Math.abs(deltaX) == Math.abs(deltaY) || deltaX == 0 || deltaY == 0;
	}

	/**
	 * Creates a direction from the differences between X and Y.
	 * 
	 * @param deltaX
	 *            The difference between two X coordinates.
	 * @param deltaY
	 *            The difference between two Y coordinates.
	 * @return The direction.
	 */
	public static Direction fromDeltas(int deltaX, int deltaY) {
		if (deltaY == 1) {
			if (deltaX == 1) {
				return Direction.NORTH_EAST;
			} else if (deltaX == 0) {
				return Direction.NORTH;
			} else {
				return Direction.NORTH_WEST;
			}
		} else if (deltaY == -1) {
			if (deltaX == 1) {
				return Direction.SOUTH_EAST;
			} else if (deltaX == 0) {
				return Direction.SOUTH;
			} else {
				return Direction.SOUTH_WEST;
			}
		} else {
			if (deltaX == 1) {
				return Direction.EAST;
			} else if (deltaX == -1) {
				return Direction.WEST;
			}
		}
		return Direction.NONE;
	}

	public static Position directionToPosition(Position source, Direction direction) {
		int sourceX = source.getX();
		int sourceY = source.getY();
		String dir = direction.name().toLowerCase();
		if (dir.contains("north"))
			sourceY += 1;
		else if (dir.contains("south"))
			sourceY -= 1;
		if (dir.contains("west"))
			sourceX -= 1;
		else if (dir.contains("east"))
			sourceX += 1;
		return new Position(sourceX, sourceY, source.getZ());

	}

	/**
	 * The direction as an integer.
	 */
	private final int intValue;

	/**
	 * Creates the direction.
	 * 
	 * @param intValue
	 *            The direction as an integer.
	 */
	private Direction(int intValue) {
		this.intValue = intValue;
	}

	/**
	 * Gets the direction as an integer which the client can understand.
	 * 
	 * @return The movement as an integer.
	 */
	public int toInteger() {
		return intValue;
	}

	/**
	 * The Constant XLATE_DIRECTION_TO_CLIENT.
	 */
	public static final byte[] XLATE_DIRECTION_TO_CLIENT = new byte[] { 1, 2, 4, 7, 6, 5, 3, 0 };

}
