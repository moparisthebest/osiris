package osiris.game.event.impl;

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

import java.util.LinkedList;
import java.util.Queue;

import osiris.Main;
import osiris.game.action.Action;
import osiris.game.action.impl.DeathItemsScreenAction;
import osiris.game.action.impl.EmoteAction;
import osiris.game.action.impl.EquipmentScreenAction;
import osiris.game.action.impl.SwitchAutoCastAction;
import osiris.game.action.impl.TradeAction;
import osiris.game.event.GameEvent;
import osiris.game.model.Player;
import osiris.game.model.XValue;
import osiris.game.model.def.ItemDef;
import osiris.game.model.effect.PrayerEffect;
import osiris.game.model.item.Item;
import osiris.game.model.item.ItemContainer;
import osiris.game.model.item.Trade;
import osiris.game.model.magic.MagicManager;
import osiris.game.model.magic.Spell;
import osiris.game.model.magic.SpellBook;
import osiris.game.model.skills.Prayer;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class ActionButtonEvent.
 * 
 * @author Boomer
 * @author samuraiblood2
 * @author Blake
 */
public class ActionButtonEvent extends GameEvent {
	/**
	 * Instantiates a new game event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public ActionButtonEvent(Player player, Packet packet) {
		super(player, packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.event.GameEvent#process()
	 */
	public void process() throws Exception {
		PacketReader reader = new PacketReader(getPacket());
		int interfaceId = reader.readShort(false);
		int buttonId = reader.readShort(false);
		int buttonInfo = 0;
		if (getPacket().getHeader().getLength() >= 6) {
			buttonInfo = reader.readShort(false);
		}
		if (buttonInfo == 65535) {
			buttonInfo = 0;
		}
		if (Main.isLocal() && getPlayer().getPlayerStatus() == 2)
			getPlayer().getEventWriter().sendMessage("[" + getPacket().getHeader().getOpcode() + "]Interface: " + interfaceId + " Button: " + buttonId + " Info: " + buttonInfo);
		switch (interfaceId) {
		case 192:
			Spell spell = MagicManager.getSpell(buttonId, SpellBook.NORMAL);
			if (spell != null)
				spell.execute(getPlayer(), false);
			break;
		case 193:
			spell = MagicManager.getSpell(buttonId, SpellBook.ANCIENT);
			if (spell != null)
				spell.execute(getPlayer(), false);
			break;
		case 464:
			handleEmote(buttonId);
			break;
		case 182:
			if (buttonId == 6) {
				getPlayer().logout();
			}
			break;
		case 763:
			if (buttonId == 0) {
				Item item = getPlayer().getInventory().getItem(buttonInfo);
				if (item == null)
					return;
				int amount = 0;

				switch (getPacket().getHeader().getOpcode()) {
				case 233:
					amount = 1;
					getPlayer().depositItem(buttonInfo, amount);
					break;
				case 21:
					amount = 5;
					getPlayer().depositItem(buttonInfo, amount);
					break;
				case 169:
					amount = 10;
					getPlayer().depositItem(buttonInfo, amount);
					break;
				case 232:
					amount = getPlayer().getInventory().amountOfItem(item.getId());
					getPlayer().depositItem(buttonInfo, amount);
					break;
				case 173:
					new XValue(getPlayer(), getPlayer().getCurrentAction(), buttonInfo, false);
					break;
				}
			}
			break;
		case 762:
			if (buttonId == 73) {
				Item item = null;
				Item[] bankItems = getPlayer().getBank().getItems();
				if (bankItems != null && bankItems.length > buttonInfo)
					item = bankItems[buttonInfo];
				if (item == null)
					return;
				int amount = 0;
				int hasAmount = item.getAmount();
				switch (getPacket().getHeader().getOpcode()) {
				case 233:
					amount = 1;
					getPlayer().withdrawItem(buttonInfo, amount);
					break;
				case 21:
					amount = 5;
					getPlayer().withdrawItem(buttonInfo, amount);
					break;
				case 169:
					amount = 10;
					getPlayer().withdrawItem(buttonInfo, amount);
					break;
				case 232:
					amount = hasAmount;
					getPlayer().withdrawItem(buttonInfo, amount);
					break;
				case 133:
					amount = (hasAmount - 1);
					getPlayer().withdrawItem(buttonInfo, amount);
					break;
				case 173:
					new XValue(getPlayer(), getPlayer().getCurrentAction(), buttonInfo, true);
					break;
				}

			} else if (buttonId == 16) {
				getPlayer().getBank().switchWithdrawNotes();
			}
			break;

		case 261:
			switch (buttonId) {
			case 1:
				/*
				 * Toggle run.
				 */
				getPlayer().setRunToggle(!getPlayer().isRunToggled());
				break;

			case 14:
				/*
				 * Settings screen.
				 */
				getPlayer().getEventWriter().sendInterface(742);
				break;

			case 16:
				/*
				 * Settings screen.
				 */
				getPlayer().getEventWriter().sendInterface(743);
				break;

			case 2:
				/*
				 * Chat effects.
				 */
				getPlayer().getSettings().setChatEffects(!getPlayer().getSettings().isChatEffects());
				getPlayer().getSettings().refresh();
				break;

			case 3:
				/*
				 * Split private chat.
				 */
				getPlayer().getSettings().setSplitPrivateChat(!getPlayer().getSettings().isSplitPrivateChat());
				getPlayer().getSettings().refresh();
				break;

			case 4:
				// TODO: Mouse button config.
				break;

			case 5:
				// TODO: Accept aid config.
				break;
			}
			break;

		case 750:
			switch (buttonId) {
			case 1:
				/*
				 * Toggle run.
				 */
				getPlayer().setRunToggle(!getPlayer().isRunToggled());
				break;
			}
			break;
		case 334:
			if (getPacket().getHeader().getOpcode() == 233) {
				if (buttonId == 8 || buttonId == 21) {
					Trade trade = getPlayer().getTradeOpen();
					if (trade == null)
						getPlayer().getEventWriter().sendCloseInterface();
					else
						getPlayer().getCurrentAction().cancel();
				} else if (buttonId == 20) {
					Trade trade = getPlayer().getTradeOpen();
					if (trade == null)
						getPlayer().getEventWriter().sendCloseInterface();
					else
						trade.accept(getPlayer(), true);
				}
			}
			break;
		case 335:
			if (getPacket().getHeader().getOpcode() == 233) {
				if (buttonId == 18 || buttonId == 12) {
					Trade trade = getPlayer().getTradeOpen();
					if (trade == null)
						getPlayer().getEventWriter().sendCloseInterface();
					else
						getPlayer().getCurrentAction().cancel();
				} else if (buttonId == 16) {
					Trade trade = getPlayer().getTradeOpen();
					if (trade == null)
						getPlayer().getEventWriter().sendCloseInterface();
					else
						trade.accept(getPlayer(), false);
				} else if (buttonId == 30) {
					Trade trade = getPlayer().getTradeOpen();
					if (trade == null)
						getPlayer().getEventWriter().sendCloseInterface();
					else {
						ItemContainer tradeItems = trade.getTradeItems(getPlayer());
						if (tradeItems == null)
							return;
						Item item = tradeItems.getItems()[buttonInfo];
						if (item == null)
							return;
						if (ItemDef.forId(item.getId()).isStackable())
							trade.removeTradeItem(getPlayer(), buttonInfo, tradeItems.amountOfItem(item.getId()));
						else
							trade.removeTradeItem(getPlayer(), buttonInfo, 1);
					}
				}
			}
			break;
		case 336:
			Action action = getPlayer().getCurrentAction();
			Trade trade = null;
			if (action == null)
				return;
			else if (action instanceof TradeAction)
				trade = ((TradeAction) action).getTrade();
			if (trade == null)
				return;
			switch (getPacket().getHeader().getOpcode()) {
			case 233:
				trade.addTradeItem(getPlayer(), buttonInfo, 1);
				break;
			case 21:
				trade.addTradeItem(getPlayer(), buttonInfo, 5);
				break;
			case 169:
				trade.addTradeItem(getPlayer(), buttonInfo, 10);
				break;
			case 214:
				trade.addTradeItem(getPlayer(), buttonInfo, getPlayer().getInventory().amountOfItem(getPlayer().getInventory().getItem(buttonInfo).getId()));
				break;
			case 232:
				getPlayer().getEventWriter().sendMessage("that will be one dolla");
				break;
			case 133:
				getPlayer().getEventWriter().sendMessage("sharing is caring, but we don't care here.");
				break;
			case 173:
				new XValue(getPlayer(), getPlayer().getCurrentAction(), buttonInfo, false);
				break;

			}
			break;

		case 387:
			switch (buttonId) {
			case 55:
				new EquipmentScreenAction(getPlayer()).run();
				break;
			case 52:
				new DeathItemsScreenAction(getPlayer()).run();
				break;
			}
			break;
		case 319:
		case 388:
			Action curAction = getPlayer().getCurrentAction();
			if (curAction == null || !(curAction instanceof SwitchAutoCastAction)) {
				getPlayer().refreshWeaponTab();
			} else if (buttonId < 0 || buttonId >= 16)
				curAction.cancel();
			else {
				((SwitchAutoCastAction) curAction).setAutoSpellId(buttonId).run();
			}
			break;
		case 574:
			Action currentAction = getPlayer().getCurrentAction();
			if (currentAction != null) {
				if (buttonId == 17) {
					currentAction.run();
				} else if (buttonId == 18 || buttonId == 12) {
					currentAction.cancel();
					getPlayer().getEventWriter().sendCloseInterface();
				}
			}
			break;

		case 271:
			PrayerEffect prayerEffect = new PrayerEffect((buttonId - 5) / 2);
			Prayer prayer = prayerEffect.getPrayer();
			PrayerEffect existing = getPlayer().getPrayers().put(prayer.getType(), null);
			boolean samePrayer = false;
			if (existing == null) {
				Queue<Prayer.Type> combatTypes = new LinkedList<Prayer.Type>();
				combatTypes.add(Prayer.Type.STRENGTH);
				combatTypes.add(Prayer.Type.ATTACK);
				combatTypes.add(Prayer.Type.DEFENCE);
				if (combatTypes.contains(prayer.getType())) {
					existing = getPlayer().getPrayers().put(Prayer.Type.BADASS, null);
					if (existing != null) {
						existing.terminate(getPlayer());
						getPlayer().getEffects().remove(existing);
						if (getPlayer().getEffects().add(prayerEffect))
							prayerEffect.execute(getPlayer());
					}
				} else if (prayer.getType() == Prayer.Type.BADASS) {
					while (combatTypes.size() > 0) {
						existing = getPlayer().getPrayers().put(combatTypes.poll(), null);
						if (existing != null) {
							existing.terminate(getPlayer());
							getPlayer().getEffects().remove(existing);
							if (getPlayer().getEffects().add(prayerEffect))
								prayerEffect.execute(getPlayer());
						}
					}
				}
			} else if (existing != null) {
				samePrayer = (existing.getPrayer().getConfigId() == prayer.getConfigId());
				existing.terminate(getPlayer());
				getPlayer().getEffects().remove(existing);
			}
			if (!samePrayer) {
				if (getPlayer().getEffects().add(prayerEffect))
					prayerEffect.execute(getPlayer());
			}
			break;

		default:
			if (getPlayer().getEquippedAttack().getTabId() == interfaceId) {
				if (interfaceId == 90) {
					if (buttonId == 4 || buttonId == 5) {
						new SwitchAutoCastAction(getPlayer(), buttonId == 4);
						break;
					}
				}
				if (buttonId == getPlayer().getEquippedAttack().getRetaliateId()) {
					getPlayer().getSettings().setAutoRetaliate(!getPlayer().getSettings().isAutoRetaliate());
					getPlayer().getSettings().refresh();
					break;
				}
				if (buttonId < 6) {
					int style = buttonId - 2;
					if (style < getPlayer().getEquippedAttack().getSkills().length && style >= 0) {
						getPlayer().setAttackStyle(style, false);
						break;
					}
				} else if (buttonId == 10 || buttonId == 8 || buttonId == 11) {
					getPlayer().setSpecEnabled(!getPlayer().isSpecEnabled());
					break;
				}
			}
			break;
		}

	}

	/**
	 * Handle emote.
	 * 
	 * @param buttonId
	 *            the button id
	 */
	private void handleEmote(int buttonId) {
		int emoteIndex = (buttonId - 2);
		if (emoteIndex >= Settings.AUTO_UNLOCKED_EMOTES && getPlayer().getEmoteStatus()[(emoteIndex - Settings.AUTO_UNLOCKED_EMOTES)] == 0)
			return;
		if (emoteIndex >= Settings.EMOTES.length)
			return;
		int emote = Settings.EMOTES[emoteIndex];
		int graphic = -1;
		if (emote == -1) {
			if (getPlayer().getEquipment().getItem(Settings.SLOT_CAPE) == null) {
				getPlayer().getEventWriter().sendMessage("You need to be wearing a skillcape in order to preform this emote!");
				return;
			}
			for (int i = 0; i < Settings.SKILL_CAPE_INFO.length; i++) {
				if (Settings.SKILL_CAPE_INFO[i][0] == getPlayer().getEquipment().getItem(Settings.SLOT_CAPE).getId() || Settings.SKILL_CAPE_INFO[i][0] + 1 == getPlayer().getEquipment().getItem(Settings.SLOT_CAPE).getId()) {
					emote = Settings.SKILL_CAPE_INFO[i][1];
					graphic = Settings.SKILL_CAPE_INFO[i][2];
					continue;
				}
				if (i == Settings.SKILL_CAPE_INFO.length && emote == -1)
					getPlayer().getEventWriter().sendMessage("You need to be wearing a skillcape in order to preform this emote!");
			}
		}
		if (emote != -1) {
			for (int gIndex[] : Settings.EMOTE_GRAPHICS) {
				if (gIndex[0] == buttonId)
					graphic = gIndex[1];
			}
			new EmoteAction(getPlayer(), emote, graphic).run();
		}
	}
}
