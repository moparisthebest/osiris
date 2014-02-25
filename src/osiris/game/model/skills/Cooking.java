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

import osiris.game.action.DistancedAction;
import osiris.game.action.TickedAction;
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
 * The Class Cooking.
 * 
 * @author Boomer
 */
public class Cooking {

	/**
	 * To recipe name.
	 * 
	 * @param itemName
	 *            the item name
	 * @return the string
	 */
	public static String toRecipeName(String itemName) {
		return itemName.toLowerCase().replaceAll("raw ", "").replaceAll(" ", "_").toUpperCase();
	}

	/**
	 * Cook item.
	 * 
	 * @param player
	 *            the player
	 * @param itemSlot
	 *            the item slot
	 * @param recipe
	 *            the recipe
	 * @param fire
	 *            the fire
	 */
	public static void cookItem(final Player player, final Position objectPosition, int itemSlot, final Recipe recipe, final boolean fire) {
		final Item food = player.getInventory().getItem(itemSlot);
		final String foodName = ItemDef.forId(food.getId()).getName();
		final int amount = player.getInventory().amountOfItem(food.getId());
		final boolean burned = burnedFood(player, recipe);
		final Skill cookingSkill = new Skill() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#canIterate()
			 */
			@Override
			public boolean canIterate() {
				return true;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#calculateCooldown()
			 */
			@Override
			public int calculateCooldown() {
				return 4;
			}
		};

		new DistancedAction(player, objectPosition, 1) {

			@Override
			public void execute() {
				player.addUpdateBlock(new FaceToPositionBlock(player, objectPosition));
				TickedAction action = new SkillAction(player, cookingSkill, Skills.SKILL_COOKING, recipe.requiredLevel, recipe.expGained, fire ? 897 : 896, new Item[0], new Item[] { Item.create(food.getId()) }, new Item[] { Item.create(burned ? recipe.burnId : recipe.cookedId) }, burned ? "You burn the " + foodName + "." : "You cook the " + foodName + ".", amount) {

					/*
					 * (non-Javadoc)
					 * 
					 * @see osiris.game.action.impl.SkillAction#execute()
					 */
					@Override
					public void execute() {
						boolean burned = burnedFood(player, recipe);
						this.setItemsGained(new Item[] { Item.create(burned ? recipe.burnId : recipe.cookedId) });
						this.setExperienceGained(burned ? 0 : recipe.expGained);
						this.setSuccessMessage(burned ? "You burn the " + foodName + "." : "You cook the " + foodName + ".");
						super.execute();
					}
				};
				action.run();
				action.execute();
			}
		}.run();
	}

	/**
	 * Burned food.
	 * 
	 * @param player
	 *            the player
	 * @param recipe
	 *            the recipe
	 * @return true, if successful
	 */
	public static boolean burnedFood(Player player, Recipe recipe) {
		int cookingLevel = player.getSkills().currentLevel(Skills.SKILL_COOKING);
		return cookingLevel < recipe.masteredLevel && Utilities.random(cookingLevel) < Utilities.random(recipe.requiredLevel);
	}

	/**
	 * The Enum Recipe.
	 */
	public enum Recipe {

		SHRIMPS(315, 323, 1, 34, 30), ANCHOVIES(315, 319, 1, 34, 30), HERRING(315, 347, 5, 37, 50), PIKE(315, 351, 20, 52, 80);

		/** The mastered level. */
		int cookedId, burnId, requiredLevel, masteredLevel;

		/** The exp gained. */
		double expGained;

		/**
		 * Instantiates a new recipe.
		 * 
		 * @param cookedId
		 *            the cooked id
		 * @param burnId
		 *            the burn id
		 * @param requiredLevel
		 *            the required level
		 * @param masteredLevel
		 *            the mastered level
		 * @param expGained
		 *            the exp gained
		 */
		Recipe(int cookedId, int burnId, int requiredLevel, int masteredLevel, double expGained) {
			this.cookedId = cookedId;
			this.burnId = burnId;
			this.requiredLevel = requiredLevel;
			this.masteredLevel = masteredLevel;
			this.expGained = expGained;
		}
	}
}
