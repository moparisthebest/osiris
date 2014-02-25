package osiris.game.action.impl;

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

import osiris.game.action.Action;
import osiris.game.model.Character;

// TODO: Auto-generated Javadoc
/**
 * Displays the "What would you like to build?" chat interface.
 * 
 * @author samuraiblood2
 * 
 */
public class DisplaySelectOptionAction extends Action {

	/** The interface ID. */
	private int id;

	/** The size of the model's being displayed. */
	private int size;

	/** The various item ID's. */
	private int[] items;

	/**
	 * Instantiates a new display select option event.
	 * 
	 * @param character
	 *            the character
	 * @param id
	 *            The interface ID.
	 * @param size
	 *            The size of the model's being displayed.
	 * @param items
	 *            The various item ID's.
	 */
	public DisplaySelectOptionAction(Character character, int id, int size, int[] items) {
		super(character);
		this.id = id;
		this.size = size;
		this.items = items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
		getPlayer().getEventWriter().sendCloseChatboxInterface();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {

		/**
		 * The child interface. XXX: This should remain 2 until more then one
		 * item is being displayed.
		 */
		int child = 2;

		/**
		 * Displays the interface.
		 */
		getPlayer().getEventWriter().sendChatboxInterface(id);

		/**
		 * Iterates through all the item ID's and displays the models on the
		 * specified interface.
		 */
		for (int i = 0; i < items.length; i++) {
			getPlayer().getEventWriter().sendInterfaceItem(id, child++, size, items[i]);
		}
	}
}
