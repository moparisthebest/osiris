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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Blake Beaupain
 * 
 */
public class Mining {

	/*
	 * TODO: Figure out a way to find object face and other things so we can
	 * actually have a specific type for each empty rock. For now, they will all
	 * share the same ID/face.
	 */

	// Ore keywords.
	public static final String COPPER_KEYWORD = "copper";
	public static final String TIN_KEYWORD = "tin";
	public static final String CLAY_KEYWORD = "clay";
	public static final String IRON_KEYWORD = "iron";
	public static final String COAL_KEYWORD = "coal";
	public static final String SILVER_KEYWORD = "silver";
	public static final String GOLD_KEYWORD = "gold";
	public static final String MITHRIL_KEYWORD = "mithril";
	public static final String ADAMANTITE_KEYWORD = "adamantite";
	public static final String RUNITE_KEYWORD = "runite";

	public static final int BRONZE_SPEED = 2;
	public static final int IRON_SPEED = 3;
	public static final int STEEL_SPEED = 4;
	public static final int MITHRIL_SPEED = 5;
	public static final int ADAMANT_SPEED = 6;
	public static final int RUNE_SPEED = 7;

	public static final int BRONZE_PICKAXE = 1265;
	public static final int IRON_PICKAXE = 1267;
	public static final int STEEL_PICKAXE = 1269;
	public static final int MITHRIL_PICKAXE = 1273;
	public static final int ADAMANT_PICKAXE = 1271;
	public static final int RUNE_PICKAXE = 1275;

	public static final int BRONZE_ANIMATION = 625;
	public static final int IRON_ANIMATION = 626;
	public static final int STEEL_ANIMATION = 627;
	public static final int MITHRIL_ANIMATION = 629;
	public static final int ADAMANT_ANIMATION = 628;
	public static final int RUNE_ANIMATION = 624;

	public static final int LEVEL_BRONZE_PICKAXE = 1;
	public static final int LEVEL_IRON_PICKAXE = 1;
	public static final int LEVEL_STEEL_PICKAXE = 6;
	public static final int LEVEL_MITHRIL_PICKAXE = 21;
	public static final int LEVEL_ADAMANT_PICKAXE = 31;
	public static final int LEVEL_RUNE_PICKAXE = 41;

	public static final int LEVEL_COPPER = 1;
	public static final int LEVEL_TIN = 1;
	public static final int LEVEL_CLAY = 1;
	public static final int LEVEL_IRON = 15;
	public static final int LEVEL_SILVER = 20;
	public static final int LEVEL_COAL = 30;
	public static final int LEVEL_GOLD = 40;
	public static final int LEVEL_MITHRIL = 55;
	public static final int LEVEL_ADAMANTITE = 70;
	public static final int LEVEL_RUNITE = 85;

	public static final int ORE_COPPER = 436;
	public static final int ORE_TIN = 438;
	public static final int ORE_CLAY = 434;
	public static final int ORE_IRON = 440;
	public static final int ORE_COAL = 453;
	public static final int ORE_SILVER = 442;
	public static final int ORE_GOLD = 444;
	public static final int ORE_MITHRIL = 447;
	public static final int ORE_ADAMANTITE = 449;
	public static final int ORE_RUNITE = 451;

	public static final int EXP_CLAY = 5;
	public static final int EXP_COPPER = 18;
	public static final int EXP_TIN = 18;
	public static final int EXP_IRON = 35;
	public static final int EXP_SILVER = 40;
	public static final int EXP_COAL = 50;
	public static final int EXP_GOLD = 65;
	public static final int EXP_MITHRIL = 80;
	public static final int EXP_ADAMANTITE = 95;
	public static final int EXP_RUNITE = 125;

	public static final int RESPAWN_CLAY = 1;
	public static final int RESPAWN_TIN = 5;
	public static final int RESPAWN_COPPER = 5;
	public static final int RESPAWN_IRON = 10;
	public static final int RESPAWN_SILVER = 120;
	public static final int RESPAWN_COAL = 60;
	public static final int RESPAWN_GOLD = 120;
	public static final int RESPAWN_MITHRIL = 240;
	public static final int RESPAWN_ADAMANTITE = 480;
	public static final int RESPAWN_RUNITE = 1200;

	// Rock IDs
	public static final int[] COPPER = { 11938, 11937, 11936, 11962, 11960, 11961 };
	public static final int[] TIN = { 11933, 11959, 11957, 11958, 11950, 11949, 11948, 11934, 11935 };
	public static final int[] CLAY = { 11556, 11557 };
	public static final int[] IRON = { 37309, 37308, 37307, 11955, 11954, 11956 };
	public static final int[] COAL = { 11932, 11930 };
	public static final int[] SILVER = { 37306, 37305, 37304 };
	public static final int[] GOLD = { 37310, 37312, 15505, 15503, 15504 };
	public static final int[] MITHRIL = { 11942, 11944 };
	public static final int[] ADAMANTITE = { 11939, 11941 };
	public static final int[] RUNITE = { 14860, 14859 };

	private static final Map<Integer, String> keywordMap = new HashMap<Integer, String>();

	private static final Map<Integer, Integer> pickaxeLevelMap = new HashMap<Integer, Integer>();

	private static final Map<Integer, Integer> animationMap = new HashMap<Integer, Integer>();

	private static final Map<String, Integer> rockLevelMap = new HashMap<String, Integer>();

	private static final Map<Integer, Integer> pickaxeSpeedMap = new HashMap<Integer, Integer>();

	private static final Map<String, Integer> oreMap = new HashMap<String, Integer>();

	private static final Map<String, Integer> expMap = new HashMap<String, Integer>();

	private static final Map<String, Integer> respawnMap = new HashMap<String, Integer>();

	static {
		// Map the keywords
		for (int id : COPPER)
			keywordMap.put(id, COPPER_KEYWORD);
		for (int id : TIN)
			keywordMap.put(id, TIN_KEYWORD);
		for (int id : CLAY)
			keywordMap.put(id, CLAY_KEYWORD);
		for (int id : IRON)
			keywordMap.put(id, IRON_KEYWORD);
		for (int id : COAL)
			keywordMap.put(id, COAL_KEYWORD);
		for (int id : SILVER)
			keywordMap.put(id, SILVER_KEYWORD);
		for (int id : GOLD)
			keywordMap.put(id, GOLD_KEYWORD);
		for (int id : MITHRIL)
			keywordMap.put(id, MITHRIL_KEYWORD);
		for (int id : ADAMANTITE)
			keywordMap.put(id, ADAMANTITE_KEYWORD);
		for (int id : RUNITE)
			keywordMap.put(id, RUNITE_KEYWORD);

		// Map the pickaxe levels.
		pickaxeLevelMap.put(BRONZE_PICKAXE, LEVEL_BRONZE_PICKAXE);
		pickaxeLevelMap.put(IRON_PICKAXE, LEVEL_IRON_PICKAXE);
		pickaxeLevelMap.put(STEEL_PICKAXE, LEVEL_STEEL_PICKAXE);
		pickaxeLevelMap.put(MITHRIL_PICKAXE, LEVEL_MITHRIL_PICKAXE);
		pickaxeLevelMap.put(ADAMANT_PICKAXE, LEVEL_ADAMANT_PICKAXE);
		pickaxeLevelMap.put(RUNE_PICKAXE, LEVEL_RUNE_PICKAXE);

		// Map the rock levels.
		rockLevelMap.put(COPPER_KEYWORD, LEVEL_COPPER);
		rockLevelMap.put(TIN_KEYWORD, LEVEL_TIN);
		rockLevelMap.put(CLAY_KEYWORD, LEVEL_CLAY);
		rockLevelMap.put(IRON_KEYWORD, LEVEL_IRON);
		rockLevelMap.put(COAL_KEYWORD, LEVEL_COAL);
		rockLevelMap.put(SILVER_KEYWORD, LEVEL_SILVER);
		rockLevelMap.put(GOLD_KEYWORD, LEVEL_GOLD);
		rockLevelMap.put(MITHRIL_KEYWORD, LEVEL_MITHRIL);
		rockLevelMap.put(ADAMANTITE_KEYWORD, LEVEL_ADAMANTITE);
		rockLevelMap.put(RUNITE_KEYWORD, LEVEL_RUNITE);

		// Mining animations
		animationMap.put(BRONZE_PICKAXE, BRONZE_ANIMATION);
		animationMap.put(IRON_PICKAXE, IRON_ANIMATION);
		animationMap.put(STEEL_PICKAXE, STEEL_ANIMATION);
		animationMap.put(MITHRIL_PICKAXE, MITHRIL_ANIMATION);
		animationMap.put(ADAMANT_PICKAXE, ADAMANT_ANIMATION);
		animationMap.put(RUNE_PICKAXE, RUNE_ANIMATION);

		// Pickaxe speeds
		pickaxeSpeedMap.put(BRONZE_PICKAXE, BRONZE_SPEED);
		pickaxeSpeedMap.put(IRON_PICKAXE, IRON_SPEED);
		pickaxeSpeedMap.put(STEEL_PICKAXE, STEEL_SPEED);
		pickaxeSpeedMap.put(MITHRIL_PICKAXE, MITHRIL_SPEED);
		pickaxeSpeedMap.put(ADAMANT_PICKAXE, ADAMANT_SPEED);
		pickaxeSpeedMap.put(RUNE_PICKAXE, RUNE_SPEED);

		// Ore obtained.
		oreMap.put(COPPER_KEYWORD, ORE_COPPER);
		oreMap.put(TIN_KEYWORD, ORE_TIN);
		oreMap.put(CLAY_KEYWORD, ORE_CLAY);
		oreMap.put(IRON_KEYWORD, ORE_IRON);
		oreMap.put(COAL_KEYWORD, ORE_COAL);
		oreMap.put(SILVER_KEYWORD, ORE_SILVER);
		oreMap.put(GOLD_KEYWORD, ORE_GOLD);
		oreMap.put(MITHRIL_KEYWORD, ORE_MITHRIL);
		oreMap.put(ADAMANTITE_KEYWORD, ORE_ADAMANTITE);
		oreMap.put(RUNITE_KEYWORD, ORE_RUNITE);

		// Experience obtained
		expMap.put(COPPER_KEYWORD, EXP_COPPER);
		expMap.put(TIN_KEYWORD, EXP_TIN);
		expMap.put(CLAY_KEYWORD, EXP_CLAY);
		expMap.put(IRON_KEYWORD, EXP_IRON);
		expMap.put(COAL_KEYWORD, EXP_COAL);
		expMap.put(SILVER_KEYWORD, EXP_SILVER);
		expMap.put(GOLD_KEYWORD, EXP_GOLD);
		expMap.put(MITHRIL_KEYWORD, EXP_MITHRIL);
		expMap.put(ADAMANTITE_KEYWORD, EXP_ADAMANTITE);
		expMap.put(RUNITE_KEYWORD, EXP_RUNITE);

		respawnMap.put(COPPER_KEYWORD, RESPAWN_COPPER);
		respawnMap.put(TIN_KEYWORD, RESPAWN_TIN);
		respawnMap.put(CLAY_KEYWORD, RESPAWN_CLAY);
		respawnMap.put(IRON_KEYWORD, RESPAWN_IRON);
		respawnMap.put(COAL_KEYWORD, RESPAWN_COAL);
		respawnMap.put(SILVER_KEYWORD, RESPAWN_SILVER);
		respawnMap.put(GOLD_KEYWORD, RESPAWN_GOLD);
		respawnMap.put(MITHRIL_KEYWORD, RESPAWN_MITHRIL);
		respawnMap.put(ADAMANTITE_KEYWORD, RESPAWN_ADAMANTITE);
		respawnMap.put(RUNITE_KEYWORD, RESPAWN_RUNITE);
	}

	public static int getDelay(int playerLevel, int pickaxeSpeed, int rockLevel) {
		return (30 - playerLevel / 5) - pickaxeSpeed + rockLevel;
	}

	public static Map<Integer, String> getKeywordMap() {
		return keywordMap;
	}

	public static Map<Integer, Integer> getPickaxeLevelMap() {
		return pickaxeLevelMap;
	}

	public static Map<String, Integer> getRockLevelMap() {
		return rockLevelMap;
	}

	public static Map<Integer, Integer> getAnimationMap() {
		return animationMap;
	}

	public static Map<Integer, Integer> getPickaxeSpeedMap() {
		return pickaxeSpeedMap;
	}

	public static Map<String, Integer> getOreMap() {
		return oreMap;
	}

	public static Map<String, Integer> getExpMap() {
		return expMap;
	}

	public static Map<String, Integer> getRespawnMap() {
		return respawnMap;
	}
}
