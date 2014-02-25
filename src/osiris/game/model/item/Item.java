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

/**
 * The Class Item.
 * 
 * @author Boomer
 * 
 */
public class Item {

	/**
	 * The item n.
	 */
	private int itemId;

	/**
	 * The item n.
	 */
	private int itemN;

	/**
	 * The respawns.
	 */
	private boolean respawns;

	/**
	 * Instantiates a new item.
	 * 
	 * @param itemId
	 *            the item id
	 */
	public Item(int itemId) {
		this.itemId = itemId;
		this.itemN = 1;
	}

	/**
	 * Instantiates a new item.
	 * 
	 * @param itemId
	 *            the item id
	 * @param itemN
	 *            the item n
	 */
	public Item(int itemId, int itemN) {
		this.itemId = itemId;
		this.itemN = itemN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Item[id=" + itemId + " amount=" + itemN + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Item clone() {
		return new Item(itemId, itemN);
	}

	/**
	 * Sets the respawns.
	 * 
	 * @param respawns
	 *            the respawns
	 * @return the item
	 */
	public Item setRespawns(boolean respawns) {
		this.respawns = respawns;
		return this;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return itemId;
	}

	/**
	 * Gets the amt.
	 * 
	 * @return the amt
	 */
	public int getAmount() {
		return itemN;
	}

	/**
	 * Respawns.
	 * 
	 * @return true, if successful
	 */
	public boolean respawns() {
		return respawns;
	}

	/**
	 * Sets the amount.
	 * 
	 * @param itemN
	 *            the new amount
	 */
	public void setAmount(int itemN) {
		this.itemN = itemN;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(int id) {
		this.itemId = id;
	}

	/**
	 * Adjust amount.
	 * 
	 * @param amount
	 *            the amount
	 * @return true, if successful
	 */
	public boolean adjustAmount(int amount) {
		if (amount < 0 && Math.abs(amount) > itemN)
			return false;
		else if (amount > 0 && (Integer.MAX_VALUE - itemN) < amount)
			itemN = Integer.MAX_VALUE;
		else
			this.itemN += amount;
		return true;
	}

	/**
	 * Creates the.
	 * 
	 * @param itemId
	 *            the item id
	 * @return the item
	 */
	public static Item create(int itemId) {
		return new Item(itemId);
	}

	/**
	 * Creates the.
	 * 
	 * @param itemId
	 *            the item id
	 * @param itemN
	 *            the item n
	 * @return the item
	 */
	public static Item create(int itemId, int itemN) {
		return new Item(itemId, itemN);
	}

}