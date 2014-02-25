package osiris.game.action;

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

import static osiris.game.model.skills.Mining.ADAMANTITE;
import static osiris.game.model.skills.Mining.CLAY;
import static osiris.game.model.skills.Mining.COAL;
import static osiris.game.model.skills.Mining.COPPER;
import static osiris.game.model.skills.Mining.GOLD;
import static osiris.game.model.skills.Mining.IRON;
import static osiris.game.model.skills.Mining.MITHRIL;
import static osiris.game.model.skills.Mining.RUNITE;
import static osiris.game.model.skills.Mining.SILVER;
import static osiris.game.model.skills.Mining.TIN;
import static osiris.game.model.skills.Woodcutting.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import osiris.Main;
import osiris.data.parser.impl.PositionChangeParser;
import osiris.game.action.object.ObjectAction;
import osiris.game.action.object.impl.CookingListener;
import osiris.game.action.object.impl.PositionChangeActionListener;
import osiris.game.action.object.impl.RockActionListener;
import osiris.game.action.object.impl.TreeActionListener;

// TODO: Auto-generated Javadoc
/**
 * The Class ObjectActionListeners.
 * 
 * @author Blake Beaupain
 */
public class ObjectActionListeners {

	/** The listener map. */
	public static Map<Integer, List<ObjectActionListener>> listenerMap;

	/**
	 * Fire object action.
	 * 
	 * @param action
	 *            the action
	 */
	public static void fireObjectAction(ObjectAction action) {
		List<ObjectActionListener> listeners = listenerMap.get(action.getObjectID());
		if (listeners == null) {
			System.out.println("Couldn't find listerer for " + action.getObjectID());
			return;
		}
		for (ObjectActionListener listener : listeners) {
			listener.onObjectAction(action);
		}
	}

	/**
	 * Adds the listener.
	 * 
	 * @param listener
	 *            the listener
	 * @param objectIDs
	 *            the object i ds
	 */
	public static void addListener(ObjectActionListener listener, int... objectIDs) {
		for (int objectID : objectIDs) {
			List<ObjectActionListener> listeners = listenerMap.get(objectID);
			if (listeners == null) {
				listeners = new LinkedList<ObjectActionListener>();
				listenerMap.put(objectID, listeners);
			}
			listeners.add(listener);
		}
	}

	/**
	 * Adds the listeners.
	 * 
	 * @param objectID
	 *            the object id
	 * @param listeners
	 *            the listeners
	 */
	public static void addListeners(int objectID, ObjectActionListener... listeners) {
		List<ObjectActionListener> list = listenerMap.get(objectID);
		if (list == null) {
			list = new LinkedList<ObjectActionListener>();
			listenerMap.put(objectID, list);
		}
		for (ObjectActionListener listener : listeners) {
			list.add(listener);
		}
	}

	static {
		listenerMap = new HashMap<Integer, List<ObjectActionListener>>();

		// Woodcutting.
		TreeActionListener treeActionListener = new TreeActionListener();
		addListener(treeActionListener, TREE_1, TREE_2, TREE_3, TREE_4, TREE_OAK, TREE_WILLOW_1, TREE_WILLOW_2, TREE_WILLOW_3, TREE_WILLOW_4, TREE_MAPLE, TREE_YEW, TREE_MAGIC);

		// Mining
		RockActionListener rockActionListener = new RockActionListener();
		addListener(rockActionListener, COPPER);
		addListener(rockActionListener, TIN);
		addListener(rockActionListener, CLAY);
		addListener(rockActionListener, IRON);
		addListener(rockActionListener, COAL);
		addListener(rockActionListener, SILVER);
		addListener(rockActionListener, GOLD);
		addListener(rockActionListener, MITHRIL);
		addListener(rockActionListener, ADAMANTITE);
		addListener(rockActionListener, RUNITE);

		addListener(new CookingListener(true), 2732);
		for (PositionChangeParser.PositionChangeInfo info : Main.getStairs()) {
			ObjectActionListeners.addListener(new PositionChangeActionListener(info), info.getId());
		}

	}

}
