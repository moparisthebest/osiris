package osiris.game.model.combat.missile;

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
import osiris.game.model.Npc;
import osiris.game.model.Player;
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;

// TODO: Auto-generated Javadoc
/**
 * The Enum Knife.
 * 
 * @author Boomer
 */
public enum Knife {

	// DRAGON(),
	/** The RUNE. */
	RUNE(218, 225, 24),
	/** The ADAMANT. */
	ADAMANT(217, 224, 14),
	/** The MITHRIL. */
	MITHRIL(216, 223, 10),
	/** The BLACK. */
	BLACK(215, 222, 8),
	/** The STEEL. */
	STEEL(214, 221, 7),
	/** The IRON. */
	IRON(213, 220, 4),
	/** The BRONZE. */
	BRONZE(212, 219, 3);

	/** The Constant KNIVES. */
	public static final Knife[] KNIVES = { RUNE, ADAMANT, MITHRIL, BLACK, STEEL, IRON, BRONZE };

	/** The range bonus. */
	private int projectileId, pullback, rangeBonus;

	/**
	 * Instantiates a new knife.
	 * 
	 * @param projectileId
	 *            the projectile id
	 * @param pullback
	 *            the pullback
	 * @param rangeBonus
	 *            the range bonus
	 */
	Knife(int projectileId, int pullback, int rangeBonus) {
		this.projectileId = projectileId;
		this.pullback = pullback;
		this.rangeBonus = rangeBonus;
	}

	/**
	 * Gets the ammo.
	 * 
	 * @param character
	 *            the character
	 * @param slot
	 *            the slot
	 * @return the ammo
	 */
	public static RangedAttack getAmmo(Character character, int slot) {
		Knife found = null;
		String itemKey = "knife";
		if (character instanceof Npc) {
			found = BRONZE;
			return new RangedAttack(found.projectileId, found.pullback, 0, null);
		} else if (character instanceof Player) {
			Item item = ((Player) character).getEquipment().getItem(slot);
			if (item == null || !((Player) character).getEquipment().removeBySlot(slot, 1)) {
				((Player) character).getEventWriter().sendMessage("You do not have enough ammo!");
				return null;
			}
			String name = ItemDef.forId(item.getId()).getName().replaceAll(" " + itemKey, "").replaceAll("\\(e\\)", "").replaceAll("\\+", "").replaceAll("\\(p", "").replaceAll("\\)", "");
			for (Knife knife : KNIVES) {
				if (knife.toString().equalsIgnoreCase(name)) {
					return new RangedAttack(knife.projectileId, knife.pullback, knife.rangeBonus, Item.create(item.getId(), 1));
				}
			}
		}
		return null;
	}
}