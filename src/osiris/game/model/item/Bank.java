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

import osiris.game.model.Player;
import osiris.game.model.def.ItemDef;
import osiris.util.BankUtilities;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class Bank.
 * 
 * @author Boomer
 * 
 */
public class Bank {

	/**
	 * The main.
	 */
	private final ItemContainer main;

	/**
	 * The tabs.
	 */
	private final ArrayList<ItemContainer> tabs;

	/** The bank open. */
	private boolean bankOpen;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The viewing tab.
	 */
	private int viewingTab = -1;

	/**
	 * The withdraw notes.
	 */
	private boolean withdrawNotes = false;

	/**
	 * Instantiates a new bank.
	 * 
	 * @param player
	 *            the player
	 */
	public Bank(Player player) {
		this.tabs = new ArrayList<ItemContainer>();
		main = getBlankTab(player);
		this.player = player;
		this.bankOpen = false;
	}

	/**
	 * Withdraw notes.
	 * 
	 * @return true, if successful
	 */
	public boolean withdrawNotes() {
		return withdrawNotes;
	}

	/**
	 * Switch withdraw notes.
	 */
	public void switchWithdrawNotes() {
		withdrawNotes = !withdrawNotes;
	}

	/**
	 * Adds the.
	 * 
	 * @param itemId
	 *            the item id
	 * @param itemN
	 *            the item n
	 */
	public void add(int itemId, int itemN) {
		int slotOfItem = main.getSlotById(itemId);
		int tabOfItem = -1;
		if (slotOfItem == -1)
			for (int i = 0; i < tabs.size(); i++) {
				if (slotOfItem == -1) {
					slotOfItem = tabs.get(i).getSlotById(itemId);
					tabOfItem = i;
				}
			}
		if (slotOfItem != -1) {
			if (tabOfItem == -1)
				main.add(itemId, itemN);
			else
				tabs.get(tabOfItem).add(itemId, itemN);
		} else {
			if (tabs.size() == 0)
				main.add(itemId, itemN);
			else {
				if (viewingTab == -1)
					main.add(itemId, itemN);
				else
					tabs.get(viewingTab).add(itemId, itemN);
			}
		}
		refresh();
	}

	/**
	 * Change item tab.
	 * 
	 * @param slotFrom
	 *            the slot from
	 * @param toTabId
	 *            the to tab id
	 * @param slotTo
	 *            the slot to
	 */
	public void changeItemTab(int slotFrom, int toTabId, int slotTo) {

		int tabSlotFrom = this.getTabBySlot(slotFrom);
		int tabSlotTo = BankUtilities.tabIdToContainerSlot(toTabId);
		if (getItems().length < slotFrom)
			return;
		if (tabSlotFrom == tabSlotTo) {
			ItemContainer container = main;
			if (tabSlotFrom != -1)
				container = tabs.get(tabSlotFrom);
			container.move(slotFrom, slotTo);
		}
		refresh();
	}

	/**
	 * Removes the.
	 * 
	 * @param slotFrom
	 *            the slot from
	 * @param amount
	 *            the amount
	 * @return true, if successful
	 */
	public boolean remove(int slotFrom, int amount) {
		Item item = getItems()[slotFrom];
		int totalAmount = item.getAmount();
		int tabSlotFrom = this.getTabBySlot(slotFrom);
		int id = item.getId();
		boolean removed;
		if (totalAmount < amount)
			amount = totalAmount;
		if (tabSlotFrom == -1)
			removed = main.removeById(id, amount);
		else
			removed = tabs.get(tabSlotFrom).removeById(id, amount);
		cleanup();
		refresh();
		return removed;
	}

	/**
	 * Gets the main.
	 * 
	 * @return the main
	 */
	public ItemContainer getMain() {
		return main;
	}

	/**
	 * Gets the tabs.
	 * 
	 * @return the tabs
	 */
	public ArrayList<ItemContainer> getTabs() {
		return tabs;
	}

	/**
	 * Refresh.
	 */
	public void refresh() {
		for (ContainerDef def : main.getDefs())
			player.getEventWriter().sendItems(def.interfaceId, def.childId, def.type, getItems());
		player.getEventWriter().sendString(Utilities.toProperCase(player.getUsername()) + "'s Bank", 762, 24);
		player.getEventWriter().sendString("" + main.countFilledSlots(), 762, 97);
		player.getEventWriter().sendString("" + main.getLength(), 762, 98);
		// BankUtilities.sendBankTabConfig(player);
	}

	/**
	 * Gets the items.
	 * 
	 * @return the items
	 */
	public Item[] getItems() {
		ArrayList<Item> items = new ArrayList<Item>();
		for (int i = 0; i < tabs.size(); i++)
			for (Item item : tabs.get(i).getItems())
				if (item != null)
					items.add(item);
		for (Item item : main.getItems())
			if (item != null)
				items.add(item);
		return items.toArray(new Item[items.size()]);
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
		int slotOfItem = getSlotOfItem(itemId);
		if (slotOfItem == -1)
			return getItems().length < BankUtilities.BANK_SIZE;
		else {
			int tab = this.getTabBySlot(slotOfItem);
			ItemContainer container = main;
			if (tab != -1)
				container = tabs.get(tab);
			Item item = container.getItem(slotOfItem);
			return item != null && ((Integer.MAX_VALUE - item.getAmount()) > itemN);
		}
	}

	/**
	 * Gets the slot of item.
	 * 
	 * @param itemId
	 *            the item id
	 * @return the slot of item
	 */
	public int getSlotOfItem(int itemId) {
		for (int i = 0; i < getItems().length; i++)
			if (getItems()[i].getId() == itemId || (ItemDef.forId(itemId).isNoted() && (itemId - 1) == getItems()[i].getId()))
				return i;
		return -1;
	}

	/**
	 * Gets the tab by slot.
	 * 
	 * @param slot
	 *            the slot
	 * @return the tab by slot
	 */
	public int getTabBySlot(int slot) {
		int filledSlots = 0;
		for (int i = 0; i < tabs.size(); i++) {
			filledSlots += tabs.get(i).countFilledSlots();
			if (slot < filledSlots)
				return i;
		}
		return -1;
	}

	/**
	 * Cleanup.
	 */
	public void cleanup() {
		ArrayList<ItemContainer> temp = new ArrayList<ItemContainer>();
		for (ItemContainer container : tabs)
			if (container != null && container.countFilledSlots() > 0)
				temp.add(container);
		tabs.clear();
		for (int i = 0; i < temp.size(); i++)
			tabs.add(i, temp.get(i));
	}

	/**
	 * Checks if is bank open.
	 * 
	 * @return true, if is bank open
	 */
	public boolean isBankOpen() {
		return bankOpen;
	}

	/**
	 * Sets the bank open.
	 * 
	 * @param open
	 *            the new bank open
	 */
	public void setBankOpen(boolean open) {
		this.bankOpen = open;
	}

	/**
	 * Gets the blank tab.
	 * 
	 * @param player
	 *            the player
	 * @return the blank tab
	 */
	public static final ItemContainer getBlankTab(Player player) {
		return new ItemContainer(BankUtilities.BANK_SIZE, true, true, false, new ContainerDef[] { new ContainerDef(-1, 64207, 95) }, player);
	}
}