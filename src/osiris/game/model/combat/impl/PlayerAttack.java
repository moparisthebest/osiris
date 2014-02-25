package osiris.game.model.combat.impl;

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

import java.util.Random;

import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.combat.Attack;
import osiris.game.model.combat.CombatUtilities;
import osiris.game.model.combat.EquippedWeapon;
import osiris.game.model.combat.HitType;
import osiris.game.model.combat.SpecialManager;
import osiris.game.model.combat.missile.RangedAttack;
import osiris.game.model.item.Item;
import osiris.util.Settings;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class PlayerAttack.
 * 
 * @author Boomer
 */
public class PlayerAttack extends Attack {

	/** The weapon. */
	private EquippedWeapon weapon;

	/**
	 * Instantiates a new player attack.
	 * 
	 * @param weapon
	 *            the weapon
	 */
	public PlayerAttack(EquippedWeapon weapon) {
		setAnimation(weapon.getAttackAnimations());
		setHitType(weapon.getHitType());
		setDelay(weapon.getAttackDelay());
		this.weapon = weapon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.combat.Attack#execute(osiris.game.model.Character,
	 * osiris.game.model.Character, boolean)
	 */
	@Override
	public int execute(Character character, Character other, boolean random) {
		RangedAttack ranged = null;
		if (weapon.getHitType() == HitType.RANGED) {
			ranged = character.getCombatManager().getRangedAttack();
			if (ranged == null)
				return -1;
		}
		int maxHit = CombatUtilities.calculateMaxDamage(character, weapon, ranged);
		int hitDelay = 0;
		int amountOfHits = 1;
		int hits[][] = null;
		if (ranged != null) {
			if (ranged.getArrow() == null)
				amountOfHits = 1;
			else
				amountOfHits = ranged.getArrow().getAmount();
			int[][] projectiles = new int[amountOfHits][4];
			setAttackGfx(new int[] { ranged.getPullback(), 100 });
			for (int i = 0; i < amountOfHits; i++) {
				projectiles[i][0] = ranged.getProjectileId();
				projectiles[i][1] = 90 + (i * 20);
				projectiles[i][2] = 46 + (i * 20);
				projectiles[i][3] = 0;
			}
			setProjectiles(projectiles);
			hitDelay = 3;
			hits = new int[amountOfHits][3];
		} else
			hits = new int[amountOfHits][2];
		if ((character instanceof Player && ((Player) character).isSpecEnabled())) {
			Player player = (Player) character;
			Item item = player.getEquipment().getItem(Settings.SLOT_WEAPON);
			Attack special = SpecialManager.getSpecial(player, other, item, maxHit, hitDelay, ranged);
			if (special != null) {
				return special.setDelay(getAttackDelay()).execute(player, other, false);
			}
		}
		for (int i = 0; i < amountOfHits; i++) {
			int randomDamage = Utilities.random(maxHit);
			hits[i][0] = randomDamage;
			hits[i][1] = hitDelay + i;
			if (hits[i].length > 2 && ranged != null && ranged.getArrow() != null)
				hits[i][2] = new Random().nextDouble() <= .85 ? ranged.getArrow().getId() : -1;
		}
		setHits(hits);
		return super.execute(character, other, false);
	}

}
