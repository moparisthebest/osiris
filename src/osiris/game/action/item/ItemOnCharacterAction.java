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
 * The Class ItemOnCharacterAction.
 */
public class ItemOnCharacterAction extends ItemAction {

	/** The used on. */
	private final Character usedOn;

	/**
	 * Instantiates a new item on character action.
	 * 
	 * @param character
	 *            the character
	 * @param item
	 *            the item
	 * @param slot
	 *            the slot
	 * @param usedOn
	 *            the used on
	 */
	public ItemOnCharacterAction(Character character, Item item, int slot, Character usedOn) {
		super(character, item, slot);
		this.usedOn = usedOn;
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
	public Character getUsedOn() {
		return usedOn;
	}

}
