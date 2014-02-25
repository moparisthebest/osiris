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
import java.util.LinkedList;
import java.util.List;

import osiris.game.model.def.NpcCombatDef;
import osiris.game.model.item.Item;
import osiris.game.update.UpdateFlags.UpdateFlag;
import osiris.game.update.block.TransformNpcBlock;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc

/**
 * An in-game non-player-character.
 * 
 * @author Blake
 * 
 */
public class Npc extends Character {

	/**
	 * The id.
	 */
	private int id, currentHp;

	/** The is walking. */
	private boolean isWalking = true;

	/** The default facing. */
	private Direction defaultFacing;

	/** The default position. */
	private Position defaultPosition;

	/** The combat def. */
	private NpcCombatDef combatDef;

	/** The drops. */
	private List<Item> drops = new ArrayList<Item>();

	/**
	 * The walkable positions.
	 */
	private List<Position> walkablePositions = new LinkedList<Position>();

	/** The aggressive distance. */
	private int aggressiveDistance = -1;

	/**
	 * Instantiates a new npc.
	 * 
	 * @param id
	 *            the id
	 */
	public Npc(int id) {
		this.setId(id);
		this.combatDef = NpcCombatDef.forId(id);
		currentHp = getMaxHp();
		if (currentHp == 0)
			currentHp = 1;
		this.defaultFacing = null;
		this.defaultPosition = new Position();
	}

	/**
	 * Sets the walking missile.
	 * 
	 * @param minX
	 *            the min x
	 * @param minY
	 *            the min y
	 * @param maxX
	 *            the max x
	 * @param maxY
	 *            the max y
	 * @return the npc
	 */
	public Npc setWalkingRange(int minX, int minY, int maxX, int maxY) {
		return setWalkingRange(Utilities.generatePositionsInBox(minX, minY, maxX, maxY));
	}

	/**
	 * Sets the walking range.
	 * 
	 * @param points
	 *            the points
	 * @return the npc
	 */
	public Npc setWalkingRange(ArrayList<Position> points) {
		walkablePositions.clear();
		walkablePositions.addAll(points);
		return this;
	}

	/**
	 * Sets the default facing.
	 * 
	 * @param position
	 *            the new default facing
	 */
	public void setDefaultFacing(Position position) {
		if (position == null)
			return;
		Direction direction = Direction.fromDeltas(position.getX() - getPosition().getX(), position.getY() - getPosition().getY());
		this.defaultFacing = direction;
	}

	/**
	 * Sets the default position.
	 * 
	 * @param position
	 *            the new default position
	 */
	public void setDefaultPosition(Position position) {
		this.defaultPosition = position;
	}

	/**
	 * Gets the default position.
	 * 
	 * @return the default position
	 */
	public Position getDefaultPosition() {
		return defaultPosition;
	}

	/**
	 * Gets the default facing.
	 * 
	 * @return the default facing
	 */
	public Direction getDefaultFacing() {
		return defaultFacing;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the walkable positions.
	 * 
	 * @return the walkablePositions
	 */
	public List<Position> getWalkablePositions() {
		return walkablePositions;
	}

	/**
	 * Current hp.
	 * 
	 * @return the int
	 */
	public int currentHp() {
		return currentHp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.Character#setCurrentHp(int)
	 */
	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	/**
	 * Checks if is walking.
	 * 
	 * @return true, if is walking
	 */
	public boolean isWalking() {
		return isWalking;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.Character#teleport(osiris.game.model.Position)
	 */
	public void teleport(Position to) {
		setPosition(to);
		getUpdateFlags().flag(UpdateFlag.TELEPORTED);
		getMovementQueue().reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.Character#transform(int)
	 */
	public void transform(int toNpcId) {
		this.setId(toNpcId);
		this.addUpdateBlock(new TransformNpcBlock(this, toNpcId));
	}

	/**
	 * Face default.
	 */
	public void faceDefault() {
		facePosition(Direction.directionToPosition(getPosition(), getDefaultFacing()));
	}

	/**
	 * Gets the combat def.
	 * 
	 * @return the combat def
	 */
	public NpcCombatDef getCombatDef() {
		return combatDef;
	}

}
