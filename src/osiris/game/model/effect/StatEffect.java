package osiris.game.model.effect;

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
import osiris.game.model.Player;
import osiris.game.model.Skills;
import osiris.util.StopWatch;

// TODO: Auto-generated Javadoc
/**
 * The Class StatEffect.
 * 
 * @author Boomer
 */
@SuppressWarnings("unused")
public class StatEffect extends CombatEffect {

	/** The skill id. */
	private int skillId;

	/** The adjustment. */
	private double adjustment;

	/** The timer. */
	private StopWatch timer;

	/**
	 * Instantiates a new stat effect.
	 * 
	 * @param cooldown
	 *            the cooldown
	 * @param character
	 *            the character
	 * @param skillId
	 *            the skill id
	 * @param adjustment
	 *            the adjustment
	 */
	public StatEffect(Integer cooldown, Character character, Object skillId, Object adjustment) {
		super(cooldown, null);
		this.skillId = (Integer) skillId;
		this.adjustment = (Double) adjustment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.effect.Effect#execute(osiris.game.model.Character)
	 */
	@Override
	public void execute(Character character) {
		if (character instanceof Player) {
			Skills skills = ((Player) character).getSkills();
			skills.setCurLevel(skillId, skills.currentLevel(skillId) + (int) (skills.maxLevel(skillId) * adjustment));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.effect.Effect#equals(osiris.game.model.effect.Effect)
	 */
	@Override
	public boolean equals(Effect effect) {
		return effect instanceof StatEffect && ((StatEffect) effect).skillId == skillId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.effect.Effect#update(osiris.game.model.Character)
	 */
	@Override
	public void update(Character character) {
	}
}
