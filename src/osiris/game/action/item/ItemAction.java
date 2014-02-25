package osiris.game.action.item;

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

import osiris.game.action.Action;
import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.item.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemAction.
 * 
 * @author Blakeman8192
 */
public abstract class ItemAction extends Action {

	/** The item. */
	private final Item item;

	/** The slot. */
	private final int slot;

	/**
	 * Instantiates a new item action.
	 * 
	 * @param character
	 *            the character
	 * @param item
	 *            the item
	 * @param slot
	 *            the slot
	 */
	public ItemAction(Character character, Item item, int slot) {
		super(character);
		this.item = item;
		this.slot = slot;
		if (character instanceof Player) {
			Item inv = ((Player) character).getInventory().getItem(slot);
			if (inv == null || !inv.equals(item)) {
				cancel();
				return;
			}
		}
	}

	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Gets the slot.
	 * 
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

}
