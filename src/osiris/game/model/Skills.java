package osiris.game.model;

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

import java.util.Arrays;

import osiris.game.update.block.GraphicsBlock;
import osiris.util.Settings;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc

/**
 * The Class Skills.
 * 
 * @author Boomer
 * 
 */
public class Skills {

	/** The recovery times */
	private int[] recovery;

	/**
	 * The experience.
	 */
	private double[] curLevel, experience;

	/**
	 * The level.
	 */
	private int[] maxLevel;

	/**
	 * The player.
	 */
	private final Player player;

	/* The player's combat level */
	private int combatLevel;

	/**
	 * Instantiates a new skills.
	 * 
	 * @param player
	 *            the player
	 */
	public Skills(Player player) {
		this.player = player;
		this.experience = new double[NUMBER_OF_SKILLS];
		this.curLevel = new double[NUMBER_OF_SKILLS];
		this.maxLevel = new int[NUMBER_OF_SKILLS];
		this.recovery = new int[NUMBER_OF_SKILLS];
		Arrays.fill(recovery, -1);
		Arrays.fill(curLevel, 1);
		Arrays.fill(maxLevel, 1);
		curLevel[SKILL_HITPOINTS] = 10;
		maxLevel[SKILL_HITPOINTS] = (int) curLevel[SKILL_HITPOINTS];
		experience[SKILL_HITPOINTS] = Utilities.expForLevel(maxLevel[SKILL_HITPOINTS]);
		setCombatLevel();
	}

	/**
	 * sets the max levels on login
	 */
	public void initialize() {
		for (int i = 0; i < NUMBER_OF_SKILLS; i++)
			maxLevel[i] = Utilities.levelForExp(experience[i]);
		setCombatLevel();
	}

	/**
	 * Current level.
	 * 
	 * @param skillId
	 *            the skill id
	 * @return the int
	 */
	public int currentLevel(int skillId) {
		return (int) curLevel[skillId];
	}

	public double realCurrentLevel(int skillId) {
		return curLevel[skillId];
	}

	/**
	 * Max level.
	 * 
	 * @param skillId
	 *            the skill id
	 * @return the int
	 */
	public int maxLevel(int skillId) {
		return maxLevel[skillId];
	}

	/**
	 * Sets the level.
	 * 
	 * @param skillId
	 *            the skill id
	 * @param level
	 *            the level
	 */
	public void setCurLevel(int skillId, double level) {
		this.curLevel[skillId] = level;
		player.getEventWriter().sendSkillLevel(skillId);
	}

	/**
	 * Gets the exp.
	 * 
	 * @param skillId
	 *            the skill id
	 * @return the exp
	 */
	public double getExp(int skillId) {
		return experience[skillId];
	}

	public int[] getRecoveryTimes() {
		return recovery;
	}

	public int[] getCurLevels() {
		int[] levels = new int[curLevel.length];
		for (int i = 0; i < curLevel.length; i++)
			levels[i] = (int) curLevel[i];
		return levels;
	}

	public int[] getMaxLevels() {
		int[] levels = new int[maxLevel.length];
		for (int i = 0; i < maxLevel.length; i++)
			levels[i] = maxLevel[i];
		return levels;
	}

	public double[] getExperience() {
		return experience;
	}

	public void setSkill(int skillId, double curLevel, double experience, boolean refresh) {
		this.curLevel[skillId] = curLevel;
		this.maxLevel[skillId] = Utilities.levelForExp(experience);
		this.experience[skillId] = experience;
		if (refresh) {
			player.getEventWriter().sendSkillLevel(skillId);
			player.updateAppearance();
		}
	}

	public void addExp(int skillId, double exp) {
		addExp(skillId, exp, true);
	}

	/**
	 * Adds the exp.
	 * 
	 * @param skillId
	 *            the skill id
	 * @param exp
	 *            the exp
	 */
	public void addExp(int skillId, double exp, boolean useBonusRate) {

		double expEarned = exp;
		if (useBonusRate)
			expEarned *= player.getExpRate();
		double expToAdd = ((Double.MAX_VALUE - experience[skillId]) < expEarned) ? Double.MAX_VALUE : experience[skillId] + expEarned;
		setExp(skillId, expToAdd);
		int levelForExp = Utilities.levelForExp(experience[skillId]);
		if (maxLevel[skillId] < levelForExp) {
			curLevel[skillId] = levelForExp;
			maxLevel[skillId] = (int) curLevel[skillId];
			String skillName = SKILL_NAMES[skillId];
			boolean startsWithVowel = false;
			String[] vowels = { "a", "e", "i", "o", "u" };
			for (String v : vowels)
				if (skillName.startsWith(v))
					startsWithVowel = true;
			player.getEventWriter().sendMessage("You have just advanced a" + (startsWithVowel ? "n" : "") + " " + skillName + " level!  You have reached level " + maxLevel[skillId] + ".");
			player.addUpdateBlock(new GraphicsBlock(player, 199, 100));
			player.getEventWriter().sendString("You have now reached level " + maxLevel[skillId] + ".", 740, 1);
			if (maxLevel[skillId] == 99 && player.getEmoteStatus()[Settings.EMOTE_SKILL_CAPE] == 0) {
				player.getEmoteStatus()[Settings.EMOTE_SKILL_CAPE] = 1;
				player.sendEmoteStatus();
			}
			setCombatLevel();
			player.getEventWriter().sendSkillLevel(skillId);
			player.updateAppearance();
		}
	}

	/**
	 * Sets the exp.
	 * 
	 * @param skillId
	 *            the skill id
	 * @param exp
	 *            the exp
	 */
	public void setExp(int skillId, double exp) {
		experience[skillId] = exp;
		player.getEventWriter().sendSkillLevel(skillId);
	}

	public void setCombatLevel() {
		this.combatLevel = calculateCombatLevel();
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	/**
	 * Gets the combat level.
	 * 
	 * @return the combat level
	 */
	public int calculateCombatLevel() {
		int attack = Utilities.levelForExp(experience[SKILL_ATTACK]);
		int defence = Utilities.levelForExp(experience[SKILL_DEFENCE]);
		int strength = Utilities.levelForExp(experience[SKILL_STRENGTH]);
		int hp = Utilities.levelForExp(experience[SKILL_HITPOINTS]);
		int prayer = Utilities.levelForExp(experience[SKILL_PRAYER]);
		int ranged = Utilities.levelForExp(experience[SKILL_RANGE]);
		int magic = Utilities.levelForExp(experience[SKILL_MAGIC]);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(ranged * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		int summoning = Utilities.levelForExp(experience[SKILL_SUMMONING]);
		summoning /= 8;
		return combatLevel + summoning;
	}

	/**
	 * The Constant NUMBER_OF_SKILLS.
	 */
	public static final int NUMBER_OF_SKILLS = 24;

	/**
	 * The Constant SKILL_SUMMONING.
	 */
	public static final int SKILL_ATTACK = 0;

	/**
	 * The Constant SKILL_DEFENCE.
	 */
	public static final int SKILL_DEFENCE = 1;

	/**
	 * The Constant SKILL_STRENGTH.
	 */
	public static final int SKILL_STRENGTH = 2;

	/**
	 * The Constant SKILL_HITPOINTS.
	 */
	public static final int SKILL_HITPOINTS = 3;

	/**
	 * The Constant SKILL_RANGE.
	 */
	public static final int SKILL_RANGE = 4;

	/**
	 * The Constant SKILL_PRAYER.
	 */
	public static final int SKILL_PRAYER = 5;

	/**
	 * The Constant SKILL_MAGIC.
	 */
	public static final int SKILL_MAGIC = 6;

	/**
	 * The Constant SKILL_COOKING.
	 */
	public static final int SKILL_COOKING = 7;

	/**
	 * The Constant SKILL_WOODCUTTING.
	 */
	public static final int SKILL_WOODCUTTING = 8;

	/**
	 * The Constant SKILL_FLETCHING.
	 */
	public static final int SKILL_FLETCHING = 9;

	/**
	 * The Constant SKILL_FISHING.
	 */
	public static final int SKILL_FISHING = 10;

	/**
	 * The Constant SKILL_FIREMAKING.
	 */
	public static final int SKILL_FIREMAKING = 11;

	/**
	 * The Constant SKILL_CRAFTING.
	 */
	public static final int SKILL_CRAFTING = 12;

	/**
	 * The Constant SKILL_SMITHING.
	 */
	public static final int SKILL_SMITHING = 13;

	/**
	 * The Constant SKILL_MINING.
	 */
	public static final int SKILL_MINING = 14;

	/**
	 * The Constant SKILL_HERBLORE.
	 */
	public static final int SKILL_HERBLORE = 15;

	/**
	 * The Constant SKILL_AGILITY.
	 */
	public static final int SKILL_AGILITY = 16;

	/**
	 * The Constant SKILL_THIEVING.
	 */
	public static final int SKILL_THIEVING = 17;

	/**
	 * The Constant SKILL_SLAYER.
	 */
	public static final int SKILL_SLAYER = 18;

	/**
	 * The Constant SKILL_FARMING.
	 */
	public static final int SKILL_FARMING = 19;

	/**
	 * The Constant SKILL_RUNECRAFTING.
	 */
	public static final int SKILL_RUNECRAFTING = 20;

	/**
	 * The Constant SKILL_CONSTRUCTION.
	 */
	public static final int SKILL_CONSTRUCTION = 21;

	/**
	 * The Constant SKILL_HUNTER.
	 */
	public static final int SKILL_HUNTER = 22;

	/**
	 * The Constant SKILL_SUMMONING.
	 */
	public static final int SKILL_SUMMONING = 23;

	/**
	 * The Constant SKILL_NAMES.
	 */
	public static final String[] SKILL_NAMES = { "Attack", "Defence", "Strength", "Hitpoints", "Range", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Construction", "Hunter", "Summoning" };
}
