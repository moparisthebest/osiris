package osiris.game.model.item;

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
import java.util.Arrays;
import java.util.HashMap;

import osiris.game.model.Player;
import osiris.game.model.def.ItemDef;

// TODO: Auto-generated Javadoc
/**
 * inventory, bank, equipment, shops, trades, duels, deposit boxes, etc.
 * 
 * @author Boomer
 * 
 */
public class ItemContainer {

	/** The items. */
	private Item[] items;

	/** The length. */
	private final int length;

	/** The allows notes. */
	private final boolean slides, forceStacks, allowsNotes;

	/** The players. */
	private final ArrayList<Player> players;

	/** The defs. */
	private final ContainerDef[] defs;

	/**
	 * Instantiates a new item container.
	 * 
	 * @param length
	 *            the length
	 * @param slides
	 *            the slides
	 * @param forceStacks
	 *            the force stacks
	 * @param allowsNotes
	 *            the allows notes
	 * @param defs
	 *            the defs
	 * @param players
	 *            the players
	 */
	public ItemContainer(final int length, final boolean slides, final boolean forceStacks, boolean allowsNotes, ContainerDef[] defs, Player... players) {
		this.length = length;
		this.items = new Item[length];
		this.slides = slides;
		this.forceStacks = forceStacks;
		this.allowsNotes = allowsNotes;
		this.defs = defs;
		this.players = new ArrayList<Player>(Arrays.asList(players));

	}

	/**
	 * Gets the items.
	 * 
	 * @return the items
	 */
	public Item[] getItems() {
		return items;
	}

	/**
	 * Sets the.
	 * 
	 * @param slot
	 *            the slot
	 * @param item
	 *            the item
	 * @param refresh
	 *            the refresh
	 */
	public void set(int slot, Item item, boolean refresh) {
		try {
			if (item != null) {
				if (ItemDef.forId(item.getId()).isNoted() && !this.allowsNotes)
					item.setId(item.getId() - 1);
			}
			items[slot] = item;
			if (refresh)
				refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the.
	 * 
	 * @param slot
	 *            the slot
	 * @param item
	 *            the item
	 */
	public void set(int slot, Item item) {
		set(slot, item, true);
	}

	/**
	 * Adds the.
	 * 
	 * @param itemId
	 *            the item id
	 * @param itemN
	 *            the item n
	 * @return true, if successful
	 */
	public boolean add(int itemId, int itemN) {
		return add(Item.create(itemId, itemN));
	}

	/**
	 * Adds the.
	 * 
	 * @param item
	 *            the item
	 * @return true, if successful
	 */
	public boolean add(Item item) {
		if (item == null)
			return true;
		int itemId = item.getId();
		int itemN = item.getAmount();
		ItemDef itemDef = ItemDef.forId(itemId);
		boolean stackable = itemDef.isStackable();
		if (!allowsNotes && itemDef.isNoted())
			itemId -= 1;
		if (forceStacks || stackable) {
			int slotOfItem = getSlotById(itemId);
			if (slotOfItem == -1) {
				int emptySlot = getEmptySlot();
				if (emptySlot == -1)
					return false; // No space in container
				else
					items[emptySlot] = Item.create(itemId, itemN);
				refresh();
				return true; // Added to new blank slot
			} else {
				boolean added = items[slotOfItem].adjustAmount(itemN);
				if (added)
					refresh();
				return added;
			}
		} else {
			ArrayList<Integer> emptySlots = getEmptySlots();
			if (emptySlots.size() < itemN)
				return false;
			for (int i = 0; i < itemN; i++)
				items[emptySlots.get(i)] = Item.create(itemId);
			refresh();
			return true; // Added to new blank slot
		}
	}

	/**
	 * Removes the by id.
	 * 
	 * @param itemId
	 *            the item id
	 * @param itemN
	 *            the item n
	 * @return true, if successful
	 */
	public boolean removeById(int itemId, int itemN) {
		return removeById(itemId, itemN, true);
	}

	/**
	 * Removes the by id.
	 * 
	 * @param itemId
	 *            the item id
	 * @param itemN
	 *            the item n
	 * @param refresh
	 *            the refresh
	 * @return true, if successful
	 */
	public boolean removeById(int itemId, int itemN, boolean refresh) {
		int slot = this.getSlotById(itemId);
		return slot != -1 && removeBySlot(slot, itemN, refresh);
	}

	/**
	 * Removes the by slot.
	 * 
	 * @param slot
	 *            the slot
	 * @param itemN
	 *            the item n
	 * @return true, if successful
	 */
	public boolean removeBySlot(int slot, int itemN) {
		return removeBySlot(slot, itemN, true);
	}

	/**
	 * Removes the by slot.
	 * 
	 * @param slot
	 *            the slot
	 * @param itemN
	 *            the item n
	 * @param refresh
	 *            the refresh
	 * @return true, if successful
	 */
	public boolean removeBySlot(int slot, int itemN, boolean refresh) {
		try {
			Item item = items[slot];
			if (item == null)
				return false;
			boolean stackable = ItemDef.forId(item.getId()).isStackable();
			if (forceStacks || stackable) {
				boolean removed = item.adjustAmount(-itemN);
				if (item.getAmount() == 0 && !item.respawns()) {
					items[slot] = null;
					if (slides)
						slide();
				}
				if (removed && refresh)
					refresh();
				return removed;
			} else if (itemN == 1) {
				items[slot] = null;
				if (slides)
					slide();
				if (refresh)
					refresh();
				return true;
			} else {
				HashMap<Integer, Item> commonItems = getAllItemsById(item.getId());
				Integer[] common = commonItems.keySet().toArray(new Integer[commonItems.keySet().size()]);
				if (common.length >= itemN) {
					for (int i = 0; i < itemN; i++) {
						int itemSlot = common[i];
						if (items[itemSlot].respawns()) {
							items[itemSlot].setAmount(0);
						} else
							items[itemSlot] = null;
					}
					if (slides)
						slide();
					if (refresh)
						refresh();
					return true;
				} else
					return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Can fit.
	 * 
	 * @param itemId
	 *            the item id
	 * @param itemN
	 *            the item n
	 * @return true, if successful
	 */
	public boolean canFit(int itemId, int itemN) {
		if (forceStacks || ItemDef.forId(itemId).isStackable()) {
			return this.getSlotById(itemId) != -1 || this.countEmptySlots() > 0;
		} else
			return this.countEmptySlots() > itemN;
	}

	/**
	 * Checks if is full.
	 * 
	 * @return true, if is full
	 */
	public boolean isFull() {
		return countEmptySlots() == 0;
	}

	/**
	 * Slide.
	 */
	public void slide() {
		ArrayList<Item> temp = new ArrayList<Item>();
		for (Item item : items)
			if (item != null)
				temp.add(item);
		clear();
		System.arraycopy(temp.toArray(new Item[temp.size()]), 0, items, 0, temp.size());
	}

	/**
	 * Clear.
	 */
	public void clear() {
		items = new Item[length];
	}

	/**
	 * Empty.
	 */
	public void empty() {
		clear();
		refresh();
	}

	/**
	 * Gets the empty slot.
	 * 
	 * @return the empty slot
	 */
	public int getEmptySlot() {
		for (int i = 0; i < items.length; i++)
			if (items[i] == null)
				return i;
		return -1;
	}

	/**
	 * Count empty slots.
	 * 
	 * @return the int
	 */
	public int countEmptySlots() {
		int count = 0;
		for (Item item : items)
			if (item == null)
				count++;
		return count;
	}

	/**
	 * Gets the empty slots.
	 * 
	 * @return the empty slots
	 */
	public ArrayList<Integer> getEmptySlots() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < items.length; i++)
			if (items[i] == null)
				temp.add(i);
		return temp;
	}

	/**
	 * Count filled slots.
	 * 
	 * @return the int
	 */
	public int countFilledSlots() {
		int count = 0;
		for (Item item : items)
			if (item != null)
				count++;
		return count;
	}

	/**
	 * Amount of item.
	 * 
	 * @param itemId
	 *            the item id
	 * @return the int
	 */
	public int amountOfItem(int itemId) {
		int count = 0;
		for (Item item : items)
			if (item != null && item.getId() == itemId)
				count += item.getAmount();
		return count;
	}

	/**
	 * Gets the slot by id.
	 * 
	 * @param itemId
	 *            the item id
	 * @return the slot by id
	 */
	public int getSlotById(int itemId) {
		for (int i = 0; i < items.length; i++)
			if (items[i] != null && items[i].getId() == itemId)
				return i;
		return -1;
	}

	/**
	 * Gets the item.
	 * 
	 * @param slot
	 *            the slot
	 * @return the item
	 */
	public Item getItem(int slot) {
		try {
			return items[slot];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the item by id.
	 * 
	 * @param itemId
	 *            the item id
	 * @return the item by id
	 */
	public Item getItemById(int itemId) {
		for (Item item : items)
			if (item != null && item.getId() == itemId)
				return item;
		return null;
	}

	/**
	 * Gets the all items by id.
	 * 
	 * @param itemId
	 *            the item id
	 * @return the all items by id
	 */
	public HashMap<Integer, Item> getAllItemsById(int itemId) {
		HashMap<Integer, Item> temp = new HashMap<Integer, Item>();
		for (int i = 0; i < items.length; i++)
			if (items[i] != null && items[i].getId() == itemId)
				temp.put(i, items[i]);
		return temp;
	}

	/**
	 * Gets the item by name.
	 * 
	 * @param name
	 *            the name
	 * @return the item by name
	 */
	public Item getItemByName(String name) {
		for (Item item : items)
			if (item != null && ItemDef.forId(item.getId()).getName().toLowerCase().contains(name.toLowerCase()))
				return item;
		return null;
	}

	/**
	 * Gets the all items by name.
	 * 
	 * @param name
	 *            the name
	 * @return the all items by name
	 */
	public HashMap<Integer, Item> getAllItemsByName(String name) {
		HashMap<Integer, Item> temp = new HashMap<Integer, Item>();
		for (int i = 0; i < items.length; i++)
			if (items[i] != null && ItemDef.forId(items[i].getId()).getName().toLowerCase().contains(name.toLowerCase()))
				temp.put(i, items[i]);
		return temp;
	}

	/**
	 * Move.
	 * 
	 * @param slotFrom
	 *            the slot from
	 * @param slotTo
	 *            the slot to
	 */
	public void move(int slotFrom, int slotTo) {
		Item from = items[slotFrom];
		Item to = items[slotTo];
		items[slotFrom] = to;
		items[slotTo] = from;
		refresh();
	}

	/**
	 * Amount can hold.
	 * 
	 * @param itemId
	 *            the item id
	 * @return the int
	 */
	public int amountCanHold(int itemId) {
		ItemDef def = ItemDef.forId(itemId);
		ArrayList<Integer> emptySlots = this.getEmptySlots();
		if ((forceStacks || ItemDef.forId(itemId).isStackable()) && this.amountOfItem(itemId) > 0)
			return Integer.MAX_VALUE;
		else if (emptySlots.size() == 0)
			return 0;
		if (def.isStackable())
			return Integer.MAX_VALUE;
		else
			return emptySlots.size();
	}

	/**
	 * Refresh.
	 */
	public void refresh() {
		for (Player player : players)
			for (ContainerDef def : defs)
				player.getEventWriter().sendItems(def.interfaceId, def.childId, def.type, this);
	}

	/**
	 * Gets the defs.
	 * 
	 * @return the defs
	 */
	public ContainerDef[] getDefs() {
		return defs;
	}

	/**
	 * Gets the length.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets the items.
	 * 
	 * @param items
	 *            the new items
	 */
	public void setItems(Item[] items) {
		this.items = items;
	}

	/**
	 * Can fit all.
	 * 
	 * @param items
	 *            the items
	 * @return true, if successful
	 */
	public boolean canFitAll(Item[] items) {
		int slotsRequired = slotsRequired(items);
		return this.countEmptySlots() >= slotsRequired;
	}

	/**
	 * Slots required.
	 * 
	 * @param items
	 *            the items
	 * @return the int
	 */
	public int slotsRequired(Item[] items) {
		int slotsRequired = 0;
		for (Item item : items) {
			if (item == null)
				continue;
			boolean stackable = ItemDef.forId(item.getId()).isStackable();
			if (item != null && (!stackable || this.getSlotById(item.getId()) != -1)) {
				if (!stackable)
					slotsRequired += item.getAmount();
				else
					slotsRequired++;
			}
		}
		return slotsRequired;

	}

	/**
	 * Gets the players.
	 * 
	 * @return the players
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}
}
