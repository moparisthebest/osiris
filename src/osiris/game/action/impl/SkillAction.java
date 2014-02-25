package osiris.game.action.impl;

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

import osiris.game.action.TickedAction;
import osiris.game.model.Player;
import osiris.game.model.Skills;
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.Item;
import osiris.game.model.skills.Skill;
import osiris.game.update.block.AnimationBlock;

// TODO: Auto-generated Javadoc
/**
 * The Class SkillAction.
 * 
 * @author Boomer
 */
public class SkillAction extends TickedAction {

	/** The max attempts. */
	private int skillId, levelRequired, iterationsQueued, iterationsCompleted, emote, attempts, maxAttempts;

	/** The tools required. */
	private Item[] itemsRemoved, itemsGained, toolsRequired;

	/** The experience gained. */
	private double experienceGained;

	/** The success message. */
	private String successMessage;

	/** The skill. */
	private Skill skill;

	/**
	 * Instantiates a new ticked action.
	 * 
	 * @param player
	 *            the player
	 * @param skill
	 *            the skill
	 * @param skillId
	 *            the skill id
	 * @param levelRequired
	 *            the level required
	 * @param expGained
	 *            the exp gained
	 * @param emote
	 *            the emote
	 * @param toolsRequired
	 *            the tools required
	 * @param itemsRemoved
	 *            the items removed
	 * @param itemsGained
	 *            the items gained
	 * @param successMessage
	 *            the success message
	 * @param iterationsQueued
	 *            the iterations queued
	 */
	public SkillAction(Player player, Skill skill, int skillId, int levelRequired, double expGained, int emote, Item[] toolsRequired, Item[] itemsRemoved, Item[] itemsGained, String successMessage, int iterationsQueued) {
		super(player, skill.calculateCooldown());
		this.skill = skill;
		this.skillId = skillId;
		this.levelRequired = levelRequired;
		this.iterationsCompleted = 0;
		this.itemsRemoved = itemsRemoved;
		this.itemsGained = itemsGained;
		this.toolsRequired = toolsRequired;
		this.experienceGained = expGained;
		this.emote = emote;
		this.successMessage = successMessage;
		this.iterationsQueued = iterationsQueued;
		this.attempts = 0;
		this.maxAttempts = -1;
		player.getMovementQueue().reset();
	}

	/**
	 * Sets the max attempts.
	 * 
	 * @param attempts
	 *            the attempts
	 * @return the skill action
	 */
	public SkillAction setMaxAttempts(int attempts) {
		this.maxAttempts = attempts;
		return this;
	}

	/**
	 * Sets the items gained.
	 * 
	 * @param itemsGained
	 *            the new items gained
	 */
	public void setItemsGained(Item[] itemsGained) {
		this.itemsGained = itemsGained;
	}

	/**
	 * Sets the success message.
	 * 
	 * @param message
	 *            the new success message
	 */
	public void setSuccessMessage(String message) {
		this.successMessage = message;
	}

	/**
	 * Sets the experience gained.
	 * 
	 * @param exp
	 *            the new experience gained
	 */
	public void setExperienceGained(double exp) {
		this.experienceGained = exp;
	}

	/**
	 * Sets the level required.
	 * 
	 * @param level
	 *            the new level required
	 */
	public void setLevelRequired(int level) {
		this.levelRequired = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.TickedAction#execute()
	 */
	@Override
	public void execute() {

		if (getPlayer().getSkills().currentLevel(skillId) < levelRequired) {
			getPlayer().getEventWriter().sendMessage("You need a level of at least " + levelRequired + " " + Skills.SKILL_NAMES[skillId] + " to do that.");
			cancel();
			return;
		}

		if (toolsRequired.length > 0) {
			for (Item item : toolsRequired) {
				if (item == null) {
					continue;
				}

				if (getPlayer().getInventory().amountOfItem(item.getId()) < item.getAmount()) {
					getPlayer().getEventWriter().sendMessage("You need a " + ItemDef.forId(item.getId()).getName() + " to do that.");
					cancel();
					return;
				}
			}
		}

		if (emote != -1)
			getPlayer().addUpdateBlock(new AnimationBlock(getPlayer(), emote, 0));

		if (!skill.canIterate()) {
			attempts++;
			if (maxAttempts != -1 && attempts == maxAttempts)
				cancel();
			return;
		}

		Item[] invBefore = getPlayer().getInventory().getItems();
		if (itemsRemoved.length > 0) {
			for (Item item : itemsRemoved) {
				if (item == null)
					continue;

				if (!getPlayer().getInventory().removeById(item.getId(), item.getAmount(), false)) {
					getPlayer().getEventWriter().sendMessage("You do not have all the resources required!");
					cancel();
					getPlayer().getInventory().setItems(invBefore);
					getPlayer().getInventory().refresh();
					return;
				}
			}
		}

		if (itemsGained.length > 0) {
			if (!getPlayer().getInventory().canFitAll(itemsGained)) {
				getPlayer().getEventWriter().sendMessage("You do not have enough room for that!");
				cancel();
				getPlayer().getInventory().setItems(invBefore);
				getPlayer().getInventory().refresh();
				return;
			}

			getPlayer().addAllItems(itemsGained);
		} else
			getPlayer().getInventory().refresh();

		if (successMessage != null) {
			getPlayer().getEventWriter().sendMessage(successMessage);
		}

		iterationsCompleted++;
		getPlayer().getSkills().addExp(skillId, experienceGained);
		if (iterationsQueued != -1 && iterationsCompleted == iterationsQueued) {
			skill.onCompletion();
			cancel();
			return;
		} else {
			setTicks(skill.calculateCooldown());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#cancel()
	 */
	@Override
	public void cancel() {
		getPlayer().getEventWriter().sendCloseChatboxInterface();
		getPlayer().getEventWriter().sendCloseInterface();
		getPlayer().getEventWriter().removeSideLockingInterface();
		super.cancel();
	}
}
