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
import osiris.game.model.skills.Prayer;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class PrayerEffect.
 * 
 * @author Boomer
 */
public class PrayerEffect extends Effect {

	/** The prayer. */
	private Prayer prayer;

	/**
	 * Instantiates a new prayer effect.
	 * 
	 * @param slot
	 *            the slot
	 */
	public PrayerEffect(int slot) {
		this.prayer = Prayer.values()[slot];
	}

	/**
	 * Gets the prayer.
	 * 
	 * @return the prayer
	 */
	public Prayer getPrayer() {
		return prayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.effect.Effect#execute(osiris.game.model.Character)
	 */
	@Override
	public void execute(Character character) {
		if (!(character instanceof Player))
			return;
		Player player = (Player) character;
		if (prayer == Prayer.PROTECT_FROM_SUMMONING) {
			player.getEventWriter().sendMessage("This prayer has not been added yet!");
			player.getEffects().remove(this);
			terminate(player);
			return;
		}
		if (player.getSkills().maxLevel(Skills.SKILL_PRAYER) < prayer.getLevelRequired()) {
			player.getEventWriter().sendMessage("You need level " + prayer.getLevelRequired() + " to use " + Utilities.toProperCase(prayer.name().replaceAll("_", "")) + "!");
			player.getEffects().remove(this);
			terminate(player);
			return;
		}

		if (player.getSkills().realCurrentLevel(Skills.SKILL_PRAYER) < 1) {
			player.getEffects().remove(this);
			terminate(player);
			return;
		}

		if (prayer.getHeadIcon() != -1)
			player.setHeadIcon(prayer.getHeadIcon());
		player.getEventWriter().sendConfig(prayer.getConfigId(), 1);
		player.getPrayers().put(prayer.getType(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.effect.Effect#equals(osiris.game.model.effect.Effect)
	 */
	@Override
	public boolean equals(Effect effect) {
		return effect instanceof PrayerEffect && ((PrayerEffect) effect).prayer.getType() == prayer.getType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.effect.Effect#terminate(osiris.game.model.Character)
	 */
	@Override
	public void terminate(Character character) {
		if (!(character instanceof Player))
			return;
		Player player = (Player) character;
		if (prayer.getHeadIcon() != -1)
			player.setHeadIcon(-1);
		player.getEventWriter().sendConfig(prayer.getConfigId(), 0);
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
