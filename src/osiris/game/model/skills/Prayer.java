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

/**
 * 
 * @author Boomer
 * 
 */
public enum Prayer {

	THICK_SKIN(1, 1, .05, Type.DEFENCE, 83), BURST_OF_SKIN(4, 1, .05, Type.STRENGTH, 84), CLARITY_OF_THOUGHT(7, 1, .05, Type.ATTACK, 85), SHARP_EYE(8, 1, .05, Type.RANGE, 862), MYSTIC_WILL(9, 1, .05, Type.MAGIC, 863), ROCK_SKIN(10, 2, .1, Type.DEFENCE, 86), SUPERHUMAN_STRENGTH(13, 2, .1, Type.STRENGTH, 87), IMPROVED_REFLEXES(16, 2, .1, Type.ATTACK, 88), RAPID_RESTORE(19, 1, .035, Type.RESTORE, 89), RAPID_HEAL(22, 2, .07, Type.RESTORE, 90), PROTECT_ITEM(25, 1, .07, Type.SALVAGE, 91), HAWK_EYE(26, 2, .1, Type.RANGE, 864), MYSTIC_LORE(27, 2, .1, Type.MAGIC, 865), STEEL_SKIN(28, 3, .2, Type.DEFENCE, 92), ULTIMATE_STRENGTH(31, 3, .2, Type.STRENGTH, 93), INCREDIBLE_REFLEXES(34, 3, .2, Type.ATTACK, 94), PROTECT_FROM_MAGIC(37, 1, .2, Type.HEADICON, 95, 2), PROTECT_FROM_MISSILES(40, 2, .2,
			Type.HEADICON, 96, 1), PROTECT_FROM_MELEE(43, 3, .2, Type.HEADICON, 97, 0), EAGLE_EYE(44, 3, .2, Type.RANGE, 866), MYSTIC_MIGHT(45, 3, .2, Type.MAGIC, 867), RETRIBUTION(46, 4, .05, Type.HEADICON, 98, 3), REDEMPTION(49, 5, .1, Type.HEADICON, 99, 5), SMITE(52, 6, .2, Type.HEADICON, 100, 4), PROTECT_FROM_SUMMONING(35, 1, .2, Type.HEADICON, 1168), CHIVALRY(60, 1, .4, Type.BADASS, 1052), PIETY(70, 1, .4, Type.BADASS, 1053);

	/** The level required. */
	private int levelRequired, stage, headIcon, configId;

	/** The drain rate. */
	private double drainRate;

	/** The type. */
	private Type type;

	/**
	 * Instantiates a new prayer.
	 * 
	 * @param levelRequired
	 *            the level required
	 * @param stage
	 *            the stage
	 * @param drainRate
	 *            the drain rate
	 * @param type
	 *            the type
	 * @param configId
	 *            the config id
	 * @param headIcon
	 *            the head icon
	 */
	Prayer(int levelRequired, int stage, double drainRate, Type type, int configId, int headIcon) {
		this.levelRequired = levelRequired;
		this.stage = stage;
		this.drainRate = drainRate;
		this.type = type;
		this.headIcon = headIcon;
		this.configId = configId;
	}

	/**
	 * Instantiates a new prayer.
	 * 
	 * @param levelRequired
	 *            the level required
	 * @param stage
	 *            the stage
	 * @param drainRate
	 *            the drain rate
	 * @param type
	 *            the type
	 * @param configId
	 *            the config id
	 */
	Prayer(int levelRequired, int stage, double drainRate, Type type, int configId) {
		this.levelRequired = levelRequired;
		this.stage = stage;
		this.drainRate = drainRate;
		this.type = type;
		this.configId = configId;
		this.headIcon = -1;

	}

	/**
	 * Gets the level required.
	 * 
	 * @return the level required
	 */
	public int getLevelRequired() {
		return levelRequired;
	}

	/**
	 * Gets the stage.
	 * 
	 * @return the stage
	 */
	public int getStage() {
		return stage;
	}

	/**
	 * Gets the config id.
	 * 
	 * @return the config id
	 */
	public int getConfigId() {
		return configId;
	}

	/**
	 * Gets the head icon.
	 * 
	 * @return the head icon
	 */
	public int getHeadIcon() {
		return headIcon;
	}

	/**
	 * Gets the drain rate.
	 * 
	 * @return the drain rate
	 */
	public double getDrainRate() {
		return drainRate;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * The Enum Type.
	 */
	public enum Type {
		ATTACK, STRENGTH, DEFENCE, RANGE, MAGIC, RESTORE, SALVAGE, HEADICON, BADASS
	}
}
