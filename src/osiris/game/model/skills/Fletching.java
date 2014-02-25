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

import osiris.game.action.impl.DisplaySelectOptionAction;
import osiris.game.action.impl.SkillAction;
import osiris.game.model.Player;
import osiris.game.model.Skills;
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc

/**
 * The Class Fletching.
 * 
 * TODO: Needs correct animations.
 * 
 * @author Blake
 * @author samuraiblood2
 * 
 */
public class Fletching {

	/** The fletch item. */
	private static FletchItem fletchItem;

	/**
	 * Feather arrows.
	 * 
	 * @param player
	 *            the player
	 * @param item
	 *            the item
	 * @param amount
	 *            the amount
	 */
	public static void featherArrows(Player player, Item item, int amount) {
		boolean feather = item.getId() == 314;
		Item[] req = { feather ? Item.create(314) : Item.create(52), item };
		Item[] remove = { Item.create(314, 15), Item.create(52, 15) };
		Item[] give = { Item.create(53, 15) };
		String message = "You fletch a Headless arrow!";
		new SkillAction(player, new Skill() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#calculateCooldown()
			 */
			@Override
			public int calculateCooldown() {
				return 1;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#canIterate()
			 */
			@Override
			public boolean canIterate() {
				return true;
			}

		}, Skills.SKILL_FLETCHING, 1, 1, 1248, req, remove, give, message, 1).run();
	}

	/**
	 * Fletch arrows.
	 * 
	 * @param player
	 *            the player
	 * @param item
	 *            the item
	 */
	public static void fletchArrows(Player player, Item item) {
		ItemDef def = ItemDef.forId(item.getId());
		String name = def.getName().toUpperCase().replaceAll(" ", "_");
		fletchItem = FletchItem.valueOf(name);
		if (fletchItem == null) {
			return;
		}

		Item[] req = { Item.create(53), item };
		Item[] remove = { Item.create(53, 15), Item.create(item.getId(), 15) };
		Item[] give = { Item.create(fletchItem.getCompleted(), 15) };
		String message = "You fletch a " + Utilities.toProperCase(ItemDef.forId(fletchItem.getCompleted()).getName()) + "!";
		new SkillAction(player, new Skill() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#calculateCooldown()
			 */
			@Override
			public int calculateCooldown() {
				return 1;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#canIterate()
			 */
			@Override
			public boolean canIterate() {
				return true;
			}

		}, Skills.SKILL_FLETCHING, fletchItem.getReq(), fletchItem.getExp(), 1248, req, remove, give, message, 1).run();
	}

	/**
	 * Fletch bow.
	 * 
	 * @param player
	 *            the player
	 * @param option
	 *            the option
	 * @param amount
	 *            the amount
	 */
	public static void fletchBow(Player player, int option, int amount) {
		if (fletchItem == null) {
			return;
		}

		FletchItem bow = fletchItem.getItems()[option];
		Item give = Item.create(bow.getId());
		Item[] req = { Item.create(946), Item.create(fletchItem.getId()) };
		Item[] remove = { Item.create(fletchItem.getId()) };
		Item[] given = { bow.getId() == 52 ? Item.create(52, 15) : give };
		String message = "You fletch a " + Utilities.toProperCase(ItemDef.forId(give.getId()).getName()) + "!";
		new SkillAction(player, new Skill() {

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
			 * @see osiris.game.model.skills.Skill#canIterate()
			 */
			@Override
			public boolean canIterate() {
				return true;
			}

		}, Skills.SKILL_FLETCHING, bow.getReq(), bow.getExp(), 1248, req, remove, given, message, amount).run();
	}

	/**
	 * String bow.
	 * 
	 * @param player
	 *            the player
	 * @param item
	 *            the item
	 */
	public static void stringBow(Player player, Item item) {
		ItemDef def = ItemDef.forId(item.getId());
		String name = def.getName().toUpperCase().replaceAll(" ", "_");
		fletchItem = FletchItem.valueOf(name);
		if (fletchItem == null) {
			return;
		}

		Item bowstring = Item.create(1777);
		Item[] req = { bowstring, item };
		Item[] remove = { bowstring, item };
		Item[] give = { Item.create(fletchItem.getCompleted()) };
		String message = "You fletch a " + Utilities.toProperCase(ItemDef.forId(fletchItem.getCompleted()).getName()) + "!";
		new SkillAction(player, new Skill() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#calculateCooldown()
			 */
			@Override
			public int calculateCooldown() {
				return 1;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see osiris.game.model.skills.Skill#canIterate()
			 */
			@Override
			public boolean canIterate() {
				return true;
			}

		}, Skills.SKILL_FLETCHING, fletchItem.getReq(), fletchItem.getExp(), 1248, req, remove, give, message, 1).run();
	}

	/**
	 * Display options.
	 * 
	 * @param player
	 *            the player
	 * @param item
	 *            the item
	 */
	public static void displayOptions(Player player, Item item) {
		ItemDef def = ItemDef.forId(item.getId());
		String name = def.getName().toUpperCase().replaceAll(" ", "_");
		fletchItem = FletchItem.valueOf(name);
		if (fletchItem == null) {
			return;
		}

		int inter = (302 + (fletchItem.getItems().length - 1));
		int[] items = new int[fletchItem.getItems().length];
		for (int i = 0; i < items.length; i++) {
			items[i] = fletchItem.getItems()[i].getId();
		}
		new DisplaySelectOptionAction(player, inter, 175, items).run();
	}

	/**
	 * Gets the fletch item.
	 * 
	 * @return the fletch item
	 */
	public static FletchItem getFletchItem() {
		return fletchItem;
	}

	/**
	 * The Enum FletchItem.
	 */
	private enum FletchItem {

		/*
		 * Arrow tips.
		 */
		BRONZE_ARROWTIPS(39, 1, 2.6, 882), IRON_ARROWTIPS(40, 15, 3.8, 884), STEEL_ARROWTIPS(41, 30, 6.3, 886), MITHRIL_ARROWTIPS(42, 45, 8.8, 888), ADAMANT_ARROWTIPS(43, 60, 11.3, 890), RUNE_ARROWTIPS(44, 75, 13.8, 892), DRAGON_ARROWTIPS(0, 90, 16.3, 11212),

		/*
		 * Misc.
		 */
		SHAFTS(52, 1, .33D, 53),

		/*
		 * Unstrung bows.
		 */
		SHORTBOW_U(50, 5, 5D, 841), LONGBOW_U(48, 10, 10D, 839), OAK_SHORTBOW_U(54, 20, 16.5D, 843), OAK_LONGBOW_U(56, 25, 25D, 845), WILLOW_SHORTBOW_U(60, 35, 33.3D, 849), WILLOW_LONGBOW_U(58, 40, 41.5D, 847), MAPLE_SHORTBOW_U(64, 50, 50D, 853), MAPLE_LONGBOW_U(62, 55, 58.5D, 851), YEW_SHORTBOW_U(68, 65, 67.5D, 857), YEW_LONGBOW_U(66, 70, 75D, 855), MAGIC_SHORTBOW_U(72, 80, 83.3D, 861), MAGIC_LONGBOW_U(70, 85, 91.5D, 859),

		/*
		 * Logs.
		 */
		LOGS(1511, SHAFTS, SHORTBOW_U, LONGBOW_U), OAK_LOGS(1521, OAK_SHORTBOW_U, OAK_LONGBOW_U), WILLOW_LOGS(1519, WILLOW_SHORTBOW_U, WILLOW_LONGBOW_U), MAPLE_LOGS(1517, MAPLE_SHORTBOW_U, MAPLE_LONGBOW_U), YEW_LOGS(1515, YEW_SHORTBOW_U, YEW_LONGBOW_U), MAGIC_LOGS(1513, MAGIC_SHORTBOW_U, MAGIC_LONGBOW_U);

		/** The req. */
		private int req;

		/** The exp. */
		private double exp;

		/** The completed. */
		private int completed;

		/** The id. */
		private int id;

		/** The items. */
		private FletchItem[] items;

		/**
		 * Instantiates a new fletch item.
		 * 
		 * @param id
		 *            the id
		 * @param items
		 *            the items
		 */
		private FletchItem(int id, FletchItem... items) {
			this.id = id;
			this.items = items;
		}

		/**
		 * Instantiates a new fletch item.
		 * 
		 * @param id
		 *            the id
		 * @param req
		 *            the req
		 * @param exp
		 *            the exp
		 * @param completed
		 *            the completed
		 */
		private FletchItem(int id, int req, double exp, int completed) {
			this.id = id;
			this.req = req;
			this.exp = exp;
			this.completed = completed;
		}

		/**
		 * Gets the id.
		 * 
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets the req.
		 * 
		 * @return the req
		 */
		public int getReq() {
			return req;
		}

		/**
		 * Gets the exp.
		 * 
		 * @return the exp
		 */
		public double getExp() {
			return exp;
		}

		/**
		 * Gets the completed.
		 * 
		 * @return the completed
		 */
		public int getCompleted() {
			return completed;
		}

		/**
		 * Gets the items.
		 * 
		 * @return the items
		 */
		public FletchItem[] getItems() {
			return items;
		}
	}
}
