package osiris.game.model;

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

import java.util.Iterator;
import java.util.Map;

import osiris.Main;
import osiris.game.model.effect.Effect;
import osiris.game.model.effect.ExpiringEffect;
import osiris.game.model.effect.PrayerEffect;
import osiris.game.model.skills.Prayer;
import osiris.util.StopWatch;

// TODO: Auto-generated Javadoc
/**
 * The Class InfoUpdater.
 * 
 * @author Boomer
 */
public class InfoUpdater implements Runnable {

	/** The Constant STAT_RECOVERY_TICKS. */
	public static final int STAT_RECOVERY_TICKS = 45;

	/** The Constant HEALTH_RECOVERY_TICKS. */
	public static final int HEALTH_RECOVERY_TICKS = 60;

	/** The timer. */
	private StopWatch timer;

	/**
	 * Instantiates a new info updater.
	 */
	public InfoUpdater() {
		timer = new StopWatch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			int elapsed = timer.elapsed();
			for (Player player : Main.getPlayers()) {
				if (player.isDying())
					continue;
				updateEffects(player);
				double healthPrayerBonus = 1, otherPrayerBonus = 1;
				PrayerEffect restore = player.getPrayers().get(Prayer.Type.RESTORE);
				if (restore != null) {
					if (restore.getPrayer() == Prayer.RAPID_HEAL)
						healthPrayerBonus *= .5;
					else if (restore.getPrayer() == Prayer.RAPID_RESTORE)
						otherPrayerBonus *= .5;
				}
				for (int s = 0; s < Skills.NUMBER_OF_SKILLS; s++) {
					int curLevel = player.getSkills().currentLevel(s);
					int maxLevel = player.getSkills().maxLevel(s);
					int recoveryTime = player.getSkills().getRecoveryTimes()[s];
					if (curLevel != maxLevel) {
						boolean greater = curLevel > maxLevel;
						if (recoveryTime == -1)
							player.getSkills().getRecoveryTimes()[s] = elapsed + (s == Skills.SKILL_HITPOINTS ? (int) (HEALTH_RECOVERY_TICKS * (greater ? 1 : healthPrayerBonus)) : (int) (STAT_RECOVERY_TICKS * (!greater ? 1 : (s == Skills.SKILL_PRAYER ? 0 : otherPrayerBonus))));
						else if (recoveryTime <= elapsed) {
							player.getSkills().getRecoveryTimes()[s] = elapsed + (s == Skills.SKILL_HITPOINTS ? (int) (HEALTH_RECOVERY_TICKS * (greater ? 1 : healthPrayerBonus)) : (int) (STAT_RECOVERY_TICKS * (!greater ? 1 : (s == Skills.SKILL_PRAYER ? 0 : otherPrayerBonus))));
							player.getSkills().setCurLevel(s, greater ? curLevel - 1 : curLevel + 1);
						}
						continue;
					}
					if (recoveryTime != -1)
						player.getSkills().getRecoveryTimes()[s] = -1;
				}
			}

			for (Npc npc : Main.getNpcs()) {
				if (npc.isDying())
					continue;
				updateEffects(npc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update effects.
	 * 
	 * @param character
	 *            the character
	 */
	public void updateEffects(Character character) {
		for (Iterator<Effect> effects = character.getEffects().iterator(); effects.hasNext();) {
			Effect effect = effects.next();
			if (effect instanceof ExpiringEffect && ((ExpiringEffect) effect).hasCooledOff()) {
				effect.terminate(character);
				effects.remove();
			} else
				effect.update(character);
		}
		if (character instanceof Player) {
			Player player = (Player) character;
			if (player.getPlayerStatus() == 2 && !Main.isLocal()) {
				return;
			}

			double prayerDrain = player.getPrayerDrainRate();
			if (prayerDrain > 0) {
				double prayerLevel = player.getSkills().realCurrentLevel(Skills.SKILL_PRAYER);
				double adjustedPrayerLevel = prayerLevel -= prayerDrain;
				if (adjustedPrayerLevel <= 0) {
					adjustedPrayerLevel = 0;
					player.getEventWriter().sendMessage("You have run out of prayer points!");
					for (Map.Entry<Prayer.Type, PrayerEffect> entry : player.getPrayers().entrySet()) {
						if (entry.getValue() != null) {
							entry.getValue().terminate(player);
							player.getEffects().remove(entry.getValue());
						}
						entry.setValue(null);
					}
				}
				player.getSkills().setCurLevel(Skills.SKILL_PRAYER, adjustedPrayerLevel);
			}
		}
	}
}
