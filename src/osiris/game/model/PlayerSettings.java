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

// TODO: Auto-generated Javadoc
/**
 * The Class PlayerSettings.
 * 
 * @author Boomer
 * @author samuraiblood2
 * 
 */
public class PlayerSettings {

	/** The auto retaliate. */
	private boolean autoRetaliate;

	/** The split private chat. */
	private boolean splitPrivateChat;

	/** The trade chat. */
	private int publicChat, privateChat, tradeChat;

	/** The chat effects. */
	private boolean chatEffects;

	/** The player. */
	private final Player player;

	/** The Constant CHAT_HIDE. */
	public static final int CHAT_ON = 0, CHAT_FRIENDS = 1, CHAT_OFF = 2, CHAT_HIDE = 3;

	/** The Constant CHAT_SLOT_TRADE. */
	public static final int CHAT_SLOT_PUBLIC = 0, CHAT_SLOT_PRIVATE = 1, CHAT_SLOT_TRADE = 2;

	/**
	 * Instantiates a new player settings.
	 * 
	 * @param player
	 *            the player
	 */
	public PlayerSettings(Player player) {
		this.player = player;
		setDefault();
		publicChat = CHAT_ON;
		privateChat = CHAT_ON;
		tradeChat = CHAT_ON;
	}

	/**
	 * Refresh.
	 */
	public void refresh() {
		if (splitPrivateChat) {
			player.getEventWriter().sendConfig(287, 0);
		} else {
			player.getEventWriter().runScript(83, "s");
			player.getEventWriter().sendConfig(287, 1);
		}
		player.getEventWriter().sendConfig(171, chatEffects ? 0 : 1);
		player.getEventWriter().sendConfig(172, autoRetaliate ? 0 : 1);
		player.getEventWriter().sendChatOptions(publicChat, privateChat, tradeChat);
		// player.showFrame(false);
	}

	/**
	 * Sets the default.
	 */
	public void setDefault() {
		autoRetaliate = false;
		splitPrivateChat = false;
		chatEffects = true;
	}

	/**
	 * Checks if is auto retaliate.
	 * 
	 * @return true, if is auto retaliate
	 */
	public boolean isAutoRetaliate() {
		return autoRetaliate;
	}

	/**
	 * Sets the auto retaliate.
	 * 
	 * @param autoRetaliate
	 *            the new auto retaliate
	 */
	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}

	/**
	 * Checks if is split private chat.
	 * 
	 * @return true, if is split private chat
	 */
	public boolean isSplitPrivateChat() {
		return splitPrivateChat;
	}

	/**
	 * Sets the split private chat.
	 * 
	 * @param splitPrivateChat
	 *            the new split private chat
	 */
	public void setSplitPrivateChat(boolean splitPrivateChat) {
		this.splitPrivateChat = splitPrivateChat;
	}

	/**
	 * Checks if is chat effects.
	 * 
	 * @return true, if is chat effects
	 */
	public boolean isChatEffects() {
		return chatEffects;
	}

	/**
	 * Sets the chat effects.
	 * 
	 * @param chatEffects
	 *            the new chat effects
	 */
	public void setChatEffects(boolean chatEffects) {
		this.chatEffects = chatEffects;
	}

	/**
	 * Sets the chat options.
	 * 
	 * @param publicChat
	 *            the public chat
	 * @param privateChat
	 *            the private chat
	 * @param tradeChat
	 *            the trade chat
	 */
	public void setChatOptions(int publicChat, int privateChat, int tradeChat) {
		this.publicChat = publicChat;
		this.privateChat = privateChat;
		this.tradeChat = tradeChat;
	}

	/**
	 * Gets the chat options.
	 * 
	 * @return the chat options
	 */
	public int[] getChatOptions() {
		return new int[] { publicChat, privateChat, tradeChat };
	}

	/**
	 * Gets the public chat.
	 * 
	 * @return the public chat
	 */
	public int getPublicChat() {
		return publicChat;
	}
}
