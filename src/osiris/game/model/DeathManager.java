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
import java.util.Random;

import osiris.game.model.def.NpcCombatDef;
import osiris.game.model.ground.GroundItem;
import osiris.game.model.ground.GroundManager;
import osiris.game.model.item.Item;
import osiris.game.update.block.AnimationBlock;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class DeathManager.
 * 
 * @author Boomer
 * 
 */
public class DeathManager implements Runnable {

	/**
	 * The Constant PLAYER_DEATH_ANIMATION.
	 */
	public static final int PLAYER_DEATH_ANIMATION = 7197;

	/**
	 * The deaths.
	 */
	private ArrayList<Death> deaths;

	/**
	 * Instantiates a new death manager.
	 */
	public DeathManager() {
		this.deaths = new ArrayList<Death>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			for (Iterator<Death> iterator = deaths.iterator(); iterator.hasNext();) {
				Death death = iterator.next();
				if (death.getTimer().elapsed() >= 1 && death.getStage() == 1) {
					if (death.getDying() != null)
						death.getDying().addPriorityUpdateBlock(new AnimationBlock(death.getDying(), getDyingAnimation(death.getDying()), 0));
					death.progress();
				}
				if (death.getTimer().elapsed() >= Death.DEFAULT_DURATION && death.getStage() == 2) {
					Character getItems = death.getKiller();
					if (death.getDying() != null) {
						if (death.getDying() instanceof Player) {
							Player player = (Player) death.getCharacter();
							player.teleport(new Position(Settings.DEFAULT_X, Settings.DEFAULT_Y, Settings.DEFAULT_Z));
							player.getEventWriter().sendMessage("Oh dear, you have died!");
							ArrayList<Item> keptItems = death.getKeptOnDeath();
							if (player.getPlayerStatus() < 2) {
								for (Item kept : keptItems) {
									if (kept == null)
										continue;
									for (Iterator<Item> droppedItems = death.getItems().iterator(); droppedItems.hasNext();) {
										Item dropped = droppedItems.next();
										if (dropped != null) {
											if (dropped.getId() == kept.getId()) {
												dropped.adjustAmount(-1);
												if (dropped.getAmount() == 0)
													droppedItems.remove();
												break;
											}
										}
									}
								}
							}
							player.getEquipment().refresh();
							player.getInventory().refresh();
							for (int i = 0; i < Skills.NUMBER_OF_SKILLS; i++)
								player.getSkills().setCurLevel(i, player.getSkills().maxLevel(i));
							player.setEnergy(100, true);
							player.setSpecialEnergy(1000, true);
							if (player.getPlayerStatus() < 2)
								player.addAllItems(keptItems.toArray(new Item[keptItems.size()]));
							else
								death.getItems().clear();
							player.updateAppearance();
							player.refreshWeaponTab();
							player.updateAnimationIndices();
						}

						if (death.getKiller() == null || death.getKiller() instanceof Npc)
							getItems = death.getDying();
					}

					for (Item item : death.getItems()) {
						if (item == null) {
							continue;
						}

						GroundItem ground = new GroundItem(item, death.getDying(), getItems, death.getGravestone());
						GroundManager.getManager().dropItem(ground);
					}

					death.progress();
				}
				if (death.getStage() == 3) {
					if (death.getTimer().elapsed() >= death.getDuration()) {
						if (death.getDying() != null) {
							if (death.getDying() instanceof Npc) {
								Npc npc = (Npc) death.getDying();
								if (npc.getWalkablePositions().size() != 0)
									death.getDying().teleport(npc.getWalkablePositions().get(new Random().nextInt(npc.getWalkablePositions().size())));
								else
									death.getDying().teleport(npc.getDefaultPosition());
								death.getDying().setVisible(true);
								death.getDying().setCurrentHp(death.getDying().getMaxHp());
							}
						}
						death.progress();
						iterator.remove();
						death.cancel();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the death animation.
	 * 
	 * @param character
	 *            the character
	 * @return the death animation
	 */
	public static int getDeathAnimation(Character character) {
		if (character instanceof Player)
			return PLAYER_DEATH_ANIMATION;
		else
			return /*
					 * (NpcDef.forId(((Npc)
					 * character).getId()).getDeathAnimation())
					 */0;
	}

	/**
	 * Gets the deaths.
	 * 
	 * @return the deaths
	 */
	public ArrayList<Death> getDeaths() {
		return deaths;
	}

	/**
	 * Gets the dying animation.
	 * 
	 * @param character
	 *            the character
	 * @return the dying animation
	 */
	public static final int getDyingAnimation(Character character) {
		if (character instanceof Npc) {
			int id = ((Npc) character).getId();
			NpcCombatDef def = NpcCombatDef.forId(id);
			if (def != null)
				return def.getDeathAnimation();
		}
		return PLAYER_DEATH_ANIMATION;
	}

}
