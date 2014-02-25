package osiris.game.action;

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

import osiris.game.model.Character;
import osiris.game.model.Position;
import osiris.util.DistancedTask;

// TODO: Auto-generated Javadoc

/**
 * The Class DistancedAction.
 * 
 * @author Blake
 * 
 */
public abstract class DistancedAction extends Action {

	/** The task. */
	private DistancedTask task;

	/**
	 * Instantiates a new distanced action.
	 * 
	 * @param character
	 *            the character
	 * @param position
	 *            the position
	 * @param distance
	 *            the distance
	 */
	public DistancedAction(Character character, Position position, int distance) {
		super(character);

		// Initialize the task.
		task = new DistancedTask(getCharacter(), position, distance) {
			@Override
			public void execute() {
				DistancedAction.this.execute();
			}
		};
	}

	/**
	 * Instantiates a new distanced action.
	 * 
	 * @param character
	 *            the character
	 * @param other
	 *            the other
	 * @param distance
	 *            the distance
	 */
	public DistancedAction(Character character, final Character other, final int distance) {
		super(character);

		// Initialize the task.
		task = new DistancedTask(getCharacter(), other.getPosition(), distance) {
			@Override
			public void execute() {
				DistancedAction.this.execute();
			}

			public Position getPosition() {
				return other.getPosition();
			}
		};
	}

	/**
	 * Execute.
	 */
	public abstract void execute();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#cycle()
	 */
	@Override
	public final void onRun() {
		task.run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#cancelAttack()
	 */
	@Override
	public void onCancel() {
		task.cancel();
	}

	/**
	 * Gets the task.
	 * 
	 * @return the task
	 */
	public DistancedTask getTask() {
		return task;
	}
}
