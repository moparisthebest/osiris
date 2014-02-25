package osiris.util;

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

import static osiris.util.Settings.DISTANCED_TASK_CHECK_DELAY;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import osiris.ServerEngine;
import osiris.game.model.Character;
import osiris.game.model.Position;

/**
 * A task that fires when the position of a Character comes within a specified
 * distance of another position.
 * 
 * @author Blake
 * 
 */
public abstract class DistancedTask implements Runnable {

	/**
	 * The character.
	 */
	private final Character character;

	/**
	 * The position.
	 */
	private final Position position;

	/**
	 * The distance.
	 */
	private final int distance;

	/**
	 * The task.
	 */
	private ScheduledFuture<?> task;

	/**
	 * Instantiates a new distanced task.
	 * 
	 * @param character
	 *            the character
	 * @param position
	 *            the position
	 * @param distance
	 *            the distance
	 */
	public DistancedTask(Character character, Position position, int distance) {
		this.character = character;
		this.position = position;
		this.distance = distance;
	}

	/**
	 * Executes the task.
	 */
	public abstract void execute();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#cycle()
	 */
	@Override
	public void run() {
		/*
		 * We must schedule a new Runnable and hold a reference to the returned
		 * ScheduledFuture<?> so that the task itself can cancelAttack the
		 * repeating execution (used for checking) of itself once the condition
		 * is met and the actual logic within execute() runs.
		 */
		task = ServerEngine.getScheduler().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (atDestination()) {
					execute();
					task.cancel(false);
				}
			}
		}, 0, DISTANCED_TASK_CHECK_DELAY, TimeUnit.MILLISECONDS);
	}

	/**
	 * At destination.
	 * 
	 * @return true, if successful
	 */
	public boolean atDestination() {
		return Utilities.getDistance(this.getPosition(), character.getPosition()) <= distance;
	}

	/**
	 * Cancel.
	 */
	public void cancel() {
		task.cancel(false);
	}

	/**
	 * Gets the character.
	 * 
	 * @return the character
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * Gets the position.
	 * 
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the distance.
	 * 
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

}
