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

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * A utility class used to write data to Packets.
 * 
 * @author Blake
 * 
 */
public class PacketWriter {

	/**
	 * Defines the current access type.
	 */
	public enum AccessType {

		/**
		 * Bit access.
		 */
		BIT_ACCESS,

		/**
		 * Byte access.
		 */
		BYTE_ACCESS

	}

	/**
	 * The access type.
	 */
	private AccessType accessType = AccessType.BYTE_ACCESS;

	/**
	 * The BI t_ mask.
	 */
	private static int BIT_MASK[] = { 0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f, 0x7f, 0xff, 0x1ff, 0x3ff, 0x7ff, 0xfff, 0x1fff, 0x3fff, 0x7fff, 0xffff, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1 };

	/**
	 * The packet.
	 */
	private Packet packet;

	/**
	 * The current bit index.
	 */
	private int bitIndex;

	/**
	 * Instantiates a new packet writer.
	 */
	public PacketWriter() {
		packet = new Packet();
	}

	/**
	 * Instantiates a new packet writer.
	 * 
	 * @param header
	 *            the header
	 */
	public PacketWriter(PacketHeader header) {
		packet = new Packet(header);
	}

	/**
	 * Sets the access type.
	 * 
	 * @param type
	 *            the new access type
	 */
	public void setAccessType(AccessType type) {
		accessType = type;
		if (accessType == AccessType.BIT_ACCESS) {
			bitIndex = packet.getBuffer().writerIndex() * 8;
		} else {
			packet.getBuffer().writerIndex((bitIndex + 7) / 8);
		}
	}

	/**
	 * Check bit access.
	 */
	public void checkBitAccess() {
		if (accessType != AccessType.BIT_ACCESS) {
			throw new IllegalStateException("bit access must be enabled for bit writing");
		}
	}

	/**
	 * Check byte access.
	 */
	public void checkByteAccess() {
		if (accessType != AccessType.BYTE_ACCESS) {
			throw new IllegalStateException("byte access must be enabled for byte writing");
		}
	}

	/**
	 * Write bit.
	 * 
	 * @param flag
	 *            the flag
	 */
	public void writeBit(boolean flag) {
		writeBit(flag ? 1 : 0);
	}

	/**
	 * Write bit.
	 * 
	 * @param value
	 *            the value
	 */
	public void writeBit(int value) {
		writeBits(1, value);
	}

	int i = 0;

	/**
	 * Write bits.
	 * 
	 * @param numBits
	 *            the num bits
	 * @param value
	 *            the value
	 */
	public void writeBits(int numBits, int value) {
		if (numBits < 0 || numBits > 32) {
			throw new IllegalArgumentException("Number of bits must be between 1 and 32 inclusive");
		}

		checkBitAccess();

		ChannelBuffer buffer = packet.getBuffer();
		int bytePos = bitIndex >> 3;
		int bitOffset = 8 - (bitIndex & 7);
		bitIndex += numBits;

		int requiredSpace = bytePos - buffer.writerIndex() + 1;
		requiredSpace += (numBits + 7) / 8;

		// Manual ensure-capacity.
		if (buffer.writableBytes() < requiredSpace) {
			int position = buffer.writerIndex();
			for (int i = 0; i < requiredSpace; i++) {
				buffer.writeByte((byte) 0);
			}
			buffer.writerIndex(position);
		}

		for (; numBits > bitOffset; bitOffset = 8) {
			byte tmp = buffer.getByte(bytePos);
			tmp &= ~BIT_MASK[bitOffset];
			tmp |= (value >> (numBits - bitOffset)) & BIT_MASK[bitOffset];
			buffer.setByte(bytePos++, tmp);
			numBits -= bitOffset;
		}
		if (numBits == bitOffset) {
			byte tmp = buffer.getByte(bytePos);
			tmp &= ~BIT_MASK[bitOffset];
			tmp |= value & BIT_MASK[bitOffset];
			buffer.setByte(bytePos, tmp);
		} else {
			byte tmp = buffer.getByte(bytePos);
			tmp &= ~(BIT_MASK[numBits] << (bitOffset - numBits));
			tmp |= (value & BIT_MASK[numBits]) << (bitOffset - numBits);
			buffer.setByte(bytePos, tmp);
		}
	}

	/**
	 * Writes a byte.
	 * 
	 * @param value
	 *            the value
	 */
	public void writeByte(int value) {
		writeByte(value, ValueType.NORMAL);
	}

	/**
	 * Writes a byte.
	 * 
	 * @param value
	 *            the value
	 * @param type
	 *            the type
	 */
	public void writeByte(int value, ValueType type) {
		checkByteAccess();
		switch (type) {
		case A:
			value += 128;
			break;
		case C:
			value = -value;
			break;
		case S:
			value = 128 - value;
			break;
		}
		packet.getBuffer().writeByte((byte) value);
	}

	/**
	 * Write short.
	 * 
	 * @param value
	 *            the value
	 */
	public void writeShort(int value) {
		writeShort(value, ValueType.NORMAL, ByteForm.BIG);
	}

	/**
	 * Write short.
	 * 
	 * @param value
	 *            the value
	 * @param form
	 *            the form
	 */
	public void writeShort(int value, ByteForm form) {
		writeShort(value, ValueType.NORMAL, form);
	}

	/**
	 * Write short.
	 * 
	 * @param value
	 *            the value
	 * @param type
	 *            the type
	 */
	public void writeShort(int value, ValueType type) {
		writeShort(value, type, ByteForm.BIG);
	}

	/**
	 * Write short.
	 * 
	 * @param value
	 *            the value
	 * @param type
	 *            the type
	 * @param form
	 *            the form
	 */
	public void writeShort(int value, ValueType type, ByteForm form) {
		switch (form) {
		case BIG:
			writeByte(value >> 8);
			writeByte(value, type);
			break;
		case MIDDLE:
			throw new UnsupportedOperationException("middle-endian short is impossible!");
		case INVERSE_MIDDLE:
			throw new UnsupportedOperationException("inverse-middle-endian short is impossible!");
		case LITTLE:
			writeByte(value, type);
			writeByte(value >> 8);
			break;
		}
	}

	/**
	 * Write int.
	 * 
	 * @param value
	 *            the value
	 */
	public void writeInt(int value) {
		writeInt(value, ValueType.NORMAL, ByteForm.BIG);
	}

	/**
	 * Write int.
	 * 
	 * @param value
	 *            the value
	 * @param type
	 *            the type
	 */
	public void writeInt(int value, ValueType type) {
		writeInt(value, type, ByteForm.BIG);
	}

	/**
	 * Write int.
	 * 
	 * @param value
	 *            the value
	 * @param form
	 *            the form
	 */
	public void writeInt(int value, ByteForm form) {
		writeInt(value, ValueType.NORMAL, form);
	}

	/**
	 * Write int.
	 * 
	 * @param value
	 *            the value
	 * @param type
	 *            the type
	 * @param form
	 *            the form
	 */
	public void writeInt(int value, ValueType type, ByteForm form) {
		switch (form) {
		case BIG:
			writeByte(value >> 24);
			writeByte(value >> 16);
			writeByte(value >> 8);
			writeByte(value, type);
			break;
		case MIDDLE:
			writeByte(value >> 8);
			writeByte(value, type);
			writeByte(value >> 24);
			writeByte(value >> 16);
			break;
		case INVERSE_MIDDLE:
			writeByte(value >> 16);
			writeByte(value >> 24);
			writeByte(value, type);
			writeByte(value >> 8);
			break;
		case LITTLE:
			writeByte(value, type);
			writeByte(value >> 8);
			writeByte(value >> 16);
			writeByte(value >> 24);
			break;
		}
	}

	/**
	 * Write long.
	 * 
	 * @param value
	 *            the value
	 */
	public void writeLong(long value) {
		writeLong(value, ValueType.NORMAL, ByteForm.BIG);
	}

	/**
	 * Write long.
	 * 
	 * @param value
	 *            the value
	 * @param type
	 *            the type
	 */
	public void writeLong(long value, ValueType type) {
		writeLong(value, type, ByteForm.BIG);
	}

	/**
	 * Write long.
	 * 
	 * @param value
	 *            the value
	 * @param form
	 *            the form
	 */
	public void writeLong(long value, ByteForm form) {
		writeLong(value, ValueType.NORMAL, form);
	}

	/**
	 * Write long.
	 * 
	 * @param value
	 *            the value
	 * @param type
	 *            the type
	 * @param form
	 *            the form
	 */
	public void writeLong(long value, ValueType type, ByteForm form) {
		switch (form) {
		case BIG:
			writeByte((int) (value >> 56));
			writeByte((int) (value >> 48));
			writeByte((int) (value >> 40));
			writeByte((int) (value >> 32));
			writeByte((int) (value >> 24));
			writeByte((int) (value >> 16));
			writeByte((int) (value >> 8));
			writeByte((int) value, type);
			break;
		case MIDDLE:
			throw new UnsupportedOperationException("middle-endian long is not implemented!");
		case INVERSE_MIDDLE:
			throw new UnsupportedOperationException("inverse-middle-endian long is not implemented!");
		case LITTLE:
			writeByte((int) value, type);
			writeByte((int) (value >> 8));
			writeByte((int) (value >> 16));
			writeByte((int) (value >> 24));
			writeByte((int) (value >> 32));
			writeByte((int) (value >> 40));
			writeByte((int) (value >> 48));
			writeByte((int) (value >> 56));
			break;
		}
	}

	/**
	 * Write string.
	 * 
	 * @param msg
	 *            the msg
	 */
	public void writeString(String msg) {
		for (int i = 0; i < msg.length(); i++) {
			writeByte(msg.getBytes()[i]);
		}
		writeByte(0);
	}

	/**
	 * Writes a buffer.
	 * 
	 * @param buffer
	 *            the buffer
	 */
	public void writeBuffer(ChannelBuffer buffer) {
		packet.getBuffer().writeBytes(buffer);
	}

	/**
	 * Writes a buffer.
	 * 
	 * @param buffer
	 *            the buffer
	 */
	public void writeBuffer(ByteBuffer buffer) {
		packet.getBuffer().writeBytes(buffer);
	}

	/**
	 * Writes a buffer.
	 * 
	 * @param buffer
	 *            the buffer
	 */
	public void writeBytes(byte[] buffer) {
		packet.getBuffer().writeBytes(buffer);
	}

	/**
	 * Sets the packet.
	 * 
	 * @param packet
	 *            the packet to set
	 */
	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	/**
	 * Gets the packet.
	 * 
	 * @return the packet
	 */
	public Packet getPacket() {
		return packet;
	}

}
