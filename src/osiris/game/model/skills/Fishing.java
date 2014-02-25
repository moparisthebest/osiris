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

import osiris.game.action.DistancedAction;
import osiris.game.action.impl.SkillAction;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.game.model.Skills;
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;
import osiris.game.update.block.FaceToPositionBlock;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class Fishing.
 * 
 * @author Boomer
 */
public class Fishing {

	/**
	 * Gets the fishing spot.
	 * 
	 * @param objectId
	 *            the object id
	 * @param option
	 *            the option
	 * @return the fishing spot
	 */
	public static FishingSpot getFishingSpot(int objectId, int option) {
		for (FishingSpot spot : FishingSpot.values())
			if (spot.option == option)
				for (int id : spot.objectIds)
					if (id == objectId)
						return spot;
		return null;
	}

	/**
	 * Start fishing.
	 * 
	 * @param player
	 *            the player
	 * @param spot
	 *            the spot
	 */
	public static void startFishing(final Player player, final Position fishingPosition, final FishingSpot spot) {
		final Fish fish = getFishCaught(player, spot);
		if (fish == null)
			return;
		final Skill fishingSkill = new Skill() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#canIterate()
			 */
			@Override
			public boolean canIterate() {
				int randomSkill = Utilities.random(player.getSkills().currentLevel(Skills.SKILL_FISHING));
				double randomRequired = new Random().nextDouble() * fish.levelRequired * 3;
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
		};
		new DistancedAction(player, fishingPosition, 1) {
			@Override
			public void execute() {
				player.addUpdateBlock(new FaceToPositionBlock(player, fishingPosition));
				new SkillAction(player, fishingSkill, Skills.SKILL_FISHING, fish.levelRequired, fish.expGained, spot.emote, new Item[] { Item.create(spot.itemRequired) }, spot.baitRequired != -1 ? new Item[] { Item.create(spot.baitRequired) } : new Item[0], new Item[] { Item.create(fish.itemId) }, "You catch " + ItemDef.forId(fish.itemId).getName() + ".", -1) {

					/*
					 * (non-Javadoc)
					 * 
					 * @see osiris.game.action.impl.SkillAction#execute()
					 */
					@Override
					public void execute() {
						final Fish fish = getFishCaught(getPlayer(), spot);
						if (fish == null) {
							cancel();
							return;
						}
						this.setItemsGained(new Item[] { Item.create(fish.itemId) });
						this.setLevelRequired(fish.levelRequired);
						this.setExperienceGained(fish.expGained);
						this.setSuccessMessage("You catch " + ItemDef.forId(fish.itemId).getName() + ".");
						super.execute();
					}

				}.run();
			}
		}.run();
	}

	/**
	 * Gets the fish caught.
	 * 
	 * @param player
	 *            the player
	 * @param spot
	 *            the spot
	 * @return the fish caught
	 */
	public static Fish getFishCaught(Player player, FishingSpot spot) {
		Fish catching = null;
		Random rand = new Random();
		int fishingLevel = player.getSkills().currentLevel(Skills.SKILL_FISHING);
		for (Fish fish : spot.fishes) {
			boolean canCatch = fishingLevel >= fish.levelRequired && rand.nextInt(fishingLevel + 1) >= rand.nextInt(fish.levelRequired + 1);
			if (catching != null) {
				if (rand.nextBoolean() && canCatch && catching.levelRequired < fish.levelRequired)
					catching = fish;
			} else
				catching = fish;
		}
		return catching;
	}

	/**
	 * The Enum FishingSpot.
	 */
	public enum FishingSpot {

		SHRIMPS_SPOT(new int[] { 316 }, 0, 620, new Fish[] { Fish.SHRIMPS, Fish.ANCHOVY }, 303), PIKE_SPOT(new int[] { 316 }, 1, 622, new Fish[] { Fish.HERRING, Fish.PIKE }, 307, 313);

		/** The bait required. */
		int option, emote, itemRequired, baitRequired;

		/** The object ids. */
		int[] objectIds;

		/** The fishes. */
		Fish[] fishes;

		/**
		 * Instantiates a new fishing spot.
		 * 
		 * @param objectIds
		 *            the object ids
		 * @param option
		 *            the option
		 * @param emote
		 *            the emote
		 * @param fishes
		 *            the fishes
		 * @param itemRequired
		 *            the item required
		 */
		FishingSpot(int[] objectIds, int option, int emote, Fish[] fishes, int itemRequired) {
			this(objectIds, option, emote, fishes, itemRequired, -1);
		}

		/**
		 * Instantiates a new fishing spot.
		 * 
		 * @param objectIds
		 *            the object ids
		 * @param option
		 *            the option
		 * @param emote
		 *            the emote
		 * @param fishes
		 *            the fishes
		 * @param itemRequired
		 *            the item required
		 * @param baitRequired
		 *            the bait required
		 */
		FishingSpot(int[] objectIds, int option, int emote, Fish[] fishes, int itemRequired, int baitRequired) {
			this.objectIds = objectIds;
			this.option = option;
			this.emote = emote;
			this.fishes = fishes;
			this.itemRequired = itemRequired;
			this.baitRequired = baitRequired;
		}
	}

	/**
	 * The Enum Fish.
	 */
	public enum Fish {

		SHRIMPS(317, 1, 10), ANCHOVY(321, 15, 40), HERRING(345, 10, 30), PIKE(349, 25, 60);

		/** The level required. */
		int itemId, levelRequired;

		/** The exp gained. */
		double expGained;

		/**
		 * Instantiates a new fish.
		 * 
		 * @param itemId
		 *            the item id
		 * @param levelRequired
		 *            the level required
		 * @param expGained
		 *            the exp gained
		 */
		Fish(int itemId, int levelRequired, double expGained) {
			this.itemId = itemId;
			this.levelRequired = levelRequired;
			this.expGained = expGained;
		}
	}

}
