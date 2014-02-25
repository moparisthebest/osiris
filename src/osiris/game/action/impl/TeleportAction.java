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

// TODO: Auto-generated Javadoc
/**
 * The Class TeleportAction.
 * 
 * @author Boomer
 */
public class TeleportAction extends TickedAction {

	/** The to. */
	private Position to;

	/** The type. */
	private TeleportType type;

	/** The stage. */
	private int stage;

	/**
	 * The Enum TeleportType.
	 */
	public enum TeleportType {
		HOME, NORMAL, ANCIENT
	}

	/**
	 * Instantiates a new timed action.
	 * 
	 * @param character
	 *            the character
	 * @param to
	 *            the position
	 * @param type
	 *            the type
	 */
	public TeleportAction(Character character, Position to, TeleportType type) {
		super(character, type == TeleportType.HOME ? 1 : 2);
		character.getMovementQueue().reset();
		this.to = to;
		this.type = type;
		this.stage = 0;
		boolean ancients = type == TeleportType.ANCIENT;
		boolean home = type == TeleportType.HOME;
		getCharacter().addUpdateBlock(new AnimationBlock(getCharacter(), (ancients ? 1979 : (home ? 4852 : 8939)), 0));
		getCharacter().addUpdateBlock(new GraphicsBlock(getCharacter(), (ancients ? 392 : (home ? 1713 : 1576)), 0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.TickedAction#execute()
	 */
	@Override
	public void execute() {
		if (!getCharacter().canTeleport()) {
			cancel();
			return;
		}
		if (stage == 0) {
			getCharacter().teleport(to);
			boolean ancients = type == TeleportType.ANCIENT;
			boolean home = type == TeleportType.HOME;
			if (!home) {
				getCharacter().addUpdateBlock(new AnimationBlock(getCharacter(), (ancients ? -1 : 8941), 0));
				getCharacter().addUpdateBlock(new GraphicsBlock(getCharacter(), (ancients ? 455 : 1577), 0));
			}
		}
		if (stage == 2) {
			this.cancel();
		}
		stage++;
	}

}
