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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jboss.netty.channel.Channel;

import osiris.data.sql.SqlManager;
import osiris.game.event.GameEvent;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.PacketWriter;
import osiris.net.codec.LoginRequestDecoder;
import osiris.net.codec.UpdateRequestDecoder;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * Handles a service request.
 * 
 * @author Boomer
 * @author Blake
 */
public class ServiceRequestEvent extends GameEvent {

	/**
	 * The channel.
	 */
	private final Channel channel;

	/**
	 * Instantiates a new service request event.
	 * 
	 * @param channel
	 *            the channel
	 * @param packet
	 *            the packet
	 */
	public ServiceRequestEvent(Channel channel, Packet packet) {
		super(null, packet);
		this.channel = channel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.event.GameEvent#process()
	 */

	@Override
	public void process() throws Exception {
		PacketReader reader = new PacketReader(getPacket());
		int opcode = reader.readByte();
		if (opcode == LOGIN_REQUEST) {
			reader.readByte();
			PacketWriter writer = new PacketWriter();
			long serverHalf = new SecureRandom().nextLong();
			writer.writeByte(0);
			writer.writeLong(serverHalf);
			channel.write(writer.getPacket());
			channel.getPipeline().replace("decoder", "decoder", new LoginRequestDecoder());
		}
		if (opcode == UPDATE_REQUEST) {
			PacketWriter writer = new PacketWriter();
			int clientVersion = reader.readInt();
			if (clientVersion == 508) {
				writer.writeByte(UpdateRequestEvent.STATUS_OK);
				channel.write(writer.getPacket());
				channel.getPipeline().replace("decoder", "decoder", new UpdateRequestDecoder());
			} else {
				writer.writeByte(UpdateRequestEvent.STATUS_OUT_OF_DATE);
				channel.write(writer.getPacket());
				// TODO: implement JAGGRAB to update the client
			}
		}
		if (opcode == SERVER_REQUEST) {
			PacketWriter writer = new PacketWriter();
			File file = new File("./bin/dist/osiris.jar");
			if (file.exists()) {
				writer.writeByte(1);
				DataInputStream in = new DataInputStream(new FileInputStream(file));
				while (in.available() > 0) {
					writer.writeByte(in.readByte());
				}
			} else {
				writer.writeByte(0);
			}
			channel.write(writer.getPacket());
		}
		if (opcode == ACCOUNT_CREATION_BIRTHDAY) {
			PacketWriter writer = new PacketWriter();
			writer.writeByte(2);
			channel.write(writer.getPacket());
		}
		if (opcode == ACCOUNT_CREATION_USERNAME) {
			int returnCode = 2;
			String name = Utilities.longToString(reader.readLong());
			if (name == null)
				returnCode = 22;
			else
				name = name.toLowerCase().replaceAll("_", " ").trim();
			ResultSet results = SqlManager.manager.getResults(SqlManager.manager.serverPlayerResultsStatement, name);
			if (results.next())
				returnCode = 20;
			PacketWriter writer = new PacketWriter();
			writer.writeByte(returnCode);
			channel.write(writer.getPacket());
		}
		if (opcode == ACCOUNT_CREATION_PASSWORD) {
			int returnCode = 2;
			for (int i = 0; i < 3; i++)
				reader.readByte();
			reader.readShort();
			int revision = reader.readShort();
			if (revision != 508)
				returnCode = 37;
			String name = Utilities.longToString(reader.readLong()).toLowerCase().replaceAll("_", " ").trim();
			reader.readInt();
			String password = reader.readString();
			reader.readInt();
			reader.readShort();
			/* Birthday & Location */
			reader.readByte();
			reader.readByte();
			reader.readInt();
			reader.readShort();
			reader.readShort();
			/* End of Birthday & Location */
			reader.readInt();
			if (name == null)
				returnCode = 20;
			else if (password == null)
				returnCode = 4;
			else if (password.length() < 5 || password.length() > 20)
				returnCode = 32;
			else if (password.contains(name))
				returnCode = 34;
			/*
			 * else if (!validPassword(password) returnCode = 31;
			 */
			ResultSet results = SqlManager.manager.getResults(SqlManager.manager.serverPlayerResultsStatement, name);
			if (results.next())
				returnCode = 20;

			if (returnCode == 2) {
				int salt = Utilities.generateSalt();
				PreparedStatement statement = SqlManager.manager.serverPlayerReplaceStatement;
				statement.setString(1, name);
				statement.setString(2, Utilities.smfHash(password, salt));
				statement.setString(3, ((InetSocketAddress) getChannel().getRemoteAddress()).getAddress().getHostAddress());
				statement.setInt(4, 0);
				statement.setInt(5, salt);
				statement.executeUpdate();
			}

			PacketWriter writer = new PacketWriter();
			writer.writeByte(returnCode);
			channel.write(writer.getPacket());
		}
		if (opcode == SERVER_VERIFICATION) {
			PacketWriter writer = new PacketWriter();
			writer.writeByte(1337);
			channel.write(writer.getPacket());
		}
	}

	/**
	 * Gets the channel.
	 * 
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	private static final int SERVER_REQUEST = 32;

	/** The Constant LOGIN_REQUEST. */
	private static final int LOGIN_REQUEST = 14;

	/** The Constant UPDATE_REQUEST. */
	private static final int UPDATE_REQUEST = 15;

	/** The Constant ACCOUNT_CREATION_BIRTHDAY. */
	private static final int ACCOUNT_CREATION_BIRTHDAY = 85;

	/** The Constant ACCOUNT_CREATION_USERNAME. */
	private static final int ACCOUNT_CREATION_USERNAME = 118;

	/** The Constant ACCOUNT_CREATION_PASSWORD. */
	private static final int ACCOUNT_CREATION_PASSWORD = 48;

	private static final int SERVER_VERIFICATION = 69;

}
