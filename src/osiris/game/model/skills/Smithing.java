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

import osiris.game.action.TickedAction;
import osiris.game.model.Player;
import osiris.game.model.Skills;
import osiris.game.model.item.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class Smithing.
 * 
 * @author Blakeman8192?
 */
public class Smithing {

	/**
	 * The Enum Bar.
	 */
	public enum Bar {

		BRONZE(2349, 1, new Item[] { new Item(436), new Item(438) }), IRON(2351, 15, new Item[] { new Item(440) }), SILVER(2355, 20, new Item[] { new Item(442) }), STEEL(2353, 30, new Item[] { new Item(440), new Item(453, 2) }), GOLD(2357, 40, new Item[] { new Item(444) }), MITHRIL(2359, 50, new Item[] { new Item(447), new Item(453, 4) }), ADAMANT(2361, 70, new Item[] { new Item(449), new Item(453, 6) }), RUNE(2363, 85, new Item[] { new Item(451), new Item(453, 8) });

		/** The bar id. */
		private final int barId;

		/** The required level. */
		private final int requiredLevel;

		/** The required ore. */
		private final Item[] requiredOre;

		/**
		 * Instantiates a new bar.
		 * 
		 * @param barId
		 *            the bar id
		 * @param requiredLevel
		 *            the required level
		 * @param requiredOre
		 *            the required ore
		 */
		private Bar(int barId, int requiredLevel, Item[] requiredOre) {
			this.barId = barId;
			this.requiredLevel = requiredLevel;
			this.requiredOre = requiredOre;
		}

		/**
		 * Gets the bar id.
		 * 
		 * @return the bar id
		 */
		public int getBarId() {
			return barId;
		}

		/**
		 * Gets the required level.
		 * 
		 * @return the required level
		 */
		public int getRequiredLevel() {
			return requiredLevel;
		}

		/**
		 * Gets the required ore.
		 * 
		 * @return the required ore
		 */
		public Item[] getRequiredOre() {
			return requiredOre;
		}
	}

	/**
	 * Smelt.
	 * 
	 * @param player
	 *            the player
	 * @param bar
	 *            the bar
	 * @param amount
	 *            the amount
	 */
	public static void smelt(final Player player, final Bar bar, final int amount) {
		if (player.getSkills().currentLevel(Skills.SKILL_SMITHING) < bar.requiredLevel) {
			player.getEventWriter().sendMessage("You need a smithing level of " + bar.requiredLevel + " to smelt this.");
		}

		new TickedAction(player, 4) {
			private int ticks = 0;

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.action.TickedAction#execute()
			 */
			@Override
			public void execute() {
				if (ticks++ == amount) {
					cancel();
					return;
				}
				Item[] ore = bar.requiredOre;
				for (Item item : ore) {
					if (player.getInventory().amountOfItem(item.getId()) < item.getAmount()) {
						cancel();
						return;
					}
					player.getInventory().removeById(item.getId(), item.getAmount());
				}
				player.getEventWriter().sendMessage("You smelt a " + bar.toString() + " bar.");
				player.getInventory().add(new Item(bar.getBarId()));
			}
		}.run();
	}

}
