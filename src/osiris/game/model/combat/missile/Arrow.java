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
 * The Enum Arrow.
 * 
 * @author Boomer
 */
public enum Arrow {

	DRAGON(1120, 1116, 1111, 60), RUNE(15, 24, 1109, 49), ADAMANT(13, 22, 1108, 31), MITHRIL(12, 21, 1107, 22), BLACK(14, 23, -1, 18), STEEL(11, 20, 1106, 16), IRON(9, 18, 1105, 10), BRONZE(10, 19, 1104, 7), ICE(16, 25, 1110, 16), FIRE(17, 26, 1113, 10);

	/** The Constant ARROWS. */
	public static final Arrow[] ARROWS = { DRAGON, RUNE, ADAMANT, MITHRIL, BLACK, STEEL, IRON, BRONZE, ICE, FIRE };

	/** The double pull back. */
	private int projectileId, pullback, rangeBonus, doublePullBack;

	/**
	 * Instantiates a new arrow.
	 * 
	 * @param projectileId
	 *            the projectile id
	 * @param pullback
	 *            the pullback
	 * @param doublePullBack
	 *            the double pull back
	 * @param rangeBonus
	 *            the range bonus
	 */
	Arrow(int projectileId, int pullback, int doublePullBack, int rangeBonus) {
		this.projectileId = projectileId;
		this.pullback = pullback;
		this.rangeBonus = rangeBonus;
		this.doublePullBack = doublePullBack;
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
		return getAmmo(character, slot, 1, true);
	}

	/**
	 * Gets the ammo.
	 * 
	 * @param character
	 *            the character
	 * @param slot
	 *            the slot
	 * @param arrows
	 *            the arrows
	 * @param requiresAll
	 *            the requires all
	 * @return the ammo
	 */
	public static RangedAttack getAmmo(Character character, int slot, int arrows, boolean requiresAll) {
		try {
			Arrow found = null;
			String itemKey = "arrows";
			if (character instanceof Npc) {
				found = BRONZE;
				return new RangedAttack(found.projectileId, found.pullback, 0, null);
			} else if (character instanceof Player) {
				Item item = ((Player) character).getEquipment().getItem(slot);
				if (requiresAll) {
					if (item == null || !((Player) character).getEquipment().removeBySlot(slot, arrows)) {
						((Player) character).getEventWriter().sendMessage("You do not have enough ammo!");
						arrows = 0;
					}
				} else
					for (int i = 0; i < arrows; i++) {
						if (item == null || !((Player) character).getEquipment().removeBySlot(slot, 1)) {
							((Player) character).getEventWriter().sendMessage("You do not have enough ammo!");
							arrows = i;
							break;
						}
					}
				if (arrows == 0)
					return null;
				String name = ItemDef.forId(item.getId()).getName().replaceAll(" " + itemKey, "").replaceAll(" arrow", "").replaceAll("\\(e\\)", "").replaceAll("\\+", "").replaceAll("\\(p", "").replaceAll("\\)", "");
				for (Arrow arrow : ARROWS) {
					if (arrow.toString().equalsIgnoreCase(name)) {
						return new RangedAttack(arrow.projectileId, ((arrows == 2 && arrow.doublePullBack != -1) ? arrow.doublePullBack : arrow.pullback), arrow.rangeBonus, Item.create(item.getId(), arrows));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
