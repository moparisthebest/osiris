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

import osiris.util.StopWatch;

// TODO: Auto-generated Javadoc
/**
 * The Class HitResult.
 * 
 * @author Boomer
 * 
 */
public class HitResult {

	/** The attacker u id. */
	private final int damage, attackerUId;

	/** The attack type. */
	private final HitType hitType;

	/** The decay. */
	private StopWatch decay;

	/**
	 * Instantiates a new hit result.
	 * 
	 * @param damage
	 *            the damage
	 * @param hitType
	 *            the attack type
	 * @param attackerUId
	 *            the attacker u id
	 */
	public HitResult(int damage, HitType hitType, int attackerUId) {
		this.damage = damage;
		this.hitType = hitType;
		this.attackerUId = attackerUId;
		this.decay = new StopWatch();
	}

	/**
	 * Gets the damage.
	 * 
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Gets the hit type.
	 * 
	 * @return the hit type
	 */
	public int getHitType() {
		if (hitType == HitType.POISON)
			return 2;
		else if (hitType == HitType.BURN)
			return 3;
		else
			return getDamage() == 0 ? 0 : 1;
	}

	/**
	 * Gets the attacker u id.
	 * 
	 * @return the attacker u id
	 */
	public int getAttackerUId() {
		return attackerUId;
	}

	/**
	 * Gets the decay.
	 * 
	 * @return the decay
	 */
	public int getDecay() {
		return decay.elapsed();
	}

}
