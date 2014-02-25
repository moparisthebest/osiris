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

import osiris.Main;
import osiris.ServerEngine;
import osiris.game.action.Action;
import osiris.game.action.impl.CombatAction;
import osiris.game.action.impl.TeleportAction;
import osiris.game.model.Character;
import osiris.game.model.Npc;
import osiris.game.model.Player;
import osiris.game.model.Skills;
import osiris.game.model.effect.ExpiringEffect;
import osiris.game.model.effect.PrayerEffect;
import osiris.game.model.ground.GroundItem;
import osiris.game.model.ground.GroundManager;
import osiris.game.model.item.Item;
import osiris.game.model.skills.Prayer;
import osiris.game.update.block.AnimationBlock;
import osiris.game.update.block.GraphicsBlock;
import osiris.util.StopWatch;

// TODO: Auto-generated Javadoc
/**
 * The Class Hit.
 * 
 * @author Boomer
 * 
 */
public class Hit {

	/**
	 * The victim.
	 */
	private final Character attacker;

	/** The victim. */
	private Character victim;

	/**
	 * The tick delay.
	 */
	private final int delay;

	/** The graphic. */
	private int[] graphic;

	/** The hit. */
	private int hit;

	/** The max hit. */
	private final int maxHit;

	/** The override animation. */
	private int overrideAnimation;

	/** The leech. */
	private double leech;

	/** The drop arrow. */
	private Item dropArrow;

	/** The effects. */
	private ArrayList<ExpiringEffect> effects;

	/**
	 * The tick timer.
	 */
	private final StopWatch stopWatch;

	/**
	 * The attack type.
	 */
	private final HitType hitType;

	/**
	 * Instantiates a new hit.
	 * 
	 * @param attacker
	 *            the attacker
	 * @param victim
	 *            the victim \
	 * @param hitDamage
	 *            the max damage
	 * @param delay
	 *            the delay before the hit shows
	 * @param hitType
	 *            the attack type
	 */
	public Hit(final Character attacker, final Character victim, int hitDamage, int delay, HitType hitType) {
		this.victim = victim;
		this.attacker = attacker;
		this.hitType = hitType;
		this.stopWatch = new StopWatch();
		this.hit = hitDamage;
		this.maxHit = hit;
		this.delay = delay;
		this.leech = 0;
		this.effects = new ArrayList<ExpiringEffect>();
		this.dropArrow = null;
		this.overrideAnimation = -1;
		this.graphic = new int[] { -1, 0 };
		if (attacker instanceof Player) {
			if (victim != null && victim instanceof Player) {
				PrayerEffect overHeadEffect = ((Player) victim).getPrayers().get(Prayer.Type.HEADICON);
				if (overHeadEffect != null) {
					Prayer overHead = overHeadEffect.getPrayer();
					if (overHead != null) {
						switch (hitType) {
						case MELEE:
							if (overHead == Prayer.PROTECT_FROM_MELEE)
								hit *= .4;
							break;
						case RANGED:
							if (overHead == Prayer.PROTECT_FROM_MISSILES)
								hit *= .4;
							break;
						case MAGIC:
							if (overHead == Prayer.PROTECT_FROM_MAGIC)
								hit *= .4;
							break;
						}
					}
				}
			}
		}
		if (this.hit > 0) {
			if (hitType == HitType.MELEE && victim != null) {
				if (overrideAnimation != -1 && victim instanceof Player)
					victim.addUpdateBlock(new AnimationBlock(victim, overrideAnimation, 0));
				else {
					int block = victim.getBlockAnimation();
					if (block != -1)
						victim.addUpdateBlock(new AnimationBlock(victim, block, 0));
				}
			}
			if (attacker instanceof Player && hitType != HitType.POISON && hitType != HitType.RECOIL && hitType != HitType.BURN) {
				int[] skillsEarned;
				if (hitType == HitType.MAGIC)
					skillsEarned = new int[] { 6 };
				else
					skillsEarned = ((Player) attacker).getEquippedAttack().getSkills()[((Player) attacker).getAttackStyle()];
				double experiencePerStat = 4.0 / skillsEarned.length;
				for (int i = 0; i < skillsEarned.length; i++)
					((Player) attacker).getSkills().addExp(skillsEarned[i], hit * experiencePerStat);
				((Player) attacker).getSkills().addExp(Skills.SKILL_HITPOINTS, hit * 1.5);
			}
		}
	}

	/**
	 * Sets the graphic.
	 * 
	 * @param graphicId
	 *            the graphic id
	 * @param height
	 *            the height
	 */
	public void setGraphic(int graphicId, int height) {
		this.graphic = new int[] { graphicId, height };
	}

	/**
	 * Sets the leech.
	 * 
	 * @param leech
	 *            the new leech
	 */
	public void setLeech(double leech) {
		this.leech = leech;
	}

	/**
	 * Sets the effects.
	 * 
	 * @param effect
	 *            the new effects
	 */
	public void setEffects(ExpiringEffect effect) {
		this.effects.add(effect);
	}

	/**
	 * Sets the drop arrow.
	 * 
	 * @param item
	 *            the new drop arrow
	 */
	public void setDropArrow(Item item) {
		this.dropArrow = item;
	}

	/**
	 * Execute.
	 * 
	 * @return the int
	 */
	public boolean execute() {
		if (victim == null)
			return false;
		Action currentAction = victim.getCurrentAction();
		if (currentAction instanceof TeleportAction)
			return false;
		if (victim.getCurrentAction() != null && !(victim.getCurrentAction() instanceof CombatAction))
			if (hitType != HitType.POISON)
				victim.getCurrentAction().cancel();
		int currentHitpoints = victim.getCurrentHp();
		if (currentHitpoints == 0)
			return false;
		for (ExpiringEffect effect : effects)
			if (victim.addEffect(effect))
				effect.execute(victim);
		if (hit != -1) {
			if (hit >= currentHitpoints)
				hit = currentHitpoints;
			if (attacker instanceof Npc) {
				if (victim != null && victim instanceof Player) {
					PrayerEffect overHeadEffect = ((Player) victim).getPrayers().get(Prayer.Type.HEADICON);
					if (overHeadEffect != null) {
						Prayer overHead = overHeadEffect.getPrayer();
						if (overHead != null) {
							switch (hitType) {
							case MELEE:
								if (overHead == Prayer.PROTECT_FROM_MELEE)
									hit = 0;
								break;
							case RANGED:
								if (overHead == Prayer.PROTECT_FROM_MISSILES)
									hit = 0;
								break;
							case MAGIC:
								if (overHead == Prayer.PROTECT_FROM_MAGIC)
									hit = 0;
								break;
							}
						}
					}
				}
			}

			if (leech != 0) {
				int attackerHp = attacker.getCurrentHp();
				int adjustedHp = attackerHp + (int) Math.ceil(hit * leech);
				int maxHp = attacker.getMaxHp();
				if (adjustedHp > attacker.getMaxHp())
					adjustedHp = maxHp;
				attacker.setCurrentHp(adjustedHp);
			}

			if (dropArrow != null) {
				GroundItem item = new GroundItem(dropArrow, attacker, victim.getPosition());
				GroundManager.getManager().dropItem(item);
			}

			// XXX: This way admins aren't killed by accident. This can be
			// disabled by
			// running the server in verbose mode.
			boolean canDamage = true;
			if (victim instanceof Player) {
				if (((Player) victim).getPlayerStatus() == 2 && !Main.isLocal()) {
					canDamage = false;
				}
			}

			if (canDamage) {
				victim.setCurrentHp(currentHitpoints - hit);
			}
			if (victim instanceof Player) {
				if (attacker instanceof Player) {
					Player player = (Player) attacker;
					Player vic = (Player) victim;
					PrayerEffect revenge = player.getPrayers().get(Prayer.Type.HEADICON);
					if (revenge != null && revenge.getPrayer() == Prayer.SMITE) {
						double prayerSmited = hit / 4;
						double currentPrayer = vic.getSkills().realCurrentLevel(Skills.SKILL_PRAYER);
						double adjustedPrayer = currentPrayer -= prayerSmited;
						if (adjustedPrayer < 0)
							adjustedPrayer = 0;
						vic.getSkills().setCurLevel(Skills.SKILL_PRAYER, adjustedPrayer);
					}

				}
				if (victim.getCurrentHp() > 0 && victim.getCurrentHp() < (victim.getMaxHp() * .1)) {
					Player player = (Player) victim;
					PrayerEffect revenge = player.getPrayers().get(Prayer.Type.HEADICON);
					if (revenge != null && revenge.getPrayer() == Prayer.REDEMPTION) {
						player.getSkills().setCurLevel(Skills.SKILL_PRAYER, 0);
						player.addUpdateBlock(new GraphicsBlock(player, 436, 0));
						int currentHp = player.getCurrentHp();
						int prayerLevel = player.getSkills().maxLevel(Skills.SKILL_PRAYER);
						int adjustedHp = currentHp += prayerLevel * .25;
						int maxHp = player.getMaxHp();
						if (adjustedHp > maxHp)
							adjustedHp = maxHp;
						player.setCurrentHp(adjustedHp);
					}
				}

			}

			if (victim.getCurrentHp() != 0 && this.getAttackType() != HitType.MELEE && this.getAttackType() != HitType.POISON && this.getAttackType() != HitType.RECOIL) {
				if (overrideAnimation != -1 && victim instanceof Player)
					victim.addUpdateBlock(new AnimationBlock(victim, overrideAnimation, 0));
				else {
					int block = victim.getBlockAnimation();
					if (block != -1)
						victim.addUpdateBlock(new AnimationBlock(victim, block, 0));
				}
			}
		}
		if (graphic[0] != -1) {
			ServerEngine.staticGraphic(victim, graphic[0], graphic[1]);
		}
		if (attacker != null && hitType != HitType.POISON && hitType != HitType.BURN && hitType != HitType.RECOIL) {
			if (victim instanceof Npc || (victim instanceof Player && ((Player) victim).getSettings().isAutoRetaliate())) {
				if (!victim.getCombatManager().combatEnabled()) {
					if (victim.getCurrentAction() == null) {
						victim.getCombatManager().setCombatAction(new CombatAction(victim, attacker));
						victim.getMovementQueue().reset();
					}
				}
			}
		}
		return true;
	}

	/**
	 * Gets the attacker.
	 * 
	 * @return the attacker
	 */
	public Character getAttacker() {
		return attacker;
	}

	/**
	 * Gets the victim.
	 * 
	 * @return the victim
	 */
	public Character getVictim() {
		return victim;
	}

	/**
	 * Gets the attack type.
	 * 
	 * @return the attack type
	 */
	public HitType getAttackType() {
		return hitType;
	}

	/**
	 * Gets the timer.
	 * 
	 * @return the timer
	 */
	public StopWatch getTimer() {
		return stopWatch;
	}

	/**
	 * Gets the delay.
	 * 
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the hit.
	 * 
	 * @return the hit
	 */
	public int getHit() {
		return hit;
	}

}
