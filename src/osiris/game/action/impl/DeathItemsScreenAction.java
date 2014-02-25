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

import java.util.ArrayList;
import java.util.Arrays;

import osiris.game.action.Action;
import osiris.game.model.item.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class DeathItemsScreenAction.
 * 
 * @author Boomer
 */
public class DeathItemsScreenAction extends Action {

	/**
	 * Instantiates a new action.
	 * 
	 * @param character
	 *            the character
	 */
	public DeathItemsScreenAction(osiris.game.model.Character character) {
		super(character);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
		getPlayer().getEventWriter().removeSideLockingInterface();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
		getPlayer().getEventWriter().openSideLockingInterface(102, 149);
		getPlayer().getEventWriter().setAccessMask(211, 0, 2, 6684690, 4);
		getPlayer().getEventWriter().setAccessMask(212, 0, 2, 6684693, 42);

		ArrayList<Item> items = getPlayer().getItemsKeptOnDeath();
		int[] itemIds = new int[4];
		Arrays.fill(itemIds, -1);
		for (int i = 0; i < items.size(); i++) {
			itemIds[i] = items.get(i).getId();
		}
		Object[] params = { 11510, 12749, "", 0, itemIds[3], itemIds[2], itemIds[1], itemIds[0], items.size(), 0 };
		getPlayer().getEventWriter().runScript(118, params, "iioooiisii");
	}
}
