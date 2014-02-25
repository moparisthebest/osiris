package osiris.game.model;

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
import osiris.game.action.impl.BankAction;
import osiris.game.action.impl.TradeAction;

/**
 * @author Boomer
 * 
 */
public class XValue {

	private Player player;
	private Action action;
	private int itemSlot;
	private boolean toInventory;

	public XValue(Player player, Action action) {
		this.player = player;
		this.action = action;
		this.itemSlot = -1;
		this.toInventory = false;
		player.setXValue(this);
		player.getEventWriter().runScript(108, new Object[] { "Enter amount." }, "s");
	}

	public XValue(Player player, Action action, int itemSlot, boolean toInventory) {
		this(player, action);
		this.itemSlot = itemSlot;
		this.toInventory = toInventory;
	}

	public boolean execute(int value) {
		if (action == null || player.getCurrentAction() == null || !player.getCurrentAction().equals(action) || value == 0)
			return cancel(false);
		else {
			if (action instanceof TradeAction) {
				if (itemSlot == -1)
					return cancel(false);
				if (toInventory)
					((TradeAction) action).getTrade().removeTradeItem(player, itemSlot, value);
				else
					((TradeAction) action).getTrade().addTradeItem(player, itemSlot, value);
			} else if (action instanceof BankAction) {
				if (itemSlot == -1)
					return cancel(false);
				if (toInventory)
					action.getPlayer().withdrawItem(itemSlot, value);
				else
					action.getPlayer().depositItem(itemSlot, value);
			}
			cancel(true);
		}
		return true;
	}

	public boolean cancel(boolean finished) {
		player.getEventWriter().sendCloseChatboxInterface();
		player.setXValue(null);
		return finished;
	}
}
