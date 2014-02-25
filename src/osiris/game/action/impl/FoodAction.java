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
import osiris.game.model.Character;
import osiris.game.model.def.ItemDef;
import osiris.game.model.effect.Effect;
import osiris.game.model.effect.ExpiringEffect;
import osiris.game.model.effect.FoodEffect;

/**
 * The Class FoodAction.
 * 
 * @author Boomer
 */
public class FoodAction extends Action {

	/** The food. */
	private Food food;

	/** The item id. */
	private int itemId, itemSlot;

	/**
	 * Instantiates a new food action.
	 * 
	 * @param character
	 *            the character
	 * @param food
	 *            the food
	 * @param itemSlot
	 *            the item slot
	 * @param itemId
	 *            the item id
	 */
	public FoodAction(Character character, Food food, int itemSlot, int itemId) {
		super(character);
		this.food = food;
		this.itemId = itemId;
		this.itemSlot = itemSlot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
		for (Effect effect : getCharacter().getEffects()) {
			if (effect instanceof FoodEffect) {
				return;
			}
		}
		if (food == null)
			return;

		ExpiringEffect effect = new FoodEffect(itemSlot, itemId, food.getHeal());
		getCharacter().addEffect(effect);
		effect.execute(getCharacter());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
	}

	/**
	 * Calculate food.
	 * 
	 * @param itemId
	 *            the item id
	 * @return the food
	 */
	public static Food calculateFood(int itemId) {
		String value = ItemDef.forId(itemId).getName().toUpperCase().replaceAll(" ", "_");
		Food food = null;
		try {
			food = Food.valueOf(value);
		} catch (IllegalArgumentException e) {
		}
		return food;
	}

	/**
	 * The Enum Food.
	 * 
	 * TODO: Add the rest of the foods. TODO: Check if the current foods have
	 * the correct heal amount.
	 * 
	 * @author samuraiblood2
	 */
	public enum Food {

		// XXX: Meat
		COOKED_CHIKEN(3), COOKED_MEAT(3), COOKED_KARAMBWAN(18),

		// XXX: Fish
		ANCHOIVES(1), SHRIMPS(3), BREAD(5), HERRING(5), MACKEREL(6), TROUT(7), COD(7), PIKE(8), SALMON(9), TUNA(10), LOBSTER(12), BASS(13), SWORDFISH(14), MONKFISH(16), SHARK(20), TURTLE(21), MANTA_RAY(22);

		/** The heal. */
		private int heal;

		/**
		 * Instantiates a new food.
		 * 
		 * @param heal
		 *            the heal
		 */
		private Food(int heal) {
			this.heal = heal;
		}

		/**
		 * Gets the heal.
		 * 
		 * @return the heal
		 */
		public int getHeal() {
			return heal;
		}
	}
}
