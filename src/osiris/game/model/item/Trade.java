package osiris.game.model.item;

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
import osiris.game.action.impl.TradeAction;
import osiris.game.model.Player;
import osiris.game.model.def.ItemDef;

// TODO: Auto-generated Javadoc
/**
 * The Class Trade.
 * 
 * @author Boomer
 */
@SuppressWarnings("unused")
public class Trade {

	/** The exchange stage. */
	private boolean tradeOpen, exchangeStage;

	/** The players. */
	private Player[] players;

	/** The containers. */
	private ItemContainer[][] containers;

	/** The player accepted. */
	private Player playerAccepted;

	/** The Constant TRADE_INTERFACE. */
	public static final ContainerDef[] TRADE_INTERFACE = { new ContainerDef(-1, 2, 90), new ContainerDef(-2, 60981, 90) };

	/**
	 * Instantiates a new trade.
	 * 
	 * @param players
	 *            the players
	 */
	public Trade(Player[] players) {
		this.players = players;
		containers = new ItemContainer[players.length][2];
		playerAccepted = null;
		for (int i = 0; i < players.length; i++) {
			containers[i][0] = new ItemContainer(28, true, false, true, new ContainerDef[] { TRADE_INTERFACE[0] }, players[i]);
			containers[i][1] = new ItemContainer(28, true, false, true, new ContainerDef[] { TRADE_INTERFACE[1] }, players[i]);
		}
	}

	/**
	 * Open trade.
	 * 
	 * @param player
	 *            the player
	 */
	public void openTrade(Player player) {
		player.getEventWriter().openSideLockingInterface(335, 336);
		player.getEventWriter().setAccessMask(1026, 30, 335, 0, 27);
		player.getEventWriter().setAccessMask(1026, 32, 335, 0, 27);
		player.getEventWriter().setAccessMask(1278, 0, 336, 0, 27);
		Object[] tparams1 = new Object[] { "", "", "", "Value", "Remove-X", "Remove-All", "Remove-10", "Remove-5", "Remove", -1, 0, 7, 4, 90, 21954590 };
		Object[] tparams2 = new Object[] { "", "", /* Lend */"", "Value", "Offer-X", "Offer-All", "Offer-10", "Offer-5", "Offer", -1, 0, 7, 4, 93, 22020096 };
		Object[] tparams3 = new Object[] { "", "", "", "", "", "", "", "", "Value<col=FF9040>", -1, 0, 7, 4, 90, 21954592 };
		player.getEventWriter().runScript(150, tparams1, "IviiiIsssssssss");
		player.getEventWriter().runScript(150, tparams2, "IviiiIsssssssss");
		player.getEventWriter().runScript(695, tparams3, "IviiiIsssssssss");
		refreshTradeScreen(false);
		tradeOpen = true;
		exchangeStage = true;

	}

	/**
	 * Adds the trade item.
	 * 
	 * @param player
	 *            the player
	 * @param slot
	 *            the slot
	 * @param amount
	 *            the amount
	 */
	public void addTradeItem(Player player, int slot, int amount) {
		if (!exchangeStage)
			return;
		int index = -1;
		for (int i = 0; i < players.length; i++)
			if (players[i].equals(player))
				index = i;
		if (index == -1)
			return;
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (!ItemDef.forId(item.getId()).isTradeable() && player.getPlayerStatus() < 2) {
			player.getEventWriter().sendMessage("That item is untradeable!");
			return;
		}
		int amountOfItem = player.getInventory().amountOfItem(item.getId());
		if (amountOfItem < amount)
			amount = amountOfItem;
		int amountCanHold = containers[index][0].amountCanHold(item.getId());
		if (amountCanHold < amount)
			amount = amountCanHold;
		if (player.getInventory().removeById(item.getId(), amount)) {
			containers[index][0].add(item.getId(), amount);
			containers[1 - index][1].setItems(containers[index][0].getItems());
			containers[1 - index][1].refresh();
		}
		playerAccepted = null;
		refreshTradeScreen(false);
	}

	/**
	 * Removes the trade item.
	 * 
	 * @param player
	 *            the player
	 * @param slot
	 *            the slot
	 * @param amount
	 *            the amount
	 */
	public void removeTradeItem(Player player, int slot, int amount) {
		if (!exchangeStage)
			return;
		int index = -1;
		for (int i = 0; i < players.length; i++)
			if (players[i].equals(player))
				index = i;
		if (index == -1)
			return;
		Item item = containers[index][0].getItem(slot);
		if (item == null)
			return;
		int amountOfItem = containers[index][0].amountOfItem(item.getId());
		if (amountOfItem < amount)
			amount = amountOfItem;
		int amountCanHold = player.getInventory().amountCanHold(item.getId());
		if (amountCanHold < amount)
			amount = amountCanHold;
		if (containers[index][0].removeById(item.getId(), amount)) {
			containers[1 - index][1].setItems(containers[index][0].getItems());
			containers[1 - index][1].refresh();
			player.getInventory().add(item.getId(), amount);
		}
		playerAccepted = null;
		refreshTradeScreen(false);
	}

	/**
	 * Cancel.
	 * 
	 * @param completed
	 *            the completed
	 */
	public void cancel(boolean completed) {
		for (int p = 0; p < getPlayers().length; p++) {
			if (!completed)
				players[p].addAllItems(containers[p][0].getItems());
			for (int i = 0; i < containers[p].length; i++)
				containers[p][i].empty();
			players[p].getEventWriter().removeSideLockingInterface();

			Action action = players[p].getCurrentAction();
			if (action != null && action instanceof TradeAction)
				players[p].setCurrentAction(null);
			if (players[p].getXValue() != null)
				players[p].getXValue().cancel(false);
		}
		tradeOpen = false;
	}

	/**
	 * Confirmation screen.
	 */
	public void confirmationScreen() {
		exchangeStage = false;
		for (int p = 0; p < players.length; p++) {
			ItemContainer received = containers[1 - p][0];
			if (!players[p].getInventory().canFitAll(received.getItems())) {
				players[p].getEventWriter().sendMessage("You do not have enough room to accept this trade!");
				players[1 - p].getEventWriter().sendMessage("The other player does not have enough room!");
				playerAccepted = null;
				refreshTradeScreen(false);
				return;
			}
		}
		for (int p = 0; p < players.length; p++) {
			players[p].getEventWriter().sendInterface(334);
			players[p].getEventWriter().updateTabs(false);
		}
		playerAccepted = null;
		refreshTradeScreen(true);

	}

	/**
	 * Complete trade.
	 */
	public void completeTrade() {
		for (int p = 0; p < players.length; p++) {
			ItemContainer received = containers[1 - p][0];
			players[p].addAllItems(received.getItems());
			players[p].getEventWriter().sendMessage("Trade completed.");
		}
		cancel(true);
	}

	/**
	 * Gets the players.
	 * 
	 * @return the players
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * Gets the other.
	 * 
	 * @param player
	 *            the player
	 * @return the other
	 */
	public Player getOther(Player player) {
		for (Player other : players)
			if (!other.equals(player))
				return other;
		return null;
	}

	/**
	 * Accept.
	 * 
	 * @param player
	 *            the player
	 * @param confirmationScreen
	 *            the confirmation screen
	 */
	public void accept(Player player, boolean confirmationScreen) {
		if (playerAccepted == null) {
			playerAccepted = player;
			refreshTradeScreen(confirmationScreen);
		} else if (!playerAccepted.equals(player)) {
			if (!confirmationScreen)
				confirmationScreen();
			else
				completeTrade();
		}
	}

	/**
	 * Refresh trade screen.
	 * 
	 * @param confirmationScreen
	 *            the confirmation screen
	 */
	public void refreshTradeScreen(boolean confirmationScreen) {
		if (!confirmationScreen) {
			for (int p = 0; p < players.length; p++) {
				if (playerAccepted == null)
					players[p].getEventWriter().sendString(" ", 335, 36);
				else if (playerAccepted.equals(players[p]))
					players[p].getEventWriter().sendString("Waiting for the other player to accept...", 335, 36);
				else
					players[p].getEventWriter().sendString("Other player has accepted the trade.", 335, 36);
				players[p].getEventWriter().sendString("Trading With: " + players[1 - p].getUsername(), 335, 15);
			}
		} else {
			String[][] itemStrings = new String[players.length][2];
			for (int p = 0; p < players.length; p++) {
				itemStrings[p][0] = formItemString(containers[p][0], players[p]);
				itemStrings[p][1] = formItemString(containers[p][1], players[p]);
			}
			for (int p = 0; p < players.length; p++) {
				players[p].getEventWriter().sendString(itemStrings[p][0], 334, 37);
				players[p].getEventWriter().sendString(itemStrings[p][1], 334, 41);
				players[p].getEventWriter().sendString("<col=00FFFF>Trading with:<br>" + "<col=00FFFF>" + players[1 - p].getUsername(), 334, 46);
				players[p].getEventWriter().sendInterfaceConfig(334, 37, false);
				players[p].getEventWriter().sendInterfaceConfig(334, 41, false);
				players[p].getEventWriter().sendInterfaceConfig(334, 45, true);
				players[p].getEventWriter().sendInterfaceConfig(334, 46, false);
				if (playerAccepted == null)
					players[p].getEventWriter().sendString(" ", 334, 33);
				else if (playerAccepted.equals(players[p]))
					players[p].getEventWriter().sendString("Waiting for the other player to accept...", 334, 33);
				else
					players[p].getEventWriter().sendString("Other player has accepted the trade.", 334, 33);
			}
		}

	}

	/**
	 * Form item string.
	 * 
	 * @param container
	 *            the container
	 * @param player
	 *            the player
	 * @return the string
	 */
	public String formItemString(ItemContainer container, Player player) {
		String formation = "";
		if (container.countFilledSlots() != 0) {
			for (int i = 0; i < container.countFilledSlots(); i++) {
				Item item = container.getItem(i);
				ItemDef def = ItemDef.forId(container.getItem(i).getId());
				formation += "<col=FF9040>" + def.getName();
				if (item.getAmount() > 1)
					formation += "<col=FFFFFF> x " + item.getAmount() + "";
				formation += "<br>";
			}
		} else
			formation = "<col=FFFFFF>Absolutely nothing!";
		return formation;
	}

	/**
	 * Gets the trade items.
	 * 
	 * @param player
	 *            the player
	 * @return the trade items
	 */
	public ItemContainer getTradeItems(Player player) {
		int slot = -1;
		for (int p = 0; p < players.length; p++)
			if (players[p].equals(player))
				slot = p;
		if (slot == -1)
			return null;
		return containers[slot][0];
	}
}
