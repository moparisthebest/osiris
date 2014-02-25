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
public class Woodcutting {

	// Axe speed constants
	public static final int BRONZE_SPEED = 1;
	public static final int IRON_SPEED = 1;
	public static final int STEEL_SPEED = 2;
	public static final int BLACK_SPEED = 3;
	public static final int MITHRIL_SPEED = 4;
	public static final int ADAMANT_SPEED = 5;
	public static final int RUNE_SPEED = 6;
	public static final int DRAGON_SPEED = 7;

	// Axe constants
	public static final int BRONZE_AXE = 1351;
	public static final int IRON_AXE = 1349;
	public static final int STEEL_AXE = 1353;
	public static final int BLACK_AXE = 1361;
	public static final int MITHRIL_AXE = 1355;
	public static final int ADAMANT_AXE = 1357;
	public static final int RUNE_AXE = 1359;
	public static final int DRAGON_AXE = 6739;

	// Tree constants
	public static final int TREE_1 = 1278;
	public static final int TREE_2 = 1276;
	public static final int TREE_3 = 1315;
	public static final int TREE_4 = 1316;
	public static final int TREE_OAK = 1281;
	public static final int TREE_WILLOW_1 = 1308;
	public static final int TREE_WILLOW_2 = 5551;
	public static final int TREE_WILLOW_3 = 5552;
	public static final int TREE_WILLOW_4 = 5553;
	public static final int TREE_MAPLE = 1307;
	public static final int TREE_YEW = 1309;
	public static final int TREE_MAGIC = 1306;

	// Animation constants
	public static final int BRONZE_ANIMATION = 879;
	public static final int IRON_ANIMATION = 877;
	public static final int STEEL_ANIMATION = 875;
	public static final int BLACK_ANIMATION = 873;
	public static final int MITHRIL_ANIMATION = 871;
	public static final int ADAMANT_ANIMATION = 869;
	public static final int RUNE_ANIMATION = 867;
	public static final int DRAGON_ANIMATION = 2846;

	// Axe level constants
	public static final int LEVEL_BRONZE = 1;
	public static final int LEVEL_IRON = 1;
	public static final int LEVEL_STEEL = 5;
	public static final int LEVEL_BLACK = 10;
	public static final int LEVEL_MITHRIL = 21;
	public static final int LEVEL_ADAMANT = 31;
	public static final int LEVEL_RUNE = 41;
	public static final int LEVEL_DRAGON = 61;

	// Tree level constants.
	public static final int LEVEL_TREE = 1;
	public static final int LEVEL_OAK = 15;
	public static final int LEVEL_WILLOW = 30;
	public static final int LEVEL_MAPLE = 45;
	public static final int LEVEL_YEW = 60;
	public static final int LEVEL_MAGIC = 75;

	// Tree fall chances - the closer to 1.0, the higher the chance of falling
	// per log cut.
	public static final double CHANCE_TREE = 1;
	public static final double CHANCE_OAK = .33;
	public static final double CHANCE_WILLOW = .2;
	public static final double CHANCE_MAPLE = .33;
	public static final double CHANCE_YEW = .15;
	public static final double CHANCE_MAGIC = .15;

	// Logs - logs obtained from trees.
	public static final int LOGS_TREE = 1511;
	public static final int LOGS_OAK = 1521;
	public static final int LOGS_WILLOW = 1519;
	public static final int LOGS_MAPLE = 1517;
	public static final int LOGS_YEW = 1515;
	public static final int LOGS_MAGIC = 1513;

	// Experience gained from each log from each tree.
	public static final int EXP_TREE = 25;
	public static final int EXP_OAK = 38;
	public static final int EXP_WILLOW = 68;
	public static final int EXP_MAPLE = 100;
	public static final int EXP_YEW = 175;
	public static final int EXP_MAGIC = 250;

	// Tree stumps
	public static final int STUMP_TREE = 4822;
	public static final int STUMP_OAK = 1356;
	public static final int STUMP_WILLOW = 7399;
	public static final int STUMP_MAPLE = 7400;
	public static final int STUMP_YEW = 7402;
	public static final int STUMP_MAGIC = 7401;

	// Tree respawn delays
	public static final int DELAY_TREE = 30;
	public static final int DELAY_OAK = 13;
	public static final int DELAY_WILLOW = 13;
	public static final int DELAY_MAPLE = 60;
	public static final int DELAY_YEW = 98;
	public static final int DELAY_MAGIC = 190;

	// A map of axe speeds.
	private static final Map<Integer, Integer> axeSpeedMap = new HashMap<Integer, Integer>();

	// A map of axe levels.
	private static final Map<Integer, Integer> axeLevelMap = new HashMap<Integer, Integer>();

	// A map of tree levels.
	private static final Map<Integer, Integer> treeLevelMap = new HashMap<Integer, Integer>();

	// A map of chopping animations.
	private static final Map<Integer, Integer> animationMap = new HashMap<Integer, Integer>();

	// A map of tree-fall chances.
	private static final Map<Integer, Double> treeChanceMap = new HashMap<Integer, Double>();

	// Logs map
	private static final Map<Integer, Integer> logsMap = new HashMap<Integer, Integer>();

	// Map for experience gained from trees.
	private static final Map<Integer, Integer> experienceMap = new HashMap<Integer, Integer>();

	// Tree stumps
	private static final Map<Integer, Integer> stumpMap = new HashMap<Integer, Integer>();

	// Tree respawn delays
	private static final Map<Integer, Integer> respawnDelayMap = new HashMap<Integer, Integer>();

	public static int getDelay(int playerLevel, int axeSpeed, int treeLevel) {
		return (30 - playerLevel / 5) - axeSpeed + treeLevel;
	}

	static {
		// Maybe load these from a config file?

		// Axe speeds
		axeSpeedMap.put(BRONZE_AXE, BRONZE_SPEED);
		axeSpeedMap.put(IRON_AXE, IRON_SPEED);
		axeSpeedMap.put(STEEL_AXE, STEEL_SPEED);
		axeSpeedMap.put(BLACK_AXE, BLACK_SPEED);
		axeSpeedMap.put(MITHRIL_AXE, MITHRIL_SPEED);
		axeSpeedMap.put(ADAMANT_AXE, ADAMANT_SPEED);
		axeSpeedMap.put(RUNE_AXE, RUNE_SPEED);
		axeSpeedMap.put(DRAGON_AXE, DRAGON_SPEED);

		// Axe levels
		axeLevelMap.put(BRONZE_AXE, LEVEL_BRONZE);
		axeLevelMap.put(IRON_AXE, LEVEL_IRON);
		axeLevelMap.put(STEEL_AXE, LEVEL_STEEL);
		axeLevelMap.put(BLACK_AXE, LEVEL_BLACK);
		axeLevelMap.put(MITHRIL_AXE, LEVEL_MITHRIL);
		axeLevelMap.put(ADAMANT_AXE, LEVEL_ADAMANT);
		axeLevelMap.put(RUNE_AXE, LEVEL_RUNE);
		axeLevelMap.put(DRAGON_AXE, LEVEL_DRAGON);

		// Tree levels
		treeLevelMap.put(TREE_1, LEVEL_TREE);
		treeLevelMap.put(TREE_2, LEVEL_TREE);
		treeLevelMap.put(TREE_3, LEVEL_TREE);
		treeLevelMap.put(TREE_4, LEVEL_TREE);
		treeLevelMap.put(TREE_OAK, LEVEL_OAK);
		treeLevelMap.put(TREE_WILLOW_1, LEVEL_WILLOW);
		treeLevelMap.put(TREE_WILLOW_2, LEVEL_WILLOW);
		treeLevelMap.put(TREE_WILLOW_3, LEVEL_WILLOW);
		treeLevelMap.put(TREE_WILLOW_4, LEVEL_WILLOW);
		treeLevelMap.put(TREE_MAPLE, LEVEL_MAPLE);
		treeLevelMap.put(TREE_YEW, LEVEL_YEW);
		treeLevelMap.put(TREE_MAGIC, LEVEL_MAGIC);

		// Animation constants
		animationMap.put(BRONZE_AXE, BRONZE_ANIMATION);
		animationMap.put(IRON_AXE, IRON_ANIMATION);
		animationMap.put(STEEL_AXE, STEEL_ANIMATION);
		animationMap.put(BLACK_AXE, BLACK_ANIMATION);
		animationMap.put(MITHRIL_AXE, MITHRIL_ANIMATION);
		animationMap.put(ADAMANT_AXE, ADAMANT_ANIMATION);
		animationMap.put(RUNE_AXE, RUNE_ANIMATION);
		animationMap.put(DRAGON_AXE, DRAGON_ANIMATION);

		// Tree fall chances
		treeChanceMap.put(TREE_1, CHANCE_TREE);
		treeChanceMap.put(TREE_2, CHANCE_TREE);
		treeChanceMap.put(TREE_3, CHANCE_TREE);
		treeChanceMap.put(TREE_4, CHANCE_TREE);
		treeChanceMap.put(TREE_OAK, CHANCE_OAK);
		treeChanceMap.put(TREE_WILLOW_1, CHANCE_WILLOW);
		treeChanceMap.put(TREE_WILLOW_2, CHANCE_WILLOW);
		treeChanceMap.put(TREE_WILLOW_3, CHANCE_WILLOW);
		treeChanceMap.put(TREE_WILLOW_4, CHANCE_WILLOW);
		treeChanceMap.put(TREE_MAPLE, CHANCE_MAPLE);
		treeChanceMap.put(TREE_YEW, CHANCE_YEW);
		treeChanceMap.put(TREE_MAGIC, CHANCE_MAGIC);

		// Logs obtained from trees.
		logsMap.put(TREE_1, LOGS_TREE);
		logsMap.put(TREE_2, LOGS_TREE);
		logsMap.put(TREE_3, LOGS_TREE);
		logsMap.put(TREE_4, LOGS_TREE);
		logsMap.put(TREE_OAK, LOGS_OAK);
		logsMap.put(TREE_WILLOW_1, LOGS_WILLOW);
		logsMap.put(TREE_WILLOW_2, LOGS_WILLOW);
		logsMap.put(TREE_WILLOW_3, LOGS_WILLOW);
		logsMap.put(TREE_WILLOW_4, LOGS_WILLOW);
		logsMap.put(TREE_MAPLE, LOGS_MAPLE);
		logsMap.put(TREE_YEW, LOGS_YEW);
		logsMap.put(TREE_MAGIC, LOGS_MAGIC);

		// Experience from trees.
		experienceMap.put(TREE_1, EXP_TREE);
		experienceMap.put(TREE_2, EXP_TREE);
		experienceMap.put(TREE_3, EXP_TREE);
		experienceMap.put(TREE_4, EXP_TREE);
		experienceMap.put(TREE_OAK, EXP_OAK);
		experienceMap.put(TREE_WILLOW_1, EXP_WILLOW);
		experienceMap.put(TREE_WILLOW_2, EXP_WILLOW);
		experienceMap.put(TREE_WILLOW_3, EXP_WILLOW);
		experienceMap.put(TREE_WILLOW_4, EXP_WILLOW);
		experienceMap.put(TREE_MAPLE, EXP_MAPLE);
		experienceMap.put(TREE_YEW, EXP_YEW);
		experienceMap.put(TREE_MAGIC, EXP_MAGIC);

		// Tree stumps
		stumpMap.put(TREE_1, STUMP_TREE);
		stumpMap.put(TREE_2, STUMP_TREE);
		stumpMap.put(TREE_3, STUMP_TREE);
		stumpMap.put(TREE_4, STUMP_TREE);
		stumpMap.put(TREE_OAK, STUMP_OAK);
		stumpMap.put(TREE_WILLOW_1, STUMP_WILLOW);
		stumpMap.put(TREE_WILLOW_2, STUMP_WILLOW);
		stumpMap.put(TREE_WILLOW_3, STUMP_WILLOW);
		stumpMap.put(TREE_WILLOW_4, STUMP_WILLOW);
		stumpMap.put(TREE_MAPLE, STUMP_MAPLE);
		stumpMap.put(TREE_YEW, STUMP_YEW);
		stumpMap.put(TREE_MAGIC, STUMP_MAGIC);

		// Tree respawn delays.
		respawnDelayMap.put(TREE_1, DELAY_TREE);
		respawnDelayMap.put(TREE_2, DELAY_TREE);
		respawnDelayMap.put(TREE_3, DELAY_TREE);
		respawnDelayMap.put(TREE_4, DELAY_TREE);
		respawnDelayMap.put(TREE_OAK, DELAY_OAK);
		respawnDelayMap.put(TREE_WILLOW_1, DELAY_WILLOW);
		respawnDelayMap.put(TREE_WILLOW_2, DELAY_WILLOW);
		respawnDelayMap.put(TREE_WILLOW_3, DELAY_WILLOW);
		respawnDelayMap.put(TREE_WILLOW_4, DELAY_WILLOW);
		respawnDelayMap.put(TREE_MAPLE, DELAY_MAPLE);
		respawnDelayMap.put(TREE_YEW, DELAY_YEW);
		respawnDelayMap.put(TREE_MAGIC, DELAY_MAGIC);
	}

	public static Map<Integer, Integer> getAxeSpeedMap() {
		return axeSpeedMap;
	}

	public static Map<Integer, Integer> getAxeLevelMap() {
		return axeLevelMap;
	}

	public static Map<Integer, Integer> getTreeLevelMap() {
		return treeLevelMap;
	}

	public static Map<Integer, Integer> getAnimationMap() {
		return animationMap;
	}

	public static Map<Integer, Double> getTreeChanceMap() {
		return treeChanceMap;
	}

	public static Map<Integer, Integer> getLogsMap() {
		return logsMap;
	}

	public static Map<Integer, Integer> getExperienceMap() {
		return experienceMap;
	}

	public static Map<Integer, Integer> getStumpMap() {
		return stumpMap;
	}

	public static Map<Integer, Integer> getRespawnDelayMap() {
		return respawnDelayMap;
	}

}
