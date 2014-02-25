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

import osiris.game.model.Character;
import osiris.game.model.item.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemOnItemAction.
 * 
 * @author Blakeman8192
 */
public class ItemOnItemAction extends ItemAction {

	/** The used on. */
	private final Item usedOn;

	/** The used on slot. */
	private final int usedOnSlot;

	/**
	 * Instantiates a new item on item action.
	 * 
	 * @param character
	 *            the character
	 * @param first
	 *            the first
	 * @param slot
	 *            the slot
	 * @param usedOn
	 *            the used on
	 * @param usedOnSlot
	 *            the used on slot
	 */
	public ItemOnItemAction(Character character, Item first, int slot, Item usedOn, int usedOnSlot) {
		super(character, first, slot);
		this.usedOn = usedOn;
		this.usedOnSlot = usedOnSlot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets the used on.
	 * 
	 * @return the used on
	 */
	public Item getUsedOn() {
		return usedOn;
	}

	/**
	 * Gets the used on slot.
	 * 
	 * @return the used on slot
	 */
	public int getUsedOnSlot() {
		return usedOnSlot;
	}

}
