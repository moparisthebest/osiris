package osiris.game.model.magic;

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

import osiris.game.action.impl.HomeTeleportAction;
import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class HomeTeleportSpell.
 * 
 * @author Boomer
 */
public class HomeTeleportSpell extends TeleportSpell {

	/** The Constant COOLDOWN_TICKS. */
	public static final int COOLDOWN_TICKS = 1000;

	/**
	 * Instantiates a new home teleport spell.
	 * 
	 * @param to
	 *            the to
	 * @param radiusX
	 *            the radius x
	 * @param radiusY
	 *            the radius y
	 */
	public HomeTeleportSpell(Position to, int radiusX, int radiusY) {
		super(0, 0, null, false, to, radiusX, radiusY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.magic.TeleportSpell#onExecution(osiris.game.model.Character
	 * , java.lang.Object[])
	 */
	@Override
	public boolean onExecution(Character character, Object... params) {
		if (!(character.getCurrentAction() instanceof HomeTeleportAction)) {
			int elapsed = character.getHomeTeleTimer().elapsed();
			if (elapsed < COOLDOWN_TICKS) {
				if (character instanceof Player)
					((Player) character).getEventWriter().sendMessage("You need to wait " + Utilities.ticksToMins(COOLDOWN_TICKS - elapsed) + " more minutes before casting that spell!");
				return false;
			}
			new HomeTeleportAction(character, calculatePosition()).run();
			return true;
		}
		return false;
	}
}
