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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import osiris.game.action.Action;
import osiris.game.action.ObjectActionListener;
import osiris.game.action.impl.SwitchAutoCastAction;
import osiris.game.action.impl.TeleportAction;
import osiris.game.model.combat.CombatManager;
import osiris.game.model.combat.EquippedWeapon;
import osiris.game.model.effect.BindingEffect;
import osiris.game.model.effect.BindingSpellEffect;
import osiris.game.model.effect.Effect;
import osiris.game.model.effect.ExpiringEffect;
import osiris.game.model.magic.HomeTeleportSpell;
import osiris.game.model.magic.SpellBook;
import osiris.game.update.UpdateBlock;
import osiris.game.update.UpdateFlags;
import osiris.game.update.UpdateFlags.UpdateFlag;
import osiris.game.update.block.AnimationBlock;
import osiris.game.update.block.FaceToCharacterBlock;
import osiris.game.update.block.FaceToPositionBlock;
import osiris.util.Settings;
import osiris.util.StopWatch;
import osiris.util.TickTimer;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * An in-game character.
 * 
 * @author Blake
 * @author Boomer
 * 
 */
public abstract class Character extends Viewable {

	/**
	 * The slot.
	 */
	private int slot;

	/** The effects. */
	private ArrayList<Effect> effects = new ArrayList<Effect>() {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 4881615339096765156L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.ArrayList#clear()
		 */
		@Override
		public void clear() {
			for (Effect effect : effects)
				effect.terminate(Character.this);
			super.clear();
		}
	};

	/** The local players. */
	protected List<Player> localPlayers = new LinkedList<Player>();

	/** The local npcs. */
	protected List<Npc> localNpcs = new LinkedList<Npc>();

	/**
	 * The update flags.
	 */
	private UpdateFlags updateFlags = new UpdateFlags();

	/** The action timer. */
	private TickTimer actionTimer = new TickTimer();

	/**
	 * The movement queue.
	 */
	private MovementQueue movementQueue = new MovementQueue(this);

	/**
	 * The primary direction.
	 */
	private Direction primaryDirection, secondaryDirection;

	/** The spell book. */
	private SpellBook spellBook = SpellBook.NORMAL;

	/** The home tele timer. */
	private StopWatch homeTeleTimer = new StopWatch().setTicks(HomeTeleportSpell.COOLDOWN_TICKS);

	/**
	 * The blocks.
	 */
	private List<UpdateBlock> updateBlocks = new LinkedList<UpdateBlock>();

	/**
	 * The interacting character.
	 */
	private Character interactingCharacter;

	/**
	 * The current action.
	 */
	private Action currentAction;

	/**
	 * The is visible.
	 */
	private boolean isVisible = true;

	/** The combat manager. */
	private final CombatManager combatManager = new CombatManager(this);

	/** The players run energy. */
	private int energy = 100;

	/** Whether or not the run button is toggled. */
	private boolean runToggle;

	/** The update block q. */
	private Queue<UpdateBlock> updateBlockQ = new LinkedList<UpdateBlock>();

	/**
	 * Handle queued update blocks.
	 */
	public void handleQueuedUpdateBlocks() {
		for (int i = 0; i < updateBlockQ.size(); i++) {
			UpdateBlock block = updateBlockQ.poll();
			addUpdateBlock(block);
		}
	}

	/**
	 * Stop animation.
	 */
	public void stopAnimation() {
		for (Iterator<UpdateBlock> blocks = updateBlocks.iterator(); blocks.hasNext();)
			if (blocks.next() instanceof AnimationBlock)
				blocks.remove();
		addUpdateBlock(new AnimationBlock(this, -1, 0));
	}

	/**
	 * Sets the slot.
	 * 
	 * @param slot
	 *            the slot to set
	 */
	public void setSlot(int slot) {
		this.slot = slot;
	}

	/**
	 * Gets the slot.
	 * 
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Sets the update flags.
	 * 
	 * @param updateFlags
	 *            the updateFlags to set
	 */
	public void setUpdateFlags(UpdateFlags updateFlags) {
		this.updateFlags = updateFlags;
	}

	/**
	 * Gets the update flags.
	 * 
	 * @return the updateFlags
	 */
	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

	/**
	 * Sets the primary direction.
	 * 
	 * @param primaryDirection
	 *            the primaryDirection to set
	 */
	public void setPrimaryDirection(Direction primaryDirection) {
		this.primaryDirection = primaryDirection;
	}

	/**
	 * Gets the primary direction.
	 * 
	 * @return the primaryDirection
	 */
	public Direction getPrimaryDirection() {
		return primaryDirection;
	}

	/**
	 * Sets the secondary direction.
	 * 
	 * @param secondaryDirection
	 *            the secondaryDirection to set
	 */
	public void setSecondaryDirection(Direction secondaryDirection) {
		this.secondaryDirection = secondaryDirection;
	}

	/**
	 * Gets the secondary direction.
	 * 
	 * @return the secondaryDirection
	 */
	public Direction getSecondaryDirection() {
		return secondaryDirection;
	}

	/**
	 * Sets the movement queue.
	 * 
	 * @param movementQueue
	 *            the movementQueue to set
	 */
	public void setMovementQueue(MovementQueue movementQueue) {
		this.movementQueue = movementQueue;
	}

	/**
	 * Gets the movement queue.
	 * 
	 * @return the movementQueue
	 */
	public MovementQueue getMovementQueue() {
		return movementQueue;
	}

	/**
	 * Sets the current action.
	 * 
	 * @param currentAction
	 *            the currentAction to set
	 */
	public void setCurrentAction(Action currentAction) {
		this.currentAction = currentAction;
	}

	/**
	 * Gets the current action.
	 * 
	 * @return the currentAction
	 */
	public Action getCurrentAction() {
		return currentAction;
	}

	/**
	 * Sets the update blocks.
	 * 
	 * @param updateBlocks
	 *            the updateBlocks to set
	 */
	public void setUpdateBlocks(List<UpdateBlock> updateBlocks) {
		this.updateBlocks = updateBlocks;
	}

	/**
	 * Adds the update block.
	 * 
	 * @param block
	 *            the block
	 */
	public void addUpdateBlock(UpdateBlock block) {
		for (Iterator<UpdateBlock> i = updateBlocks.iterator(); i.hasNext();) {
			UpdateBlock block_ = i.next();
			if (block.equals(block_)) {
				// Can't add it this cycle, queue it.
				updateBlockQ.add(block);
				return;
			}
		}

		updateFlags.flag(UpdateFlag.UPDATE);
		updateBlocks.add(block);
	}

	/**
	 * Adds the update block, and replaces an existing one instead of queuing.
	 * 
	 * @param block
	 *            the block
	 */
	public void addPriorityUpdateBlock(UpdateBlock block) {
		for (Iterator<UpdateBlock> i = updateBlocks.iterator(); i.hasNext();) {
			UpdateBlock block_ = i.next();
			if (block.equals(block_)) {
				i.remove();
			}
		}

		updateFlags.flag(UpdateFlag.UPDATE);
		updateBlocks.add(block);
	}

	/**
	 * Gets the update blocks.
	 * 
	 * @return the updateBlocks
	 */
	public List<UpdateBlock> getUpdateBlocks() {
		return updateBlocks;
	}

	/**
	 * Checks for update blocks.
	 * 
	 * @return true, if successful
	 */
	public boolean hasUpdateBlocks() {
		return !updateBlocks.isEmpty();
	}

	/**
	 * Sets the interacting character.
	 * 
	 * @param interactingCharacter
	 *            the interactingCharacter to set
	 */
	public void setInteractingCharacter(Character interactingCharacter) {
		this.interactingCharacter = interactingCharacter;
		faceCharacter(interactingCharacter);
	}

	/**
	 * Gets the interacting character.
	 * 
	 * @return the interactingCharacter
	 */
	public Character getInteractingCharacter() {
		return interactingCharacter;
	}

	/**
	 * Max hitpoints.
	 * 
	 * @return the int
	 */
	public int getMaxHp() {
		if (this instanceof Player)
			return ((Player) this).getSkills().maxLevel(Skills.SKILL_HITPOINTS);
		else
			return ((Npc) this).getCombatDef().getMaxHp();
	}

	/**
	 * Current hitpoints.
	 * 
	 * @return the int
	 */
	public int getCurrentHp() {
		if (this instanceof Player)
			return ((Player) this).getSkills().currentLevel(Skills.SKILL_HITPOINTS);
		else
			return ((Npc) this).currentHp();
	}

	/**
	 * Sets the current health.
	 * 
	 * @param health
	 *            the new current health
	 */
	public void setCurrentHp(int health) {
		if (this instanceof Player)
			((Player) this).getSkills().setCurLevel(Skills.SKILL_HITPOINTS, health);
		else
			((Npc) this).setCurrentHp(health);
	}

	/**
	 * Checks if is dying.
	 * 
	 * @return true, if is dying
	 */
	public boolean isDying() {
		return currentAction != null && (currentAction instanceof Death);
	}

	/**
	 * Sets the players run energy.
	 * 
	 * @param energy
	 *            The players run energy.
	 * @param send
	 *            the send
	 */
	public void setEnergy(int energy, boolean send) {
		this.energy = energy;
	}

	/**
	 * Gets the remaining run energy for this player.
	 * 
	 * @return The players run energy.
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * Sets whether or not the run button is toggled.
	 * 
	 * @param runToggle
	 *            Whether or not the run button is toggled.
	 */
	public void setRunToggle(boolean runToggle) {
		this.runToggle = runToggle;
	}

	/**
	 * Checks whether or not the run button is toggled.
	 * 
	 * @return Returns true if it is, false otherwise.
	 */
	public boolean isRunToggled() {
		return runToggle;
	}

	/**
	 * Gets the combat manager.
	 * 
	 * @return the combat manager
	 */
	public CombatManager getCombatManager() {
		return combatManager;
	}

	/**
	 * Gets the block animation.
	 * 
	 * @return the block animation
	 */
	public int getBlockAnimation() {
		if (this instanceof Player) {
			Player player = (Player) this;
			if (player.getEquipment().getItem(Settings.SLOT_SHIELD) == null) {
				if (player.getEquippedAttack() != null) {
					return player.getEquippedAttack().getBlockAnimation();
				} else {
					return EquippedWeapon.getFists().getBlockAnimation();
				}
			} else {
				return 403; // Block with shield.
			}
		} else if (this instanceof Npc)
			return ((Npc) this).getCombatDef().getBlockAnimation();
		return -1;
	}

	/**
	 * Teleport.
	 * 
	 * @param to
	 *            the to
	 */
	public abstract void teleport(Position to);

	/**
	 * Can move.
	 * 
	 * @return true, if successful
	 */
	public boolean canMove() {
		boolean binded = false;
		for (Effect effect : effects) {
			if ((effect instanceof BindingEffect && ((BindingEffect) effect).isBinding()) || (effect instanceof BindingSpellEffect && ((BindingSpellEffect) effect).isBinding())) {
				binded = true;
				if (this instanceof Player)
					((Player) this).getEventWriter().sendMessage("You are frozen and can not move!");
				break;
			}
		}
		return !binded;
	}

	/**
	 * Checks if is teleporting.
	 * 
	 * @return true, if is teleporting
	 */
	public boolean isTeleporting() {
		boolean teleporting = (currentAction != null && currentAction instanceof TeleportAction);
		return teleporting;
	}

	/**
	 * Can teleport.
	 * 
	 * @return true, if successful
	 */
	public boolean canTeleport() {
		boolean dying = isDying();
		return !dying;
	}

	/**
	 * Sets the visible.
	 * 
	 * @param isVisible
	 *            the isVisible to set
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * Checks if is visible.
	 * 
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Sets the spell book.
	 * 
	 * @param spellBook
	 *            the new spell book
	 */
	public void setSpellBook(SpellBook spellBook) {
		if (this.spellBook == spellBook)
			return;
		Action currentAction = getCurrentAction();
		if (currentAction != null && currentAction instanceof SwitchAutoCastAction)
			currentAction.cancel();
		this.spellBook = spellBook;
		if (this instanceof Player) {
			int tabId = spellBook.getInterfaceId();
			((Player) this).getEventWriter().sendTab(79, tabId);
			((Player) this).getEventWriter().restoreGameframe();
		}
	}

	/**
	 * Sets the spell book.
	 * 
	 * @param name
	 *            the new spell book
	 */
	public void setSpellBook(String name) {
		if (name.equalsIgnoreCase(spellBook.name())) {
			return;
		}
		spellBook = SpellBook.valueOf(name.toUpperCase());
	}

	/**
	 * Gets the spell book.
	 * 
	 * @return the spell book
	 */
	public SpellBook getSpellBook() {
		return spellBook;
	}

	/**
	 * Gets the home tele timer.
	 * 
	 * @return the home tele timer
	 */
	public StopWatch getHomeTeleTimer() {
		return homeTeleTimer;
	}

	/**
	 * Face character.
	 * 
	 * @param faceTo
	 *            the face to
	 */
	public void faceCharacter(Character faceTo) {
		int face = 65535;
		if (faceTo != null) {
			face = faceTo.getSlot();
			if (faceTo instanceof Player)
				face += 32768;
		}
		this.addUpdateBlock(new FaceToCharacterBlock(this, face));
	}

	/**
	 * Face position.
	 * 
	 * @param position
	 *            the position
	 */
	public void facePosition(Position position) {
		if (position == null)
			return;
		this.addUpdateBlock(new FaceToPositionBlock(this, position));
	}

	/**
	 * Adds the effect.
	 * 
	 * @param effect
	 *            the effect
	 * @return true, if successful
	 */
	public boolean addEffect(ExpiringEffect effect) {
		if (!canAddEffect(effect))
			return false;
		effects.add(effect);
		return true;
	}

	/**
	 * Can add effect.
	 * 
	 * @param effect
	 *            the effect
	 * @return true, if successful
	 */
	public boolean canAddEffect(Effect effect) {
		for (Effect other : effects)
			if (other.equals(effect))
				return false;
		return true;
	}

	/**
	 * Gets the effects.
	 * 
	 * @return the effects
	 */
	public ArrayList<Effect> getEffects() {
		return effects;
	}

	/**
	 * Sets the local players.
	 * 
	 * @param localPlayers
	 *            the new local players
	 */
	public void setLocalPlayers(List<Player> localPlayers) {
		this.localPlayers = localPlayers;
	}

	/**
	 * Gets the local players.
	 * 
	 * @return the local players
	 */
	public List<Player> getLocalPlayers() {
		return localPlayers;
	}

	/**
	 * Gets the closest player.
	 * 
	 * @return the closest player
	 */
	public Player getClosestPlayer() {
		Player closest = null;
		int closestDistance = 0;
		for (Player other : localPlayers) {
			int distance = Utilities.getDistance(getPosition(), other.getPosition());
			if (closest == null || distance < closestDistance) {
				closest = other;
				closestDistance = distance;
			}
		}
		return closest;
	}

	/**
	 * Checks if is movement locked.
	 * 
	 * @return true, if is movement locked
	 */
	public boolean isMovementLocked() {
		if (currentAction != null && currentAction instanceof ObjectActionListener) // not
																					// gunna
																					// work
																					// since
																					// OALs
																					// all
																					// make
																					// new
																					// Distanced
																					// Actions
			return true;
		if (movementQueue.isLocked())
			return true;
		return isDying() || isTeleporting();
	}

	/**
	 * Gets the action timer.
	 * 
	 * @return the action timer
	 */
	public TickTimer getActionTimer() {
		return actionTimer;
	}

	/**
	 * Transform.
	 * 
	 * @param toNpcId
	 *            the to npc id
	 */
	public abstract void transform(int toNpcId);

}
