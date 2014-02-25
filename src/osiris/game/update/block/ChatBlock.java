package osiris.game.update.block;

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

import osiris.game.model.Player;
import osiris.game.update.UpdateBlock;
import osiris.io.PacketWriter;
import osiris.io.ValueType;

// TODO: Auto-generated Javadoc

/**
 * The chat update block.
 * 
 * @author Blake
 * 
 */
public class ChatBlock extends UpdateBlock {

	/**
	 * The effects.
	 */
	private final int effects;

	/**
	 * The player status.
	 */
	private final int playerStatus;

	/**
	 * The data.
	 */
	private final byte[] data;

	/**
	 * The encryption offset.
	 */
	private final int encryptionOffset;

	/**
	 * Instantiates a new chat block.
	 * 
	 * @param player
	 *            the player
	 * @param effects
	 *            the effects
	 * @param playerStatus
	 *            the player status
	 * @param data
	 *            the data
	 * @param encryptionOffset
	 *            the encryption offset
	 */
	public ChatBlock(Player player, int effects, int playerStatus, byte[] data, int encryptionOffset) {
		super(player, 0x8, 6);
		this.effects = effects;
		this.playerStatus = playerStatus;
		this.data = data;
		this.encryptionOffset = encryptionOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.update.UpdateBlock#append(osiris.io.PacketWriter)
	 */

	@Override
	public void append(PacketWriter writer) throws Exception {
		// TODO MUTING
		writer.writeShort(effects, ValueType.A);
		writer.writeByte(playerStatus, ValueType.C);
		writer.writeByte(encryptionOffset, ValueType.C);
		for (int i = 0; i < encryptionOffset; i++) {
			writer.writeByte(data[i]);
		}
	}
}
