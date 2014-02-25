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

import static osiris.game.model.combat.CombatDefaults.DEFAULT_ANIMATIONS;
import static osiris.game.model.combat.CombatDefaults.DEFAULT_BLOCK;
import static osiris.game.model.combat.CombatDefaults.DEFAULT_SKILLS;
import static osiris.game.model.combat.CombatDefaults.DEFAULT_SPRITES;
import static osiris.game.model.combat.CombatDefaults.DEFAULT_TAB;
import static osiris.game.model.combat.CombatDefaults.RANGE_SKILLS;
import static osiris.game.model.combat.CombatDefaults.SWORD_SKILLS;

// TODO: Auto-generated Javadoc
/**
 * The Class EquippedWeapon.
 * 
 * @author Boomer
 */
public class EquippedWeapon {

	// stand, standturn, walk, turn180, turn90CW, turn90CCW, run
	/**
	 * Gets the fists.
	 * 
	 * @return the fists
	 */
	public static EquippedWeapon getFists() {
		return new EquippedWeapon(WeaponType.FISTS, HitType.MELEE, 4, new int[] { 92, 10, 24 }, new int[][] { { 0 }, { 2 }, { 1 } }, array(DEFAULT_SPRITES), new int[] { 422, 423, 422 }, 424);
	}

	/**
	 * Gets the sword.
	 * 
	 * @return the sword
	 */
	public static EquippedWeapon getSword() {
		return new EquippedWeapon(WeaponType.SWORD, HitType.MELEE, 4, new int[] { DEFAULT_TAB, 12, 26 }, SWORD_SKILLS, DEFAULT_SPRITES, array(DEFAULT_ANIMATIONS), 397);
	}

	/**
	 * Gets the longsword.
	 * 
	 * @return the longsword
	 */
	public static EquippedWeapon getLongsword() {
		return new EquippedWeapon(WeaponType.LONGSWORD, HitType.MELEE, 5, new int[] { DEFAULT_TAB, 12, 26 }, SWORD_SKILLS, DEFAULT_SPRITES, array(DEFAULT_ANIMATIONS), 397);
	}

	/**
	 * Gets the 2h sword.
	 * 
	 * @return the 2h sword
	 */
	public static EquippedWeapon get2hSword() {
		return new EquippedWeapon(WeaponType.TWO_HANDED_SWORD, HitType.MELEE, 7, new int[] { 81, 12, 26 }, DEFAULT_SKILLS, new int[] { 7047, 7046, 7039 }, new int[] { 7048, 7041, 7041, 7049 }, 7050);
	}

	/**
	 * Gets the mace.
	 * 
	 * @return the mace
	 */
	public static EquippedWeapon getMace() {
		return new EquippedWeapon(WeaponType.MACE, HitType.MELEE, 5, new int[] { 88, 12, 26 }, DEFAULT_SKILLS, DEFAULT_SPRITES, array(DEFAULT_ANIMATIONS), DEFAULT_BLOCK);
	}

	/**
	 * Gets the scimitar.
	 * 
	 * @return the scimitar
	 */
	public static EquippedWeapon getScimitar() {
		return new EquippedWeapon(WeaponType.SCIMITAR, HitType.MELEE, 4, new int[] { DEFAULT_TAB, 12, 26 }, SWORD_SKILLS, DEFAULT_SPRITES, new int[] { 390, 390, 386, 390 }, 388);
	}

	/**
	 * Gets the dagger.
	 * 
	 * @return the dagger
	 */
	public static EquippedWeapon getDagger() {
		return new EquippedWeapon(WeaponType.DAGGER, HitType.MELEE, 4, new int[] { 89, 12, 26 }, SWORD_SKILLS, DEFAULT_SPRITES, new int[] { 400, 402, 451, 400 }, 403);
	}

	/**
	 * Gets the pick axe.
	 * 
	 * @return the pick axe
	 */
	public static EquippedWeapon getPickAxe() {
		return new EquippedWeapon(WeaponType.PICKAXE, HitType.MELEE, 4, new int[] { 89, 12, 26 }, SWORD_SKILLS, DEFAULT_SPRITES, new int[] { 401, 401, 400, 401 }, 397);
	}

	/**
	 * Gets the axe.
	 * 
	 * @return the axe
	 */
	public static EquippedWeapon getAxe() {
		return new EquippedWeapon(WeaponType.AXE, HitType.MELEE, 6, new int[] { 75, 12, 26 }, new int[][] { { 0 }, { 1 }, { 2 }, { 2 } }, DEFAULT_SPRITES, new int[] { 395, 395, 401, 395 }, 397);
	}

	/**
	 * Gets the warhammer.
	 * 
	 * @return the warhammer
	 */
	public static EquippedWeapon getWarhammer() {
		return new EquippedWeapon(WeaponType.WARHAMMER, HitType.MELEE, 6, new int[] { 76, -1, 26 }, SWORD_SKILLS, DEFAULT_SPRITES, new int[] { 401 }, 403);
	}

	/**
	 * Gets the staff.
	 * 
	 * @return the staff
	 */
	public static EquippedWeapon getStaff() {
		return new EquippedWeapon(WeaponType.STAFF, HitType.MELEE, 5, new int[] { 90, -1, 9 }, new int[][] { { 0 }, { 2 }, { 1 } }, new int[] { 809, 1146, 1210 }, new int[] { 419 }, 420);
	}

	/**
	 * Gets the godsword.
	 * 
	 * @return the godsword
	 */
	public static EquippedWeapon getGodsword() {
		return new EquippedWeapon(WeaponType.GODSWORD, HitType.MELEE, 6, new int[] { 81, 12, 26 }, SWORD_SKILLS, new int[] { 7047, 7046, 7039 }, new int[] { 7048, 7041, 7041, 7049 }, 7050);
	}

	/**
	 * Gets the whip.
	 * 
	 * @return the whip
	 */
	public static EquippedWeapon getWhip() {
		return new EquippedWeapon(WeaponType.WHIP, HitType.MELEE, 4, new int[] { 93, 10, 24 }, new int[][] { { 0 }, { 0, 1, 2 }, { 1 } }, new int[] { 1832, 1660, 1661 }, new int[] { 1658 }, 1659);
	}

	/**
	 * Gets the shortbow.
	 * 
	 * @return the shortbow
	 */
	public static EquippedWeapon getShortbow() {
		return new EquippedWeapon(WeaponType.SHORTBOW, HitType.RANGED, 4, new int[] { 77, 13, 27 }, RANGE_SKILLS, DEFAULT_SPRITES, new int[] { 426 }, 424);
	}

	/**
	 * Gets the longbow.
	 * 
	 * @return the longbow
	 */
	public static EquippedWeapon getLongbow() {
		return new EquippedWeapon(WeaponType.LONGBOW, HitType.RANGED, 6, new int[] { 77, 13, 27 }, RANGE_SKILLS, DEFAULT_SPRITES, new int[] { 426 }, 424);
	}

	/**
	 * Gets the throwing knife.
	 * 
	 * @return the throwing knife
	 */
	public static EquippedWeapon getThrowingKnife() {
		return new EquippedWeapon(WeaponType.THROWING_KNIFE, HitType.RANGED, 3, new int[] { 91, -1, 24 }, RANGE_SKILLS, DEFAULT_SPRITES, new int[] { 929 }, 424);
	}

	/** The hit type. */
	private HitType hitType;

	/** The block animation. */
	private int blockAnimation, attackDelay;

	/** The tab info. */
	private int[] tabInfo, mobSprites, attackAnimations;

	/** The skills advanced. */
	private int[][] skillsAdvanced;

	/** The wep type. */
	private WeaponType wepType;

	/**
	 * The Enum WeaponType.
	 */
	enum WeaponType {

		FISTS, SWORD, LONGSWORD, TWO_HANDED_SWORD, GODSWORD, MACE, SCIMITAR, DAGGER, PICKAXE, AXE, WARHAMMER, STAFF, WHIP, SHORTBOW, LONGBOW, THROWING_KNIFE
	}

	/**
	 * Instantiates a new equipped weapon.
	 * 
	 * @param wepType
	 *            the wep type
	 * @param hitType
	 *            the hit type
	 * @param attackDelay
	 *            the attack delay
	 * @param tabInfo
	 *            the tab info
	 * @param skillsAdvanced
	 *            the skills advanced
	 * @param mobSprites
	 *            the mob sprites
	 * @param attackAnimations
	 *            the attack animations
	 * @param blockAnimation
	 *            the block animation
	 */
	public EquippedWeapon(WeaponType wepType, HitType hitType, int attackDelay, int[] tabInfo, int[][] skillsAdvanced, int[] mobSprites, int[] attackAnimations, int blockAnimation) {
		this.wepType = wepType;
		this.hitType = hitType;
		this.attackDelay = attackDelay;
		this.tabInfo = tabInfo;
		this.mobSprites = mobSprites;
		this.attackAnimations = attackAnimations;
		this.blockAnimation = blockAnimation;
		this.skillsAdvanced = skillsAdvanced;
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
	 * Gets the tab id.
	 * 
	 * @return the tab id
	 */
	public int getTabId() {
		return tabInfo[0];
	}

	/**
	 * Gets the retaliate id.
	 * 
	 * @return the retaliate id
	 */
	public int getRetaliateId() {
		return tabInfo[2];
	}

	/**
	 * Gets the spec button.
	 * 
	 * @return the spec button
	 */
	public int getSpecButton() {
		return tabInfo[1];
	}

	/**
	 * Gets the attack delay.
	 * 
	 * @return the attack delay
	 */
	public int getAttackDelay() {
		return attackDelay;
	}

	/**
	 * Gets the wep type.
	 * 
	 * @return the wep type
	 */
	public WeaponType getWepType() {
		return wepType;
	}

	/**
	 * Gets the block animation.
	 * 
	 * @return the block animation
	 */
	public int getBlockAnimation() {
		return blockAnimation;
	}

	/**
	 * Gets the mob sprites.
	 * 
	 * @return the mob sprites
	 */
	public int[] getMobSprites() {
		return mobSprites;
	}

	/**
	 * Gets the attack animations.
	 * 
	 * @return the attack animations
	 */
	public int[] getAttackAnimations() {
		return attackAnimations;
	}

	/**
	 * Gets the skills.
	 * 
	 * @return the skills
	 */
	public int[][] getSkills() {
		return skillsAdvanced;
	}

	/**
	 * Gets the type.
	 * 
	 * @param weaponName
	 *            the weapon name
	 * @return the type
	 */
	public static EquippedWeapon getType(String weaponName) {
		if (weaponName == null) {
			return getFists();
		}
		weaponName = weaponName.toLowerCase();
		if (weaponName.contains("whip")) {
			return getWhip();
		} else if (weaponName.endsWith("warhammer")) {
			return getWarhammer();
		} else if (weaponName.endsWith("longsword") || weaponName.equals("excalibur") || weaponName.equals("brine sabre")) {
			return getLongsword();
		} else if (weaponName.endsWith("battleaxe") || weaponName.endsWith(" axe")) {
			return getAxe();
		} else if (weaponName.endsWith("godsword") || weaponName.endsWith("saradomin sword")) {
			return getGodsword();
		} else if (weaponName.endsWith("scimitar")) {
			return getScimitar();
		} else if (weaponName.contains("staff")) {
			return getStaff();
		} else if (weaponName.endsWith("mace")) {
			return getMace();
		} else if (weaponName.endsWith("pickaxe")) {
			return getPickAxe();
		} else if (weaponName.endsWith("2h sword")) {
			return get2hSword();
		} else if (weaponName.endsWith("shortbow")) {
			return getShortbow();
		} else if (weaponName.endsWith("bow")) {
			return getLongbow();
		} else if (weaponName.contains("dagger")) {
			return getDagger();
		} else if (weaponName.contains("knife")) {
			return getThrowingKnife();
		} else {
			return getSword();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public EquippedWeapon clone() {
		return new EquippedWeapon(wepType, hitType, attackDelay, tabInfo, skillsAdvanced, mobSprites, attackAnimations, blockAnimation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if (object instanceof EquippedWeapon) {
			EquippedWeapon other = (EquippedWeapon) object;
			return (other.wepType == wepType && other.hitType == hitType && other.attackDelay == attackDelay && other.tabInfo == tabInfo && other.skillsAdvanced == skillsAdvanced && other.mobSprites == mobSprites && other.attackAnimations == attackAnimations && other.blockAnimation == blockAnimation);
		}
		return false;
	}

	/**
	 * Array.
	 * 
	 * @param src
	 *            the src
	 * @return the int[]
	 */
	public static int[] array(int[] src) {
		int[] copy = new int[src.length];
		System.arraycopy(src, 0, copy, 0, src.length);
		return copy;
	}

}
