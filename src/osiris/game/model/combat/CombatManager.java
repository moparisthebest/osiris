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
import java.util.Arrays;
import java.util.Iterator;

import osiris.ServerEngine;
import osiris.data.NpcDropLoader;
import osiris.game.action.impl.CombatAction;
import osiris.game.model.Character;
import osiris.game.model.Death;
import osiris.game.model.Npc;
import osiris.game.model.NpcDrop;
import osiris.game.model.NpcDropContainer;
import osiris.game.model.Player;
import osiris.game.model.combat.impl.MagicAttack;
import osiris.game.model.combat.impl.PlayerAttack;
import osiris.game.model.combat.missile.Arrow;
import osiris.game.model.combat.missile.Knife;
import osiris.game.model.combat.missile.RangedAttack;
import osiris.game.model.effect.Effect;
import osiris.game.model.effect.InCombatEffect;
import osiris.game.model.item.Item;
import osiris.game.model.magic.MagicManager;
import osiris.game.model.magic.Spell;
import osiris.game.model.magic.SpellBook;
import osiris.game.update.block.PrimaryHitBlock;
import osiris.game.update.block.SecondaryHitBlock;
import osiris.util.Settings;
import osiris.util.StopWatch;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc

/**
 * The Class CombatManager.
 * 
 * @author Boomer
 * 
 */
public class CombatManager {

	/**
	 * The character.
	 */
	private final Character character;

	/**
	 * The attack timer.
	 */
	private StopWatch attackTimer;

	/**
	 * The attack delay.
	 */
	private int attackDelay;

	/**
	 * The combat enabled.
	 */
	private boolean combatEnabled;

	/** The combat tempSpell. */
	private Spell tempSpell, autoSpell;

	/**
	 * The updating hits.
	 */
	private ArrayList<HitResult> updatingHits;

	/**
	 * The hits story. Its punny. Get it?
	 */
	private ArrayList<HitResult> hitsStory;

	/** The projectiles. */
	private ArrayList<Projectile> projectiles;

	/** The combat action. */
	private CombatAction combatAction;

	/** The equipped weapon. */
	private EquippedWeapon equippedWeapon;

	/** The Constant HITS_STORY_DECAY. */
	public static final int HITS_STORY_DECAY = 150;

	/**
	 * Instantiates a new combat manager.
	 * 
	 * @param character
	 *            the character
	 */
	public CombatManager(Character character) {
		this.character = character;
		this.updatingHits = new ArrayList<HitResult>();
		this.hitsStory = new ArrayList<HitResult>();
		this.attackDelay = 0;
		this.attackTimer = new StopWatch();
		this.projectiles = new ArrayList<Projectile>();
		this.tempSpell = null;
		this.autoSpell = null;
		this.equippedWeapon = null;
	}

	/**
	 * Cycle.
	 */
	public void cycle() {
		if (character == null)
			return;

		if (combatAction != null) {
			if (character.getInteractingCharacter() != null) {
				Character victim = character.getInteractingCharacter();
				Attack attack = new Attack();
				if (character instanceof Player) {
					Spell spell = autoSpell;
					if (tempSpell != null)
						spell = tempSpell;
					if (spell != null)
						attack = new MagicAttack(spell);
					else
						attack = new PlayerAttack(((Player) character).getEquippedAttack());
				} else if (character instanceof Npc) {
					Npc npc = (Npc) character;
					attack = npc.getCombatDef().getAttack();
				}
				HitType hitType = attack.getHitType();
				int attackRange = CombatUtilities.calculateCombatDistance(hitType);
				int distanceFromVictim = Utilities.getDistance(character.getPosition(), victim.getPosition());
				if (distanceFromVictim <= attackRange) {
					character.getMovementQueue().reset();
					if (attackTimer.elapsed() >= attackDelay && character.getCurrentHp() > 0) {
						if (combatEnabled) {
							if (hitType == HitType.MAGIC || hitType == HitType.RANGED) {
								character.getMovementQueue().reset();
							}
							int attackResult = executeAttack(attack);
							if (tempSpell != null) {
								combatEnabled = false;
							}
							if (attackResult == -1) {
								cancel();
							} else
								attackDelay = attackResult;
							attackTimer.reset();
						} else {
							cancel();
						}
					}
				} else {
					if (character.getMovementQueue().isEmpty()) {
						cancel();
					}
				}
			} else {
				cancel();
			}
		}

		for (Iterator<Projectile> shotProjectiles = projectiles.iterator(); shotProjectiles.hasNext();) {
			Projectile projectile = shotProjectiles.next();
			if (projectile.shouldFire()) {
				projectile.show(character);
				shotProjectiles.remove();
			}
		}

		boolean primaryTaken = false;
		for (Iterator<HitResult> hits = updatingHits.iterator(); hits.hasNext();) {
			HitResult hit = hits.next();
			if (!primaryTaken) {
				character.addUpdateBlock(new PrimaryHitBlock(character, hit.getDamage(), hit.getHitType()));
				primaryTaken = true;
				if (hit.getDamage() > 0 && hit.getAttackerUId() != -1)
					hitsStory.add(hit);
				hits.remove();
			} else {
				character.addUpdateBlock(new SecondaryHitBlock(character, hit.getDamage(), hit.getHitType()));
				if (hit.getDamage() > 0 && hit.getAttackerUId() != -1)
					hitsStory.add(hit);
				hits.remove();
				break;
			}
		}
		if (character.getCurrentHp() == 0)
			executeDeath();
		for (Iterator<HitResult> pastHits = hitsStory.iterator(); pastHits.hasNext();) {
			if (pastHits.next().getDecay() >= HITS_STORY_DECAY)
				pastHits.remove();
		}
	}

	/**
	 * Can attack.
	 * 
	 * @param victim
	 *            the victim
	 * @return true, if successful
	 */
	public boolean canAttack(Character victim) {
		if (victim == null || victim.isDying() || victim.getCurrentHp() == 0 || character == null || character.getCurrentHp() == 0 || character.isDying() || !character.isInSight(victim)) {
			return false;
		}
		return !victim.isDying() && CombatUtilities.checkWilderness(character, victim);
	}

	/**
	 * Attack character.
	 * 
	 * @param victim
	 *            the victim
	 * @param spellId
	 *            the spell id
	 * @return true, if successful
	 */
	public boolean attackCharacter(Character victim, int spellId) {
		if (victim.getMaxHp() == 0)
			return false;
		if (spellId != -1)
			this.tempSpell = MagicManager.getSpell(spellId, character.getSpellBook());
		else
			tempSpell = null;
		if (canAttack(victim)) {
			this.character.setInteractingCharacter(victim);
			combatEnabled = true;
			return true;
		}
		return false;
	}

	/**
	 * Reset attack vars.
	 */
	public void resetAttackVars() {
		character.setInteractingCharacter(null);
		combatEnabled = false;
		this.combatAction = null;
		this.setAttackType(null);
		this.tempSpell = null;
	}

	/**
	 * Execute attack.
	 * 
	 * @param attack
	 *            the attack
	 * @return the int
	 */
	public int executeAttack(Attack attack) {
		Character victim = character.getInteractingCharacter();
		if (!canAttack(victim))
			return -1;
		character.faceCharacter(victim);
		return attack.execute(character, victim, true);
	}

	/**
	 * Execute death.
	 */
	public void executeDeath() {
		if (character.isDying())
			return;
		ArrayList<Item> items = new ArrayList<Item>();
		Character killer = CombatUtilities.calculateKiller(hitsStory);
		if (character instanceof Player) {
			items.addAll(Arrays.asList(((Player) character).getInventory().getItems()));
			items.addAll(Arrays.asList(((Player) character).getEquipment().getItems()));
		} else if (character instanceof Npc) {
			ArrayList<NpcDropContainer> dropContainers = NpcDropLoader.getContainers(((Npc) character).getId());
			for (NpcDropContainer dropContainer : dropContainers)
				for (NpcDrop drop : dropContainer.produceRandomDrops(killer))
					items.add(new Item(drop.getItemId(), drop.getAmount()));
		}
		Death death = new Death(character, killer, items);
		if (character instanceof Npc)
			death.setDuration(30);
		ServerEngine.getDeathManager().getDeaths().add(death);
	}

	/**
	 * Gets the updating hits.
	 * 
	 * @return the updating hits
	 */
	public ArrayList<HitResult> getUpdatingHits() {
		return updatingHits;
	}

	/**
	 * Combat enabled.
	 * 
	 * @return true, if successful
	 */
	public boolean combatEnabled() {
		return combatEnabled;
	}

	/**
	 * Update hits.
	 */
	public static void updateHits() {
		if (ServerEngine.getHitQueue() == null || ServerEngine.getHitQueue().size() == 0)
			return;
		for (Iterator<Hit> hits = ServerEngine.getHitQueue().iterator(); hits.hasNext();) {
			Hit hit = hits.next();
			if (hit.getVictim().isDying())
				hits.remove();
			else if (hit.getTimer().elapsed() < hit.getDelay()) {
				continue;
			} else if (hit.getVictim() != null) {
				if (hit.execute()) {
					if (hit.getHit() != -1)
						hit.getVictim().getCombatManager().getUpdatingHits().add(new HitResult(hit.getHit(), hit.getAttackType(), ((hit.getAttacker() != null && hit.getAttacker() instanceof Player) ? ((Player) hit.getAttacker()).getUniqueId() : -1)));
				}
				hits.remove();
			}
		}
	}

	/**
	 * Sets the combat action.
	 * 
	 * @param action
	 *            the new combat action
	 */
	public void setCombatAction(CombatAction action) {
		this.combatAction = action;
		action.run();
	}

	/**
	 * Sets the attack type.
	 * 
	 * @param equippedWeapon
	 *            the new attack type
	 */
	public void setAttackType(EquippedWeapon equippedWeapon) {
		this.equippedWeapon = equippedWeapon;
	}

	/**
	 * Gets the attack type.
	 * 
	 * @return the attack type
	 */
	public EquippedWeapon getAttackType() {
		return equippedWeapon;
	}

	/**
	 * Gets the projectiles.
	 * 
	 * @return the projectiles
	 */
	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	/**
	 * Gets the temp spell.
	 * 
	 * @return the temp spell
	 */
	public Spell getTempSpell() {
		return tempSpell;
	}

	/**
	 * Gets the auto spell.
	 * 
	 * @return the auto spell
	 */
	public Spell getAutoSpell() {
		return autoSpell;
	}

	/**
	 * Sets the auto spell.
	 * 
	 * @param spellId
	 *            the new auto spell
	 */
	public void setAutoSpell(int spellId) {
		setAutoSpell(spellId, character.getSpellBook(), false, -1);
	}

	/**
	 * Sets the auto spell.
	 * 
	 * @param spellId
	 *            the spell id
	 * @param spellBook
	 *            the spell book
	 * @param defence
	 *            the defence
	 * @param spellSprite
	 *            the spell sprite
	 */
	public void setAutoSpell(int spellId, SpellBook spellBook, boolean defence, int spellSprite) {
		if (spellId == -1)
			this.autoSpell = null;
		else
			this.autoSpell = MagicManager.getSpell(spellId, spellBook);
		if (character instanceof Player) {
			Player player = (Player) character;
			if (spellId == -1) {
				player.getEventWriter().sendConfig2(439, 0);
				player.getEventWriter().sendInterfaceConfig(90, 83, false);
				player.getEventWriter().sendInterfaceConfig(90, 183, false);
				player.refreshWeaponTab();
			} else if (spellSprite != -1) {
				player.getEventWriter().sendInterfaceConfig(90, defence ? 183 : 83, true);
				player.getEventWriter().sendInterfaceConfig(90, spellSprite, false);
				player.getEventWriter().sendConfig2(defence ? 439 : 43, defence ? -5 : 3);
			}
		}
	}

	/**
	 * Cancel.
	 */
	public void cancel() {
		if (character instanceof Npc) {
			if (!((Npc) character).getWalkablePositions().contains(character.getPosition()))
				character.getMovementQueue().addStep(((Npc) character).getDefaultPosition());
		}
		if (character.getCurrentAction() != null && character.getCurrentAction() instanceof CombatAction) {
			character.getCurrentAction().cancel();
		}
	}

	/**
	 * Gets the ranged attack.
	 * 
	 * @return the ranged attack
	 */
	public RangedAttack getRangedAttack() {
		EquippedWeapon.WeaponType wepType = null;
		if (this.getAttackType() != null)
			wepType = this.getAttackType().getWepType();
		else {
			if (character instanceof Player)
				wepType = ((Player) character).getEquippedAttack().getWepType();
		}
		if (wepType == null)
			return null;
		else if (wepType == EquippedWeapon.WeaponType.SHORTBOW)
			return Arrow.getAmmo(character, Settings.SLOT_ARROWS);
		else if (wepType == EquippedWeapon.WeaponType.LONGBOW)
			return Arrow.getAmmo(character, Settings.SLOT_ARROWS);
		else if (wepType == EquippedWeapon.WeaponType.THROWING_KNIFE)
			return Knife.getAmmo(character, Settings.SLOT_WEAPON);
		return null;
	}

	/**
	 * Adjust attack delay.
	 * 
	 * @param adjustment
	 *            the adjustment
	 */
	public void adjustAttackDelay(int adjustment) {
		if (adjustment < 0) {
			attackDelay += adjustment;
			if (attackDelay < 0)
				attackDelay = 0;
		} else
			attackDelay += adjustment;
	}

	/**
	 * Gets the combat effect.
	 * 
	 * @return the combat effect
	 */
	public InCombatEffect getCombatEffect() {
		for (Effect effect : character.getEffects())
			if (effect instanceof InCombatEffect)
				return (InCombatEffect) effect;
		return null;
	}
}
