package osiris.game.model.ground;

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

import java.util.Iterator;
import java.util.LinkedList;

import osiris.Main;
import osiris.game.model.Character;
import osiris.game.model.Npc;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.game.model.def.ItemDef;

// TODO: Auto-generated Javadoc
/**
 * The Class GroundManager.
 * 
 * @author Boomer
 */
public class GroundManager implements Runnable {

	/** The ground items. */
	private LinkedList<GroundItem> groundItems = new LinkedList<GroundItem>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			for (Iterator<GroundItem> items = groundItems.iterator(); items.hasNext();) {
				GroundItem item = items.next();
				if (item.getTimer().elapsed() >= 60) {
					boolean tradeable = ItemDef.forId(item.getItem().getId()).isTradeable();
					switch (item.getStage()) {
					case PRIVATE:
						if (!tradeable) {
							items.remove();
							item.setStage(GroundItem.GroundStage.HIDDEN);
						} else
							item.setStage(GroundItem.GroundStage.PUBLIC);
						updateItem(item);
						break;
					case HIDDEN:
						item.setStage(GroundItem.GroundStage.PUBLIC);
						updateItem(item);
						break;
					case PUBLIC:
						if (item.getItem().respawns())
							break;
						else
							items.remove();
						item.setStage(GroundItem.GroundStage.HIDDEN);
						updateItem(item);
						break;
					}
					item.getTimer().reset();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Refresh landscape display.
	 * 
	 * @param player
	 *            the player
	 */
	public void refreshLandscapeDisplay(Player player) {
		for (GroundItem item : player.getGroundItems())
			player.getEventWriter().sendDestroyGroundItem(item.getItem(), item.getPosition());
		player.getGroundItems().clear();
		displayGroundItems(player, groundItems);
	}

	/**
	 * Refresh position display.
	 * 
	 * @param player
	 *            the player
	 * @param position
	 *            the position
	 */
	public void refreshPositionDisplay(Player player, Position position) {
		LinkedList<GroundItem> tileItems = new LinkedList<GroundItem>();
		for (Iterator<GroundItem> items = player.getGroundItems().iterator(); items.hasNext();) {
			GroundItem item = items.next();
			if (item.getPosition().equals(position)) {
				if (groundItems.contains(item))
					tileItems.add(item);
				player.getEventWriter().sendDestroyGroundItem(item.getItem(), item.getPosition());
				items.remove();
			}
		}
		displayGroundItems(player, tileItems);
	}

	/**
	 * Display ground items.
	 * 
	 * @param player
	 *            the player
	 * @param items
	 *            the items
	 */
	public void displayGroundItems(Player player, LinkedList<GroundItem> items) {
		for (GroundItem item : items)
			displayGroundItem(player, item);
	}

	/**
	 * Display ground item.
	 * 
	 * @param player
	 *            the player
	 * @param item
	 *            the item
	 */
	public void displayGroundItem(Player player, GroundItem item) {
		if (canSeeItem(player, item)) {
			player.getEventWriter().sendCreateGroundItem(item.getItem(), item.getPosition());
			player.getGroundItems().add(item);
		}
	}

	/**
	 * Update item.
	 * 
	 * @param item
	 *            the item
	 */
	public void updateItem(GroundItem item) {
		Character viewFirst = item.getViewFirst();
		if (item.getStage() == GroundItem.GroundStage.PRIVATE) {
			if (viewFirst != null && viewFirst instanceof Player) {
				mergeItems(item, viewFirst);
				if (viewFirst.isInSight(item.getPosition(), 32)) {
					((Player) viewFirst).getGroundItems().add(item);
					refreshPositionDisplay(((Player) viewFirst), item.getPosition());
				}
			}
		} else if (item.getStage() == GroundItem.GroundStage.PUBLIC) {
			if (viewFirst != null && viewFirst instanceof Player)
				((Player) viewFirst).getGroundItems().remove(item);
			mergeItems(item, viewFirst);
			for (Player player : Main.getPlayers())
				if (player.isInSight(item.getPosition(), 32)) {
					player.getGroundItems().add(item);
					refreshPositionDisplay(player, item.getPosition());
				}
		} else if (item.getStage() == GroundItem.GroundStage.HIDDEN) {
			for (Player player : Main.getPlayers())
				refreshPositionDisplay(player, item.getPosition());
		}
	}

	/**
	 * Merge items.
	 * 
	 * @param item
	 *            the item
	 * @param viewFirst
	 *            the view first
	 */
	public void mergeItems(GroundItem item, Character viewFirst) {
		if (ItemDef.forId(item.getItem().getId()).isStackable()) {
			// MERGING
			for (Iterator<GroundItem> items = groundItems.iterator(); items.hasNext();) {
				GroundItem other = items.next();
				Character otherViewFirst = other.getViewFirst();
				if (!item.equals(other) && item.getItem().getId() == other.getItem().getId() && other.getPosition().equals(item.getPosition()) && otherViewFirst != null && otherViewFirst instanceof Player && canSeeItem((Player) otherViewFirst, item) && viewFirst != null && viewFirst instanceof Player && canSeeItem((Player) viewFirst, other)) {
					item.getItem().adjustAmount(other.getItem().getAmount());
					item.getTimer().reset();
					items.remove();
					break;
				}
			}
			// END OF MERGING
		}
	}

	/**
	 * Drop item.
	 * 
	 * @param item
	 *            the item
	 */
	public void dropItem(GroundItem item) {
		groundItems.add(item);
		updateItem(item);
	}

	/**
	 * Pickup item.
	 * 
	 * @param item
	 *            the item
	 */
	public void pickupItem(GroundItem item) {
		item.setStage(GroundItem.GroundStage.HIDDEN);
		updateItem(item);
		if (!item.getItem().respawns())
			groundItems.remove(item);
	}

	/**
	 * Can see item.
	 * 
	 * @param player
	 *            the player
	 * @param item
	 *            the item
	 * @return true, if successful
	 */
	public boolean canSeeItem(Player player, GroundItem item) {
		if (item.getStage() == GroundItem.GroundStage.HIDDEN)
			return false;
		ItemDef def = ItemDef.forId(item.getItem().getId());
		Character viewFirst = item.getViewFirst();
		Character dropper = item.getDropper();
		if (!def.isTradeable()) {
			if (dropper != null) {
				if (dropper instanceof Npc)
					return viewFirst != null && viewFirst.equals(player);
				else
					return dropper.equals(player);
			} else
				return false;
		} else
			return (item.isInSight(player) && ((item.getStage() == GroundItem.GroundStage.PUBLIC || (item.getStage() == GroundItem.GroundStage.PRIVATE && (viewFirst == null || viewFirst.equals(player))))));
	}

	/**
	 * Display empty landscape.
	 * 
	 * @param player
	 *            the player
	 */
	public void displayEmptyLandscape(Player player) {
		for (GroundItem item : player.getGroundItems())
			player.getEventWriter().sendDestroyGroundItem(item.getItem(), item.getPosition());
	}

	/** The manager. */
	private static GroundManager manager;

	/**
	 * Creates the.
	 * 
	 * @return the ground manager
	 */
	public static GroundManager create() {
		manager = new GroundManager();
		return manager;
	}

	/**
	 * Gets the manager.
	 * 
	 * @return the manager
	 */
	public static GroundManager getManager() {
		return manager;
	}
}
