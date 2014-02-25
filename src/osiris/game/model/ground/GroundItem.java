package osiris.game.model.ground;

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

import osiris.Main;
import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.game.model.Viewable;
import osiris.game.model.item.Item;
import osiris.util.StopWatch;

// TODO: Auto-generated Javadoc
/**
 * The Class GroundItem.
 * 
 * @author Boomer
 */
public class GroundItem extends Viewable {

	/** The item. */
	private Item item;

	/** The view first. */
	private DropCharacter dropper = null, viewFirst = null;

	/** The timer. */
	private StopWatch timer;

	/** The stage. */
	private GroundStage stage = null;

	/**
	 * The Enum GroundStage.
	 */
	public enum GroundStage {

		/** The PRIVATE. */
		PRIVATE,

		/** The PUBLIC. */
		PUBLIC,

		/** The HIDDEN. */
		HIDDEN
	}

	/* Use for respawning world items */
	/**
	 * Instantiates a new ground item.
	 * 
	 * @param item
	 *            the item
	 * @param position
	 *            the position
	 */
	public GroundItem(Item item, Position position) {
		this.item = item;
		this.setPosition(position);
		this.timer = new StopWatch();
		this.stage = GroundStage.PUBLIC;
	}

	/**
	 * Instantiates a new ground item.
	 * 
	 * @param item
	 *            the item
	 * @param dropper
	 *            the dropper
	 * @param position
	 *            the position
	 */
	public GroundItem(Item item, Character dropper, Position position) {
		this(item, position);
		this.dropper = new DropCharacter(dropper);
		this.stage = GroundStage.PRIVATE;
		this.viewFirst = this.dropper;
		Character views = viewFirst.getCharacter();
	}

	/* Use for dropping an item */
	/**
	 * Instantiates a new ground item.
	 * 
	 * @param item
	 *            the item
	 * @param dropper
	 *            the dropper
	 */
	public GroundItem(Item item, Character dropper) {
		this(item, dropper, dropper.getPosition());
	}

	/* Use for kill drops */
	/**
	 * Instantiates a new ground item.
	 * 
	 * @param item
	 *            the item
	 * @param dropper
	 *            the dropper
	 * @param viewFirst
	 *            the view first
	 * @param position
	 *            the position
	 */
	public GroundItem(Item item, Character dropper, Character viewFirst, Position position) {
		this(item, dropper, position);
		this.viewFirst = new DropCharacter(viewFirst);
		Character views = this.viewFirst.getCharacter();
	}

	/**
	 * Gets the dropper.
	 * 
	 * @return the dropper
	 */
	public Character getDropper() {
		if (dropper == null)
			return null;
		return dropper.getCharacter();
	}

	/**
	 * Gets the view first.
	 * 
	 * @return the view first
	 */
	public Character getViewFirst() {
		if (viewFirst == null)
			return null;
		return viewFirst.getCharacter();
	}

	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	public Item getItem() {
		return item;
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
	 * Gets the stage.
	 * 
	 * @return the stage
	 */
	public GroundStage getStage() {
		return stage;
	}

	/**
	 * Sets the stage.
	 * 
	 * @param stage
	 *            the new stage
	 */
	public void setStage(GroundStage stage) {
		this.stage = stage;
	}

	/**
	 * The Class DropCharacter.
	 */
	class DropCharacter {

		/** The player. */
		boolean player;

		/** The unique id. */
		int uniqueId = -1;

		/**
		 * Instantiates a new drop character.
		 * 
		 * @param dropper
		 *            the dropper
		 */
		public DropCharacter(Character dropper) {
			if (dropper != null) {
				this.player = dropper instanceof Player;
				this.uniqueId = player ? ((Player) dropper).getUniqueId() : dropper.getSlot();
			}
		}

		/**
		 * Gets the character.
		 * 
		 * @return the character
		 */
		public Character getCharacter() {
			if (uniqueId == -1)
				return null;
			if (player)
				return Main.findPlayer(uniqueId);
			else
				return Main.getNpcs().get(uniqueId);
		}
	}
}
