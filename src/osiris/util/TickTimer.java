package osiris.util;

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

import java.util.concurrent.TimeUnit;

// TODO: Auto-generated Javadoc
/**
 * The Class TickTimer.
 * 
 * @author Boomer
 */
public class TickTimer {

	/** The ticks required. */
	private long ticksRequired;

	/** The stop watch. */
	private StopWatch stopWatch;

	/**
	 * Instantiates a new tick timer.
	 */
	public TickTimer() {
		this.stopWatch = new StopWatch();
	}

	/**
	 * Sets the delay.
	 * 
	 * @param time
	 *            the time
	 * @param unit
	 *            the unit
	 * @return the tick timer
	 */
	public TickTimer setDelay(long time, TimeUnit unit) {
		if (unit != null)
			this.ticksRequired = Utilities.secondsToTicks(unit.toSeconds(time));
		else
			this.ticksRequired = time;
		stopWatch.reset();
		return this;
	}

	/**
	 * Completed.
	 * 
	 * @return true, if successful
	 */
	public boolean completed() {
		return this.stopWatch.elapsed() >= ticksRequired;
	}
}
