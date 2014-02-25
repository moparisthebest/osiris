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

/**
 * The Class Appearance.
 * 
 * @author Boomer
 * 
 */
public class Appearance {

	/**
	 * The gender.
	 */
	private int npcMaskId = -1, gender = 0;

	/**
	 * The colors.
	 */
	private int[] looks = new int[7], colors = new int[5];

	/**
	 * The Constant DEFAULT_LOOKS. 3, 10, 18, 26, 33, 36, 42
	 */
	public static final int[][] DEFAULT_LOOKS = { { 6, 14, 18, 26, 34, 38, 42 }, { 48, 57, 57, 64, 68, 77, 80 } };

	/**
	 * Instantiates a new appearance.
	 */
	public Appearance() {
		System.arraycopy(DEFAULT_LOOKS[gender], 0, looks, 0, looks.length);
		for (int i = 0; i < 5; i++)
			colors[i] = i * 3 + 2;
	}

	/**
	 * Sets the look.
	 * 
	 * @param slot
	 *            the slot
	 * @param id
	 *            the id
	 */
	public void setLook(int slot, int id) {
		looks[slot] = id;
	}

	/**
	 * Gets the npc mask id.
	 * 
	 * @return the npc mask id
	 */
	public int getNpcMaskId() {
		return npcMaskId;
	}

	/**
	 * Gets the gender.
	 * 
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * Gets the looks.
	 * 
	 * @return the looks
	 */
	public int[] getLooks() {
		return looks;
	}

	/**
	 * Gets the colors.
	 * 
	 * @return the colors
	 */
	public int[] getColors() {
		return colors;
	}

	/**
	 * To npc.
	 * 
	 * @param npcMaskId
	 *            the npc mask id
	 * @return the appearance
	 */
	public Appearance toNpc(int npcMaskId) {
		this.npcMaskId = npcMaskId;
		return this;
	}

	/**
	 * To player.
	 * 
	 * @return the appearance
	 */
	public Appearance toPlayer() {
		this.npcMaskId = -1;
		return this;
	}

	/**
	 * Sets the gender.
	 * 
	 * @param gender
	 *            the new gender
	 */
	public void setGender(int gender) {
		this.gender = gender;
		System.arraycopy(DEFAULT_LOOKS[gender], 0, looks, 0, looks.length);
	}

	/**
	 * The Constant LOOK_HEAD.
	 */
	public static final int LOOK_HEAD = 0;

	/**
	 * The Constant LOOK_BEARD.
	 */
	public static final int LOOK_BEARD = 1;

	/**
	 * The Constant LOOK_CHEST.
	 */
	public static final int LOOK_CHEST = 2;

	/**
	 * The Constant LOOK_ARMS.
	 */
	public static final int LOOK_ARMS = 3;

	/**
	 * The Constant LOOK_LEGS.
	 */
	public static final int LOOK_LEGS = 5;

	/**
	 * The Constant LOOK_HANDS.
	 */
	public static final int LOOK_HANDS = 4;

	/**
	 * The Constant LOOK_FEET.
	 */
	public static final int LOOK_FEET = 6;

}
