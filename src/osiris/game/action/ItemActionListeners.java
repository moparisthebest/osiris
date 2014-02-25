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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import osiris.game.action.item.ItemAction;
import osiris.game.action.item.ItemOnItemAction;
import osiris.game.action.item.impl.BuryBonesListener;
import osiris.game.action.item.impl.FiremakingListener;
import osiris.game.model.item.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemActionListeners.
 */
public class ItemActionListeners {

	/** The listener map. */
	public static Map<Integer, List<ItemActionListener>> listenerMap;

	static {
		listenerMap = new HashMap<Integer, List<ItemActionListener>>();
		// Register listeners here.
		addListener(new BuryBonesListener(), 526, 528, 530, 532, 534, 536);
		addListener(new FiremakingListener(), 590);
	}

	/**
	 * Fire item action.
	 * 
	 * @param action
	 *            the action
	 */
	public static void fireItemAction(ItemAction action) {
		List<ItemActionListener> listeners = new LinkedList<ItemActionListener>();
		List<ItemActionListener> primary = listenerMap.get(action.getItem().getId());
		if (primary != null)
			listeners.addAll(primary);
		if (action instanceof ItemOnItemAction) {
			Item usedOn = ((ItemOnItemAction) action).getUsedOn();
			if (usedOn != null) {
				List<ItemActionListener> other = listenerMap.get(usedOn.getId());
				if (other != null)
					listeners.addAll(other);
			}
		}
		if (listeners.size() == 0) {
			System.out.println("Couldn't find listener for " + action.getItem().getId());
			return;
		}
		for (ItemActionListener listener : listeners) {
			listener.onItemAction(action);
		}
	}

	/**
	 * Adds the listener.
	 * 
	 * @param listener
	 *            the listener
	 * @param itemIDs
	 *            the item i ds
	 */
	public static void addListener(ItemActionListener listener, int... itemIDs) {
		for (int itemID : itemIDs) {
			List<ItemActionListener> listeners = listenerMap.get(itemID);
			if (listeners == null) {
				listeners = new LinkedList<ItemActionListener>();
				listenerMap.put(itemID, listeners);
			}
			listeners.add(listener);
		}
	}

	/**
	 * Adds the listeners.
	 * 
	 * @param itemID
	 *            the item id
	 * @param listeners
	 *            the listeners
	 */
	public static void addListeners(int itemID, ItemActionListener... listeners) {
		List<ItemActionListener> list = listenerMap.get(itemID);
		if (list == null) {
			list = new LinkedList<ItemActionListener>();
			listenerMap.put(itemID, list);
		}
		for (ItemActionListener listener : listeners) {
			list.add(listener);
		}
	}

}
