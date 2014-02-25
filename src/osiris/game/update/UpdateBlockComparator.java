package osiris.game.update;

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

import java.util.Comparator;

/**
 * Used for update block sorting.
 * 
 * @author Blake
 * 
 */
public class UpdateBlockComparator implements Comparator<UpdateBlock> {

	/** The Constant singleton. */
	private static final UpdateBlockComparator singleton = new UpdateBlockComparator();

	/**
	 * Gets the singleton.
	 * 
	 * @return the singleton
	 */
	public static UpdateBlockComparator getSingleton() {
		return singleton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */

	@Override
	public int compare(UpdateBlock o1, UpdateBlock o2) {
		return o1.getPriority() - o2.getPriority();
	}

}
