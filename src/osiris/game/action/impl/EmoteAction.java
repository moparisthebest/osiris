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

import osiris.game.action.Action;
import osiris.game.model.Character;
import osiris.game.update.block.AnimationBlock;
import osiris.game.update.block.GraphicsBlock;
import osiris.util.StopWatch;

// TODO: Auto-generated Javadoc
/**
 * The Class EmoteAction.
 * 
 * @author Boomer
 */
public class EmoteAction extends Action {

	/** The graphic id. */
	private int emoteId, graphicId = -1;

	/** The emote duration. */
	private StopWatch emoteDuration;

	/**
	 * Instantiates a new ticked action.
	 * 
	 * @param character
	 *            the character
	 * @param emoteId
	 *            the emote id
	 */
	public EmoteAction(Character character, int emoteId) {
		super(character);
		this.emoteId = emoteId;
		emoteDuration = new StopWatch();
	}

	/**
	 * Instantiates a new emote action.
	 * 
	 * @param character
	 *            the character
	 * @param emoteId
	 *            the emote id
	 * @param graphicId
	 *            the graphic id
	 */
	public EmoteAction(Character character, int emoteId, int graphicId) {
		this(character, emoteId);
		this.graphicId = graphicId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
		getCharacter().addUpdateBlock(new AnimationBlock(getCharacter(), emoteId, 0));
		if (graphicId != -1)
			getCharacter().addUpdateBlock(new GraphicsBlock(getCharacter(), graphicId, 0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
		if (emoteDuration.elapsed() < 3) {
			getCharacter().addUpdateBlock(new AnimationBlock(getCharacter(), -1, 0));
			getCharacter().addUpdateBlock(new GraphicsBlock(getCharacter(), -1, 0));
		}

	}
}
