package osiris.game.model.combat;

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
import osiris.game.model.combat.missile.RangedAttack;
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class SpecialManager.
 * 
 * @author Boomer
 */
public class SpecialManager {

	/**
	 * Gets the special.
	 * 
	 * @param character
	 *            the character
	 * @param victim
	 *            the victim
	 * @param item
	 *            the item
	 * @param damage
	 *            the damage
	 * @param hitDelay
	 *            the hit delay
	 * @param range
	 *            the range
	 * @return the special
	 */
	public static Attack getSpecial(Character character, Character victim, Item item, int damage, int hitDelay, RangedAttack range) {
		if (item == null)
			return null;
		int index = specWeaponIndex(item.getId());
		if (index == -1)
			return null;
		Attack attack = new Attack();
		if (character instanceof Player) {
			boolean adjusted = ((Player) character).adjustSpecialEnergy(-(int) (1000 * ENERGY_REQUIRED[index]));
			if (!adjusted)
				return null;
		}
		switch (index) {
		case 0: // dragon dagger
			damage *= 1.1;
			int firstHit = Utilities.random(damage);
			int secondHit = Utilities.random(damage);
			attack.setAnimation(new int[] { 0x426 }).setAttackGfx(new int[] { 252, 100 }).setHits(new int[][] { { firstHit, hitDelay }, { secondHit, hitDelay } });
			break;
		}

		if (character instanceof Player)
			((Player) character).setSpecEnabled(false);
		return attack;
	}

	/**
	 * Spec weapon index.
	 * 
	 * @param itemId
	 *            the item id
	 * @return the int
	 */
	public static int specWeaponIndex(int itemId) {
		String wepName = ItemDef.forId(itemId).getName();
		for (int i = 0; i < SPECIAL_WEAPONS.length; i++)
			if (wepName.toLowerCase().startsWith(SPECIAL_WEAPONS[i]))
				return i;
		return -1;
	}

	/** The Constant ENERGY_REQUIRED. */
	public static final double[] ENERGY_REQUIRED = { .25 };

	/** The Constant SPECIAL_WEAPONS. */
	public static final String[] SPECIAL_WEAPONS = { "dragon dagger" };
}
