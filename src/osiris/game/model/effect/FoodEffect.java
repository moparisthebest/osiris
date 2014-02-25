package osiris.game.model.effect;

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

import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;
import osiris.game.update.block.AnimationBlock;

// TODO: Auto-generated Javadoc
/**
 * The Class FoodEffect.
 * 
 * @author Boomer
 */
public class FoodEffect extends ConsumptionEffect {

	/** The healing. */
	private int itemSlot, itemId, healing;

	/**
	 * Instantiates a new food effect.
	 * 
	 * @param itemSlot
	 *            the item slot
	 * @param itemId
	 *            the item id
	 * @param healing
	 *            the healing
	 */
	public FoodEffect(int itemSlot, int itemId, int healing) {
		super();
		this.itemSlot = itemSlot;
		this.itemId = itemId;
		this.healing = healing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.effect.Effect#execute(osiris.game.model.Character)
	 */
	@Override
	public void execute(Character character) {
		if (character instanceof Player) {
			Item item = ((Player) character).getInventory().getItem(itemSlot);
			if (item == null || item.getId() != itemId || !((Player) character).getInventory().removeBySlot(itemSlot, 1)) {
				cancel();
				return;
			}
		}
		int currentHp = character.getCurrentHp();
		int maxHp = character.getMaxHp();
		int adjustedHp = currentHp + healing;
		if (adjustedHp > maxHp)
			adjustedHp = maxHp;
		character.getCombatManager().adjustAttackDelay(2);
		character.setCurrentHp(adjustedHp);
		character.addUpdateBlock(new AnimationBlock(character, 829, 0));
		if (character instanceof Player) {
			((Player) character).getEventWriter().sendMessage("You eat the " + ItemDef.forId(itemId).getName() + ".");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.effect.Effect#equals(osiris.game.model.effect.Effect)
	 */
	@Override
	public boolean equals(Effect effect) {
		return effect instanceof ConsumptionEffect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.effect.Effect#update(osiris.game.model.Character)
	 */
	@Override
	public void update(Character character) {
	}
}
