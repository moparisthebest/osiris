package osiris.data;

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
import java.util.LinkedList;
import java.util.List;

import osiris.game.model.NpcDropContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class NpcDropLoader.
 * 
 * @author Boomer
 */
public class NpcDropLoader {

	/** The drop containers. */
	private static List<NpcDropContainer> dropContainers = new LinkedList<NpcDropContainer>();

	/**
	 * Adds the.
	 * 
	 * @param container
	 *            the container
	 */
	public static void add(NpcDropContainer container) {
		dropContainers.add(container);
	}

	/**
	 * Gets the containers.
	 * 
	 * @param npcId
	 *            the npc id
	 * @return the containers
	 */
	public static ArrayList<NpcDropContainer> getContainers(int npcId) {
		ArrayList<NpcDropContainer> containers = new ArrayList<NpcDropContainer>();
		for (NpcDropContainer container : dropContainers)
			for (int i : container.getNpcs())
				if (i == npcId)
					containers.add(container);
		return containers;
	}

	/**
	 * Gets the all containers.
	 * 
	 * @return the all containers
	 */
	public static List<NpcDropContainer> getAllContainers() {
		return dropContainers;
	}
}
