package osiris.game.model.combat;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import osiris.Main;
import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.game.model.Skills;
import osiris.game.model.combat.missile.RangedAttack;
import osiris.game.model.effect.InCombatEffect;
import osiris.game.model.effect.PrayerEffect;
import osiris.game.model.skills.Prayer;

// TODO: Auto-generated Javadoc
/**
 * The Class CombatUtilities.
 * 
 * @author Boomer
 * 
 */
public class CombatUtilities {

	/**
	 * Calculate hit damage.
	 * 
	 * @param character
	 *            the character
	 * @param attack
	 *            the attack
	 * @param ranged
	 *            the ranged
	 * @return the int
	 */
	public static int calculateMaxDamage(Character character, EquippedWeapon attack, RangedAttack ranged) {
		if (character instanceof Player) {
			Player player = (Player) character;
			if (attack.getHitType() == HitType.RANGED) {
				double prayerBonus = 1;
				PrayerEffect prayer = player.getPrayers().get(Prayer.Type.RANGE);
				if (prayer != null)
					prayerBonus += .05 * prayer.getPrayer().getStage();
				int rangedLevel = player.getSkills().currentLevel(Skills.SKILL_RANGE);
				double effectiveStrength = Math.floor(rangedLevel * prayerBonus) + (player.getAttackStyle() == 0 ? 3 : 0);
				int rangedStrength = 0;
				if (ranged != null)
					rangedStrength = ranged.getRangeBonus();
				double baseDamage = 5 + (((effectiveStrength + 8) * (rangedStrength + 64)) / 64);
				double bonus = 1;
				baseDamage *= bonus;
				int max = (int) Math.floor(baseDamage) / 10;
				return max;

			} else if (attack.getHitType() == HitType.MELEE) {
				int strengthLevel = ((Player) character).getSkills().currentLevel(Skills.SKILL_STRENGTH);
				int strengthBonus = ((Player) character).getBonuses().get(10);
				double prayerBonus = 1;
				PrayerEffect prayer = player.getPrayers().get(Prayer.Type.RANGE);
				if (prayer != null)
					prayerBonus += .05 * prayer.getPrayer().getStage();
				else if ((prayer = player.getPrayers().get(Prayer.Type.BADASS)) != null)
					prayerBonus += (prayer.getPrayer() == Prayer.CHIVALRY ? .18 : .23);
				strengthLevel *= prayerBonus;
				int attackStyle = ((Player) character).getAttackStyle();
				for (int i : attack.getSkills()[attackStyle])
					if (i == Skills.SKILL_STRENGTH) {
						strengthLevel += 3;
						break;
					}
				double multiplier = 0.10 + (strengthBonus * 0.00175);
				double maxHit = 1.05 + (strengthLevel * multiplier);
				double finalMultiplier = 1;
				maxHit *= finalMultiplier;
				return (int) maxHit;
			}
		}
		return 0;
	}

	/**
	 * Single combat.
	 * 
	 * @param position
	 *            the position
	 * @return true, if successful
	 */
	public static boolean singleCombat(Position position) {
		return true;
	}

	/**
	 * Calculate killer.
	 * 
	 * @param hitsStory
	 *            the hits story
	 * @return the character
	 */
	public static Character calculateKiller(ArrayList<HitResult> hitsStory) {
		Map<Integer, Integer> sortedHits = new HashMap<Integer, Integer>();
		for (HitResult hit : hitsStory) {
			if (sortedHits.containsKey(hit.getAttackerUId()))
				sortedHits.put(hit.getAttackerUId(), sortedHits.get(hit.getAttackerUId()) + hit.getDamage());
			else
				sortedHits.put(hit.getAttackerUId(), hit.getDamage());
		}
		int currentLeader = -1;
		int currentDamage = 0;
		for (Map.Entry<Integer, Integer> e : sortedHits.entrySet()) {
			if (e.getValue() > currentDamage) {
				currentLeader = e.getKey();
				currentDamage = e.getValue();
			}
		}
		if (currentLeader == -1)
			return null;
		else
			return Main.findPlayer(currentLeader);
	}

	/**
	 * Calculate combat distance.
	 * 
	 * @param type
	 *            the type
	 * @return the int
	 */
	public static int calculateCombatDistance(HitType type) {
		switch (type) {
		case MELEE:
			return 1;
		case RANGED:
			return 8;
		case MAGIC:
		case OTHER:
			return 12;
		}
		return -1;
	}

	/**
	 * Check wilderness.
	 * 
	 * @param attacker
	 *            the attacker
	 * @param victim
	 *            the victim
	 * @return true, if successful
	 */
	public static boolean checkWilderness(Character attacker, Character victim) {
		InCombatEffect attackerInCombat = attacker.getCombatManager().getCombatEffect();
		InCombatEffect victimInCombat = victim.getCombatManager().getCombatEffect();
		InCombatEffect toAdd = new InCombatEffect(attacker);
		if (attackerInCombat == null && victimInCombat == null) {
			victim.addEffect(toAdd);
			return true;
		}
		boolean attackerSingle = singleCombat(attacker.getPosition());
		boolean victimSingle = singleCombat(victim.getPosition());
		if (attackerSingle && attackerInCombat != null && attackerInCombat.getAttacker() != null && !attackerInCombat.getAttacker().equals(victim)) {
			if (attackerInCombat.getAttacker().isDying())
				attacker.getEffects().remove(attackerInCombat);
			else {
				trySendMessage(attacker, "You are already in combat!");
				return false;
			}
		}
		if (victimSingle && victimInCombat != null && victimInCombat.getAttacker() != null && !victimInCombat.getAttacker().equals(attacker)) {
			if (victimInCombat.getAttacker().isDying())
				victim.getEffects().remove(victimInCombat);
			else {
				trySendMessage(attacker, "You cannot attack that " + victim.getClass().getSimpleName().toLowerCase());
				return false;
			}
		}
		if (victimInCombat != null)
			victimInCombat.getTimer().reset();
		else
			victim.addEffect(toAdd);
		return true;
	}

	/**
	 * Try send message.
	 * 
	 * @param character
	 *            the character
	 * @param message
	 *            the message
	 */
	public static void trySendMessage(Character character, String message) {
		if (character instanceof Player)
			((Player) character).getEventWriter().sendMessage(message);
	}
}