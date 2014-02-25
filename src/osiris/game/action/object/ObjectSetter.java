package osiris.game.action.object;

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

import java.util.concurrent.TimeUnit;

import osiris.ServerEngine;
import osiris.game.model.WorldObject;
import osiris.game.model.WorldObjects;

// TODO: Auto-generated Javadoc
/**
 * The Class ObjectSetter.
 * 
 * @author Blake Beaupain
 * @author Boomer
 */
public class ObjectSetter {

	/** The original object. */
	private final WorldObject originalObject;

	/** The new object. */
	private final WorldObject newObject;

	/** The reset delay. */
	private final int resetDelay;

	/** The resetter. */
	private final Runnable resetter;

	/** The on reset. */
	private Runnable onReset;

	/**
	 * Instantiates a new object setter.
	 * 
	 * @param originalObject
	 *            the original object
	 * @param newObject
	 *            the new object
	 * @param resetDelay
	 *            the reset delay
	 */
	public ObjectSetter(WorldObject originalObject, WorldObject newObject, int resetDelay) {
		this.originalObject = originalObject;
		this.newObject = newObject;
		this.resetDelay = resetDelay;
		resetter = new Runnable() {
			@Override
			public void run() {
				// Reset to the old ID.
				ObjectSetter o = ObjectSetter.this;
				WorldObjects.removeObject(o.newObject);
				if (o.originalObject != null)
					WorldObjects.addObject(o.originalObject);
				if (onReset != null)
					onReset.run();
			}
		};
	}

	/**
	 * Sets the on reset.
	 * 
	 * @param onReset
	 *            the on reset
	 * @return the object setter
	 */
	public ObjectSetter setOnReset(Runnable onReset) {
		this.onReset = onReset;
		return this;
	}

	/**
	 * Execute.
	 */
	public void execute() {
		// Set to the new ID.
		WorldObjects.removeObject(originalObject);
		WorldObjects.addObject(newObject);

		// Schedule the resetter.
		ServerEngine.getScheduler().schedule(resetter, resetDelay, TimeUnit.SECONDS);
	}

}
