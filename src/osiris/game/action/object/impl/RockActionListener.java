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

import static osiris.game.model.skills.Mining.ADAMANT_PICKAXE;
import static osiris.game.model.skills.Mining.BRONZE_PICKAXE;
import static osiris.game.model.skills.Mining.IRON_PICKAXE;
import static osiris.game.model.skills.Mining.MITHRIL_PICKAXE;
import static osiris.game.model.skills.Mining.RUNE_PICKAXE;
import static osiris.game.model.skills.Mining.STEEL_PICKAXE;
import static osiris.game.model.skills.Mining.getAnimationMap;
import static osiris.game.model.skills.Mining.getDelay;
import static osiris.game.model.skills.Mining.getExpMap;
import static osiris.game.model.skills.Mining.getKeywordMap;
import static osiris.game.model.skills.Mining.getOreMap;
import static osiris.game.model.skills.Mining.getPickaxeLevelMap;
import static osiris.game.model.skills.Mining.getPickaxeSpeedMap;
import static osiris.game.model.skills.Mining.getRespawnMap;
import static osiris.game.model.skills.Mining.getRockLevelMap;

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
 * ROCK ON.
 * 
 * @author Blake Beaupain
 */
public class RockActionListener implements ObjectActionListener {

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

		// Check preliminary requirements (and get the pickaxe used).
		if (player.getInventory().isFull()) {
			player.getEventWriter().sendMessage("Your inventory is too full to mine any more ore.");
			return;
		}

		final int pickaxeUsed = checkRequirements(action);
		if (pickaxeUsed == -1) {
			return;
		}

		player.getEventWriter().sendMessage("You swing your pick at the rock...");
		player.addUpdateBlock(new AnimationBlock(player, getAnimationMap().get(pickaxeUsed), 0));
		final String keyword = getKeywordMap().get(objectID);

		TickedAction miningAction = new TickedAction(player, 3) {

			private int counter = 0;
			private final Random random = new Random();

			{
				int max = (int) (getDelay(player.getSkills().currentLevel(Skills.SKILL_MINING), getPickaxeSpeedMap().get(pickaxeUsed), getRockLevelMap().get(keyword)) / .6) / 4;
				counter = random.nextInt(max);
			}

			@Override
			public void execute() {
				counter -= 2;
				player.addUpdateBlock(new AnimationBlock(player, getAnimationMap().get(pickaxeUsed), 0));
				if (counter <= 0) {
					player.getEventWriter().sendMessage("You get some " + keyword + " ore.");
					player.getInventory().add(new Item(getOreMap().get(keyword)));
					player.getSkills().addExp(Skills.SKILL_MINING, getExpMap().get(keyword));
					player.addUpdateBlock(new AnimationBlock(player, -1, 0));
					WorldObject fullObject = new WorldObject(objectID, 0, 10, action.getObjectPosition());
					WorldObject emptyObject = new WorldObject(31060, 0, 10, action.getObjectPosition());
					new ObjectSetter(fullObject, emptyObject, getRespawnMap().get(keyword)).execute();
					cancel();
				}
			}

		};
		miningAction.run();
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
		int level = player.getSkills().currentLevel(Skills.SKILL_MINING);

		int requiredLevel = getRockLevelMap().get(getKeywordMap().get(action.getObjectID()));
		if (level < requiredLevel) {
			player.getEventWriter().sendMessage("You need a mining level of " + requiredLevel + " to mine this rock.");
			return -1;
		}

		// Next, check for pickaxes.
		for (Item item : player.getEquipment().getItems()) {
			if (item == null) {
				continue;
			}
			int id = item.getId();
			if (id == BRONZE_PICKAXE || id == IRON_PICKAXE || id == STEEL_PICKAXE || id == MITHRIL_PICKAXE || id == ADAMANT_PICKAXE || id == RUNE_PICKAXE) {
				requiredLevel = getPickaxeLevelMap().get(id);
				if (level >= requiredLevel) {
					return id;
				}
			}
		}
		for (Item item : player.getInventory().getItems()) {
			if (item == null) {
				continue;
			}
			int id = item.getId();
			if (id == BRONZE_PICKAXE || id == IRON_PICKAXE || id == STEEL_PICKAXE || id == MITHRIL_PICKAXE || id == ADAMANT_PICKAXE || id == RUNE_PICKAXE) {
				requiredLevel = getPickaxeLevelMap().get(id);
				if (level >= requiredLevel) {
					return id;
				}
			}
		}

		player.getEventWriter().sendMessage("You do not have a pickaxe of which you have the level to use.");
		return -1;
	}

}
