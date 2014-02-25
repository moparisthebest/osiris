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

import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class PlayerBonuses.
 * 
 * @author Boomer
 */
public class PlayerBonuses {

	/** The player. */
	private final Player player;

	/**
	 * Instantiates a new player bonuses.
	 * 
	 * @param player
	 *            the player
	 */
	public PlayerBonuses(Player player) {
		this.player = player;
	}

	/** The bonuses. */
	private int[] bonuses = new int[Settings.BONUSES.length];

	/**
	 * Update.
	 */
	public void update() {
		for (int n = 0; n < bonuses.length; n++) {
			bonuses[n] = 0;
		}
		for (int i = 0; i < player.getEquipment().getLength(); i++) {
			Item item = player.getEquipment().getItem(i);
			if (item == null)
				continue;
			ItemDef def = ItemDef.forId(item.getId());
			if (def.getBonuses() != null) {
				for (int n = 0; n < bonuses.length; n++) {
					bonuses[n] += def.getBonus(n);
				}
			}
		}

		player.getEventWriter().sendBonus(bonuses);
	}

	/**
	 * Sets the.
	 * 
	 * @param slot
	 *            the slot
	 * @param value
	 *            the value
	 */
	public void set(int slot, int value) {
		bonuses[slot] = value;
	}

	/**
	 * Gets the.
	 * 
	 * @param slot
	 *            the slot
	 * @return the int
	 */
	public int get(int slot) {
		return bonuses[slot];
	}

}