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
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class EquipmentScreenAction.
 * 
 * @author Boomer
 */
public class EquipmentScreenAction extends Action {

	/**
	 * Instantiates a new action.
	 * 
	 * @param character
	 *            the character
	 */
	public EquipmentScreenAction(Character character) {
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
		getPlayer().getMovementQueue().reset();
		getPlayer().getEventWriter().openSideLockingInterface(667, 149);
		int[] bonuses = new int[Settings.BONUSES.length];
		for (Item item : getPlayer().getEquipment().getItems()) {
			if (item != null) {
				ItemDef def = ItemDef.forId(item.getId());
				for (int i = 0; i < def.getBonuses().length; i++) {
					if (i == 12) {
						continue;
					}
					bonuses[i] += def.getBonus(i);
				}
			}
		}
		getPlayer().getEventWriter().sendBonus(bonuses);
	}
}
