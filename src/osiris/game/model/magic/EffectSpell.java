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
import osiris.game.model.effect.CombatEffect;

// TODO: Auto-generated Javadoc
/**
 * The Class EffectSpell.
 * 
 * @author Boomer
 */
public class EffectSpell extends CombatSpell {

	/** The cooldown. */
	private int cooldown;

	/** The clazz. */
	private Class<? extends CombatEffect> clazz;

	/** The params. */
	private Object[] params;

	/** The requires effect. */
	private boolean requiresEffect;

	/**
	 * Instantiates a new effect spell.
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
	 * @param requiresEffect
	 *            the requires effect
	 * @param cooldown
	 *            the cooldown
	 * @param clazz
	 *            the clazz
	 * @param params
	 *            the params
	 */
	public EffectSpell(int level, int animation, int graphic, int expEarned, int[][] runesRequired, int maxDamage, int hitDelay, int projectileId, int hitGraphic, boolean requiresEffect, int cooldown, Class<? extends CombatEffect> clazz, Object... params) {
		super(level, animation, graphic, expEarned, runesRequired, maxDamage, hitDelay, projectileId, hitGraphic);
		this.cooldown = cooldown;
		this.clazz = clazz;
		this.params = params;
		this.requiresEffect = requiresEffect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.magic.CombatSpell#onExecution(osiris.game.model.Character
	 * , java.lang.Object[])
	 */
	@Override
	public boolean onExecution(Character character, Object... parameters) {
		Character victim = (Character) parameters[0];
		CombatEffect effect = CombatEffect.create(character, this.cooldown, this.clazz, this.params);
		if (effect == null) {
			return false;
		}
		boolean canAdd = victim.canAddEffect(effect);
		if (!canAdd && !requiresEffect)
			effect = null;
		if (canAdd || !requiresEffect) {
			return super.onExecution(character, victim, effect);
		} else {
			if (character instanceof Player && requiresEffect)
				((Player) character).getEventWriter().sendMessage("That " + victim.getClass().getSimpleName().toLowerCase() + "" + " is immune to that effect right now!");
		}
		return false;
	}
}
