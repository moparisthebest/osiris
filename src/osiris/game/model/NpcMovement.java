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

import java.util.List;
import java.util.Random;

import osiris.Main;

// TODO: Auto-generated Javadoc
/**
 * Makes NPCs walk around randomly.
 * 
 * @author Blake
 * 
 */
public class NpcMovement implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Random random = new Random();
		for (Npc npc : Main.getNpcs()) {
			if (!npc.isWalking() || npc.getCombatManager().combatEnabled() || !npc.canMove())
				continue;
			if (npc.getWalkablePositions().size() != 0) {
				if (random.nextInt(3) == 1) {
					List<Position> positions = npc.getWalkablePositions();
					Position position = positions.get(random.nextInt(positions.size()));
					npc.getMovementQueue().addFirstStep(position);
				}
			}
		}
	}

}
