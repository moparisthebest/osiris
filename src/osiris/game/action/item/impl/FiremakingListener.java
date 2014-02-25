package osiris.game.action.item.impl;

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

import osiris.game.action.ItemActionListener;
import osiris.game.action.item.ItemAction;
import osiris.game.action.item.ItemOnItemAction;
import osiris.game.model.def.ItemDef;
import osiris.game.model.skills.Firemaking;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving firemaking events. The class that is
 * interested in processing a firemaking event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addFiremakingListener<code> method. When
 * the firemaking event occurs, that object's appropriate
 * method is invoked.
 * 
 * @author Boomer
 */
public class FiremakingListener implements ItemActionListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.action.ItemActionListener#onItemAction(osiris.game.action
	 * .item.ItemAction)
	 */
	@Override
	public void onItemAction(ItemAction action) {
		if (!(action instanceof ItemOnItemAction))
			return;
		if (((ItemOnItemAction) action).getUsedOn() == null || action.getItem() == null)
			return;
		int slot = action.getItem().getId() == 590 ? ((ItemOnItemAction) action).getUsedOnSlot() : action.getSlot();
		Firemaking.Log log = null;
		try {
			log = Firemaking.Log.valueOf(Firemaking.getLogName(ItemDef.forId(action.getPlayer().getInventory().getItem(slot).getId()).getName()));
		} catch (IllegalArgumentException ignored) {
		}
		if (log != null)
			Firemaking.lightLog(action.getPlayer(), slot, log);
		else
			action.getPlayer().getEventWriter().sendMessage("You can not light that!");
	}
}
