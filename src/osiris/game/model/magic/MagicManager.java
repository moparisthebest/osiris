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

import osiris.game.model.Position;
import osiris.game.model.Skills;
import osiris.game.model.effect.BindingSpellEffect;
import osiris.game.model.effect.PoisonEffect;
import osiris.game.model.effect.StatEffect;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class MagicManager.
 * 
 * @author Boomer
 */
public class MagicManager {

	/** The lunar magic. */
	private Spell[] normalMagic, ancientMagick, lunarMagic;

	/** The manager. */
	private static MagicManager manager;

	/** The Constant SPELL_DELAY. */
	public static final int SPELL_DELAY = 6;

	/** The Constant NORMAL_AUTOCAST_SPELLS. */
	public static final int[] NORMAL_AUTOCAST_SPELLS = { 1, 4, 6, 8, 10, 14, 17, 20, 24, 27, 33, 38, 45, 48, 52, 55 };

	/** The Constant ANCIENT_AUTOCAST_SPELLS. */
	public static final int[] ANCIENT_AUTOCAST_SPELLS = { 8, 12, 4, 0, 10, 14, 6, 2, 9, 13, 5, 1, 11, 15, 7, 3 };

	/**
	 * Instantiates a new magic manager.
	 */
	public MagicManager() {
		this.normalMagic = new Spell[] {
		/* 0 */new HomeTeleportSpell(new Position(Settings.DEFAULT_X, Settings.DEFAULT_Y), 1, 1), new CombatSpell(1, 711, 90, 6, new int[][] { { 556, 1 }, { 558, 1 } }, 2, 3, 91, 92), new EffectSpell(3, 716, 102, 13, new int[][] { { 555, 3 }, { 557, 2 }, { 559, 1 } }, -1, 3, 103, 104, true, 60, StatEffect.class, Skills.SKILL_ATTACK, -.05), null,// crossbow
				new CombatSpell(5, 711, 93, 8, new int[][] { { 555, 1 }, { 556, 1 }, { 558, 1 } }, 4, 3, 94, 95),
				/* 5 */null,// enchant
				new CombatSpell(9, 711, 96, 10, new int[][] { { 557, 2 }, { 556, 1 }, { 558, 1 } }, 6, 3, 97, 98), new EffectSpell(11, 716, 105, 21, new int[][] { { 555, 3 }, { 557, 2 }, { 559, 1 } }, -1, 3, 106, 107, true, 60, StatEffect.class, Skills.SKILL_STRENGTH, -.05), new CombatSpell(13, 711, 99, 12, new int[][] { { 554, 3 }, { 556, 2 }, { 558, 1 } }, 8, 3, 100, 101), null,// bones
				/* 10 */new CombatSpell(17, 711, 117, 14, new int[][] { { 556, 2 }, { 562, 1 } }, 9, 3, 118, 119), new EffectSpell(19, 716, 108, 29, new int[][] { { 555, 2 }, { 557, 3 }, { 559, 1 } }, -1, 3, 109, 110, true, 60, StatEffect.class, Skills.SKILL_DEFENCE, -.05), new EffectSpell(20, 1164, 177, 30, new int[][] { { 557, 3 }, { 555, 3 }, { 561, 2 } }, -1, 3, 178, 181, true, 15, BindingSpellEffect.class, 5), null, new CombatSpell(23, 711, 120, 17, new int[][] { { 555, 2 }, { 556, 2 }, { 562, 1 } }, 10, 3, 121, 122),
				/* 15 */new TeleportSpell(25, 0, new int[][] { { 554, 1 }, { 556, 3 }, { 563, 1 } }, false, new Position(3210, 3422, 0), 6, 2), // varrock
				null,// enchant
				new CombatSpell(29, 711, 123, 20, new int[][] { { 557, 3 }, { 556, 2 }, { 562, 1 } }, 11, 3, 124, 125), new TeleportSpell(31, 0, new int[][] { { 557, 1 }, { 556, 3 }, { 563, 1 } }, false, new Position(3222, 3222, 0), 1, 1), // lumby
				null,// tele grab
				/* 20 */new CombatSpell(35, 711, 126, 23, new int[][] { { 554, 4 }, { 556, 3 }, { 562, 1 } }, 12, 3, 127, 128), new TeleportSpell(37, 0, new int[][] { { 555, 1 }, { 556, 3 }, { 563, 1 } }, false, new Position(2963, 3379, 0), 4, 4), // fally
				new CombatSpell(39, 711, 145, 25, new int[][] { { 557, 2 }, { 556, 2 }, { 562, 1 } }, 15, 3, 146, 147), null,// house
																																// tele
				new CombatSpell(41, 711, 132, 26, new int[][] { { 556, 3 }, { 560, 1 } }, 13, 3, 133, 134),
				/* 25 */null,// superheat
				new TeleportSpell(45, 0, new int[][] { { 563, 1 }, { 556, 5 } }, false, new Position(2756, 3476, 0), 2, 3), // cammy
				new CombatSpell(47, 711, 135, 29, new int[][] { { 555, 3 }, { 556, 3 }, { 560, 1 } }, 14, 3, 136, 137), null,// enchant
				new StaffSpell(50, 711, 87, 30, new int[][] { { 554, 5 }, { 560, 1 } }, 25, 3, 88, 89, Settings.SPELL_STAFFS[Settings.STAFF_IBAN]),
				/* 30 */new EffectSpell(50, 1164, 177, 60, new int[][] { { 557, 4 }, { 555, 4 }, { 561, 3 } }, 2, 3, 178, 180, true, 20, BindingSpellEffect.class, 10), new StaffSpell(50, 711, -1, 30, new int[][] { { 560, 1 }, { 558, 4 } }, 19, 3, 280, 281, Settings.SPELL_STAFFS[Settings.STAFF_SLAYER]), new TeleportSpell(51, 0, new int[][] { { 555, 2 }, { 563, 2 } }, false, new Position(2660, 3303, 0), 4, 2), // ardy
				new CombatSpell(53, 711, 138, 32, new int[][] { { 557, 4 }, { 556, 3 }, { 560, 1 } }, 15, 3, 139, 140), null,// high
																																// enchant
				/* 35 */null,// charge
				null,// enchant
				null,// watch tele
				new CombatSpell(59, 711, 129, 35, new int[][] { { 554, 5 }, { 556, 4 }, { 560, 1 } }, 16, 3, 130, 131), null,// charge
				/* 40 */null,// bones to peaches
				new StaffSpell(60, 811, -1, 35, new int[][] { { 554, 2 }, { 565, 2 }, { 556, 4 } }, 20, 3, -1, 76, Settings.SPELL_STAFFS[Settings.STAFF_SARADOMIN]), new StaffSpell(60, 811, -1, 35, new int[][] { { 554, 1 }, { 565, 2 }, { 556, 4 } }, 20, 3, -1, 77, Settings.SPELL_STAFFS[Settings.STAFF_GUTHIX]), new StaffSpell(60, 811, -1, 35, new int[][] { { 554, 4 }, { 565, 2 }, { 556, 1 } }, 20, 3, -1, 78, Settings.SPELL_STAFFS[Settings.STAFF_ZAMORAK]), null,// troll
																																																																																																																				// tele
				/* 45 */new CombatSpell(62, 711, 158, -36, new int[][] { { 556, 5 }, { 565, 1 } }, 17, 3, 159, 160), null,// charge
				null,// ape tele
				new CombatSpell(65, 711, 161, 38, new int[][] { { 555, 7 }, { 556, 5 }, { 565, 1 } }, 18, 3, 162, 163), null,// charge
				/* 50 */new EffectSpell(66, 729, 167, 76, new int[][] { { 557, 5 }, { 555, 5 }, { 566, 1 } }, -1, 3, 168, 169, true, 60, StatEffect.class, Skills.SKILL_ATTACK, -.1), null,// enchant
				new CombatSpell(70, 711, 164, 40, new int[][] { { 557, 7 }, { 556, 5 }, { 565, 1 } }, 19, 3, 165, 166), new EffectSpell(73, 729, 170, 83, new int[][] { { 557, 8 }, { 555, 8 }, { 566, 1 } }, -1, 3, 171, 172, true, 60, StatEffect.class, Skills.SKILL_STRENGTH, -.1), null,// lumby
																																																																								// teleother
				/* 55 */new CombatSpell(75, 711, 155, 43, new int[][] { { 554, 7 }, { 556, 5 }, { 565, 1 } }, 20, 3, 156, 157), new EffectSpell(79, 1164, 177, 89, new int[][] { { 557, 5 }, { 555, 5 }, { 561, 4 } }, 4, 3, 178, 179, true, 25, BindingSpellEffect.class, 15), new EffectSpell(80, 729, 173, 90, new int[][] { { 557, 12 }, { 555, 12 }, { 566, 1 } }, -1, 3, 174, 107, true, 60, StatEffect.class, Skills.SKILL_DEFENCE, -.1), null,// charge
				null,// fally tele other
				null,// enchant
				/* 60 */null // cammy teleother

		};

		// Ice, Blood, Smoke, Shadow
		// 4, 12, 8, 16 3,11,7,15 1,9,5,13
		this.ancientMagick = new Spell[] {
		/* 4 */new EffectSpell(58, 1978, -1, 34, new int[][] { { 562, 2 }, { 560, 2 }, { 555, 2 } }, 18, 3, 360, 361, false, 15, BindingSpellEffect.class, 5),
		/* 12 */new EffectSpell(82, 1978, 366, 46, new int[][] { { 560, 2 }, { 565, 2 }, { 555, 3 } }, 26, 5, -1, 367, false, 25, BindingSpellEffect.class, 15).setSpellDelay(SPELL_DELAY - 1),
		/* 8 */new EffectSpell(70, 1979, -1, 40, new int[][] { { 562, 4 }, { 560, 2 }, { 555, 4 } }, 22, 3, -1, 363, false, 20, BindingSpellEffect.class, 10).enableMultiTarget(),
		/* 16 */new EffectSpell(94, 1979, -1, 52, new int[][] { { 560, 4 }, { 565, 2 }, { 555, 6 } }, 30, 3, -1, 369, false, 30, BindingSpellEffect.class, 20).enableMultiTarget(),

		/* 3 */new CombatSpell(56, 1978, -1, 33, new int[][] { { 562, 2 }, { 560, 2 }, { 565, 1 } }, 17, 3, 374, 373).setLeech(.25),
		/* 11 */new CombatSpell(80, 1978, -1, 45, new int[][] { { 560, 2 }, { 565, 4 } }, 25, 5, -1, 376).setLeech(.25).setSpellDelay(SPELL_DELAY - 1),
		/* 7 */new CombatSpell(68, 1979, -1, 39, new int[][] { { 562, 4 }, { 560, 2 }, { 565, 2 } }, 21, 3, -1, 375).setLeech(.25).enableMultiTarget(),
		/* 15 */new CombatSpell(92, 1979, -1, 51, new int[][] { { 560, 4 }, { 565, 4 }, { 566, 1 } }, 29, 3, -1, 377).setLeech(.25).enableMultiTarget(),

		/* 1 */new EffectSpell(50, 1978, -1, 30, new int[][] { { 562, 2 }, { 560, 2 }, { 554, 1 }, { 556, 1 } }, 15, 3, 384, 385, false, 0, PoisonEffect.class, 2.0),
		/* 9 */new EffectSpell(74, 1978, -1, 42, new int[][] { { 560, 2 }, { 565, 2 }, { 554, 2 }, { 556, 2 } }, 23, 5, -1, 389, false, 0, PoisonEffect.class, 4.0).setSpellDelay(SPELL_DELAY - 1),
		/* 5 */new EffectSpell(62, 1979, -1, 36, new int[][] { { 562, 4 }, { 560, 2 }, { 554, 2 }, { 556, 2 } }, 19, 3, -1, 388, false, 0, PoisonEffect.class, 2.0).enableMultiTarget(),
		/* 13 */new EffectSpell(84, 1979, -1, 48, new int[][] { { 560, 4 }, { 565, 2 }, { 554, 4 }, { 556, 4 } }, 27, 3, -1, 390, false, 0, PoisonEffect.class, 4.0).enableMultiTarget(),

		/* 2 */new EffectSpell(52, 1978, -1, 31, new int[][] { { 562, 2 }, { 560, 2 }, { 556, 1 }, { 566, 1 } }, 16, 3, 380, 379, false, 60, StatEffect.class, Skills.SKILL_ATTACK, -.05),
		/* 10 */new EffectSpell(76, 1978, -1, 43, new int[][] { { 560, 2 }, { 565, 2 }, { 556, 2 }, { 566, 2 } }, 24, 5, -1, 382, false, 60, StatEffect.class, Skills.SKILL_ATTACK, -.15).setSpellDelay(SPELL_DELAY - 1),
		/* 6 */new EffectSpell(64, 1979, -1, 37, new int[][] { { 562, 4 }, { 560, 2 }, { 556, 2 }, { 566, 2 } }, 20, 3, -1, 381, false, 60, StatEffect.class, Skills.SKILL_ATTACK, -.10).enableMultiTarget(),
		/* 14 */new EffectSpell(88, 1979, -1, 49, new int[][] { { 560, 4 }, { 565, 2 }, { 556, 4 }, { 566, 3 } }, 28, 3, -1, 383, false, 60, StatEffect.class, Skills.SKILL_ATTACK, -.20).enableMultiTarget(), null, null, null, null, null, null, null, null, new HomeTeleportSpell(new Position(Settings.DEFAULT_X, Settings.DEFAULT_Y), 1, 1) };
		this.lunarMagic = new Spell[] {

		};
	}

	/**
	 * Gets the spell.
	 * 
	 * @param index
	 *            the index
	 * @param book
	 *            the book
	 * @return the spell
	 */
	public static Spell getSpell(int index, SpellBook book) {
		if (book == SpellBook.NORMAL) {
			if (index >= manager.normalMagic.length)
				return null;
			return manager.normalMagic[index];
		} else if (book == SpellBook.ANCIENT) {
			if (index >= manager.ancientMagick.length)
				return null;
			return manager.ancientMagick[index];
		} else if (book == SpellBook.LUNAR) {
			if (index >= manager.lunarMagic.length)
				return null;
			return manager.lunarMagic[index];
		}
		return null;
	}

	/**
	 * Load.
	 */
	public static void load() {
		manager = new MagicManager();
	}

	/**
	 * Gets the spell book.
	 * 
	 * @param spellBook
	 *            the spell book
	 * @return the spell book
	 */
	public static Spell[] getSpellBook(SpellBook spellBook) {
		if (spellBook == SpellBook.NORMAL)
			return manager.normalMagic;
		return null;
	}

}
