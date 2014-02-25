package osiris.io;

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

import java.awt.Color;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.game.model.WorldObject;
import osiris.game.model.item.Item;
import osiris.game.model.item.ItemContainer;
import osiris.io.PacketHeader.LengthType;
import osiris.util.LandscapeKeys;
import osiris.util.Settings;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * A class that writes game events to packets.
 * 
 * @author Blake
 * @author Boomer
 * @author samuraiblood2
 * 
 */
public class EventWriter {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Instantiates a new event writer.
	 * 
	 * @param player
	 *            the player
	 */
	public EventWriter(Player player) {
		this.player = player;
	}

	/**
	 * Sends an Npc's model to an interface.
	 * 
	 * @param id
	 *            the interface id.
	 * @param child
	 *            the child id.
	 * @param npc
	 *            the npc id.
	 */
	public void sendInterfaceNpc(int id, int child, int npc) {
		PacketWriter writer = new PacketWriter(new PacketHeader(6));
		writer.writeShort(id, ByteForm.LITTLE);
		writer.writeShort(child, ByteForm.LITTLE);
		writer.writeShort(npc, ByteForm.LITTLE);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send's an animation to an interface.
	 * 
	 * @param id
	 *            the interface id.
	 * @param child
	 *            the child id.
	 * @param emote
	 *            the emote id.
	 */
	public void sendInterfaceAnimation(int id, int child, int emote) {
		PacketWriter writer = new PacketWriter(new PacketHeader(245));
		writer.writeShort(id, ByteForm.LITTLE);
		writer.writeShort(child, ByteForm.LITTLE);
		writer.writeShort(emote);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send chat options.
	 * 
	 * @param publicChat
	 *            the public chat
	 * @param privateChat
	 *            the private chat
	 * @param tradeChat
	 *            the trade chat
	 */
	public void sendChatOptions(int publicChat, int privateChat, int tradeChat) {
		PacketWriter writer = new PacketWriter(new PacketHeader(186));
		writer.writeByte(publicChat);
		writer.writeByte(privateChat);
		writer.writeByte(tradeChat);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Spawn gfx.
	 * 
	 * @param id
	 *            the id
	 * @param height
	 *            the height
	 * @param position
	 *            the position
	 */
	public void spawnGfx(int id, int height, Position position) {
		sendPosition(position);
		PacketWriter writer = new PacketWriter(new PacketHeader(248));
		writer.writeByte(0);
		writer.writeShort(id);
		writer.writeByte(height);
		writer.writeShort(0);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send interface item.
	 * 
	 * @param inter
	 *            the inter
	 * @param child
	 *            the child
	 * @param size
	 *            the size
	 * @param item
	 *            the item
	 */
	public void sendInterfaceItem(int inter, int child, int size, int item) {
		PacketWriter writer = new PacketWriter(new PacketHeader(35));
		writer.writeInt(((inter * 65536) + child), ByteForm.INVERSE_MIDDLE);
		writer.writeInt(size, ByteForm.LITTLE);
		writer.writeShort(item, ValueType.A, ByteForm.LITTLE);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send projectile.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param graphic
	 *            the graphic
	 * @param angle
	 *            the angle
	 * @param startHeight
	 *            the start height
	 * @param endHeight
	 *            the end height
	 * @param speed
	 *            the speed
	 * @param lockOn
	 *            the lock on
	 */
	public void sendProjectile(Position from, Position to, int graphic, int angle, int startHeight, int endHeight, int speed, Character lockOn) {
		sendPosition(from, -3, -2);
		PacketWriter writer = new PacketWriter(new PacketHeader(112));
		writer.writeByte((byte) angle);
		int offsetX = (from.getX() - to.getX()) * -1;
		int offsetY = (from.getY() - to.getY()) * -1;
		writer.writeByte((byte) offsetX);
		writer.writeByte((byte) offsetY);
		writer.writeShort((lockOn instanceof Player ? lockOn.getSlot() : lockOn.getSlot()), ValueType.C, ByteForm.BIG);
		writer.writeShort(graphic);
		writer.writeByte((byte) startHeight);
		writer.writeByte((byte) endHeight);
		writer.writeShort(51);
		writer.writeShort(speed);
		writer.writeByte((byte) 16);
		writer.writeByte((byte) 64);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send destroy ground item.
	 * 
	 * @param item
	 *            the item
	 * @param position
	 *            the position
	 */
	public void sendDestroyGroundItem(Item item, Position position) {
		int id = item.getId();
		sendPosition(position);
		PacketWriter writer = new PacketWriter(new PacketHeader(201));
		writer.writeByte(0);
		writer.writeShort(id);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send create ground item.
	 * 
	 * @param item
	 *            the item
	 * @param position
	 *            the position
	 */
	public void sendCreateGroundItem(Item item, Position position) {
		sendPosition(position);
		PacketWriter writer = new PacketWriter(new PacketHeader(25));
		writer.writeShort(item.getAmount(), ValueType.A, ByteForm.LITTLE);
		writer.writeByte(0);
		writer.writeShort(item.getId(), ValueType.A, ByteForm.LITTLE);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Restore gameframe.
	 */
	public void restoreGameframe() {
		for (int b = 16; b <= 21; b++)
			sendInterfaceConfig(548, b, false);
		for (int a = 32; a <= 38; a++)
			sendInterfaceConfig(548, a, false);
		sendInterfaceConfig(548, 14, false);
		sendInterfaceConfig(548, 31, false);
		sendInterfaceConfig(548, 63, false);
		sendInterfaceConfig(548, 72, false);
	}

	/**
	 * Send friends status.
	 * 
	 * @param i
	 *            the i
	 */
	public void sendFriendsStatus(int i) {
		PacketWriter writer = new PacketWriter(new PacketHeader(115));
		writer.writeByte(i);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send a private message.
	 * 
	 * @param name
	 *            the name
	 * @param message
	 *            the message
	 */
	public void sendSentPrivateMessage(long name, String message) {
		byte[] bytes = new byte[message.length()];
		Utilities.encryptPlayerChat(bytes, 0, 0, message.length(), message.getBytes());

		PacketWriter writer = new PacketWriter(new PacketHeader(89, LengthType.VARIABLE_BYTE));
		writer.writeLong(name);
		writer.writeByte(message.length());
		writer.writeBytes(bytes);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send a received private message.
	 * 
	 * @param name
	 *            the name
	 * @param rights
	 *            the rights
	 * @param message
	 *            the message
	 */
	public void sendReceivedPrivateMessage(long name, int rights, String message) {
		int messages = player.getContacts().getMessages();
		byte[] bytes = new byte[message.length() + 1];
		bytes[0] = (byte) message.length();
		Utilities.encryptPlayerChat(bytes, 0, 1, message.length(), message.getBytes());

		PacketWriter writer = new PacketWriter(new PacketHeader(178, LengthType.VARIABLE_BYTE));
		writer.writeLong(name);
		writer.writeShort(1);
		writer.writeBytes(new byte[] { (byte) ((messages << 16) & 0xFF), (byte) ((messages << 8) & 0xFF), (byte) (messages & 0xFF) });
		writer.writeByte(rights);
		writer.writeBytes(bytes);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send a friend.
	 * 
	 * @param name
	 *            the name
	 * @param world
	 *            the world
	 */
	public void sendFriend(long name, int world) {
		PacketWriter writer = new PacketWriter(new PacketHeader(154, LengthType.VARIABLE_BYTE));
		writer.writeLong(name);
		writer.writeShort(world);
		writer.writeByte(1);
		if (world == 0) {
			writer.writeString("Offline");
		} else if (world == 1) {
			writer.writeString("<col=" + Color.GREEN.hashCode() + ">Online");
		}
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send a list of ignores.
	 * 
	 * @param names
	 *            the names
	 */
	public void sendIgnores(Long[] names) {
		PacketWriter writer = new PacketWriter(new PacketHeader(240, LengthType.VARIABLE_SHORT));
		for (int i = 0; i < names.length; i++) {
			writer.writeLong(names[i]);
		}
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send energy.
	 */
	public void sendEnergy() {
		PacketWriter writer = new PacketWriter(new PacketHeader(99));
		writer.writeByte(player.getEnergy());
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Sets the object.
	 * 
	 * @param object
	 *            the new object
	 */
	public void setObject(WorldObject object) {
		sendPosition(object.getObjectPosition());
		PacketWriter writer = new PacketWriter(new PacketHeader(30));
		writer.writeShort(object.getObjectID(), ByteForm.LITTLE);
		writer.writeByte(0, ValueType.A);
		writer.writeByte((object.getObjectType() << 2) + (object.getObjectFace() & 3), ValueType.C);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send position.
	 * 
	 * @param position
	 *            the position
	 */
	public void sendPosition(Position position) {
		sendPosition(position, 0, 0);
	}

	/**
	 * Send position.
	 * 
	 * @param position
	 *            the position
	 * @param horizontalOffset
	 *            the horizontal offset
	 * @param verticalOffset
	 *            the vertical offset
	 */
	public void sendPosition(Position position, int horizontalOffset, int verticalOffset) {
		PacketWriter writer = new PacketWriter(new PacketHeader(177));
		writer.writeByte(position.getLocalY(player.getLastRegion()) + verticalOffset);
		writer.writeByte(position.getLocalX(player.getLastRegion()) + horizontalOffset, ValueType.S);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send player option.
	 * 
	 * @param option
	 *            the option
	 * @param slot
	 *            the slot
	 * @param top
	 *            the top
	 */
	public void sendPlayerOption(String option, int slot, boolean top) {
		PacketWriter writer = new PacketWriter(new PacketHeader(252, LengthType.VARIABLE_BYTE));
		writer.writeByte(top ? 1 : 0, ValueType.C);
		writer.writeString(option);
		writer.writeByte(slot, ValueType.C);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send a window pane.
	 * 
	 * @param id
	 *            the id
	 */
	public void sendWindowPane(int id) {
		PacketWriter writer = new PacketWriter(new PacketHeader(239));
		writer.writeShort(id);
		writer.writeByte(0, ValueType.A);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Sets a tab to an interface tab.
	 * 
	 * @param tabId
	 *            the tab id
	 * @param childId
	 *            the child id
	 */
	public void sendTab(int tabId, int childId) {
		// TODO HD
		sendInterface(1, childId == 137 ? 752 : 548, tabId, childId);
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            the message
	 */
	public void sendMessage(String message) {
		PacketWriter writer = new PacketWriter(new PacketHeader(218, LengthType.VARIABLE_BYTE));
		writer.writeString(message);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Sends an interface.
	 * 
	 * @param showId
	 *            the show id
	 * @param windowId
	 *            the window id
	 * @param interfaceId
	 *            the interface id
	 * @param childId
	 *            the child id
	 */
	public void sendInterface(int showId, int windowId, int interfaceId, int childId) {
		PacketWriter writer = new PacketWriter(new PacketHeader(93));
		writer.writeShort(childId);
		writer.writeByte(showId, ValueType.A);
		writer.writeShort(windowId);
		writer.writeShort(interfaceId);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send close chatbox interface.
	 */
	public void sendCloseChatboxInterface() {
		PacketWriter packet = new PacketWriter(new PacketHeader(246));
		packet.writeShort(752);
		packet.writeShort(12);
		player.getChannel().write(packet.getPacket());
		player.setInterfaceOpen(-1);
	}

	/**
	 * Send chatbox interface.
	 * 
	 * @param childId
	 *            the child id
	 */
	public void sendChatboxInterface(int childId) {
		sendInterface(0, 752, 12, childId);
		player.setInterfaceOpen(752);
	}

	/**
	 * Send close interface.
	 */
	public void sendCloseInterface() {

		sendInterface(1, 548, 8, 56);
		player.setInterfaceOpen(-1);
	}

	/**
	 * Send close inventory interface.
	 */
	public void sendCloseInventoryInterface() {
		/*
		 * if(player.isHd()) { sendInterfaceConfig(746, 71, true); } else {
		 */
		sendInterfaceConfig(548, 71, true);
		// }
	}

	/**
	 * Send interface.
	 * 
	 * @param id
	 *            the id
	 */
	public void sendInterface(int id) {
		sendCloseInterface();
		/*
		 * if(player.isHd()) { sendInterface(0, 746, isInventoryInterface ? 4 :
		 * 6, id); // 3 norm, 4 makes bank work, 6 makes help work
		 * sendInterface(0, 746, 8, id); } else {
		 */
		sendInterface(0, 548, 8, id);
		player.setInterfaceOpen(id);
		// }
	}

	/**
	 * Send inventory interface.
	 * 
	 * @param childId
	 *            the child id
	 */
	public void sendInventoryInterface(int childId) {
		/*
		 * if(player.isHd()) { sendInterfaceConfig(746, 71, false); } else {
		 */
		// }
		/*
		 * if(player.isHd()) { sendInterface(0, 746, 71, childId); } else {
		 */
		sendInterface(0, 548, 69, childId);
		updateTabs(false);

		// }
	}

	/**
	 * Send bank options.
	 */
	public void sendBankOptions() {
		PacketWriter writer = new PacketWriter(new PacketHeader(223));
		writer.writeShort(496);
		writer.writeShort(0, ValueType.A, ByteForm.LITTLE);
		writer.writeShort(73, ByteForm.LITTLE);
		writer.writeShort(762, ByteForm.LITTLE);
		writer.writeShort(1278, ByteForm.LITTLE);
		writer.writeShort(20, ByteForm.LITTLE);
		player.getChannel().write(writer.getPacket());
		/*
		 * Object[] inventoryOptions = new Object[]{"", "", "", "", "Sell X",
		 * "Sell 10", "Sell 5", "Sell 1", "Value", -1, 0, 7, 4, 0, (149 << 16)};
		 * runScript(150, inventoryOptions, "IviiiIsssssssss");
		 * setAccessMask(1278, 0, 149, 0, 28);
		 */
		writer = new PacketWriter(new PacketHeader(223));
		writer.writeShort(27);
		writer.writeShort(0, ValueType.A, ByteForm.LITTLE);
		writer.writeShort(0, ByteForm.LITTLE);
		writer.writeShort(763, ByteForm.LITTLE);
		writer.writeShort(1150, ByteForm.LITTLE);
		writer.writeShort(18, ByteForm.LITTLE);
		player.getChannel().write(writer.getPacket());
		sendString(Utilities.toProperCase(player.getUsername()) + "'s Bank", 762, 24);
		runScript(1451, "");
	}

	/**
	 * Send interface config.
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @param childId
	 *            the child id
	 * @param set
	 *            the set
	 */
	public void sendInterfaceConfig(int interfaceId, int childId, boolean set) {
		PacketWriter writer = new PacketWriter(new PacketHeader(59));
		writer.writeByte(set ? 1 : 0, ValueType.C);
		writer.writeShort(childId);
		writer.writeShort(interfaceId);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send config.
	 * 
	 * @param id
	 *            the id
	 * @param value
	 *            the value
	 */

	public void sendConfig(int id, int value) {
		if (value < 128) {
			sendConfig1(id, value);
		} else {
			sendConfig2(id, value);
		}
	}

	/**
	 * Send config1.
	 * 
	 * @param id
	 *            the id
	 * @param value
	 *            the value
	 */
	public void sendConfig1(int id, int value) {
		PacketWriter writer = new PacketWriter(new PacketHeader(100));
		writer.writeShort(id, ValueType.A);
		writer.writeByte(value, ValueType.A);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send config2.
	 * 
	 * @param id
	 *            the id
	 * @param value
	 *            the value
	 */
	public void sendConfig2(int id, int value) {
		PacketWriter writer = new PacketWriter(new PacketHeader(161));
		writer.writeShort(id);
		writer.writeInt(value, ByteForm.MIDDLE);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send landscape.
	 */
	public void sendLandscape() {
		PacketWriter writer = new PacketWriter(new PacketHeader(142, LengthType.VARIABLE_SHORT));
		boolean forceSend = true;
		if ((((player.getPosition().getRegionX() / 8) == 48) || ((player.getPosition().getRegionX() / 8) == 49)) && ((player.getPosition().getRegionY() / 8) == 48)) {
			forceSend = false;
		}
		if (((player.getPosition().getRegionX() / 8) == 48) && ((player.getPosition().getRegionY() / 8) == 148)) {
			forceSend = false;
		}
		writer.writeShort(player.getPosition().getRegionX(), ValueType.A);
		writer.writeShort(player.getPosition().getLocalY(), ValueType.A, ByteForm.LITTLE);
		writer.writeShort(player.getPosition().getLocalX(), ValueType.A);
		for (int xCalc = (player.getPosition().getRegionX() - 6) / 8; xCalc <= ((player.getPosition().getRegionX() + 6) / 8); xCalc++) {
			for (int yCalc = (player.getPosition().getRegionY() - 6) / 8; yCalc <= ((player.getPosition().getRegionY() + 6) / 8); yCalc++) {
				int regionId = yCalc + (xCalc << 8);
				if (forceSend || ((yCalc != 49) && (yCalc != 149) && (yCalc != 147) && (xCalc != 50) && ((xCalc != 49) || (yCalc != 47)))) {
					int[] keys = LandscapeKeys.getKeys(regionId);
					if (keys == null) {
						player.teleport(new Position());
						keys = new int[4];
						for (int i = 0; i < keys.length; i++) {
							keys[i] = 0;
						}
					}
					for (int key : keys) {
						writer.writeInt(key);
					}
				}
			}
		}
		writer.writeByte(player.getPosition().getZ(), ValueType.C);
		writer.writeShort(player.getPosition().getRegionY());
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send items.
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @param childId
	 *            the child id
	 * @param type
	 *            the type
	 * @param container
	 *            the container
	 */
	public void sendItems(int interfaceId, int childId, int type, ItemContainer container) {
		sendItems(interfaceId, childId, type, container.getItems());
	}

	/**
	 * Send items.
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @param childId
	 *            the child id
	 * @param type
	 *            the type
	 * @param items
	 *            the items
	 */
	public void sendItems(int interfaceId, int childId, int type, Item[] items) {
		PacketWriter writer = new PacketWriter(new PacketHeader(255, LengthType.VARIABLE_SHORT));
		writer.writeShort(interfaceId);
		writer.writeShort(childId);
		writer.writeShort(type);
		writer.writeShort(items.length);
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
			int id, amt;
			if (item == null) {
				id = -1;
				amt = 0;
			} else {
				id = item.getId();
				amt = item.getAmount();
			}
			if (amt > 254) {
				writer.writeByte(255, ValueType.S);
				writer.writeInt(amt, ByteForm.INVERSE_MIDDLE);
			} else {
				writer.writeByte(amt, ValueType.S);
			}
			writer.writeShort(id + 1, ByteForm.LITTLE);
		}
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Run script.
	 * 
	 * @param id
	 *            the id
	 * @param val
	 *            the val
	 */
	public void runScript(int id, String val) {
		PacketWriter writer = new PacketWriter(new PacketHeader(152, LengthType.VARIABLE_SHORT));
		writer.writeString(val);
		writer.writeInt(id);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Run script.
	 * 
	 * @param id
	 *            the id
	 * @param o
	 *            the o
	 * @param valstring
	 *            the valstring
	 */
	public void runScript(int id, Object[] o, String valstring) {
		if (valstring.length() != o.length) {
			throw new IllegalArgumentException("Argument array size mismatch");
		}

		PacketWriter writer = new PacketWriter(new PacketHeader(152, LengthType.VARIABLE_SHORT));
		writer.writeString(valstring);
		int j = 0;
		for (int i = (valstring.length() - 1); i >= 0; i--) {
			if (valstring.charAt(i) == 115) {
				writer.writeString((String) o[j]);
			} else {
				writer.writeInt((Integer) o[j]);
			}
			j++;
		}
		writer.writeInt(id);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Sets the access mask.
	 * 
	 * @param set
	 *            the set
	 * @param window
	 *            the window
	 * @param inter
	 *            the inter
	 * @param off
	 *            the off
	 * @param len
	 *            the len
	 */
	public void setAccessMask(int set, int window, int inter, int off, int len) {
		PacketWriter writer = new PacketWriter(new PacketHeader(223));
		writer.writeShort(len);
		writer.writeShort(off, ValueType.A, ByteForm.LITTLE);
		writer.writeShort(window, ByteForm.LITTLE);
		writer.writeShort(inter, ByteForm.LITTLE);
		writer.writeShort(set, ByteForm.LITTLE);
		writer.writeShort(0, ByteForm.LITTLE);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send string.
	 * 
	 * @param string
	 *            the string
	 * @param interfaceId
	 *            the interface id
	 * @param childId
	 *            the child id
	 */
	public void sendString(String string, int interfaceId, int childId) {
		int sSize = string.length() + 5;
		PacketWriter writer = new PacketWriter(new PacketHeader(179));
		writer.writeByte((byte) (sSize / 256));
		writer.writeByte((byte) (sSize % 256));
		writer.writeString(string);
		writer.writeShort(childId);
		writer.writeShort(interfaceId);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send logout.
	 */
	public void sendLogout() {
		player.getChannel().write(new PacketWriter(new PacketHeader(104)).getPacket()).addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				channelFuture.getChannel().close();
			}
		});
	}

	/**
	 * Send skill level.
	 * 
	 * @param skill
	 *            the skill
	 */
	public void sendSkillLevel(int skill) {
		PacketWriter writer = new PacketWriter(new PacketHeader(217));
		writer.writeByte((int) Math.ceil(player.getSkills().currentLevel(skill)), ValueType.C);
		writer.writeInt((int) player.getSkills().getExp(skill), ByteForm.INVERSE_MIDDLE);
		writer.writeByte(skill, ValueType.C);
		player.getChannel().write(writer.getPacket());
	}

	/**
	 * Send bonus.
	 * 
	 * @param bonuses
	 *            the bonuses
	 */
	public void sendBonus(int[] bonuses) {
		int id = 35;
		for (int i = 0; i < (bonuses.length - 1); i++) {
			sendString((Settings.BONUSES[i] + ": " + (bonuses[i] > 0 ? "+" : "") + bonuses[i]), 667, id++);
			if (id == 45) {
				sendString(" ", 667, id++);
				id = 47;
			}
		}
	}

	/**
	 * Removes the side locking interface.
	 */
	public void removeSideLockingInterface() {
		sendInterface(1, 548, 69, 56);
		updateTabs(true);
		sendCloseInterface();
	}

	/**
	 * Update tabs.
	 * 
	 * @param shown
	 *            the shown
	 */
	public void updateTabs(boolean shown) {
		for (int b = 16; b <= 21; b++) {
			sendInterfaceConfig(548, b, !shown);
		}

		for (int a = 32; a <= 38; a++) {
			sendInterfaceConfig(548, a, !shown);
		}

		sendInterfaceConfig(548, 14, !shown);
		sendInterfaceConfig(548, 31, !shown);
		sendInterfaceConfig(548, 63, !shown);

		sendInterfaceConfig(548, 72, !shown);
	}

	/**
	 * Open side locking interface.
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @param inventoryId
	 *            the inventory id
	 */
	public void openSideLockingInterface(int interfaceId, int inventoryId) {
		sendInterface(interfaceId);
		sendInventoryInterface(inventoryId);
	}
}
