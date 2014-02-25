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

/**
 * A utility class for reading data from Packets.
 * 
 * @author Blake
 * 
 */
public class PacketReader {

	/**
	 * The packet.
	 */
	private Packet packet;

	/**
	 * Instantiates a new packet reader.
	 * 
	 * @param packet
	 *            the packet
	 */
	public PacketReader(Packet packet) {
		this.setPacket(packet);
	}

	/**
	 * Reads a signed byte.
	 * 
	 * @return the int
	 */
	public int readByte() {
		return readByte(true, ValueType.NORMAL);
	}

	/**
	 * Read byte.
	 * 
	 * @param signed
	 *            the signed
	 * @return the int
	 */
	public int readByte(boolean signed) {
		return readByte(signed, ValueType.NORMAL);
	}

	/**
	 * Read byte.
	 * 
	 * @param type
	 *            the type
	 * @return the int
	 */
	public int readByte(ValueType type) {
		return readByte(true, type);
	}

	/**
	 * Read byte.
	 * 
	 * @param signed
	 *            the signed
	 * @param type
	 *            the type
	 * @return the int
	 */
	public int readByte(boolean signed, ValueType type) {
		int value = packet.getBuffer().readByte();
		switch (type) {
		case A:
			value = value - 128;
			break;
		case C:
			value = -value;
			break;
		case S:
			value = 128 - value;
			break;
		}
		return signed ? value : value & 0xff;
	}

	/**
	 * Read short.
	 * 
	 * @return the int
	 */
	public int readShort() {
		return readShort(true, ValueType.NORMAL, ByteForm.BIG);
	}

	/**
	 * Read short.
	 * 
	 * @param signed
	 *            the signed
	 * @return the int
	 */
	public int readShort(boolean signed) {
		return readShort(signed, ValueType.NORMAL, ByteForm.BIG);
	}

	/**
	 * Read short.
	 * 
	 * @param type
	 *            the type
	 * @return the int
	 */
	public int readShort(ValueType type) {
		return readShort(true, type, ByteForm.BIG);
	}

	/**
	 * Read short.
	 * 
	 * @param signed
	 *            the signed
	 * @param type
	 *            the type
	 * @return the int
	 */
	public int readShort(boolean signed, ValueType type) {
		return readShort(signed, type, ByteForm.BIG);
	}

	/**
	 * Read short.
	 * 
	 * @param form
	 *            the form
	 * @return the int
	 */
	public int readShort(ByteForm form) {
		return readShort(true, ValueType.NORMAL, form);
	}

	/**
	 * Read short.
	 * 
	 * @param signed
	 *            the signed
	 * @param form
	 *            the form
	 * @return the int
	 */
	public int readShort(boolean signed, ByteForm form) {
		return readShort(signed, ValueType.NORMAL, form);
	}

	/**
	 * Read short.
	 * 
	 * @param type
	 *            the type
	 * @param form
	 *            the form
	 * @return the int
	 */
	public int readShort(ValueType type, ByteForm form) {
		return readShort(true, type, form);
	}

	/**
	 * Read short.
	 * 
	 * @param signed
	 *            the signed
	 * @param type
	 *            the type
	 * @param form
	 *            the form
	 * @return the int
	 */
	public int readShort(boolean signed, ValueType type, ByteForm form) {
		int value = 0;
		switch (form) {
		case BIG:
			value |= readByte(false) << 8;
			value |= readByte(false, type);
			break;
		case MIDDLE:
			throw new UnsupportedOperationException("middle-endian short is impossible!");
		case INVERSE_MIDDLE:
			throw new UnsupportedOperationException("inverse-middle-endian short is impossible!");
		case LITTLE:
			value |= readByte(false, type);
			value |= readByte(false) << 8;
			break;
		}
		return signed ? value : value & 0xffff;
	}

	/**
	 * Read int.
	 * 
	 * @return the int
	 */
	public int readInt() {
		return (int) readInt(true, ValueType.NORMAL, ByteForm.BIG);
	}

	/**
	 * Read int.
	 * 
	 * @param signed
	 *            the signed
	 * @return the long
	 */
	public long readInt(boolean signed) {
		return readInt(signed, ValueType.NORMAL, ByteForm.BIG);
	}

	/**
	 * Read int.
	 * 
	 * @param type
	 *            the type
	 * @return the int
	 */
	public int readInt(ValueType type) {
		return (int) readInt(true, type, ByteForm.BIG);
	}

	/**
	 * Read int.
	 * 
	 * @param signed
	 *            the signed
	 * @param type
	 *            the type
	 * @return the long
	 */
	public long readInt(boolean signed, ValueType type) {
		return readInt(signed, type, ByteForm.BIG);
	}

	/**
	 * Read int.
	 * 
	 * @param form
	 *            the form
	 * @return the int
	 */
	public int readInt(ByteForm form) {
		return (int) readInt(true, ValueType.NORMAL, form);
	}

	/**
	 * Read int.
	 * 
	 * @param signed
	 *            the signed
	 * @param form
	 *            the form
	 * @return the long
	 */
	public long readInt(boolean signed, ByteForm form) {
		return readInt(signed, ValueType.NORMAL, form);
	}

	/**
	 * Read int.
	 * 
	 * @param type
	 *            the type
	 * @param form
	 *            the form
	 * @return the int
	 */
	public int readInt(ValueType type, ByteForm form) {
		return (int) readInt(true, type, form);
	}

	/**
	 * Read int.
	 * 
	 * @param signed
	 *            the signed
	 * @param type
	 *            the type
	 * @param form
	 *            the form
	 * @return the long
	 */
	public long readInt(boolean signed, ValueType type, ByteForm form) {
		long value = 0;
		switch (form) {
		case BIG:
			value |= readByte(false) << 24;
			value |= readByte(false) << 16;
			value |= readByte(false) << 8;
			value |= readByte(false, type);
			break;
		case MIDDLE:
			value |= readByte(false) << 8;
			value |= readByte(false, type);
			value |= readByte(false) << 24;
			value |= readByte(false) << 16;
			break;
		case INVERSE_MIDDLE:
			value |= readByte(false) << 16;
			value |= readByte(false) << 24;
			value |= readByte(false, type);
			value |= readByte(false) << 8;
			break;
		case LITTLE:
			value |= readByte(false, type);
			value |= readByte(false) << 8;
			value |= readByte(false) << 16;
			value |= readByte(false) << 24;
			break;
		}
		return signed ? value : value & 0xffffffffL;
	}

	/**
	 * Read long.
	 * 
	 * @return the long
	 */
	public long readLong() {
		return readLong(ValueType.NORMAL, ByteForm.BIG);
	}

	/**
	 * Read long.
	 * 
	 * @param type
	 *            the type
	 * @return the long
	 */
	public long readLong(ValueType type) {
		return readLong(type, ByteForm.BIG);
	}

	/**
	 * Read long.
	 * 
	 * @param form
	 *            the form
	 * @return the long
	 */
	public long readLong(ByteForm form) {
		return readLong(ValueType.NORMAL, form);
	}

	/**
	 * Read long.
	 * 
	 * @param type
	 *            the type
	 * @param form
	 *            the form
	 * @return the long
	 */
	public long readLong(ValueType type, ByteForm form) {
		long value = 0;
		switch (form) {
		case BIG:
			value |= (long) readByte(false) << 56L;
			value |= (long) readByte(false) << 48L;
			value |= (long) readByte(false) << 40L;
			value |= (long) readByte(false) << 32L;
			value |= (long) readByte(false) << 24L;
			value |= (long) readByte(false) << 16L;
			value |= (long) readByte(false) << 8L;
			value |= readByte(false, type);
			break;
		case MIDDLE:
			throw new UnsupportedOperationException("middle-endian long is not implemented!");
		case INVERSE_MIDDLE:
			throw new UnsupportedOperationException("inverse-middle-endian long is not implemented!");
		case LITTLE:
			value |= readByte(false, type);
			value |= (long) readByte(false) << 8L;
			value |= (long) readByte(false) << 16L;
			value |= (long) readByte(false) << 24L;
			value |= (long) readByte(false) << 32L;
			value |= (long) readByte(false) << 40L;
			value |= (long) readByte(false) << 48L;
			value |= (long) readByte(false) << 56L;
			break;
		}
		return value;
	}

	/**
	 * Read string.
	 * 
	 * @return the string
	 */
	public String readString() {
		byte temp;
		StringBuilder b = new StringBuilder();
		while ((temp = (byte) readByte()) != 0) {
			b.append((char) temp);
		}
		return b.toString();
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