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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import osiris.game.model.Character;

// TODO: Auto-generated Javadoc
/**
 * The Class CombatEffect.
 * 
 * @author Boomer
 */
public abstract class CombatEffect extends ExpiringEffect {

	/** The attacker. */
	private Character attacker;

	/**
	 * Instantiates a new combat effect.
	 * 
	 * @param cooldown
	 *            the cooldown
	 * @param attacker
	 *            the attacker
	 */
	public CombatEffect(Integer cooldown, Character attacker) {
		super(cooldown);
		this.attacker = attacker;
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
	 * Creates the.
	 * 
	 * @param attacker
	 *            the attacker
	 * @param cooldown
	 *            the cooldown
	 * @param clazz
	 *            the clazz
	 * @param params
	 *            the params
	 * @return the combat effect
	 */
	public static CombatEffect create(Character attacker, Integer cooldown, Class<? extends CombatEffect> clazz, Object... params) {
		try {
			int defaultParams = 2;
			Class<?>[] clazzes = new Class<?>[params.length + defaultParams];
			clazzes[0] = Integer.class;
			clazzes[1] = Character.class;
			Arrays.fill(clazzes, defaultParams, params.length + defaultParams, Object.class);
			Object[] objects = new Object[params.length + defaultParams];
			objects[0] = cooldown;
			objects[1] = attacker;
			System.arraycopy(params, 0, objects, defaultParams, params.length);
			return clazz.getConstructor(clazzes).newInstance(objects);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
}
