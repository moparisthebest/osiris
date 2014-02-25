package osiris.game.model.skills;

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

import java.util.Random;

import osiris.game.action.impl.SkillAction;
import osiris.game.action.object.ObjectSetter;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.game.model.Skills;
import osiris.game.model.WorldObject;
import osiris.game.model.WorldObjects;
import osiris.game.model.ground.GroundItem;
import osiris.game.model.ground.GroundManager;
import osiris.game.model.item.Item;
import osiris.game.update.block.AnimationBlock;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class Firemaking.
 * 
 * @author Boomer
 */
public class Firemaking {

	/**
	 * Gets the log name.
	 * 
	 * @param itemName
	 *            the item name
	 * @return the log name
	 */
	public static String getLogName(String itemName) {
		return itemName.replaceAll(" ", "").replaceAll("logs", "").toUpperCase();
	}

	/**
	 * Light log.
	 * 
	 * @param player
	 *            the player
	 * @param itemSlot
	 *            the item slot
	 * @param log
	 *            the log
	 */
	public static void lightLog(final Player player, int itemSlot, final Log log) {
		for (WorldObject object : WorldObjects.getObjects()) {
			if (object != null && object.getPosition().equals(player.getPosition())) {
				player.getEventWriter().sendMessage("You can not light a fire here!");
				return;
			}
		}

		Item logItem = player.getInventory().getItem(itemSlot);
		Skill firemakingSkill = new Skill() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#canIterate()
			 */
			@Override
			public boolean canIterate() {
				int randomSkill = Utilities.random(player.getSkills().currentLevel(Skills.SKILL_FIREMAKING));
				double randomRequired = new Random().nextDouble() * log.levelNeeded * 1.5;
				return randomSkill >= randomRequired;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#calculateCooldown()
			 */
			@Override
			public int calculateCooldown() {
				return 2;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#onCompletion()
			 */
			@Override
			public void onCompletion() {
				player.addPriorityUpdateBlock(new AnimationBlock(player, -1, 0));
				WorldObject fire = new WorldObject(2732, 0, 10, player.getPosition());
				final Position firePosition = player.getPosition().clone();
				new ObjectSetter(null, fire, 45).setOnReset(new Runnable() {
					@Override
					public void run() {
						GroundManager.getManager().dropItem(new GroundItem(Item.create(592), firePosition));
					}
				}).execute();
			}
		};

		player.addUpdateBlock(new AnimationBlock(player, 10011, 0));
		new SkillAction(player, firemakingSkill, Skills.SKILL_FIREMAKING, log.levelNeeded, log.expGained, 10011, new Item[] { Item.create(590) }, new Item[] { logItem }, new Item[0], "You light a fire.", 1).setMaxAttempts(5).run();
	}

	/**
	 * The Enum Log.
	 */
	public enum Log {

		LOGS(1, 40), OAK(15, 60), WILLOW(30, 90), MAPLE(45, 135), YEW(60, 202.5), MAGIC(75, 303.8);

		/** The level needed. */
		int levelNeeded;

		/** The exp gained. */
		double expGained;

		/**
		 * Instantiates a new log.
		 * 
		 * @param levelNeeded
		 *            the level needed
		 * @param expGained
		 *            the exp gained
		 */
		Log(int levelNeeded, double expGained) {
			this.levelNeeded = levelNeeded;
			this.expGained = expGained;
		}
	}
}
