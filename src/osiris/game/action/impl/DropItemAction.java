package osiris.game.action.impl;

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
import osiris.game.model.ground.GroundItem;
import osiris.game.model.ground.GroundManager;
import osiris.game.model.item.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class DropItemAction.
 * 
 * @author Boomer
 */
public class DropItemAction extends Action {

	/** The item id. */
	private int slot, itemId;

	/**
	 * Instantiates a new action.
	 * 
	 * @param character
	 *            the character
	 * @param slot
	 *            the slot
	 * @param itemId
	 *            the item id
	 */
	public DropItemAction(Character character, int slot, int itemId) {
		super(character);
		this.slot = slot;
		this.itemId = itemId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
		if (getPlayer() == null || getPlayer().getInventory() == null || getPlayer().getInventory().getItem(slot) == null)
			return;
		Item item = getPlayer().getInventory().getItem(slot).clone();
		if (item == null || item.getId() != itemId)
			return;
		if (!getPlayer().getInventory().removeBySlot(slot, item.getAmount()))
			return;
		GroundItem ground = new GroundItem(item, getPlayer());
		GroundManager.getManager().dropItem(ground);

	}
}
