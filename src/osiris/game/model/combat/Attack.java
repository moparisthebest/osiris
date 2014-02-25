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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Random;

import osiris.ServerEngine;
import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.def.ItemDef;
import osiris.game.model.effect.CombatEffect;
import osiris.game.model.effect.PoisonEffect;
import osiris.game.model.item.Item;
import osiris.game.update.block.AnimationBlock;
import osiris.game.update.block.GraphicsBlock;
import osiris.util.Settings;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class Attack.
 * 
 * @author Boomer
 */
public class Attack {

	/** The hit type. */
	private HitType hitType;

	/** The delay. */
	private int delay;

	/** The hit graphic. */
	private int[] animation, attackGraphic, hitGraphic;
	// projectile id, speed, height, delay
	/** The projectiles. */
	private int[][] projectiles;
	// max hit, delay, drop arrow (if there is a drop arrow)
	/** The hits. */
	private int[][] hits;

	/** The chance. */
	private int chance;
	// effect, duration, chance,
	/** The effects. */
	private HashMap<Integer, Object[]> effects;

	/**
	 * Instantiates a new attack.
	 */
	public Attack() {
		this.delay = 3;
		this.animation = new int[] { 386, 451, 451, 386 };
		this.hitType = HitType.MELEE;
		this.attackGraphic = null;
		this.hitGraphic = null;
		this.projectiles = null;
		this.hits = new int[][] { { 0, 0 } };
		this.chance = 100;
		this.effects = new HashMap<Integer, Object[]>();
	}

	/**
	 * Adds the effect.
	 * 
	 * @param hitIndex
	 *            the hit index
	 * @param chance
	 *            the chance
	 * @param clazz
	 *            the clazz
	 * @param cooldown
	 *            the cooldown
	 * @param params
	 *            the params
	 * @return the attack
	 */
	public Attack addEffect(int hitIndex, int chance, Class<? extends CombatEffect> clazz, int cooldown, Object... params) {
		effects.put(hitIndex, new Object[] { chance, cooldown, clazz, params });
		return this;
	}

	/**
	 * Execute.
	 * 
	 * @param character
	 *            the character
	 * @param victim
	 *            the victim
	 * @param random
	 *            the random
	 * @return the int
	 */
	public int execute(Character character, Character victim, boolean random) {
		int anim = -1;
		int attackDelay = delay;
		if (animation != null) {
			if (character instanceof Player) {
				int attackStyle = ((Player) character).getAttackStyle();
				if (animation.length > attackStyle)
					anim = animation[attackStyle];
				else
					anim = animation[0];
			} else
				anim = animation[new Random().nextInt(animation.length)];
		}
		if (attackGraphic != null) {
			character.addPriorityUpdateBlock(new GraphicsBlock(character, attackGraphic[0], attackGraphic[1]));
		}
		if (anim != -1)
			character.addPriorityUpdateBlock(new AnimationBlock(character, anim, 0));
		if (projectiles != null)
			for (int p = 0; p < projectiles.length; p++) {
				int[] projectile = projectiles[p];
				Projectile proj = new Projectile(projectile[0], character.getPosition(), victim.getPosition(), 50, projectile[1], new int[] { projectile[2], 31 }, victim, projectile[3]);
				character.getCombatManager().getProjectiles().add(proj);
			}
		for (int h = 0; h < hits.length; h++) {
			int[] hitInfo = hits[h];
			int randomHit = hitInfo[0];
			if (random)
				randomHit = Utilities.random(randomHit);
			Hit hit = new Hit(character, victim, randomHit, hitInfo[1], hitType);
			Object[] effect = effects.get(h);
			if (effect != null) {
				if (new Random().nextInt(100) <= (Integer) effect[0]) {
					// noinspection unchecked
					hit.setEffects(CombatEffect.create(character, (Integer) effect[1], (Class<? extends CombatEffect>) effect[2], (Object[]) effect[3]));
				}
			}
			if (hitGraphic != null)
				hit.setGraphic(hitGraphic[0], hitGraphic[1]);
			if (hitInfo.length > 2)
				if (hitInfo[2] != -1)
					hit.setDropArrow(Item.create(hitInfo[2]));
			double poisonValue = -1;
			if (character instanceof Player) {
				String wepName = null;
				Item weapon = ((Player) character).getEquipment().getItem(Settings.SLOT_WEAPON);
				if (weapon != null)
					wepName = ItemDef.forId(weapon.getId()).getName().toLowerCase();
				if (wepName != null) {
					if (wepName.contains("(p") || wepName.contains("(s") || wepName.contains("(k")) {
						if (wepName.contains("++") || wepName.contains("(s"))
							poisonValue = 6.0;
						else if (wepName.contains("+"))
							poisonValue = 4.0;
						else
							poisonValue = 2.0;
					}
				}
			}
			if (poisonValue != -1)
				hit.setEffects(new PoisonEffect(-1, character, poisonValue));
			ServerEngine.getHitQueue().add(hit);
		}
		if (hitType == HitType.RANGED) {
			boolean rapid = false;
			if (character instanceof Player)
				rapid = ((Player) character).getAttackStyle() == 1;
			if (rapid)
				attackDelay--;
		}
		return attackDelay;
	}

	/**
	 * Gets the hit type.
	 * 
	 * @return the hit type
	 */
	public HitType getHitType() {
		return hitType;
	}

	/**
	 * Gets the attack delay.
	 * 
	 * @return the attack delay
	 */
	public int getAttackDelay() {
		return delay;
	}

	/**
	 * Sets the hit type.
	 * 
	 * @param hitType
	 *            the hit type
	 * @return the attack
	 */
	public Attack setHitType(HitType hitType) {
		this.hitType = hitType;
		return this;
	}

	/**
	 * Sets the delay.
	 * 
	 * @param delay
	 *            the delay
	 * @return the attack
	 */
	public Attack setDelay(int delay) {
		this.delay = delay;
		return this;
	}

	/**
	 * Sets the animation.
	 * 
	 * @param animation
	 *            the animation
	 * @return the attack
	 */
	public Attack setAnimation(int[] animation) {
		this.animation = animation;
		return this;
	}

	/**
	 * Sets the attack gfx.
	 * 
	 * @param graphic
	 *            the graphic
	 * @return the attack
	 */
	public Attack setAttackGfx(int[] graphic) {
		this.attackGraphic = graphic;
		return this;
	}

	/**
	 * Sets the hit gfx.
	 * 
	 * @param graphic
	 *            the graphic
	 * @return the attack
	 */
	public Attack setHitGfx(int[] graphic) {
		this.hitGraphic = graphic;
		return this;
	}

	/**
	 * Sets the chance.
	 * 
	 * @param chance
	 *            the chance
	 * @return the attack
	 */
	public Attack setChance(int chance) {
		this.chance = chance;
		return this;
	}

	/**
	 * Sets the projectiles.
	 * 
	 * @param projectiles
	 *            the projectiles
	 * @return the attack
	 */
	public Attack setProjectiles(int[][] projectiles) {
		this.projectiles = projectiles;
		return this;
	}

	/**
	 * Sets the hits.
	 * 
	 * @param hits
	 *            the hits
	 * @return the attack
	 */
	public Attack setHits(int[][] hits) {
		this.hits = hits;
		return this;
	}

	/**
	 * Creates the.
	 * 
	 * @param className
	 *            the class name
	 * @return the attack
	 */
	public static Attack create(String className) {
		try {
			return (Attack) Class.forName("osiris.game.model.combat.impl." + className).getConstructor().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
