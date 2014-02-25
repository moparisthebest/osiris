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

import java.util.LinkedList;
import java.util.List;

import osiris.Main;

// TODO: Auto-generated Javadoc
/**
 * The Class WorldObjects.
 * 
 * @author Blake Beaupain
 */
public class WorldObjects {

	/** The objects. */
	private static List<WorldObject> objects = new LinkedList<WorldObject>();

	/**
	 * Gets the objects.
	 * 
	 * @return the objects
	 */
	public static List<WorldObject> getObjects() {
		return objects;
	}

	/**
	 * Gets the object.
	 * 
	 * @param position
	 *            the position
	 * @return the object
	 */
	public static WorldObject getObject(Position position) {
		for (WorldObject object : getObjects())
			if (object.getObjectPosition().equals(position))
				return object;
		return null;
	}

	/**
	 * Refresh.
	 * 
	 * @param player
	 *            the player
	 */
	public static void refresh(Player player) {
		for (WorldObject object : WorldObjects.getObjects()) {
			if (object.isInSight(player))
				player.getEventWriter().setObject(object);
		}
	}

	/**
	 * Removes the object.
	 * 
	 * @param obj
	 *            the obj
	 */
	public static void removeObject(WorldObject obj) {
		if (obj == null)
			return;
		obj = obj.clone();
		getObjects().remove(obj);
		obj.setObjectId(6951);
		for (Player player : Main.getPlayers())
			if (player.isInSight(obj))
				player.getEventWriter().setObject(obj);
	}

	/**
	 * Adds the object.
	 * 
	 * @param obj
	 *            the obj
	 */
	public static void addObject(WorldObject obj) {
		if (obj == null)
			return;
		for (Player player : Main.getPlayers())
			if (player.isInSight(obj))
				player.getEventWriter().setObject(obj);
		getObjects().add(obj);
	}

}
