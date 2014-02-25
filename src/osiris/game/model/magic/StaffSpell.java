package osiris.game.model.magic;

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
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class StaffSpell.
 * 
 * @author Boomer
 */
public class StaffSpell extends CombatSpell {

	/** The staff required. */
	private int staffRequired;

	/**
	 * Instantiates a new staff spell.
	 * 
	 * @param level
	 *            the level
	 * @param animation
	 *            the animation
	 * @param graphic
	 *            the graphic
	 * @param expEarned
	 *            the exp earned
	 * @param runesRequired
	 *            the runes required
	 * @param maxDamage
	 *            the max damage
	 * @param hitDelay
	 *            the hit delay
	 * @param projectileId
	 *            the projectile id
	 * @param hitGraphic
	 *            the hit graphic
	 * @param staffRequired
	 *            the staff required
	 */
	public StaffSpell(int level, int animation, int graphic, int expEarned, int[][] runesRequired, int maxDamage, int hitDelay, int projectileId, int hitGraphic, int staffRequired) {
		super(level, animation, graphic, expEarned, runesRequired, maxDamage, hitDelay, projectileId, hitGraphic);
		this.staffRequired = staffRequired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.magic.CombatSpell#onExecution(osiris.game.model.Character
	 * , java.lang.Object[])
	 */
	@Override
	public boolean onExecution(Character character, Object... parameters) {
		if (character instanceof Player) {
			Item weapon = ((Player) character).getEquipment().getItem(Settings.SLOT_WEAPON);
			if (weapon == null || weapon.getId() != staffRequired) {
				((Player) character).getEventWriter().sendMessage("You need to wield a " + ItemDef.forId(staffRequired).getName() + " in order to cast this spell!");
				return false;
			}
		}
		return super.onExecution(character, parameters);
	}

}
