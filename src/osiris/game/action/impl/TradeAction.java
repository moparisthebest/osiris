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
import osiris.game.model.Player;
import osiris.game.model.item.Trade;

// TODO: Auto-generated Javadoc
/**
 * The Class TradeAction.
 * 
 * @author Boomer
 */
public class TradeAction extends Action {

	/** The trade. */
	private Trade trade;

	/** The player. */
	private Player player;

	/**
	 * Instantiates a new trade action.
	 * 
	 * @param player
	 *            the player
	 * @param trade
	 *            the trade
	 */
	public TradeAction(Player player, Trade trade) {
		super(player);
		this.trade = trade;
		this.player = player;
		player.setTradeRequest(null);
		run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
		trade.cancel(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
		trade.openTrade(player);
	}

	/**
	 * Gets the trade.
	 * 
	 * @return the trade
	 */
	public Trade getTrade() {
		return trade;
	}
}