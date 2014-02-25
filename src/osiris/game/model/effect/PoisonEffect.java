package osiris.game.model.effect;

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

import osiris.ServerEngine;
import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.combat.Hit;
import osiris.game.model.combat.HitType;
import osiris.util.StopWatch;

// TODO: Auto-generated Javadoc
/**
 * The Class PoisonEffect.
 * 
 * @author Boomer
 */
public class PoisonEffect extends CombatEffect {

	/** The damage. */
	double damage;

	/** The timer. */
	private StopWatch timer;

	/** The first hit. */
	private boolean firstHit;

	/** The Constant POISON_INCREMENT. */
	public static final int POISON_INCREMENT = 20;

	/**
	 * Instantiates a new poison effect.
	 * 
	 * @param cooldown
	 *            the cooldown
	 * @param attacker
	 *            the attacker
	 * @param damage
	 *            the damage
	 */
	public PoisonEffect(Integer cooldown, Character attacker, Object damage) {
		super(((int) ((damage instanceof String ? Double.parseDouble((String) damage) : (Double) damage) / .2) * POISON_INCREMENT) + 5, attacker);
		this.damage = (damage instanceof String ? Double.parseDouble((String) damage) : (Double) damage);
		this.timer = new StopWatch();
		this.firstHit = false;
	}

	/**
	 * Hit.
	 * 
	 * @param character
	 *            the character
	 */
	public void hit(Character character) {
		if (damage > 0) {
			Hit poison = new Hit(getAttacker(), character, (int) Math.ceil(damage), 0, HitType.POISON);
			ServerEngine.getHitQueue().add(poison);
			damage -= .2;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.effect.Effect#execute(osiris.game.model.Character)
	 */
	@Override
	public void execute(Character character) {
		if (character instanceof Player)
			((Player) character).getEventWriter().sendMessage("You have been poisoned!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.effect.Effect#equals(osiris.game.model.effect.Effect)
	 */
	@Override
	public boolean equals(Effect effect) {
		return effect instanceof PoisonEffect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.effect.Effect#update(osiris.game.model.Character)
	 */
	@Override
	public void update(Character character) {
		if (!firstHit && timer.elapsed() >= 3) {
			hit(character);
			firstHit = true;
			timer.reset();
		}
		if (timer.elapsed() >= POISON_INCREMENT) {
			timer.reset();
			hit(character);
		}
	}
}
