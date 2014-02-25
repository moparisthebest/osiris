package osiris.game.action.object;

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

import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.game.model.item.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemOnObjectAction.
 * 
 * @author Boomer
 */

public class ItemOnObjectAction extends ObjectAction {

	/** The item. */
	private Item item;

	/** The item slot. */
	private int itemSlot;

	/**
	 * Instantiates a new object action task.
	 * 
	 * @param character
	 *            the character
	 * @param position
	 *            the position
	 * @param distance
	 *            the distance
	 * @param type
	 *            the type
	 * @param objectID
	 *            the object id
	 * @param objectX
	 *            the object x
	 * @param objectY
	 *            the object y
	 * @param itemSlot
	 *            the item slot
	 * @param itemId
	 *            the item id
	 */
	public ItemOnObjectAction(Character character, Position position, int distance, ObjectActionType type, int objectID, int objectX, int objectY, int itemSlot, int itemId) {
		super(character, position, distance, type, objectID, objectX, objectY);
		if (!(character instanceof Player)) {
			System.out.println("Cancelled");
			cancel();
			return;
		}
		Item existing = getPlayer().getInventory().getItem(itemSlot);
		if (existing == null || existing.getId() != itemId)
			return;
		this.item = existing;
		this.itemSlot = itemSlot;
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
	 * Gets the item slot.
	 * 
	 * @return the item slot
	 */
	public int getItemSlot() {
		return itemSlot;
	}
}
