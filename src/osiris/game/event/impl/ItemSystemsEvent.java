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

import osiris.game.action.ItemActionListeners;
import osiris.game.action.impl.DropItemAction;
import osiris.game.action.impl.FoodAction;
import osiris.game.action.impl.PickupItemAction;
import osiris.game.action.item.ItemClickAction;
import osiris.game.event.GameEvent;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.io.ByteForm;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.ValueType;

// TODO: Auto-generated Javadoc

/**
 * The Class ItemSystemsEvent.
 * 
 * @author Boomer
 * 
 */
public class ItemSystemsEvent extends GameEvent {
	/**
	 * Instantiates a new game event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public ItemSystemsEvent(Player player, Packet packet) {
		super(player, packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.event.GameEvent#process()
	 */
	@Override
	public void process() throws Exception {
		int opcode = getPacket().getHeader().getOpcode();
		PacketReader reader = new PacketReader(getPacket());
		switch (opcode) {
		case 3:
			reader.readInt();
			int itemId = reader.readShort(ByteForm.LITTLE);
			int itemSlot = reader.readByte(false);
			getPlayer().equipItem(itemId, itemSlot);
			break;
		case 203:
			itemSlot = reader.readShort(ValueType.A, ByteForm.LITTLE);
			@SuppressWarnings("unused")
			int interfaceId = reader.readShort();
			reader.readShort();
			itemId = reader.readShort();
			getPlayer().unequipItem(itemId, itemSlot);
			break;
		case 220:
			reader.readByte(false);
			interfaceId = reader.readShort(false);
			reader.readByte(false);
			itemId = reader.readShort(false, ByteForm.LITTLE);
			itemSlot = reader.readShort(false, ValueType.A);
			if (getPlayer().getInventory().getItem(itemSlot) == null || getPlayer().getInventory().getItem(itemSlot).getId() != itemId)
				return;
			FoodAction.Food food = FoodAction.calculateFood(itemId);
			if (food != null) {
				new FoodAction(getPlayer(), food, itemSlot, itemId).run();
				return;
			}

			// Fire the action. Food is handled elsewhere.
			ItemActionListeners.fireItemAction(new ItemClickAction(getPlayer(), getPlayer().getInventory().getItem(itemSlot), itemSlot));
			break;
		case 167:
			int toId = reader.readShort(ValueType.A, ByteForm.LITTLE);
			reader.readByte();
			int fromId = reader.readShort(ValueType.A, ByteForm.LITTLE);
			reader.readShort();
			interfaceId = reader.readByte(false);
			reader.readByte();
			getPlayer().switchItems(fromId, toId);
			break;
		case 179:
			int interfaceTo = reader.readInt();
			int interfaceFrom = reader.readInt() >> 16;
			fromId = reader.readShort(false);
			toId = reader.readShort(false, ByteForm.LITTLE);
			if (toId == 65535)
				toId = 0;
			int tabIdTo = interfaceTo - 49938432;
			if (interfaceFrom == 763)
				getPlayer().getInventory().move(fromId, toId);
			else
				getPlayer().getBank().changeItemTab(fromId, tabIdTo, toId);
			break;

		case 211:
			reader.readInt();
			int slot = reader.readShort(false, ValueType.A, ByteForm.LITTLE);
			int id = reader.readShort(ByteForm.BIG);
			new DropItemAction(getPlayer(), slot, id).run();
			break;

		case 201:
			int y = reader.readShort(ValueType.A);
			int x = reader.readShort();
			id = reader.readShort(false, ValueType.A, ByteForm.LITTLE);
			Position position = new Position(x, y, getPlayer().getPosition().getZ());

			new PickupItemAction(getPlayer(), position, id).run();
			break;
		}
	}
}
