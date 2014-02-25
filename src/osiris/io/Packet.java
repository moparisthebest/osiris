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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * An object wrapping a network packet.
 * 
 * @author Blake
 * 
 */
public class Packet {

	/**
	 * The header.
	 */
	private PacketHeader header;

	/**
	 * The buffer.
	 */
	private ChannelBuffer buffer;

	/**
	 * Instantiates a new packet.
	 */
	public Packet() {
		this(null, ChannelBuffers.dynamicBuffer());
	}

	/**
	 * Instantiates a new packet.
	 * 
	 * @param header
	 *            the header
	 */
	public Packet(PacketHeader header) {
		this(header, ChannelBuffers.dynamicBuffer());
	}

	/**
	 * Instantiates a new packet.
	 * 
	 * @param buffer
	 *            the buffer
	 */
	public Packet(ChannelBuffer buffer) {
		this(null, buffer);
	}

	/**
	 * Instantiates a new packet.
	 * 
	 * @param header
	 *            the header
	 * @param buffer
	 *            the buffer
	 */
	public Packet(PacketHeader header, ChannelBuffer buffer) {
		this.header = header;
		this.buffer = buffer;
	}

	@Override
	public String toString() {
		return isRaw() ? "Packet[RAW]" : "Packet[opcode:" + header.getOpcode() + " length:" + header.getLength() + "]";
	}

	/**
	 * Checks if is raw.
	 * 
	 * @return true, if is raw
	 */
	public boolean isRaw() {
		return header == null;
	}

	/**
	 * Sets the buffer.
	 * 
	 * @param buffer
	 *            the buffer to set
	 */
	public void setBuffer(ChannelBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * Gets the buffer.
	 * 
	 * @return the buffer
	 */
	public ChannelBuffer getBuffer() {
		return buffer;
	}

	/**
	 * Sets the header.
	 * 
	 * @param header
	 *            the header to set
	 */
	public void setHeader(PacketHeader header) {
		this.header = header;
	}

	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	public PacketHeader getHeader() {
		return header;
	}

}
