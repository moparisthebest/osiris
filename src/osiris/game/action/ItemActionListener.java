package osiris.game.action;

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

import osiris.game.action.item.ItemAction;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving itemAction events. The class that is
 * interested in processing a itemAction event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addItemActionListener<code> method. When
 * the itemAction event occurs, that object's appropriate
 * method is invoked.
 * 
 * @author Blakeman8192
 */
public interface ItemActionListener {

	/**
	 * On item action.
	 * 
	 * @param action
	 *            the action
	 */
	public void onItemAction(ItemAction action);

}
