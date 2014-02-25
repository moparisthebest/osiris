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
import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.Skills;
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;
import osiris.game.update.block.AnimationBlock;
import osiris.game.update.block.GraphicsBlock;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class Spell.
 * 
 * @author Boomer
 */
public abstract class Spell {

	/** The runes required. */
	private int[][] runesRequired;

	/** The delay. */
	private int levelRequired, animation, graphic, delay;

	/** The exp earned. */
	private double expEarned;

	/**
	 * Instantiates a new spell.
	 * 
	 * @param levelRequired
	 *            the level required
	 * @param animation
	 *            the animation
	 * @param graphic
	 *            the graphic
	 * @param expEarned
	 *            the exp earned
	 * @param runesRequired
	 *            the runes required
	 */
	public Spell(int levelRequired, int animation, int graphic, double expEarned, int[][] runesRequired) {
		this.levelRequired = levelRequired;
		this.animation = animation;
		this.graphic = graphic;
		this.expEarned = expEarned;
		this.runesRequired = runesRequired;
		this.delay = MagicManager.SPELL_DELAY;
	}

	/**
	 * Gets the animation.
	 * 
	 * @return the animation
	 */
	public int getAnimation() {
		return animation;
	}

	/**
	 * Gets the graphic.
	 * 
	 * @return the graphic
	 */
	public int getGraphic() {
		return graphic;
	}

	/**
	 * Gets the exp earned.
	 * 
	 * @return the exp earned
	 */
	public double getExpEarned() {
		return expEarned;
	}

	/**
	 * Gets the runes required.
	 * 
	 * @return the runes required
	 */
	public int[][] getRunesRequired() {
		return runesRequired;
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
	 * Sets the spell delay.
	 * 
	 * @param delay
	 *            the delay
	 * @return the spell
	 */
	public Spell setSpellDelay(int delay) {
		this.delay = delay;
		return this;
	}

	/**
	 * Execute.
	 * 
	 * @param character
	 *            the character
	 * @param defence
	 *            the defence
	 * @param params
	 *            the params
	 * @return true, if successful
	 */
	public boolean execute(Character character, boolean defence, Object... params) {
		if (character instanceof Player) {
			if (((Player) character).getSkills().currentLevel(Skills.SKILL_MAGIC) < levelRequired) {
				((Player) character).getEventWriter().sendMessage("You need a higher level to use this spell!");
				return false;
			}
			if (runesRequired != null) {
				for (int[] runes : runesRequired) {
					if (usingStaff(((Player) character).getEquipment().getItem(Settings.SLOT_WEAPON), runes[0]))
						continue;
					if (((Player) character).getInventory().amountOfItem(runes[0]) < runes[1]) {
						((Player) character).getEventWriter().sendMessage("You do not have the runes necessary!");
						return false;
					}
				}
			}
		}
		boolean executed = onExecution(character, params);
		if (executed) {
			if (character instanceof Player) {
				if (runesRequired != null) {
					for (int[] runes : runesRequired)
						if (!usingStaff(((Player) character).getEquipment().getItem(Settings.SLOT_WEAPON), runes[0]))
							((Player) character).getInventory().removeById(runes[0], runes[1]);
				}
				((Player) character).getSkills().addExp(Skills.SKILL_MAGIC, expEarned);
			}
			if (graphic != -1)
				character.addPriorityUpdateBlock(new GraphicsBlock(character, graphic, character.getSpellBook() == SpellBook.ANCIENT ? 0 : 100));
			if (animation != -1)

				character.addPriorityUpdateBlock(new AnimationBlock(character, animation, 0));
			return true;
		}
		if (character instanceof Player) {
			Spell curAutoSpell = character.getCombatManager().getAutoSpell();
			if (curAutoSpell != null && curAutoSpell == this)
				character.getCombatManager().setAutoSpell(-1);
		}
		return false;
	}

	/**
	 * Using staff.
	 * 
	 * @param item
	 *            the item
	 * @param runeId
	 *            the rune id
	 * @return true, if successful
	 */
	public boolean usingStaff(Item item, int runeId) {
		if (item == null)
			return false;
		ItemDef def = ItemDef.forId(item.getId());
		String name = def.getName().toLowerCase();
		if (!name.contains("staff"))
			return false;
		switch (runeId) {
		case 554:
			return name.contains("fire") || name.contains("lava");
		case 555:
			return name.contains("water") || name.contains("mud");
		case 556:
			return name.contains("air");
		case 557:
			return name.contains("earth") || name.contains("lava");
		}
		return false;
	}

	/**
	 * On execution.
	 * 
	 * @param character
	 *            the character
	 * @param params
	 *            the params
	 * @return true, if successful
	 */
	public abstract boolean onExecution(Character character, Object... params);
}
