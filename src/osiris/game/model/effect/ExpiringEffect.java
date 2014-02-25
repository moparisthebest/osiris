package osiris.game.model.effect;

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
 * The Class ExpiringEffect.
 * 
 * @author Boomer
 */
public abstract class ExpiringEffect extends Effect {

	/** The timer. */
	private StopWatch timer;

	/** The cooldown. */
	private int cooldown;

	/**
	 * Instantiates a new expiring effect.
	 * 
	 * @param cooldown
	 *            the cooldown
	 */
	public ExpiringEffect(Integer cooldown) {
		this.cooldown = cooldown;
		timer = new StopWatch();
	}

	/**
	 * Checks for cooled off.
	 * 
	 * @return true, if successful
	 */
	public boolean hasCooledOff() {
		return cooldown <= timer.elapsed();
	}

	/**
	 * Gets the timer.
	 * 
	 * @return the timer
	 */
	public StopWatch getTimer() {
		return timer;
	}

	/**
	 * Cancel.
	 */
	public void cancel() {
		this.cooldown = 0;
	}

	/**
	 * Gets the cooldown.
	 * 
	 * @return the cooldown
	 */
	public int getCooldown() {
		return cooldown;
	}

	/**
	 * Sets the cooldown.
	 * 
	 * @param cooldown
	 *            the cooldown
	 * @return the expiring effect
	 */
	public ExpiringEffect setCooldown(int cooldown) {
		this.cooldown = cooldown;
		return this;
	}

}
