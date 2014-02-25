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

/**
 * The Enum SpellBook.
 * 
 * @author Boomer
 */
public enum SpellBook {

	NORMAL(192, -1), ANCIENT(193, -1), LUNAR(192, -1);

	/** The auto interface id. */
	int interfaceId, autoInterfaceId;

	/**
	 * Instantiates a new spell book.
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @param autoInterfaceId
	 *            the auto interface id
	 */
	SpellBook(int interfaceId, int autoInterfaceId) {
		this.interfaceId = interfaceId;
		this.autoInterfaceId = autoInterfaceId;
	}

	/**
	 * Gets the interface id.
	 * 
	 * @return the interface id
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the auto interface id.
	 * 
	 * @return the auto interface id
	 */
	public int getAutoInterfaceId() {
		return autoInterfaceId;
	}

}
