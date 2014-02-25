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

import osiris.ServerEngine;

// TODO: Auto-generated Javadoc

/**
 * The Class StopWatch.
 * 
 * @author Boomer
 * 
 */
public class StopWatch {

	/**
	 * The start ticks.
	 */
	private int startTicks;

	/**
	 * Instantiates a new tick timer.
	 */
	public StopWatch() {
		this.startTicks = ServerEngine.getTicks();
	}

	/**
	 * Sets the ticks.
	 * 
	 * @param ticks
	 *            the ticks
	 * @return the stop watch
	 */
	public StopWatch setTicks(int ticks) {
		startTicks = ServerEngine.getTicks() - ticks;
		return this;
	}

	/**
	 * Elapsed.
	 * 
	 * @return the int
	 */
	public int elapsed() {
		return ServerEngine.getTicks() - startTicks;
	}

	/**
	 * Reset.
	 */
	public void reset() {
		this.startTicks = ServerEngine.getTicks();
	}

	/**
	 * Gets the start.
	 * 
	 * @return the start
	 */
	public int getStart() {
		return startTicks;
	}

}
