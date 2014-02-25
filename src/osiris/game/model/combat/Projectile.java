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

import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.io.EventWriter;
import osiris.util.StopWatch;

// TODO: Auto-generated Javadoc
/**
 * The Class Projectile.
 * 
 * @author Boomer
 * 
 */
public class Projectile {

	/** The to. */
	private Position from, to;

	/** The lock on. */
	private Character lockOn;

	/** The tick delay. */
	private int graphicId, angle, speed, tickDelay;

	/** The height. */
	private int[] height;

	/** The timer. */
	private StopWatch timer;

	/**
	 * Instantiates a new projectile.
	 * 
	 * @param graphicId
	 *            the graphic id
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param angle
	 *            the angle
	 * @param speed
	 *            the speed
	 * @param height
	 *            the height
	 * @param lockOn
	 *            the lock on
	 * @param tickDelay
	 *            the tick delay
	 */
	public Projectile(int graphicId, Position from, Position to, int angle, int speed, int[] height, Character lockOn, int tickDelay) {
		this.graphicId = graphicId;
		this.from = from;
		this.to = to;
		this.angle = angle;
		this.speed = speed;
		this.height = height;
		this.lockOn = lockOn;
		this.tickDelay = tickDelay;
		this.timer = new StopWatch();
	}

	/**
	 * Should fire.
	 * 
	 * @return true, if successful
	 */
	public boolean shouldFire() {
		boolean should = timer.elapsed() >= tickDelay;
		return should;
	}

	/**
	 * Write projectile.
	 * 
	 * @param writer
	 *            the writer
	 */
	public void writeProjectile(EventWriter writer) {
		writer.sendProjectile(from, to, graphicId, angle, height[0], height[1], speed, lockOn);
	}

	/**
	 * Show.
	 * 
	 * @param character
	 *            the character
	 */
	public void show(Character character) {
		if (character instanceof Player) {
			writeProjectile(((Player) character).getEventWriter());
		}
		for (Player player : character.getLocalPlayers())
			if (character.isInSight(player))
				writeProjectile(player.getEventWriter());
	}

}
