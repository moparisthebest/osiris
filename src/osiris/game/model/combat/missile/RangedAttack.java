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

import osiris.game.model.item.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class RangedAttack.
 * 
 * @author Boomer
 */
@SuppressWarnings("unused")
public class RangedAttack {

	/** The range bonus. */
	private int projectileId, pullback, rangeBonus;

	/** The arrow. */
	private Item arrow;

	/** The chance spec. */
	private double chanceSpec;

	/**
	 * Instantiates a new ranged attack.
	 * 
	 * @param projectileId
	 *            the projectile id
	 * @param pullback
	 *            the pullback
	 * @param rangeBonus
	 *            the range bonus
	 * @param arrow
	 *            the arrow
	 */
	public RangedAttack(int projectileId, int pullback, int rangeBonus, Item arrow) {
		this.rangeBonus = rangeBonus;
		this.projectileId = projectileId;
		this.pullback = pullback;
		this.arrow = arrow;
	}

	/**
	 * Gets the projectile id.
	 * 
	 * @return the projectile id
	 */
	public int getProjectileId() {
		return projectileId;
	}

	/**
	 * Gets the pullback.
	 * 
	 * @return the pullback
	 */
	public int getPullback() {
		return pullback;
	}

	/**
	 * Gets the arrow.
	 * 
	 * @return the arrow
	 */
	public Item getArrow() {
		return arrow;
	}

	/**
	 * Gets the range bonus.
	 * 
	 * @return the range bonus
	 */
	public int getRangeBonus() {
		return rangeBonus;
	}

}
