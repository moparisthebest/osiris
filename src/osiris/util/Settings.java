package osiris.util;

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

import java.awt.Dimension;

import osiris.data.SettingsLoader;

/**
 * Server settings.
 * 
 * @author Blake
 */
public final class Settings {

	static {
		PORT = SettingsLoader.getValueAsInt("server_port");
		SERVER_NAME = SettingsLoader.getValue("server_name");
		EXP_RATE = Double.parseDouble(SettingsLoader.getValue("exp_rate"));
		SQLITE_SAVING = SettingsLoader.getValueAsBoolean("sqlite_saving");

		SERVER_HOST = SettingsLoader.getValue("server_host");
		SERVER_DATABASE = SettingsLoader.getValue("server_database");
		SERVER_USERNAME = SettingsLoader.getValue("server_username");
		SERVER_PASSWORD = SettingsLoader.getValue("server_password");

		DEFAULT_X = SettingsLoader.getValueAsInt("start_x");
		DEFAULT_Y = SettingsLoader.getValueAsInt("start_y");
		DEFAULT_Z = SettingsLoader.getValueAsInt("start_z");
	}

	/** The Constant SERVER_NAME. */
	public static final String SERVER_NAME;

	/** The Constant TIMEOUT_DELAY. */
	public static final int TIMEOUT_DELAY = 10;

	/** The Constant SCRIPTS_DIRECTORY. */
	public static final String SCRIPTS_DIRECTORY = "./src/ruby/";

	/** The Constant FRIENDS_LIMIT. */
	public static final int FRIENDS_LIMIT = 200;

	/** The Constant IGNORE_LIMIT. */
	public static final int IGNORE_LIMIT = 100;

	/** The Constant MAX_SPECIAL_ENERGY. */
	public static final int MAX_SPECIAL_ENERGY = 1000;

	/** Sql Info for server database. */
	public static final String SERVER_HOST, SERVER_DATABASE, SERVER_USERNAME, SERVER_PASSWORD;

	/** The SQLIT e_ saving. */
	public static boolean SQLITE_SAVING;

	/**
	 * The Constant EXP_RATE.
	 */
	public static final double EXP_RATE;

	/** The Constant SPELL_STAFFS. */
	public static final int[] SPELL_STAFFS = { 1409, 4170, 2415, 2416, 2417 };

	/** The Constant STAFF_ZAMORAK. */
	public static final int STAFF_IBAN = 0, STAFF_SLAYER = 1, STAFF_SARADOMIN = 2, STAFF_GUTHIX = 3, STAFF_ZAMORAK = 4;

	/**
	 * The Constant TICKRATE.
	 */
	public static final int TICKRATE = 600;

	/**
	 * The Constant DISTANCED_TASK_CHECK_DELAY.
	 */
	public static final int DISTANCED_TASK_CHECK_DELAY = TICKRATE * 2;

	/**
	 * The Constant MAX_PLAYERS.
	 */
	public static final int MAX_PLAYERS = 2000;

	/**
	 * The Constant MAX_NPCS.
	 */
	public static final int MAX_NPCS = 3200;

	/**
	 * The Constant DEFAULT_X.
	 */
	public static final int DEFAULT_X;

	/**
	 * The Constant DEFAULT_Y.
	 */
	public static final int DEFAULT_Y;

	/**
	 * The Constant DEFAULT_Z.
	 */
	public static final int DEFAULT_Z;

	/**
	 * The Constant PORT.
	 */
	public static final int PORT;

	/**
	 * The Constant LANDSCAPE_KEYS_FILE.
	 */
	public static final String LANDSCAPE_KEYS_FILE = "./data/landscapekeys.bin";

	/**
	 * The Constant NPC_SPAWNS.
	 */
	public static final String NPC_SPAWNS = "npc-spawns.xml";

	/** The Constant NPC_DROPS. */
	public static final String NPC_DROPS = "npc-drops.xml";

	/** The constant GROUND_ITEMS. */
	public static final String GROUND_ITEMS = "ground-items.xml";

	/** The Constant DIALOGUES. */
	public static final String DIALOGUES = "dialogues.xml";

	/** The Constant WORLD_OBJECTS. */
	public static final String WORLD_OBJECTS = "world-objects.xml";

	/** The Constant POSITION_CHANGE. */
	public static final String POSITION_CHANGE = "position-change.xml";

	/**
	 * Single square dimension.
	 */
	public static final Dimension SINGULAR_DIMENSION = new Dimension(1, 1);

	/**
	 * The Constant NPC_MOVEMENT_TIME.
	 */
	public static final int NPC_MOVEMENT_TIME = 3;

	/**
	 * The Equipment slots.
	 */
	public static final int SLOT_HAT = 0;

	/**
	 * The Constant SLOT_CAPE.
	 */
	public static final int SLOT_CAPE = 1;

	/**
	 * The Constant SLOT_AMULET.
	 */
	public static final int SLOT_AMULET = 2;

	/**
	 * The Constant SLOT_WEAPON.
	 */
	public static final int SLOT_WEAPON = 3;

	/**
	 * The Constant SLOT_CHEST.
	 */
	public static final int SLOT_CHEST = 4;

	/**
	 * The Constant SLOT_SHIELD.
	 */
	public static final int SLOT_SHIELD = 5;

	/**
	 * The Constant SLOT_LEGS.
	 */
	public static final int SLOT_LEGS = 7;

	/**
	 * The Constant SLOT_HANDS.
	 */
	public static final int SLOT_HANDS = 9;

	/**
	 * The Constant SLOT_FEET.
	 */
	public static final int SLOT_FEET = 10;

	/**
	 * The Constant SLOT_RING.
	 */
	public static final int SLOT_RING = 12;

	/** The Constant SLOT_ARROWS. */
	public static final int SLOT_ARROWS = 13;

	/**
	 * The Constant CAPES.
	 */
	public static final String[] CAPES = { "cape", "Cape", "Ranging cape", "Hunter cape", "Ava's", "cloak" };

	/**
	 * The Constant HATS.
	 */
	public static final String[] HATS = { "helm", "hood", "coif", "Coif", "hat", "partyhat", "Hat", "full helm (t)", "full helm (g)", "hat (t)", "hat (g)", "cav", "boater", "helmet", "mask", "Helm of neitiznot", "Runecrafter hat", "Slayer helmet", "Beret mask", "Cavalier mask", "beret", "Lunar helm", "tiara", "Customs hat", "Rogue mask", "Bunny ears", "Earmuffs", "Proselyte sallet", "goggles", "headgear", "nose", "Desert disguise", "mitre", "Feather headdress", "tricorn hat", "A powdered wig", "Verac's helm", "Saradomin full helm", "Runecrafter hat", "coif 100", "Skeleton mask", "Chicken head", "Sleeping cap" };

	/**
	 * The Constant BOOTS.
	 */
	public static final String[] BOOTS = { "boots", "Boots", "Chicken feet", "Flippers" };

	/**
	 * The Constant GLOVES.
	 */
	public static final String[] GLOVES = { "hook", "gloves", "gauntlets", "Gloves", "vambraces", "vamb", "bracers", "Runecrafter gloves", "bracelet", "brace", "bracelet(4)", "bracelet(3)", "bracelet(2)", "bracelet(1)", "brace(4)", "brace(3)", "brace(2)", "brace(1)" };

	/**
	 * The Constant SHIELDS.
	 */
	public static final String[] SHIELDS = { "spikeshield", "berserker shield", "kiteshield", "sq shield", "Toktz-ket", "books", "book", "kiteshield (t)", "kiteshield (g)", "kiteshield(h)", "defender", "satchel", "Book", "Saradomin kite", "shield" };

	/**
	 * The Constant AMULETS.
	 */
	public static final String[] AMULETS = { "amulet", "necklace", "Amulet of", "Strength amulet(t)", "stole", "Gnome scarf", "Armadyl pendant" };

	/**
	 * The Constant ARROWS.
	 */
	public static final String[] ARROWS = { "arrow", "arrows", "arrow(p)", "arrow(p+)", "arrow(p++)", "bolts", "bolt", "Bolt rack", "Opal bolts", "Dragon bolts", "bolts", "Dragon arrow" };

	/**
	 * The Constant RINGS.
	 */
	public static final String[] RINGS = { "ring", "Ring", "Ring of stone" };

	/**
	 * The Constant BODY.
	 */
	public static final String[] BODY = { "tunic", "platebody", "chainbody", "robe top", "Wizard robe", "robetop", "leathertop", "platemail", "top", "brassard", "Robe top", "body", "platebody (t)", "platebody (g)", "body(g)", "body_(g)", "chestplate", "torso", "Woven top", "shirt", "Runecrafter robe", "Saradomin d'hide", "Zamorak d'hide", "Guthix dragonhide", "Saradomin plate", "Princess blouse", "Doctors' gown", "Varrock armour", "Proselyte hauberk", "Zombie shirt", "Moonclan armour", "Chicken wings", "blouse" };

	/**
	 * The Constant LEGS.
	 */
	public static final String[] LEGS = { "leggings", "platelegs", "plateskirt", "shorts", "skirt", "bottoms", "chaps", "platelegs (t)", "platelegs (g)", "bottom", "skirt", "skirt (g)", "skirt (t)", "chaps (g)", "chaps (t)", "tassets", "legs", "Runecrafter skirt", "Void knight robe", "Saradomin d'hide", "Zamorak d'hide", "Guthix dragonhide", "Princess skirt", "navy slacks", "trousers", "Proselyte cuisse", "Proselyte tasset", "3rd age robe", "Skeleton leggings", "Pantaloons", "skirt", "Skirt" };

	/**
	 * The Constant WEAPONS.
	 */
	public static final String[] WEAPONS = { "scimitar", "longsword", "sword", "longbow", "shortbow", "dagger", "mace", "halberd", "spear", "Abyssal whip", "axe", "flail", "crossbow", "Torags hammers", "dagger(p)", "dagger(+)", "dagger(s)", "spear(p)", "spear(+)", "spear(s)", "spear(kp)", "maul", "dart", "dart(p)", "javelin", "javelin(p)", "knife", "knife(p)", "Longbow", "Shortbow", "Crossbow", "Toktz-xil", "Toktz-mej", "Tzhaar-ket", "staff", "Staff", "godsword", "c'bow", "Crystal bow", "Dark bow", "Ivandis flail", "talisman staff", "Gnomecopter", "Toy kite", "crystal bow", "anchor", "Seercull", "cane", "Gadderhammer", "Scythe", "sceptre", "Banner", "spear", "crozier", "pole", "Keris", "hasta", "Excalibur", "hasta(p++)", "hasta(p+)", "hasta(kp)", "hasta(p)", "Fixed device", "x-bow",
			"Drag dagger", "Torag's hammers", "Rubber chicken", "banner", "Wand", "wand", "Snowball", "zanik", "Basket of eggs", "Training bow", "mjolnir", "claws", "warhammer" };
	/* Fullbody is an item that covers your arms. */
	/**
	 * The Constant FULL_BODY.
	 */
	public static final String[] FULL_BODY = { "robe", "blouse", "tunic", "top", "shirt", "Ahrims robetop", "Karils leathertop", "brassard", "platebody (t)", "platebody (g)", "chestplate", "torso", "Runecrafter robe", "Saradomin d'hide", "Zamorak d'hide", "Guthix dragonhide", "Dragon chainbody", "Doctors' gown", "Varrock armour", "Proselyte hauberk", "Proselyte hauberk", "Robe top", "robetop", "Saradomin plate", "Runecrafter skirt", "Runecrafter robe", "platebody", "Moonclan armour", "Decorative armour", "Chicken wings" };
	/* Fullhat covers your head but not your beard. */
	/**
	 * The Constant FULL_HAT.
	 */
	public static final String[] FULL_HAT = { "med helm", "coif", "hood", "Initiate helm", "Coif", "Helm of neitiznot", "beret", "Customs hat", "Proselyte sallet", "headgear", "Desert disguise", "Feather headdress", "tricorn hat", "Reindeer hat", "Armadyl helmet", "A powdered wig", "Void melee helm", "Void ranger helm", "Void mage helm", "hood 100", "coif 100", "mitre", "Chicken head", "Sleeping cap", "robin hood hat" };
	/* Fullmask covers your entire head. */
	/**
	 * The Constant FULL_MASK.
	 */
	public static final String[] FULL_MASK = { "full helm", "mask", "full helm (t)", "full helm (g)", "Lunar helm", "Rogue mask", "Slayer helmet", "heraldic helm", "Grim reaper hood", "Saradomin full helm", "Dharok's helm", "Guthan's helm", "Torag's helm", "Verac's helm" };

	/**
	 * The Stat bonuses.
	 */
	public static final String[] BONUSES = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", "Strength", "Prayer" };

	/**
	 * The Constant EMOTES.
	 */
	public static final int[] EMOTES = { 855, 856, 858, 859, 857, 863, 2113, 862, 864, 861, 2109, 2111, 866, 2106, 2107, 2108, 860, 0x558, 2105, 2110, 865, 2112, 0x84F, 0x850, 1131, 1130, 1129, 1128, 4275, 10017, 4280, 4276, 3544, 3543, 7272, 2836, 6111, -1, 7531, 2414, 8770, 9990 };

	/**
	 * The Constant EMOTE_GRAPHICS.
	 */
	public static final int[][] EMOTE_GRAPHICS = { { 19, 574 }, { 33, 712 }, { 36, 1244 }, { 41, 1537 }, { 42, 1553 }, { 43, 1734 } };

	/**
	 * The Constant EMOTE_EXPLORE.
	 */
	public static final int EMOTE_GOBLIN_BOW = 0;

	/**
	 * The Constant EMOTE_GOBLIN_SALUTE.
	 */
	public static final int EMOTE_GOBLIN_SALUTE = 1;

	/**
	 * The Constant EMOTE_GLASS_BOX.
	 */
	public static final int EMOTE_GLASS_BOX = 2;

	/**
	 * The Constant EMOTE_CLIMB_ROPE.
	 */
	public static final int EMOTE_CLIMB_ROPE = 3;

	/**
	 * The Constant EMOTE_LEAN.
	 */
	public static final int EMOTE_LEAN = 4;

	/**
	 * The Constant EMOTE_GLASS_WALL.
	 */
	public static final int EMOTE_GLASS_WALL = 5;

	/**
	 * The Constant EMOTE_IDEA.
	 */
	public static final int EMOTE_IDEA = 9;

	/**
	 * The Constant EMOTE_STOMP.
	 */
	public static final int EMOTE_STOMP = 7;

	/**
	 * The Constant EMOTE_FLAP.
	 */
	public static final int EMOTE_FLAP = 8;

	/**
	 * The Constant EMOTE_SLAP_HEAD.
	 */
	public static final int EMOTE_SLAP_HEAD = 6;

	/**
	 * The Constant EMOTE_ZOMBIE_WALK.
	 */
	public static final int EMOTE_ZOMBIE_WALK = 1;

	/**
	 * The Constant EMOTE_ZOMBIE_DANCE.
	 */
	public static final int EMOTE_ZOMBIE_DANCE = 11;

	/**
	 * The Constant EMOTE_ZOMBIE_HAND.
	 */
	public static final int EMOTE_ZOMBIE_HAND = 12;

	/**
	 * The Constant EMOTE_SCARED.
	 */
	public static final int EMOTE_SCARED = 13;

	/**
	 * The Constant EMOTE_BUNNY_HOP.
	 */
	public static final int EMOTE_BUNNY_HOP = 14;

	/**
	 * The Constant EMOTE_SKILL_CAPE.
	 */
	public static final int EMOTE_SKILL_CAPE = 15;

	/**
	 * The Constant EMOTE_SNOWMAN.
	 */
	public static final int EMOTE_SNOWMAN = 16;

	/**
	 * The Constant EMOTE_AIR_GUITAR.
	 */
	public static final int EMOTE_AIR_GUITAR = 17;

	/**
	 * The Constant EMOTE_SAFETY_FIRST.
	 */
	public static final int EMOTE_SAFETY_FIRST = 18;

	/**
	 * The Constant EMOTE_EXPLORE.
	 */
	public static final int EMOTE_EXPLORE = 19;

	/**
	 * The Constant BIG_GP_EMOTES.
	 */
	public static final int[] BIG_GP_EMOTES = { EMOTE_GLASS_WALL, EMOTE_GLASS_BOX, EMOTE_CLIMB_ROPE, EMOTE_LEAN, EMOTE_SCARED, EMOTE_ZOMBIE_WALK, EMOTE_ZOMBIE_DANCE, EMOTE_BUNNY_HOP, EMOTE_SKILL_CAPE, EMOTE_SNOWMAN, EMOTE_AIR_GUITAR, EMOTE_SAFETY_FIRST, EMOTE_EXPLORE };

	/**
	 * The Constant LITTLE_GP_EMOTES.
	 */
	public static final int[] LITTLE_GP_EMOTES = { EMOTE_FLAP, EMOTE_SLAP_HEAD, EMOTE_IDEA, EMOTE_STOMP };

	/**
	 * The Constant GOBLIN_EMOTES.
	 */
	public static final int[] GOBLIN_EMOTES = { EMOTE_GOBLIN_BOW, EMOTE_GOBLIN_SALUTE };

	/**
	 * The Constant AUTO_UNLOCKED_EMOTES.
	 */
	public static final int AUTO_UNLOCKED_EMOTES = 22;

	/**
	 * The Constant SKILL_CAPE_INFO.
	 */
	public static final int[][] SKILL_CAPE_INFO = { { 9747, 4959, 823 }, { 9753, 4961, 824 }, { 9750, 4981, 828 }, { 9768, 4971, 833 }, { 9756, 4973, 832 }, { 9759, 4979, 829 }, { 9762, 4939, 813 }, { 9801, 4955, 821 }, { 9807, 4957, 822 }, { 9783, 4937, 812 }, { 9798, 4951, 819 }, { 9804, 4975, 831 }, { 9780, 4949, 818 }, { 9795, 4943, 815 }, { 9792, 4941, 814 }, { 9774, 4969, 835 }, { 9771, 4977, 830 }, { 9777, 4965, 826 }, { 9786, 4967, 1656 }, { 9810, 4963, 825 }, { 9765, 4947, 817 }, { 9948, 5158, 907 }, { 9789, 4953, 820 }, { 12169, 8525, 1515 }, { 9813, 4945, 816 } };

}
