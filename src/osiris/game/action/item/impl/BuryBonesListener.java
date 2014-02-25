package osiris.game.action.item.impl;

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
import osiris.game.action.DelayAction;
import osiris.game.action.ItemActionListener;
import osiris.game.action.item.ItemAction;
import osiris.game.model.Skills;
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;
import osiris.game.update.block.AnimationBlock;

/**
 * @author Boomer
 * 
 */
public class BuryBonesListener implements ItemActionListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.action.ItemActionListener#onItemAction(osiris.game.action
	 * .item.ItemAction)
	 */
	@Override
	public void onItemAction(ItemAction action) {
		if (!action.getPlayer().getActionTimer().completed()) {
			action.cancel();
			return;
		}
		Item item = action.getItem();
		Bone bone = Bone.valueOf(ItemDef.forId(item.getId()).getName().toUpperCase().replaceAll(" ", "_"));
		if (bone == null)
			return;
		if (action.getPlayer().getInventory().removeBySlot(action.getSlot(), 1)) {
			action.getPlayer().addUpdateBlock(new AnimationBlock(action.getPlayer(), 827, 0));
			action.getPlayer().getSkills().addExp(Skills.SKILL_PRAYER, bone.getExp());
			new DelayAction(action.getPlayer(), 2).run();
		}
	}

	/**
	 * The Enum Bone.
	 */
	public enum Bone {

		BONES(4.5), WOLF_BONES(4.5), BURNT_BONES(4.5), MONKEY_BONES(5), BAT_BONES(5.3), BIG_BONES(15), JOGRE_BONES(15), BABYDRAGON_BONES(30), DRAGON_BONES(72);

		/** The exp earned. */
		private double expEarned;

		/**
		 * Instantiates a new bone.
		 * 
		 * @param exp
		 *            the exp
		 */
		Bone(double exp) {
			this.expEarned = exp;
		}

		/**
		 * Gets the exp.
		 * 
		 * @return the exp
		 */
		public double getExp() {
			return expEarned;
		}
	}
}
