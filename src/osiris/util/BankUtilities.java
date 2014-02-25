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

/**
 * The Class BankUtilities.
 * 
 * @author Boomer
 * 
 */
public class BankUtilities {

	/**
	 * The Constant BANK_SIZE.
	 */
	public static final int BANK_SIZE = 500;

	/**
	 * Tab id to container slot.
	 * 
	 * @param tabId
	 *            the tab id
	 * @return the int
	 */
	public static int tabIdToContainerSlot(int tabId) {
		switch (tabId) {
		case 39:
		case 52:
			return 0;
		case 37:
		case 53:
			return 1;
		case 35:
		case 54:
			return 2;
		case 33:
		case 55:
			return 3;
		case 31:
		case 56:
			return 4;
		case 29:
		case 57:
			return 5;
		case 27:
		case 58:
			return 6;
		case 25:
		case 59:
			return 7;
		case 41:
		case 51:
			return -1;
		}
		return -1;
	}

}
