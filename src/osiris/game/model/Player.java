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
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.jboss.netty.channel.Channel;

import osiris.Main;
import osiris.data.PlayerData;
import osiris.game.action.Action;
import osiris.game.action.impl.BankAction;
import osiris.game.action.impl.EquipmentScreenAction;
import osiris.game.action.impl.TradeAction;
import osiris.game.model.combat.EquippedWeapon;
import osiris.game.model.combat.SpecialManager;
import osiris.game.model.def.ItemDef;
import osiris.game.model.dialogues.Dialogue;
import osiris.game.model.effect.PrayerEffect;
import osiris.game.model.ground.GroundItem;
import osiris.game.model.ground.GroundManager;
import osiris.game.model.item.Bank;
import osiris.game.model.item.ContainerDef;
import osiris.game.model.item.Item;
import osiris.game.model.item.ItemContainer;
import osiris.game.model.item.Trade;
import osiris.game.model.skills.Prayer;
import osiris.game.update.NpcUpdater;
import osiris.game.update.PlayerUpdater;
import osiris.game.update.UpdateFlags;
import osiris.game.update.UpdateFlags.UpdateFlag;
import osiris.game.update.block.AppearanceBlock;
import osiris.io.EventWriter;
import osiris.io.ProtocolConstants;
import osiris.net.OsirisUpstreamHandler;
import osiris.util.Settings;
import osiris.util.StopWatch;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc

/**
 * An in-game player.
 * 
 * @author Blake
 * @author Boomer
 * 
 */
public class Player extends Character {

	/**
	 * The player's unique identification number
	 */
	private int uniqueId;

	/**
	 * The interface id.
	 */
	public int interfaceOpen;

	/**
	 * The player's overriding npc id
	 */
	private int npcId;

	/**
	 * The channel.
	 */
	private Channel channel;

	/**
	 * The username as a long.
	 */
	private long usernameAsLong;

	/**
	 * The username.
	 */
	private String username;

	/**
	 * The password.
	 */
	private String password;

	/** The player's rights. */
	private int playerStatus;

	/** The special energy. */
	private int specialEnergy;

	/** Last connected from. */
	@SuppressWarnings("unused")
	private String lastConnectedFrom;

	private Position loadedPosition;

	/** The bonuses. */
	private final PlayerBonuses bonuses;

	private LinkedList<GroundItem> localGroundItems;

	/** The equipped attack. */
	private EquippedWeapon equippedAttack;

	/** The prayers map */
	private HashMap<Prayer.Type, PrayerEffect> prayers;

	/**
	 * The inventory.
	 */
	private final ItemContainer inventory;

	/**
	 * The equipment.
	 */
	private final ItemContainer equipment;

	/**
	 * The bank.
	 */
	private final Bank bank;

	private XValue xValue;

	/**
	 * The event writer.
	 */
	private final EventWriter eventWriter = new EventWriter(this);

	/**
	 * The appearance.
	 */
	private Appearance appearance = new Appearance();

	/** requesting to trade with. */
	private Player tradeRequest;

	/**
	 * The skills.
	 */
	private final Skills skills;

	/**
	 * The emote status.
	 */
	private int[] emoteStatus;

	/** The stand animation. */
	private int standAnimation;

	/** The walk animation. */
	private int walkAnimation;

	/** The run animation. */
	private int runAnimation;

	/**
	 * The one button.
	 */
	private boolean oneButton = false;

	/** The spec enabled. */
	private boolean specEnabled;

	/** The attack style. */
	private int attackStyle = 0;

	/**
	 * The is hd.
	 */
	private boolean isHd = false;

	/**
	 * The last region.
	 */
	private Position lastRegion = new Position();

	/**
	 * The player updater.
	 */
	private final PlayerUpdater playerUpdater = new PlayerUpdater(this);

	/**
	 * The npc updater.
	 */
	private final NpcUpdater npcUpdater = new NpcUpdater(this);

	private final StopWatch timeoutTimer = new StopWatch();

	/** The friends. */
	private Contacts contacts;

	/* Displayed head icon */
	private int headIcon = -1;

	/* Displayed skull icon */
	private int skullIcon = -1;

	/* Settings (one button, etc.) */
	private PlayerSettings settings;

	/* Player's current dialogue */
	private Dialogue dialogue;

	/**
	 * Instantiates a new player.
	 * 
	 * @param channel
	 *            the channel
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	public Player(Channel channel, String username, String password) {
		setChannel(channel);
		setUsernameAsLong(Utilities.stringToLong(username));
		setUsername(username);
		setPassword(password);
		this.inventory = new ItemContainer(28, false, false, true, new ContainerDef[] { new ContainerDef(-1, 64209, 93), new ContainerDef(-1, 1, 93), new ContainerDef(149, 0, 93), new ContainerDef(-1327, 64209, 93) }, this);
		this.equipment = new ItemContainer(14, false, true, false, new ContainerDef[] { new ContainerDef(387, 28, 94), new ContainerDef(-1, 64208, 94) }, this);
		this.bank = new Bank(this);
		this.emoteStatus = new int[20];
		this.skills = new Skills(this);
		this.uniqueId = -1;
		this.specialEnergy = Settings.MAX_SPECIAL_ENERGY;
		this.contacts = new Contacts(this);
		this.playerStatus = 0;
		this.bonuses = new PlayerBonuses(this);
		this.settings = new PlayerSettings(this);
		this.loadedPosition = null;
		this.npcId = -1;
		this.equippedAttack = EquippedWeapon.getFists();
		this.localGroundItems = new LinkedList<GroundItem>();
		this.prayers = new HashMap<Prayer.Type, PrayerEffect>();
		for (Prayer prayer : Prayer.values())
			if (!this.prayers.containsKey(prayer.getType()))
				this.prayers.put(prayer.getType(), null);
	}

	/**
	 * Login.
	 */
	public void login() {
		synchronized (OsirisUpstreamHandler.getPlayerMap()) {
			OsirisUpstreamHandler.getPlayerMap().put(channel.getId(), this);
		}

		int playersOnline = Main.getPlayers().size();
		if (playersOnline > Main.getMostPlayersOnline()) {
			Main.setMostPlayersOnline(playersOnline);
		}
		this.eventWriter.sendLandscape();
		showFrame();
		skills.initialize();

		if (loadedPosition != null) {
			teleport(loadedPosition);
		}

		eventWriter.sendMessage("Welcome to " + Settings.SERVER_NAME + " created by Boomer216, Blakeman8192, and Samuraiblood2!");
		eventWriter.sendMessage("Want to play with more people?  Visit www.enkrona.net!");
		inventory.refresh();
		equipment.refresh();
		bank.refresh();
		contacts.refresh();

		for (int i = 0; i < Skills.NUMBER_OF_SKILLS; i++)
			eventWriter.sendSkillLevel(i);

		sendEmoteStatus();
		getCombatManager().setAutoSpell(-1);
		bonuses.update();

		refreshPlayerOptions();

		settings.refresh();
		setRunToggle(false);

		updateSpecEnergy();
		updateAttackStyle();
		eventWriter.sendEnergy();
		changeGender(appearance.getGender());
		contacts.setOnlineStatus(true);

		synchronized (Main.getPlayers()) {
			updateAnimationIndices();
			// ^ will add an appearance block, so we don't need to.
			getUpdateFlags().flag(UpdateFlags.UpdateFlag.TELEPORTED);
		}
	}

	public void refreshPlayerOptions() {
		eventWriter.sendPlayerOption("Follow", 2, false);
		eventWriter.sendPlayerOption("Trade with", 4, false);
	}

	public void showFrame() {
		eventWriter.sendWindowPane(548);
		eventWriter.sendTab(6, 745);
		eventWriter.sendTab(7, 754);
		eventWriter.sendTab(68, ProtocolConstants.INTERFACE_CHATBOX); // Chatbox
		eventWriter.sendConfig2(1160, -1);
		eventWriter.sendTab(64, ProtocolConstants.INTERFACE_HP_BUBBLE); // HP
																		// bar
		eventWriter.sendTab(65, ProtocolConstants.INTERFACE_PRAYER_BUBBLE); // Prayer
																			// bar
		eventWriter.sendTab(66, ProtocolConstants.INTERFACE_ENERGY_BUBBLE); // Energy
																			// bar
		eventWriter.sendTab(8, ProtocolConstants.INTERFACE_PLAYER_NAME); // Playername
																			// on
																			// chat
		eventWriter.sendTab(11, ProtocolConstants.INTERFACE_CHAT_OPTIONS); // Chat
																			// options
		refreshWeaponTab();
		eventWriter.sendTab(74, ProtocolConstants.INTERFACE_SKILL_TAB); // Skill
																		// tab
		eventWriter.sendTab(75, ProtocolConstants.INTERFACE_QUEST_TAB); // Quest
																		// tab
		eventWriter.sendTab(76, ProtocolConstants.INTERFACE_INVENTORY_TAB); // Inventory
																			// tab
		eventWriter.sendTab(77, ProtocolConstants.INTERFACE_EQUIPMENT_TAB); // Equipment
																			// tab
		eventWriter.sendTab(78, ProtocolConstants.INTERFACE_PRAYER_TAB); // Prayer
																			// tab
		eventWriter.sendTab(79, getSpellBook().getInterfaceId()); // Magic tab
		eventWriter.sendTab(81, ProtocolConstants.INTERFACE_FRIEND_TAB); // Friend
																			// tab
		eventWriter.sendTab(82, ProtocolConstants.INTERFACE_IGNORE_TAB); // Ignore
																			// tab
		eventWriter.sendTab(83, ProtocolConstants.INTERFACE_CLAN_TAB); // Clan
																		// tab
		eventWriter.sendTab(84, ProtocolConstants.INTERFACE_SETTING_TAB); // Setting
																			// tab
		eventWriter.sendTab(85, ProtocolConstants.INTERFACE_EMOTE_TAB); // Emote
																		// tab
		eventWriter.sendTab(86, ProtocolConstants.INTERFACE_MUSIC_TAB); // Music
																		// tab
		eventWriter.sendTab(87, ProtocolConstants.INTERFACE_LOGOUT_TAB); // Logout
																			// tab
	}

	/**
	 * Gets the emote status.
	 * 
	 * @return the emote status
	 */
	public int[] getEmoteStatus() {
		return emoteStatus;
	}

	/**
	 * Force appearance update.
	 */
	public void updateAppearance() {
		addUpdateBlock(new AppearanceBlock(this));
	}

	/**
	 * Logout.
	 */
	public void logout() {
		if (!Main.getPlayers().contains(this))
			return;
		try {
			contacts.setOnlineStatus(false);
			if (this.getCurrentAction() != null)
				this.getCurrentAction().cancel();
			if (getMovementQueue().isLocked()) {
				Position last = getMovementQueue().getLastPosition();
				if (last != null)
					teleport(getMovementQueue().getLastPosition());
			}
			if (PlayerData.save(this))
				if (Main.isLocal())
					System.out.println("Game Saved for: " + this.getUsername());
				else if (Main.isLocal())
					System.out.println("Game NOT Saved for: " + this.getUsername());
			if (Main.isLocal())
				System.out.println(this + " logging out.");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			eventWriter.sendLogout();
			synchronized (Main.getPlayers()) {
				Main.getPlayers().remove(this);

			}
			synchronized (OsirisUpstreamHandler.getPlayerMap()) {
				OsirisUpstreamHandler.getPlayerMap().remove(channel);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Player(" + username + ":" + password + ")#" + getSlot();
	}

	/**
	 * Sets the channel.
	 * 
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * Gets the channel.
	 * 
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Sets the username as long.
	 * 
	 * @param usernameAsLong
	 *            the new username as long
	 */
	public void setUsernameAsLong(long usernameAsLong) {
		this.usernameAsLong = usernameAsLong;
	}

	/**
	 * Gets the username as long.
	 * 
	 * @return the username as long
	 */
	public long getUsernameAsLong() {
		return usernameAsLong;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the event writer.
	 * 
	 * @return the eventWriter
	 */
	public EventWriter getEventWriter() {
		return eventWriter;
	}

	/**
	 * Gets the inventory.
	 * 
	 * @return the inventory
	 */
	public ItemContainer getInventory() {
		return inventory;
	}

	/**
	 * Gets the bank.
	 * 
	 * @return the bank
	 */
	public Bank getBank() {
		return bank;
	}

	/**
	 * Gets the equipment.
	 * 
	 * @return the equipment
	 */
	public ItemContainer getEquipment() {
		return equipment;
	}

	/**
	 * Gets the appearance.
	 * 
	 * @return the appearance
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	public PlayerSettings getSettings() {
		return settings;
	}

	/**
	 * Adds the all items.
	 * 
	 * @param items
	 *            the items
	 */
	public void addAllItems(Item[] items) {
		for (Item item : items) {
			if (item != null)
				addItem(item);
		}
	}

	/**
	 * Adds the item.
	 * 
	 * @param item
	 *            the item
	 */
	public void addItem(Item item) {
		if (item.getAmount() == 0)
			return;
		if (!getInventory().add(item) && getPlayerStatus() < 2) {
			GroundItem ground = new GroundItem(item, this);
			GroundManager.getManager().dropItem(ground);
		}
	}

	/**
	 * Update animation indices.
	 */
	public void updateAnimationIndices() {
		if (getEquippedAttack() != null) {
			int[] indices = getEquippedAttack().getMobSprites();
			setStandAnimation(indices[0]);
			setWalkAnimation(indices[1]);
			setRunAnimation(indices[2]);
		} else {
			setStandAnimation(AppearanceBlock.STAND_INDICE);
			setWalkAnimation(AppearanceBlock.WALK_INDICE);
			setRunAnimation(AppearanceBlock.RUN_INDICE);
		}
		addUpdateBlock(new AppearanceBlock(this));
	}

	/**
	 * Equip item.
	 * 
	 * @param itemId
	 *            the item id
	 * @param slot
	 *            the slot
	 */
	public void equipItem(int itemId, int slot) {
		Item toEquip = inventory.getItem(slot);
		if (toEquip == null)
			return;
		int toEquipId = toEquip.getId();
		if (toEquipId != itemId)
			return;
		ItemDef def = ItemDef.forId(toEquip.getId());
		if (def == null)
			return;

		boolean equipmentScreen = getCurrentAction() != null && getCurrentAction() instanceof EquipmentScreenAction;
		if (getCurrentAction() != null) {
			if (!equipmentScreen)
				getCurrentAction().cancel();
		}
		int equipSlot = def.getItemType();
		int[] requirements = def.getRequirements();
		boolean meetsRequirements = true;
		if (requirements != null) {
			for (int i = 0; i < requirements.length; i++) {
				int level = skills.maxLevel(i);
				if (level < requirements[i]) {
					getEventWriter().sendMessage("You need level " + requirements[i] + " " + Skills.SKILL_NAMES[i] + " to equip this item!");
					meetsRequirements = false;
				}
			}
		}

		if (!meetsRequirements)
			return;
		Item equippedItem = equipment.getItem(equipSlot);
		Item shield = equipment.getItem(Settings.SLOT_SHIELD);
		Item weapon = equipment.getItem(Settings.SLOT_WEAPON);

		if (def.isTwoHanded()) {
			if (inventory.isFull() && shield != null) {
				getEventWriter().sendMessage("You don't have enough room in your inventory to do that!");
				return;
			}

			equipment.set(equipSlot, toEquip);
			int slotOfItem = -1;
			if (equippedItem != null)
				slotOfItem = inventory.getSlotById(equippedItem.getId());
			if (slotOfItem != -1 && ItemDef.forId(equippedItem.getId()).isStackable()) {
				inventory.set(slotOfItem, Item.create(equippedItem.getId(), equippedItem.getAmount() + inventory.getItem(slotOfItem).getAmount()));
				inventory.set(slot, null);
			} else
				inventory.set(slot, equippedItem);
			if (shield != null) {
				unequipItem(shield.getId(), Settings.SLOT_SHIELD);
			}
		} else if (def.isShield() && weapon != null) {
			equipment.set(equipSlot, toEquip);
			inventory.set(slot, equippedItem);
			if (ItemDef.forId(weapon.getId()).isTwoHanded()) {
				unequipItem(weapon.getId(), Settings.SLOT_WEAPON);
			}
			updateAnimationIndices();
		} else if (def.isStackable() && equippedItem != null && equippedItem.getId() == toEquipId) {
			equipment.set(equipSlot, Item.create(toEquipId, (toEquip.getAmount() + equippedItem.getAmount())));
			inventory.set(slot, null);
		} else {
			equipment.set(equipSlot, toEquip);
			int slotOfItem = -1;
			if (equippedItem != null)
				slotOfItem = inventory.getSlotById(equippedItem.getId());
			if (slotOfItem != -1 && ItemDef.forId(equippedItem.getId()).isStackable()) {
				inventory.set(slotOfItem, Item.create(equippedItem.getId(), equippedItem.getAmount() + inventory.getItem(slotOfItem).getAmount()));
				inventory.set(slot, null);
			} else
				inventory.set(slot, equippedItem);
		}
		bonuses.update();
		if (equipSlot == Settings.SLOT_WEAPON) {
			setSpecEnabled(false);
			refreshWeaponTab();
			getCombatManager().setAutoSpell(-1);
		}
		updateAnimationIndices();
		if (equipmentScreen)
			getEventWriter().sendInventoryInterface(149);
	}

	/**
	 * Unequip item.
	 * 
	 * @param itemId
	 *            the item id
	 * @param slot
	 *            the slot
	 */
	public void unequipItem(int itemId, int slot) {
		Item equippedItem = equipment.getItem(slot);
		if (equippedItem == null)
			return;
		if (equippedItem.getId() != itemId)
			return;
		boolean equipmentScreen = getCurrentAction() != null && getCurrentAction() instanceof EquipmentScreenAction;
		if (getCurrentAction() != null) {
			if (!equipmentScreen)
				getCurrentAction().cancel();
		}
		if (inventory.getEmptySlot() == -1 && !(ItemDef.forId(equippedItem.getId()).isStackable() && inventory.getSlotById(equippedItem.getId()) != -1)) {
			getEventWriter().sendMessage("You don't have enough room!");
			return;
		}
		equipment.set(slot, null);
		inventory.add(equippedItem.getId(), equippedItem.getAmount());
		bonuses.update();
		if (slot == Settings.SLOT_WEAPON) {
			setSpecEnabled(false);
			refreshWeaponTab();
			getCombatManager().setAutoSpell(-1);
		}
		updateAnimationIndices();
		if (equipmentScreen)
			getEventWriter().sendInventoryInterface(149);
	}

	/**
	 * Switch items.
	 * 
	 * @param fromSlot
	 *            the from slot
	 * @param toSlot
	 *            the to slot
	 */
	public void switchItems(int fromSlot, int toSlot) {
		inventory.move(fromSlot, toSlot);
	}

	/**
	 * Gets the local npcs.
	 * 
	 * @return the localNpcs
	 */
	public List<Npc> getLocalNpcs() {
		return localNpcs;
	}

	/**
	 * One button.
	 * 
	 * @return true, if successful
	 */
	public boolean oneButton() {
		return oneButton;
	}

	/**
	 * Sets the one button.
	 * 
	 * @param oneButton
	 *            the new one button
	 */
	public void setOneButton(boolean oneButton) {
		this.oneButton = oneButton;
	}

	/**
	 * Gets the items kept on death.
	 * 
	 * @return the items kept on death
	 */
	public ArrayList<Item> getItemsKeptOnDeath() {
		PriorityQueue<Item> allItems = new PriorityQueue<Item>(1, new Comparator<Item>() {
			@Override
			public int compare(Item a, Item b) {
				int difference = ItemDef.forId(b.getId()).getPrice() - ItemDef.forId(a.getId()).getPrice();
				return difference;
			}
		});
		Item[] items = new Item[equipment.getLength() + inventory.getLength()];
		System.arraycopy(equipment.getItems(), 0, items, 0, equipment.getLength());
		System.arraycopy(inventory.getItems(), 0, items, equipment.getLength(), inventory.getLength());
		for (Item item : items) {
			if (item == null)
				continue;
			if (ItemDef.forId(item.getId()).isTradeable())
				allItems.offer(Item.create(item.getId()));
		}
		ArrayList<Item> keptItems = new ArrayList<Item>();
		int itemsKept = 3;
		PrayerEffect salvage = getPrayers().get(Prayer.Type.SALVAGE);
		if (salvage != null)
			itemsKept += 1;
		while (keptItems.size() < itemsKept && allItems.size() > 0) {
			keptItems.add(allItems.poll());
		}
		return keptItems;
	}

	/**
	 * Deposit item.
	 * 
	 * @param itemSlot
	 *            the item slot
	 * @param itemN
	 *            the item n
	 */
	public void depositItem(int itemSlot, int itemN) {
		if (getCurrentAction() == null || !(getCurrentAction() instanceof BankAction))
			return;
		Item item = inventory.getItem(itemSlot);
		if (item == null)
			return;
		int itemId = item.getId();
		int hasAmount = getInventory().amountOfItem(item.getId());
		if (itemN > hasAmount)
			itemN = hasAmount;
		if (getBank().canFit(itemId, itemN) && getInventory().removeBySlot(itemSlot, itemN))
			getBank().add(itemId, itemN);
	}

	/**
	 * Withdraw item.
	 * 
	 * @param itemSlot
	 *            the item slot
	 * @param itemN
	 *            the item n
	 */
	public void withdrawItem(int itemSlot, int itemN) {
		if (getCurrentAction() == null || !(getCurrentAction() instanceof BankAction))
			return;
		int tab = bank.getTabBySlot(itemSlot);
		Item item;
		if (tab == -1)
			item = bank.getMain().getItem(itemSlot);
		else
			item = bank.getTabs().get(tab).getItem(itemSlot);
		if (item == null)
			return;
		int itemId = item.getId();
		int hasAmount = item.getAmount();
		if (itemN > hasAmount)
			itemN = hasAmount;
		int amountOfRoom = getInventory().countEmptySlots();
		if (getBank().withdrawNotes() && ItemDef.forId(itemId + 1).isNoted())
			itemId += 1;
		ItemDef def = ItemDef.forId(itemId);
		if ((!def.isStackable() && amountOfRoom < itemN) || (def.isStackable() && amountOfRoom == 0))
			itemN = amountOfRoom;
		if (getBank().remove(itemSlot, itemN)) {
			getInventory().add(itemId, itemN);
		}
	}

	/**
	 * Sets the hd.
	 * 
	 * @param isHd
	 *            the isHd to set
	 */
	public void setHd(boolean isHd) {
		this.isHd = isHd;
	}

	/**
	 * Checks if is hd.
	 * 
	 * @return the isHd
	 */
	public boolean isHd() {
		return isHd;
	}

	/**
	 * Sets the interface open.
	 * 
	 * @param openInterface
	 *            the new interface open
	 */
	public void setInterfaceOpen(int openInterface) {
		this.interfaceOpen = openInterface;
	}

	/**
	 * Gets the interface open.
	 * 
	 * @return the interface open
	 */
	public int getInterfaceOpen() {
		return interfaceOpen;
	}

	/**
	 * Send emote status.
	 */
	public void sendEmoteStatus() {
		int configBGPE = 0;
		int flag;
		for (int i = 0; i < Settings.BIG_GP_EMOTES.length; i++) {
			if (emoteStatus[Settings.BIG_GP_EMOTES[i]] == 1) {
				flag = 1 << i;
				configBGPE += flag;
			}
		}

		int configLGPE = 0;
		for (int i = 0; i < Settings.LITTLE_GP_EMOTES.length; i++) {
			if (emoteStatus[Settings.LITTLE_GP_EMOTES[i]] == 1) {
				flag = 1 << i;
				configLGPE += flag;
			}
		}

		/* Delayed until after loops so they all appear closer to same time */

		getEventWriter().sendConfig(802, configLGPE);

		getEventWriter().sendConfig(313, configBGPE);

		if (emoteStatus[Settings.GOBLIN_EMOTES[0]] == 1 && emoteStatus[Settings.GOBLIN_EMOTES[1]] == 1)
			getEventWriter().sendConfig(465, 7);

		if (emoteStatus[Settings.EMOTE_ZOMBIE_HAND] == 1)
			getEventWriter().sendConfig(1085, 12);
	}

	/**
	 * Sets the last region.
	 * 
	 * @param lastRegion
	 *            the lastRegion to set
	 */
	public void setLastRegion(Position lastRegion) {
		this.lastRegion = lastRegion;
	}

	/**
	 * Gets the last region.
	 * 
	 * @return the lastRegion
	 */
	public Position getLastRegion() {
		return lastRegion;
	}

	/**
	 * Gets the skills.
	 * 
	 * @return the skills
	 */
	public Skills getSkills() {
		return skills;
	}

	/**
	 * Gets the forum index.
	 * 
	 * @return the forum index
	 */
	public int getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * Gets the player updater.
	 * 
	 * @return the player updater
	 */
	public PlayerUpdater getPlayerUpdater() {
		return playerUpdater;
	}

	/**
	 * Gets the npc updater.
	 * 
	 * @return the npc updater
	 */
	public NpcUpdater getNpcUpdater() {
		return npcUpdater;
	}

	/**
	 * Gets the attack style.
	 * 
	 * @return the attack style
	 */
	public int getAttackStyle() {
		return attackStyle;
	}

	/**
	 * Sets the attack style.
	 * 
	 * @param style
	 *            the new attack style
	 */
	public void setAttackStyle(int style, boolean refresh) {
		if (this.equippedAttack != null && (equippedAttack.getTabId() == 77 || equippedAttack.getTabId() == 79)) {
			if (style == 1)
				style = equippedAttack.getTabId() == 76 ? 3 : 2;
			else if (style == 2)
				style = 1;
		}
		this.attackStyle = style;
		if (refresh)
			updateAttackStyle();
	}

	public void updateAttackStyle() {
		getEventWriter().sendConfig(43, attackStyle);
	}

	/**
	 * Sets the players run energy.
	 * 
	 * @param energy
	 *            The players run energy.
	 */
	@Override
	public void setEnergy(int energy, boolean send) {
		super.setEnergy(energy, send);
		if (send)
			eventWriter.sendEnergy();
	}

	/**
	 * Sets whether or not the run button is toggled.
	 * 
	 * @param runToggle
	 *            Whether or not the run button is toggled.
	 */
	@Override
	public void setRunToggle(boolean runToggle) {
		super.setRunToggle(runToggle);
		if (runToggle) {
			eventWriter.sendConfig(173, 1);
		} else {
			eventWriter.sendConfig(173, 0);
		}
	}

	/**
	 * Gets the player status.
	 * 
	 * @return the player status
	 */
	public int getPlayerStatus() {
		return playerStatus;
	}

	/**
	 * Sets the trade request.
	 * 
	 * @param player
	 *            the new trade request
	 */
	public void setTradeRequest(Player player) {
		this.tradeRequest = player;
	}

	/**
	 * Gets the trade request.
	 * 
	 * @return the trade request
	 */
	public Player getTradeRequest() {
		return tradeRequest;
	}

	/**
	 * Gets the bonuses.
	 * 
	 * @return the bonuses
	 */
	public PlayerBonuses getBonuses() {
		return bonuses;
	}

	/**
	 * Gets the friends.
	 * 
	 * @return the friends
	 */
	public Contacts getContacts() {
		return contacts;
	}

	/**
	 * Gets the trade open.
	 * 
	 * @return the trade open
	 */
	public Trade getTradeOpen() {
		Action action = getCurrentAction();
		if (action == null || !(action instanceof TradeAction))
			return null;
		return ((TradeAction) action).getTrade();
	}

	/**
	 * Equip weapon.
	 * 
	 * @param itemId
	 *            the item id
	 */
	public void equipWeapon(int itemId) {
		String weapon = null;
		if (itemId != -1)
			weapon = ItemDef.forId(equipment.getItem(Settings.SLOT_WEAPON).getId()).getName();
		equippedAttack = EquippedWeapon.getType(weapon);
		int amountOfStyles = equippedAttack.getSkills().length;
		if (this.attackStyle >= amountOfStyles)
			setAttackStyle((amountOfStyles - 1), true);
		getEventWriter().sendTab(/* player.isHd() ? 87 : */73, equippedAttack.getTabId());
		String wepName = weapon == null ? "Unarmed" : weapon;
		getEventWriter().sendString(wepName, equippedAttack.getTabId(), 0);
		updateSpecBar();
		updateAppearance();
	}

	/**
	 * Update spec bar.
	 */
	public void updateSpecBar() {
		if (equippedAttack == null)
			return;
		int childId = equippedAttack.getSpecButton();
		if (childId == -1)
			return;
		if (equipment.getItem(Settings.SLOT_WEAPON) == null || SpecialManager.specWeaponIndex(equipment.getItem(Settings.SLOT_WEAPON).getId()) == -1) {
			eventWriter.sendInterfaceConfig(equippedAttack.getTabId(), childId, true);
			return;
		}
		eventWriter.sendInterfaceConfig(equippedAttack.getTabId(), childId, false);
	}

	/**
	 * Update spec energy.
	 */
	public void updateSpecEnergy() {
		eventWriter.sendConfig(300, this.getSpecialEnergy());
	}

	/**
	 * Gets the equipped attack.
	 * 
	 * @return the equipped attack
	 */
	public EquippedWeapon getEquippedAttack() {
		return equippedAttack;
	}

	/**
	 * Gets the special energy.
	 * 
	 * @return the special energy
	 */
	public int getSpecialEnergy() {
		return specialEnergy;
	}

	/**
	 * Sets the special energy.
	 * 
	 * @param energy
	 *            the new special energy
	 */
	public void setSpecialEnergy(int energy, boolean send) {
		this.specialEnergy = energy;
		if (send)
			updateSpecEnergy();
	}

	/**
	 * Adjust special energy.
	 * 
	 * @param adjustment
	 *            the adjustment
	 * @return true, if successful
	 */
	public boolean adjustSpecialEnergy(int adjustment) {
		if (adjustment < 0 && (specialEnergy + adjustment < 0)) {
			return false;
		} else if (adjustment > 0 && (specialEnergy + adjustment > Settings.MAX_SPECIAL_ENERGY))
			specialEnergy = Settings.MAX_SPECIAL_ENERGY;
		else {
			if (adjustment > 0 || (adjustment < 0 && getPlayerStatus() != 2))
				specialEnergy += adjustment;
		}
		updateSpecEnergy();
		return true;
	}

	/**
	 * Special regain rate.
	 * 
	 * @return the int
	 */
	public int specialRegainRate() {
		return 3;
	}

	/**
	 * Checks if is spec enabled.
	 * 
	 * @return true, if is spec enabled
	 */
	public boolean isSpecEnabled() {
		return specEnabled;
	}

	/**
	 * Sets the spec enabled.
	 * 
	 * @param enabled
	 *            the new spec enabled
	 */
	public void setSpecEnabled(boolean enabled) {
		Item itemEquipped = equipment.getItem(Settings.SLOT_WEAPON);
		if (itemEquipped == null)
			return;
		int specIndex = SpecialManager.specWeaponIndex(itemEquipped.getId());
		if (enabled) {
			if (specIndex == -1)
				return;
			if ((SpecialManager.ENERGY_REQUIRED[specIndex] * 1000) > getSpecialEnergy()) {
				eventWriter.sendMessage("You do not have enough energy!");
				return;
			}
		}
		eventWriter.sendConfig(301, enabled ? 1 : 0);
		specEnabled = enabled;
	}

	/**
	 * Sets the stand animation.
	 * 
	 * @param standAnimation
	 *            the new stand animation
	 */
	public void setStandAnimation(int standAnimation) {
		this.standAnimation = standAnimation;
	}

	/**
	 * Gets the stand animation.
	 * 
	 * @return the stand animation
	 */
	public int getStandAnimation() {
		return standAnimation;
	}

	/**
	 * Sets the walk animation.
	 * 
	 * @param walkAnimation
	 *            the new walk animation
	 */
	public void setWalkAnimation(int walkAnimation) {
		this.walkAnimation = walkAnimation;
	}

	/**
	 * Gets the walk animation.
	 * 
	 * @return the walk animation
	 */
	public int getWalkAnimation() {
		return walkAnimation;
	}

	/**
	 * Sets the run animation.
	 * 
	 * @param runAnimation
	 *            the new run animation
	 */
	public void setRunAnimation(int runAnimation) {
		this.runAnimation = runAnimation;
	}

	/**
	 * Gets the run animation.
	 * 
	 * @return the run animation
	 */
	public int getRunAnimation() {
		return runAnimation;
	}

	/**
	 * Gets the exp rate.
	 * 
	 * @return the exp rate
	 */
	public double getExpRate() {
		return Settings.EXP_RATE;
	}

	public void setEmoteStatus(int[] emoteStatus, boolean send) {
		this.emoteStatus = emoteStatus;
		if (send)
			sendEmoteStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.Character#teleport(osiris.game.model.Position)
	 */
	@Override
	public void teleport(Position to) {
		getMovementQueue().reset();
		setPosition(to.clone());
		getUpdateFlags().flag(UpdateFlag.TELEPORTED);
	}

	public void setHeadIcon(int headIcon) {
		this.headIcon = headIcon;
		addUpdateBlock(new AppearanceBlock(this));
	}

	public int getHeadIcon() {
		return headIcon;
	}

	public void setSkullIcon(int skullIcon) {
		this.skullIcon = skullIcon;
		addUpdateBlock(new AppearanceBlock(this));
	}

	public int getSkullIcon() {
		return skullIcon;
	}

	public void setPlayerStatus(int status) {
		this.playerStatus = status;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setLoadedPosition(Position loaded) {
		this.loadedPosition = loaded;
	}

	public void refreshWeaponTab() {
		Item weapon = getEquipment().getItem(Settings.SLOT_WEAPON);
		if (weapon == null)
			equipWeapon(-1);
		else
			equipWeapon(weapon.getId());
	}

	public StopWatch getTimeoutTimer() {
		return timeoutTimer;
	}

	public XValue getXValue() {
		return xValue;
	}

	public void setXValue(XValue xValue) {
		this.xValue = xValue;
	}

	public void changeGender(int gender) {
		getAppearance().setGender(gender);
		updateAppearance();
	}

	public void setCurrentlyOpenDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}

	public Dialogue getCurrentlyOpenDialogue() {
		return dialogue;
	}

	public void transform(int toNpcId) {
		this.npcId = toNpcId;
		updateAnimationIndices();
	}

	public LinkedList<GroundItem> getGroundItems() {
		return localGroundItems;
	}

	public HashMap<Prayer.Type, PrayerEffect> getPrayers() {
		return prayers;
	}

	public double getPrayerDrainRate() {
		double prayerDrain = 0;
		for (PrayerEffect effect : getPrayers().values())
			if (effect != null)
				prayerDrain += effect.getPrayer().getDrainRate();
		return prayerDrain;
	}
}
