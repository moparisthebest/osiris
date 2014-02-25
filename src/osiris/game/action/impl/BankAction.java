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
import osiris.game.model.item.Bank;

// TODO: Auto-generated Javadoc
/**
 * The Class BankAction.
 * 
 * @author Boomer
 */
public class BankAction extends Action {

	/** The bank. */
	private Bank bank;

	/** The player. */
	private Player player;

	/**
	 * Instantiates a new bank action.
	 * 
	 * @param player
	 *            the player
	 */
	public BankAction(Player player) {
		super(player);
		this.bank = player.getBank();
		this.player = player;
		run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
		closeBank();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
		openBank();
	}

	/**
	 * Open bank.
	 */
	public void openBank() {
		player.getEventWriter().openSideLockingInterface(762, 763);
		player.getEventWriter().sendConfig2(563, 4194304);
		player.getEventWriter().sendConfig2(1248, -2013265920);
		player.getEventWriter().sendBankOptions();
		bank.setBankOpen(true);
	}

	/**
	 * Close bank.
	 */
	public void closeBank() {
		player.getEventWriter().removeSideLockingInterface();
		bank.setBankOpen(false);
		if (player.getXValue() != null)
			player.getXValue().cancel(false);
	}

	/**
	 * Gets the bank.
	 * 
	 * @return the bank
	 */
	public Bank getBank() {
		return bank;
	}

}