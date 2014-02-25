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

import osiris.game.action.impl.TeleportAction;
import osiris.game.model.Character;
import osiris.game.model.Position;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class TeleportSpell.
 * 
 * @author Boomer
 */
public class TeleportSpell extends Spell {

	/** The to. */
	private Position to;

	/** The ancients. */
	private boolean ancients;

	/** The radius y. */
	private int radiusX, radiusY;

	/**
	 * Instantiates a new teleport spell.
	 * 
	 * @param levelRequired
	 *            the level required
	 * @param expEarned
	 *            the exp earned
	 * @param runesRequired
	 *            the runes required
	 * @param ancients
	 *            the ancients
	 * @param to
	 *            the to
	 * @param radiusX
	 *            the radius x
	 * @param radiusY
	 *            the radius y
	 */
	public TeleportSpell(int levelRequired, double expEarned, int[][] runesRequired, boolean ancients, Position to, int radiusX, int radiusY) {
		super(levelRequired, -1, -1, expEarned, runesRequired);
		this.ancients = ancients;
		this.to = to;
		this.radiusX = radiusX;
		this.radiusY = radiusY;
	}

	/**
	 * Instantiates a new teleport spell.
	 * 
	 * @param to
	 *            the to
	 * @param radiusX
	 *            the radius x
	 * @param radiusY
	 *            the radius y
	 * @param ancients
	 *            the ancients
	 */
	public TeleportSpell(Position to, int radiusX, int radiusY, boolean ancients) {
		super(0, -1, -1, 0, null);
		this.ancients = ancients;
		this.to = to;
		this.radiusX = radiusX;
		this.radiusY = radiusY;
	}

	/**
	 * Instantiates a new teleport spell.
	 * 
	 * @param to
	 *            the to
	 * @param ancients
	 *            the ancients
	 */
	public TeleportSpell(Position to, boolean ancients) {
		this(to, 0, 0, ancients);
	}

	/**
	 * Instantiates a new teleport spell.
	 * 
	 * @param to
	 *            the to
	 */
	public TeleportSpell(Position to) {
		this(to, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.magic.Spell#onExecution(osiris.game.model.Character,
	 * java.lang.Object[])
	 */
	@Override
	public boolean onExecution(Character character, Object... params) {
		if (!(character.getCurrentAction() instanceof TeleportAction)) {
			// TODO: make it check if player can teleport
			new TeleportAction(character, calculatePosition(), (ancients ? TeleportAction.TeleportType.ANCIENT : TeleportAction.TeleportType.NORMAL)).run();
			return true;
		}
		return false;
	}

	/**
	 * Calculate position.
	 * 
	 * @return the position
	 */
	public Position calculatePosition() {
		return new Position(to.getX() + Utilities.random(radiusX), to.getY() + Utilities.random(radiusY));
	}
}
