package osiris.game.action.impl;

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

import osiris.game.action.TickedAction;
import osiris.game.model.Character;
import osiris.game.model.Position;
import osiris.game.update.block.AnimationBlock;
import osiris.game.update.block.GraphicsBlock;
import osiris.util.StopWatch;

// TODO: Auto-generated Javadoc
/**
 * The Class HomeTeleportAction.
 * 
 * @author Boomer
 */
public class HomeTeleportAction extends TickedAction {

	/** The Constant DELAY. */
	public static final int DELAY = 1;

	/** The Constant graphics. */
	public static final int[] animations = { 1723, 1724, 1725, 2798, 2799, 2800, 4847, 4848, 4849, 4850, 4851 }, graphics = { 800, 801, 802, 1703, 1704, 1705, 1706, 1707, 1708, 1711, 1712 };

	/** The timer. */
	private StopWatch timer;

	/** The increment. */
	private int increment;

	/** The to. */
	private Position to;

	/** The ignore home timer. */
	private boolean ignoreHomeTimer;

	/**
	 * Instantiates a new action.
	 * 
	 * @param character
	 *            the character
	 * @param to
	 *            teleporting to
	 */
	public HomeTeleportAction(Character character, Position to) {
		super(character, 1);
		character.getMovementQueue().reset();
		character.addUpdateBlock(new AnimationBlock(character, 1722, 0));
		this.to = to;
		this.increment = 0;
		this.timer = new StopWatch();
	}

	/**
	 * Ignore home timer.
	 * 
	 * @return the home teleport action
	 */
	public HomeTeleportAction ignoreHomeTimer() {
		this.ignoreHomeTimer = true;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.TickedAction#execute()
	 */
	@Override
	public void execute() {
		if (timer.elapsed() >= DELAY) {
			if (increment == animations.length) {
				this.cancel();
				new TeleportAction(getCharacter(), to, TeleportAction.TeleportType.HOME).run();
				if (!ignoreHomeTimer)
					getCharacter().getHomeTeleTimer().reset();
				return;
			}
			timer.reset();
			getCharacter().addUpdateBlock(new AnimationBlock(getCharacter(), animations[increment], 0));
			getCharacter().addUpdateBlock(new GraphicsBlock(getCharacter(), graphics[increment], 0));
			increment++;
		}
	}

}
