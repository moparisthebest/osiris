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

import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class NpcDrop.
 * 
 * @author Boomer
 */
public class NpcDrop {

	/** The chance. */
	private double chance;

	/** The high point. */
	private int itemId, lowPoint, highPoint;

	/**
	 * Instantiates a new npc drop.
	 * 
	 * @param itemId
	 *            the item id
	 * @param lowAmount
	 *            the low amount
	 * @param highAmount
	 *            the high amount
	 * @param chance
	 *            the chance
	 */
	public NpcDrop(int itemId, int lowAmount, int highAmount, double chance) {
		this.itemId = itemId;
		this.lowPoint = lowAmount;
		this.highPoint = highAmount;
		this.chance = chance;
	}

	/**
	 * Instantiates a new npc drop.
	 * 
	 * @param itemId
	 *            the item id
	 * @param amount
	 *            the amount
	 * @param chance
	 *            the chance
	 */
	public NpcDrop(int itemId, int amount, double chance) {
		this.itemId = itemId;
		this.lowPoint = amount;
		this.highPoint = amount;
		this.chance = chance;
	}

	/**
	 * Gets the item id.
	 * 
	 * @return the item id
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Gets the chance.
	 * 
	 * @return the chance
	 */
	public double getChance() {
		return chance;
	}

	/**
	 * Gets the amount.
	 * 
	 * @return the amount
	 */
	public int getAmount() {
		int amt = lowPoint + Utilities.random(Math.abs(highPoint - lowPoint));
		return amt;
	}
}
