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

import static osiris.util.Settings.TICKRATE;

import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import osiris.ServerEngine;
import osiris.game.model.Character;
import osiris.util.StopWatch;

// TODO: Auto-generated Javadoc
/**
 * A ticked action.
 * 
 * @author Blake
 * 
 */
public abstract class TickedAction extends Action {

	/**
	 * The ticks.
	 */
	private int ticks;

	/**
	 * The starting value for random tick values.
	 */
	private int inclusive;

	/**
	 * The ending value for random tick values.
	 */
	private int exclusive;

	/**
	 * The task.
	 */
	private ScheduledFuture<?> task;

	/**
	 * Generates a random tick based off of the inclusive and exclusive values.
	 * 
	 * @param character
	 *            The character.
	 * @param inclusive
	 *            The starting value.
	 * @param exclusive
	 *            The ending value.
	 */
	public TickedAction(Character character, int inclusive, int exclusive) {
		super(character);
		this.inclusive = inclusive;
		this.exclusive = exclusive;

		Random rand = new Random();
		this.ticks = (rand.nextInt(exclusive - inclusive) + inclusive);
	}

	/**
	 * Instantiates a new ticked action.
	 * 
	 * @param character
	 *            the character
	 * @param ticks
	 *            the ticks
	 */
	public TickedAction(Character character, int ticks) {
		super(character);
		this.ticks = ticks;
	}

	/**
	 * Execute.
	 */
	public abstract void execute();

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public final void onRun() {
		final StopWatch timer = new StopWatch();
		task = ServerEngine.getScheduler().scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if (timer.elapsed() >= ticks) {
					TickedAction.this.execute();
					timer.reset();
				}
			}
		}, 0, TICKRATE, TimeUnit.MILLISECONDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#cancelAttack()
	 */
	@Override
	public void onCancel() {
		task.cancel(false);
	}

	/**
	 * Sets the current tick to the given value.
	 * 
	 * @param ticks
	 *            The new tick value.
	 */
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	/**
	 * Gets the current tick value.
	 * 
	 * @return The tick value.
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * Sets the inclusive value for a random tick.
	 * 
	 * @param inclusive
	 *            The starting value.
	 */
	public void setInclusive(int inclusive) {
		this.inclusive = inclusive;
	}

	/**
	 * Gets the inclusive value.
	 * 
	 * @return The ending value.
	 */
	public int getInclusive() {
		return inclusive;
	}

	/**
	 * Sets the exclusive value for a random tick.
	 * 
	 * @param exclusive
	 *            The ending value.
	 */
	public void setExclusive(int exclusive) {
		this.exclusive = exclusive;
	}

	/**
	 * Gets the exclusive value.
	 * 
	 * @return The starting value.
	 */
	public int getExclusive() {
		return exclusive;
	}

}
