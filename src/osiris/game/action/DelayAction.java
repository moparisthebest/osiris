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

// TODO: Auto-generated Javadoc
/**
 * The Class DelayAction.
 * 
 * @author Boomer
 */
public class DelayAction extends Action {

	/** The delay. */
	private long delay;

	/**
	 * Instantiates a new action.
	 * 
	 * @param character
	 *            the character
	 * @param delay
	 *            the delay
	 */
	public DelayAction(Character character, long delay) {
		super(character);
		if (!character.getActionTimer().completed())
			cancel();
		this.delay = delay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#run()
	 */
	@Override
	public void run() {
		super.run();
		getCharacter().getActionTimer().setDelay(delay, null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
	}
}
