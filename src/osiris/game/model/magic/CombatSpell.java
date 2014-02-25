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

import java.util.ArrayList;

import osiris.ServerEngine;
import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.combat.CombatUtilities;
import osiris.game.model.combat.Hit;
import osiris.game.model.combat.HitType;
import osiris.game.model.combat.Projectile;
import osiris.game.model.effect.ExpiringEffect;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class CombatSpell.
 * 
 * @author Boomer
 */
public class CombatSpell extends Spell {

	/** The hit graphic. */
	private int maxDamage, hitDelay, projectileId, hitGraphic;

	/** The leech. */
	private double leech;

	/** The multi target. */
	private boolean multiTarget;

	/** The hit type. */
	private HitType hitType = HitType.MAGIC;

	/**
	 * Instantiates a new combat spell.
	 * 
	 * @param level
	 *            the level
	 * @param animation
	 *            the animation
	 * @param graphic
	 *            the graphic
	 * @param expEarned
	 *            the exp earned
	 * @param runesRequired
	 *            the runes required
	 * @param maxDamage
	 *            the max damage
	 * @param hitDelay
	 *            the hit delay
	 * @param projectileId
	 *            the projectile id
	 * @param hitGraphic
	 *            the hit graphic
	 */
	public CombatSpell(int level, int animation, int graphic, int expEarned, int[][] runesRequired, int maxDamage, int hitDelay, int projectileId, int hitGraphic) {
		super(level, animation, graphic, expEarned, runesRequired);
		this.maxDamage = maxDamage;
		this.hitDelay = hitDelay;
		this.projectileId = projectileId;
		this.hitGraphic = hitGraphic;
		this.leech = 0.0;
		this.multiTarget = false;
	}

	/**
	 * Enable multi target.
	 * 
	 * @return the combat spell
	 */
	public CombatSpell enableMultiTarget() {
		this.multiTarget = true;
		return this;
	}

	/**
	 * Sets the leech.
	 * 
	 * @param leech
	 *            the leech
	 * @return the combat spell
	 */
	public CombatSpell setLeech(double leech) {
		this.leech = leech;
		return this;
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
		Character victim = (Character) params[0];
		ExpiringEffect effect = null;
		if (params.length > 1)
			effect = (ExpiringEffect) params[params.length - 1];
		if (projectileId != -1) {
			Projectile projectile = new Projectile(projectileId, character.getPosition(), victim.getPosition(), 50, 90, new int[] { 46, 31 }, victim, 0);
			character.getCombatManager().getProjectiles().add(projectile);
		}
		Hit hit = null;
		ArrayList<Character> nearCharacters = new ArrayList<Character>();
		if (multiTarget && !CombatUtilities.singleCombat(victim.getPosition())) {
			if (victim instanceof Player)
				nearCharacters.addAll(victim.getLocalPlayers());
			else if (character instanceof Player)
				nearCharacters.addAll(character.getLocalPlayers());
			else
				nearCharacters.addAll(victim.getLocalPlayers());
			if (victim instanceof Player)
				nearCharacters.addAll(((Player) victim).getLocalNpcs());
			else if (character instanceof Player)
				nearCharacters.addAll(((Player) character).getLocalNpcs());
		}
		while (nearCharacters.contains(character))
			nearCharacters.remove(character);
		if (!nearCharacters.contains(victim))
			nearCharacters.add(victim);
		for (Character near : nearCharacters) {
			if (!character.getCombatManager().canAttack(near))
				continue;
			if (Utilities.getDistance(victim.getPosition(), near.getPosition()) > 1)
				continue;
			if (maxDamage > 0)
				hit = new Hit(character, near, Utilities.random(maxDamage), hitDelay, hitType);
			else if (maxDamage == -1)
				hit = new Hit(character, near, -1, hitDelay, hitType);
			if (hit != null) {
				if (hitGraphic != -1)
					hit.setGraphic(hitGraphic, character.getSpellBook() == SpellBook.ANCIENT ? 0 : 100);
				if (leech != 0)
					hit.setLeech(leech);
				if (effect != null)
					hit.setEffects(effect);
				ServerEngine.getHitQueue().add(hit);
			}
		}
		return true;
	}

	/**
	 * Sets the max damage.
	 * 
	 * @param damage
	 *            the new max damage
	 */
	public void setMaxDamage(int damage) {
		this.maxDamage = damage;
	}

	/**
	 * Sets the hit type.
	 * 
	 * @param hitType
	 *            the new hit type
	 */
	public void setHitType(HitType hitType) {
		this.hitType = hitType;
	}
}
