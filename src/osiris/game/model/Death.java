package osiris.game.model;

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
import java.util.Iterator;
import java.util.List;

import osiris.ServerEngine;
import osiris.game.action.Action;
import osiris.game.model.combat.CombatUtilities;
import osiris.game.model.combat.Hit;
import osiris.game.model.combat.HitType;
import osiris.game.model.effect.PrayerEffect;
import osiris.game.model.item.Item;
import osiris.game.model.skills.Prayer;
import osiris.game.update.UpdateBlock;
import osiris.game.update.block.AppearanceBlock;
import osiris.game.update.block.GraphicsBlock;
import osiris.util.StopWatch;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class Death. Making it an Action so I can set it as the Player's action,
 * however not running it so the server can manage deaths, so x logging doesn't
 * stop the action.
 * 
 * @author Boomer
 * 
 */
public class Death extends Action {

	/** The Constant DEFAULT_DURATION. */
	public static final int DEFAULT_DURATION = 7;

	/** The timer. */
	private StopWatch timer;

	/** The killer. */
	private Character dying, killer;

	/** The kept on death. */
	private ArrayList<Item> items, keptOnDeath;

	/** The stage. */
	private int stage;

	/** The duration. */
	private int duration;

	/** The gravestone. */
	private Position gravestone;

	/**
	 * Instantiates a new death.
	 * 
	 * @param dying
	 *            the dying
	 * @param killer
	 *            the killer
	 * @param items
	 *            the items
	 */
	public Death(Character dying, Character killer, ArrayList<Item> items) {
		this(dying, killer, items, DEFAULT_DURATION);
	}

	/**
	 * Instantiates a new death.
	 * 
	 * @param dying
	 *            the dying
	 * @param killer
	 *            the killer
	 * @param items
	 *            the items
	 * @param duration
	 *            the duration
	 */
	public Death(Character dying, Character killer, ArrayList<Item> items, int duration) {
		super(dying);
		if (dying instanceof Player)
			this.keptOnDeath = ((Player) dying).getItemsKeptOnDeath();
		else
			keptOnDeath = new ArrayList<Item>();
		if (dying != null) {
			for (Iterator<UpdateBlock> blocks = dying.getUpdateBlocks().iterator(); blocks.hasNext();)
				if (blocks.next() instanceof AppearanceBlock)
					blocks.remove();
			if (dying instanceof Player) {
				Player player = ((Player) dying);
				PrayerEffect revenge = player.getPrayers().get(Prayer.Type.HEADICON);
				if (revenge != null && revenge.getPrayer() == Prayer.RETRIBUTION) {
					int maxHit = (int) (player.getSkills().maxLevel(Skills.SKILL_PRAYER) * .25);
					player.addUpdateBlock(new GraphicsBlock(dying, 437, 0));
					boolean singleCombat = CombatUtilities.singleCombat(player.getPosition());
					boolean hitCharacter = false;
					for (Player other : player.getLocalPlayers())
						if (Utilities.getDistance(other.getPosition(), player.getPosition()) <= 3) {
							Hit hit = new Hit(player, other, Utilities.random(maxHit), 0, HitType.RECOIL);
							ServerEngine.getHitQueue().add(hit);
							hitCharacter = true;
							if (singleCombat)
								break;
						}
					if (!singleCombat || !hitCharacter) {
						for (Npc other : player.getLocalNpcs()) {
							if (Utilities.getDistance(other.getPosition(), player.getPosition()) <= 3) {
								Hit hit = new Hit(player, other, Utilities.random(maxHit), 0, HitType.RECOIL);
								ServerEngine.getHitQueue().add(hit);
								if (singleCombat)
									break;
							}
						}
					}
					player.getSkills().setCurLevel(Skills.SKILL_PRAYER, 0);
				}
				for (Prayer.Type key : ((Player) dying).getPrayers().keySet())
					((Player) dying).getPrayers().put(key, null);
			}
			dying.getEffects().clear();
		}
		this.timer = new StopWatch();
		this.dying = dying;
		this.killer = killer;
		this.items = items;
		if (dying != null)
			this.gravestone = dying.getPosition();
		this.duration = duration;
		if (dying instanceof Player && ((Player) dying).getPlayerStatus() < 2) {
			((Player) dying).getInventory().clear();
			((Player) dying).getEquipment().clear();
		}
		progress();
	}

	/**
	 * Sets the duration.
	 * 
	 * @param duration
	 *            the duration
	 * @return the death
	 */
	public Death setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * Gets the timer.
	 * 
	 * @return the timer
	 */
	public StopWatch getTimer() {
		return timer;
	}

	/**
	 * Gets the dying.
	 * 
	 * @return the dying
	 */
	public Character getDying() {
		return dying;
	}

	/**
	 * Gets the killer.
	 * 
	 * @return the killer
	 */
	public Character getKiller() {
		return killer;
	}

	/**
	 * Gets the items.
	 * 
	 * @return the items
	 */
	public List<Item> getItems() {
		return items;
	}

	/**
	 * Gets the kept on death.
	 * 
	 * @return the kept on death
	 */
	public ArrayList<Item> getKeptOnDeath() {
		return keptOnDeath;
	}

	/**
	 * Progress.
	 */
	public void progress() {
		this.stage++;
	}

	/**
	 * Gets the stage.
	 * 
	 * @return the stage
	 */
	public int getStage() {
		return stage;
	}

	/**
	 * Gets the gravestone.
	 * 
	 * @return the gravestone
	 */
	public Position getGravestone() {
		return gravestone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
	}

	/**
	 * Gets the duration.
	 * 
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
}
