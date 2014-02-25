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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class NpcDropContainer.
 * 
 * @author Boomer
 */
public class NpcDropContainer {

	/** The npc ids. */
	private List<Integer> npcIds;

	/** The drops. */
	private List<NpcDrop> drops;

	/**
	 * Instantiates a new npc drop container.
	 * 
	 * @param npcIds
	 *            the npc ids
	 * @param loadedDrops
	 *            the loaded drops
	 */
	public NpcDropContainer(List<Integer> npcIds, List<NpcDrop> loadedDrops) {
		this.npcIds = npcIds;
		drops = loadedDrops;
	}

	/**
	 * Produce random drops.
	 * 
	 * @param character
	 *            the character
	 * @return the array list
	 */
	public ArrayList<NpcDrop> produceRandomDrops(Character character) {
		ArrayList<NpcDrop> droppedItems = new ArrayList<NpcDrop>();
		ArrayList<NpcDrop> clonedDrops = new ArrayList<NpcDrop>();
		clonedDrops.addAll(this.drops);
		for (NpcDrop drop : drops) {
			if (drop.getChance() == 1.0) {
				droppedItems.add(drop);
				clonedDrops.remove(drop);
			}
		}

		Random random = new Random();
		double luck = random.nextDouble();
		while (clonedDrops.size() > 0) {
			int slot = random.nextInt(clonedDrops.size());
			NpcDrop randomDrop = clonedDrops.get(slot);
			if (luck <= randomDrop.getChance()) {
				droppedItems.add(randomDrop);
				clonedDrops.clear();
			} else
				clonedDrops.remove(randomDrop);
		}
		return droppedItems;
	}

	/**
	 * Gets the npcs.
	 * 
	 * @return the npcs
	 */
	public List<Integer> getNpcs() {
		return npcIds;
	}

	/**
	 * Gets the drops.
	 * 
	 * @return the drops
	 */
	public List<NpcDrop> getDrops() {
		return drops;
	}
}
