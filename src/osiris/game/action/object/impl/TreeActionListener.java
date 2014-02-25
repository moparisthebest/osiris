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

import static osiris.game.model.skills.Woodcutting.ADAMANT_AXE;
import static osiris.game.model.skills.Woodcutting.BLACK_AXE;
import static osiris.game.model.skills.Woodcutting.BRONZE_AXE;
import static osiris.game.model.skills.Woodcutting.DRAGON_AXE;
import static osiris.game.model.skills.Woodcutting.IRON_AXE;
import static osiris.game.model.skills.Woodcutting.MITHRIL_AXE;
import static osiris.game.model.skills.Woodcutting.RUNE_AXE;
import static osiris.game.model.skills.Woodcutting.STEEL_AXE;
import static osiris.game.model.skills.Woodcutting.getAnimationMap;
import static osiris.game.model.skills.Woodcutting.getAxeLevelMap;
import static osiris.game.model.skills.Woodcutting.getAxeSpeedMap;
import static osiris.game.model.skills.Woodcutting.getDelay;
import static osiris.game.model.skills.Woodcutting.getExperienceMap;
import static osiris.game.model.skills.Woodcutting.getLogsMap;
import static osiris.game.model.skills.Woodcutting.getRespawnDelayMap;
import static osiris.game.model.skills.Woodcutting.getStumpMap;
import static osiris.game.model.skills.Woodcutting.getTreeChanceMap;
import static osiris.game.model.skills.Woodcutting.getTreeLevelMap;

import java.util.Random;

import osiris.game.action.ObjectActionListener;
import osiris.game.action.TickedAction;
import osiris.game.action.object.ObjectAction;
import osiris.game.action.object.ObjectSetter;
import osiris.game.model.Player;
import osiris.game.model.Skills;
import osiris.game.model.WorldObject;
import osiris.game.model.item.Item;
import osiris.game.update.block.AnimationBlock;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving treeAction events. The class that is
 * interested in processing a treeAction event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addTreeActionListener<code> method. When
 * the treeAction event occurs, that object's appropriate
 * method is invoked.
 * 
 * @author Blake Beaupain
 */
public class TreeActionListener implements ObjectActionListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.action.ObjectActionListener#onObjectAction(osiris.game.action
	 * .object.ObjectAction)
	 */
	@Override
	public void onObjectAction(final ObjectAction action) {
		final Player player = (Player) action.getCharacter();
		final int objectID = action.getObjectID();

		// Check preliminary requirements (and get the axe used).
		if (player.getInventory().isFull()) {
			player.getEventWriter().sendMessage("Your inventory is too full to hold any more logs.");
			return;
		}
		final int axeUsed = checkRequirements(action);
		if (axeUsed == -1) { // Flags that we cannot chop this tree or have an
								// axe.
			return;
		}

		// Start chopping.
		player.getEventWriter().sendMessage("You swing your axe at the tree...");
		player.addUpdateBlock(new AnimationBlock(player, getAnimationMap().get(axeUsed), 0));

		// Schedule the woodcutting action.
		new TickedAction(player, 4) {

			private int counter;
			private final Random random = new Random();

			{
				// Calculate the initial delay (we will calculate again each
				// time a log is obtained).
				int max = (int) (getDelay(player.getSkills().currentLevel(Skills.SKILL_WOODCUTTING), getAxeSpeedMap().get(axeUsed), getTreeLevelMap().get(objectID)) / .6) / 4;
				counter = random.nextInt(max);
			}

			@Override
			public void execute() {
				counter -= 4; // Speed up the rate * 4.

				// Stop if we can't hold any more logs.
				if (player.getInventory().isFull()) {
					player.stopAnimation();
					player.getEventWriter().sendMessage("Your inventory is too full to hold any more logs.");
					cancel();
					return;
				}

				// Animate and check if we should chop a log.
				player.addUpdateBlock(new AnimationBlock(player, getAnimationMap().get(axeUsed), 0));
				if (counter <= 0) { // Time to add a log.
					// Send the message, add the log, and add the experience.
					player.getEventWriter().sendMessage("You get some logs.");
					player.getInventory().add(new Item(getLogsMap().get(objectID)));
					player.getSkills().addExp(Skills.SKILL_WOODCUTTING, getExperienceMap().get(objectID));

					// Check if the tree should fall.
					if (random.nextDouble() <= getTreeChanceMap().get(objectID)) {
						WorldObject stumpObject = new WorldObject(getStumpMap().get(objectID), 0, 10, action.getObjectPosition());
						WorldObject treeObject = new WorldObject(objectID, 0, 10, action.getObjectPosition());
						new ObjectSetter(treeObject, stumpObject, getRespawnDelayMap().get(objectID)).execute();
						player.stopAnimation();
						cancel();
						return;
					}

					// Set the delay again for the next log.
					int max = (int) (getDelay(player.getSkills().currentLevel(Skills.SKILL_WOODCUTTING), getAxeSpeedMap().get(axeUsed), getTreeLevelMap().get(objectID)) / .6) / 4;
					counter = random.nextInt(max);
				}
			}

		}.run();
	}

	/**
	 * Check requirements.
	 * 
	 * @param action
	 *            the action
	 * @return the int
	 */
	private int checkRequirements(ObjectAction action) {
		Player player = (Player) action.getCharacter();
		int level = player.getSkills().currentLevel(Skills.SKILL_WOODCUTTING);

		// First, check if they can chop the tree.
		int requiredLevel = getTreeLevelMap().get(action.getObjectID());
		if (level < requiredLevel) {
			player.getEventWriter().sendMessage("You need a woodcutting level of " + requiredLevel + " to chop this tree.");
			return -1;
		}

		// Next, check for axes.
		for (Item item : player.getEquipment().getItems()) { // First, check our
																// equipment.
			if (item == null) {
				continue;
			}
			// Check if it is an axe.
			int id = item.getId();
			if (id == BRONZE_AXE || id == IRON_AXE || id == STEEL_AXE || id == BLACK_AXE || id == MITHRIL_AXE || id == ADAMANT_AXE || id == RUNE_AXE || id == DRAGON_AXE) {
				// It is, so check if we can use it.
				requiredLevel = getAxeLevelMap().get(id);
				if (level >= requiredLevel) {
					return id;
				}
			}
		}

		for (Item item : player.getInventory().getItems()) { // Next, check our
																// inventory.
			if (item == null) {
				continue;
			}
			// Check if it is an axe.
			int id = item.getId();
			if (id == BRONZE_AXE || id == IRON_AXE || id == STEEL_AXE || id == BLACK_AXE || id == MITHRIL_AXE || id == ADAMANT_AXE || id == RUNE_AXE || id == DRAGON_AXE) {
				// It is, so check if we can use it.
				requiredLevel = getAxeLevelMap().get(id);
				if (level >= requiredLevel) {
					return id;
				}
			}
		}

		// We would have returned a value by now, so no they don't have a proper
		// axe.
		player.getEventWriter().sendMessage("You do not have an axe of which you have the level to use.");
		return -1;
	}

}
