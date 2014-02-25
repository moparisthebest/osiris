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

import osiris.game.action.impl.TeleportAction;
import osiris.game.model.Character;
import osiris.game.model.Death;
import osiris.game.model.Player;

// TODO: Auto-generated Javadoc

/**
 * A timed game action.
 * 
 * @author Blake
 */
public abstract class Action implements Runnable {

	/**
	 * The player.
	 */
	private final Character character;

	/**
	 * Instantiates a new action.
	 * 
	 * @param character
	 *            the character
	 */
	public Action(Character character) {
		this.character = character;
		if (!canRun())
			return;
		Action current = character.getCurrentAction();
		if (current != null) {
			current.cancel();
		}
		character.setCurrentAction(this);
	}

	/**
	 * Cancel.
	 */
	public abstract void onCancel();

	// public abstract boolean inProgress();

	/**
	 * Cancel.
	 */
	public void cancel() {
		onCancel();
		character.setCurrentAction(null);
	}

	/**
	 * Gets the player.
	 * 
	 * @return the player
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * Gets the player.
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return (Player) character;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (!canRun())
			return;
		else
			onRun();
	}

	/**
	 * Can run.
	 * 
	 * @return true, if successful
	 */
	public boolean canRun() {
		if (character == null)
			return false;
		Action current = character.getCurrentAction();
		if (current != null) {
			if (current instanceof Death || current instanceof TeleportAction)
				if (!this.equals(current))
					return false;
		}
		return true;
	}

	/**
	 * On run.
	 */
	public abstract void onRun();
}
