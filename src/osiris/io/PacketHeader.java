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
 * A header for a Packet.
 * 
 * @author Blake
 * 
 */
public class PacketHeader {

	/**
	 * Various possible types of packet lengths.
	 */
	public enum LengthType {
		/**
		 * A fixed length.
		 */
		FIXED,
		/**
		 * A variable byte type length.
		 */
		VARIABLE_BYTE,

		/**
		 * A variable short type length.
		 */
		VARIABLE_SHORT
	}

	/**
	 * The opcode.
	 */
	private int opcode;

	/**
	 * The length.
	 */
	private int length;

	/**
	 * The type.
	 */
	private LengthType lengthType;

	/**
	 * Instantiates a new packet header.
	 * 
	 * @param opcode
	 *            the opcode
	 */
	public PacketHeader(int opcode) {
		this(opcode, -1, LengthType.FIXED);
	}

	/**
	 * Instantiates a new packet header.
	 * 
	 * @param opcode
	 *            the opcode
	 * @param length
	 *            the length
	 */
	public PacketHeader(int opcode, int length) {
		this(opcode, length, LengthType.FIXED);
	}

	/**
	 * Instantiates a new packet header.
	 * 
	 * @param opcode
	 *            the opcode
	 * @param type
	 *            the type
	 */
	public PacketHeader(int opcode, LengthType type) {
		this(opcode, -1, type);
	}

	/**
	 * Instantiates a new packet header.
	 * 
	 * @param opcode
	 *            the opcode
	 * @param length
	 *            the length
	 * @param type
	 *            the type
	 */
	public PacketHeader(int opcode, int length, LengthType type) {
		this.opcode = opcode;
		this.length = length;
		this.lengthType = type;
	}

	/**
	 * Sets the opcode.
	 * 
	 * @param opcode
	 *            the opcode to set
	 */
	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	/**
	 * Gets the opcode.
	 * 
	 * @return the opcode
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * Sets the length.
	 * 
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Gets the length.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets the length type.
	 * 
	 * @param lengthType
	 *            the type to set
	 */
	public void setLengthType(LengthType lengthType) {
		this.lengthType = lengthType;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public LengthType getLengthType() {
		return lengthType;
	}

}
