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

import org.jboss.netty.channel.Channel;

import osiris.Main;
import osiris.data.PlayerData;
import osiris.game.model.Player;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.PacketWriter;
import osiris.net.ProtocolException;
import osiris.net.codec.OsirisPacketDecoder;
import osiris.util.Utilities;

/**
 * Processes a login request.
 * 
 * @author Blake
 * 
 */
public class LoginRequestEvent extends ServiceRequestEvent {

	/**
	 * Instantiates a new login request event.
	 * 
	 * @param channel
	 *            the channel
	 * @param packet
	 *            the packet
	 */
	public LoginRequestEvent(Channel channel, Packet packet) {
		super(channel, packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.event.impl.ServiceRequestEvent#process()
	 */

	@Override
	public void process() throws ProtocolException {
		PacketReader reader = new PacketReader(getPacket());
		int clientVersion = reader.readInt();
		if (clientVersion != 508) {
			throw new ProtocolException("invalid client version: " + clientVersion);
		}
		reader.readByte(); // memory version
		reader.readInt();
		for (int i = 0; i < 24; i++) {
			int cacheIdx = reader.readByte();
			if (cacheIdx == 0) {
				throw new ProtocolException("zero-value cache idx");
			}
		}
		reader.readString(); // settings string
		for (int i = 0; i < 29; i++) {
			int crcKey = reader.readInt();
			if (crcKey == 0 && i != 0) {
				throw new ProtocolException("zero-value CRC key");
			}
		}

		/*
		 * XXX: We changed value 10 to 0xf to prevent alien clients. -Blake
		 */
		int rsaOpcode = reader.readByte();
		if (rsaOpcode != 10) {
			// standard detail
			rsaOpcode = reader.readByte();
		}

		// If we're still not 0xf, RSA decoding failed.
		if (rsaOpcode != 10) {
			throw new ProtocolException("invalid RSA opcode: " + rsaOpcode);
		}
		reader.readLong(); // client half
		reader.readLong(); // server half

		// Player credentials
		long usernameAsLong = reader.readLong();
		String username = Utilities.longToString(usernameAsLong).replaceAll("_", " ");
		String password = reader.readString();

		// Register a new player with the world
		Player player = new Player(getChannel(), username, password);
		player.setUsernameAsLong(usernameAsLong);
		int returnCode = PlayerData.load(username, password, player);
		int status = player.getPlayerStatus();
		if (returnCode == 2) {
			synchronized (Main.getPlayers()) {
				for (Player other : Main.getPlayers()) {
					if (other.getUsername().equalsIgnoreCase(username)) {
						returnCode = 5;
					}
				}
				if (returnCode == 2) {
					Main.getPlayers().add(player);
				}
			}
		}

		// Send the response.
		PacketWriter writer = new PacketWriter();
		writer.writeByte(returnCode); // return code
		writer.writeByte(status); // player rights
		writer.writeByte(0); // idk
		writer.writeByte(0); // idk
		writer.writeByte(0); // idk
		writer.writeByte(1); // idk
		writer.writeShort(player.getSlot()); // slot
		writer.writeByte(0); // idk
		getChannel().write(writer.getPacket());
		getChannel().getPipeline().replace("decoder", "decoder", new OsirisPacketDecoder());
		if (returnCode == 2) {
			player.login();
		}
	}

}
