package osiris.net.codec;

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
 * Represents the different states of the Osiris packet decoder.
 * 
 * @author Blake
 * 
 */
public enum OsirisDecoderState {

	/**
	 * Reading the opcode
	 */
	READ_OPCODE,

	/**
	 * Reading the length.
	 */
	READ_LENGTH,

	/**
	 * Reading the payload.
	 */
	READ_PAYLOAD

}
