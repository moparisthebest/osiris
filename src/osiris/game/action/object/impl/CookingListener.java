package osiris.game.action.object.impl;

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

import osiris.game.action.ObjectActionListener;
import osiris.game.action.object.ItemOnObjectAction;
import osiris.game.action.object.ObjectAction;
import osiris.game.model.def.ItemDef;
import osiris.game.model.skills.Cooking;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving cooking events. The class that is
 * interested in processing a cooking event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addCookingListener<code> method. When
 * the cooking event occurs, that object's appropriate
 * method is invoked.
 * 
 * @author Boomer
 */
public class CookingListener implements ObjectActionListener {

	/** The fire. */
	private boolean fire;

	/**
	 * Instantiates a new cooking listener.
	 * 
	 * @param fire
	 *            the fire
	 */
	public CookingListener(boolean fire) {
		this.fire = fire;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.action.ObjectActionListener#onObjectAction(osiris.game.action
	 * .object.ObjectAction)
	 */
	@Override
	public void onObjectAction(ObjectAction action) {
		if (!(action instanceof ItemOnObjectAction))
			return;
		String recipeName = Cooking.toRecipeName(ItemDef.forId(((ItemOnObjectAction) action).getItem().getId()).getName());
		System.out.println("RECIPE: " + recipeName);
		Cooking.Recipe recipe = null;
		try {
			recipe = Cooking.Recipe.valueOf(recipeName);
		} catch (IllegalArgumentException ignored) {
		}
		if (recipe != null)
			Cooking.cookItem(action.getPlayer(), action.getObjectPosition(), ((ItemOnObjectAction) action).getItemSlot(), recipe, fire);
		else
			action.getPlayer().getEventWriter().sendMessage("You can not cook that item!");
	}
}
