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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

import osiris.io.Packet;
import osiris.io.PacketHeader;

/**
 * The standard Osiris packet decoder.
 * 
 * @author Blake
 * 
 */
@ChannelPipelineCoverage("all")
public class OsirisPacketDecoder extends ReplayingDecoder<OsirisDecoderState> {

	/**
	 * The fixed packet lengths.
	 */
	public static final int[] PACKET_LENGTHS = new int[256];

	static {
		PACKET_LENGTHS[0] = -3;
		PACKET_LENGTHS[1] = -3;
		PACKET_LENGTHS[2] = 8;
		PACKET_LENGTHS[3] = 8; // Item equipping.
		PACKET_LENGTHS[4] = -3;
		PACKET_LENGTHS[5] = -3;
		PACKET_LENGTHS[6] = -3;
		PACKET_LENGTHS[7] = 2; // First NPC option.
		PACKET_LENGTHS[8] = -3;
		PACKET_LENGTHS[9] = -3;
		PACKET_LENGTHS[10] = -3;
		PACKET_LENGTHS[11] = -3;
		PACKET_LENGTHS[12] = -3;
		PACKET_LENGTHS[13] = -3;
		PACKET_LENGTHS[14] = -3;
		PACKET_LENGTHS[15] = -3;
		PACKET_LENGTHS[16] = -3;
		PACKET_LENGTHS[17] = -3;
		PACKET_LENGTHS[18] = -3;
		PACKET_LENGTHS[19] = -3;
		PACKET_LENGTHS[20] = -3;
		PACKET_LENGTHS[21] = 6; // Buttons.
		PACKET_LENGTHS[22] = 4; // Sent every time updateReq is set to true.
		PACKET_LENGTHS[23] = -3;
		PACKET_LENGTHS[24] = 8;
		PACKET_LENGTHS[25] = -3;
		PACKET_LENGTHS[26] = -3;
		PACKET_LENGTHS[27] = -3;
		PACKET_LENGTHS[28] = -3;
		PACKET_LENGTHS[29] = -3;
		PACKET_LENGTHS[30] = 8;
		PACKET_LENGTHS[31] = -3;
		PACKET_LENGTHS[32] = -3;
		PACKET_LENGTHS[33] = -3;
		PACKET_LENGTHS[34] = -3;
		PACKET_LENGTHS[35] = -3;
		PACKET_LENGTHS[36] = -3;
		PACKET_LENGTHS[37] = 2; // Second player option.
		PACKET_LENGTHS[38] = 2; // Item examine.
		PACKET_LENGTHS[39] = -3;
		PACKET_LENGTHS[40] = 16;
		PACKET_LENGTHS[41] = -3;
		PACKET_LENGTHS[42] = -3;
		PACKET_LENGTHS[43] = 4; // value x
		PACKET_LENGTHS[44] = -3;
		PACKET_LENGTHS[45] = -3;
		PACKET_LENGTHS[46] = -3;
		PACKET_LENGTHS[47] = 0; // Idle packet.
		PACKET_LENGTHS[48] = -3;
		PACKET_LENGTHS[49] = -1; // Main walking packet.
		PACKET_LENGTHS[50] = -3;
		PACKET_LENGTHS[51] = -3;
		PACKET_LENGTHS[52] = 2; // Second NPC option.
		PACKET_LENGTHS[53] = -3;
		PACKET_LENGTHS[54] = -3;
		PACKET_LENGTHS[55] = -3;
		PACKET_LENGTHS[56] = -3;
		PACKET_LENGTHS[57] = -3;
		PACKET_LENGTHS[58] = -3;
		PACKET_LENGTHS[59] = 6; // Sent every time you click your mouse.
		PACKET_LENGTHS[60] = 0; // New map region has been entered.
		PACKET_LENGTHS[61] = 8;
		PACKET_LENGTHS[62] = -3;
		PACKET_LENGTHS[63] = 6;
		PACKET_LENGTHS[64] = -3;
		PACKET_LENGTHS[65] = -3;
		PACKET_LENGTHS[66] = -3;
		PACKET_LENGTHS[67] = -3;
		PACKET_LENGTHS[68] = -3;
		PACKET_LENGTHS[69] = -3;
		PACKET_LENGTHS[70] = 8; // Magic on players.
		PACKET_LENGTHS[71] = -3;
		PACKET_LENGTHS[72] = -3;
		PACKET_LENGTHS[73] = -3;
		PACKET_LENGTHS[74] = -3;
		PACKET_LENGTHS[75] = -3;
		PACKET_LENGTHS[76] = -3;
		PACKET_LENGTHS[77] = -3;
		PACKET_LENGTHS[78] = -3;
		PACKET_LENGTHS[79] = -3;
		PACKET_LENGTHS[80] = -3;
		PACKET_LENGTHS[81] = -3;
		PACKET_LENGTHS[82] = -3;
		PACKET_LENGTHS[83] = -3;
		PACKET_LENGTHS[84] = 2; // Object examining.
		PACKET_LENGTHS[85] = -3;
		PACKET_LENGTHS[86] = -3;
		PACKET_LENGTHS[87] = -3;
		PACKET_LENGTHS[88] = 2; // NPC examining.
		PACKET_LENGTHS[89] = -3;
		PACKET_LENGTHS[90] = -3;
		PACKET_LENGTHS[91] = -3;
		PACKET_LENGTHS[92] = -3;
		PACKET_LENGTHS[93] = -3;
		PACKET_LENGTHS[94] = -3;
		PACKET_LENGTHS[95] = -3;
		PACKET_LENGTHS[96] = -3;
		PACKET_LENGTHS[97] = -3;
		PACKET_LENGTHS[98] = -3;
		PACKET_LENGTHS[99] = 4; // Unknown.
		PACKET_LENGTHS[100] = -3;
		PACKET_LENGTHS[101] = -3;
		PACKET_LENGTHS[102] = -3;
		PACKET_LENGTHS[103] = -3;
		PACKET_LENGTHS[104] = -3;
		PACKET_LENGTHS[105] = -3;
		PACKET_LENGTHS[106] = -3;
		PACKET_LENGTHS[107] = -1; // Command packet.
		PACKET_LENGTHS[108] = 0; // Interface closing.
		PACKET_LENGTHS[109] = -3;
		PACKET_LENGTHS[110] = -3;
		PACKET_LENGTHS[111] = -3;
		PACKET_LENGTHS[112] = -3;
		PACKET_LENGTHS[113] = 4; // Interface buttons.
		PACKET_LENGTHS[114] = -3;
		PACKET_LENGTHS[115] = 0; // Ping packet, sends no bytes.
		PACKET_LENGTHS[116] = -3;
		PACKET_LENGTHS[117] = -1; // Sends a good few unknown bytes.
		PACKET_LENGTHS[118] = -3;
		PACKET_LENGTHS[119] = -1; // Minimap walking.
		PACKET_LENGTHS[120] = -3;
		PACKET_LENGTHS[121] = -3;
		PACKET_LENGTHS[122] = -3;
		PACKET_LENGTHS[123] = 2; // NPC attack option.
		PACKET_LENGTHS[124] = -3;
		PACKET_LENGTHS[125] = -3;
		PACKET_LENGTHS[126] = -3;
		PACKET_LENGTHS[127] = -3;
		PACKET_LENGTHS[128] = -3;
		PACKET_LENGTHS[129] = -3;
		PACKET_LENGTHS[130] = -3;
		PACKET_LENGTHS[131] = -3;
		PACKET_LENGTHS[132] = 8;
		PACKET_LENGTHS[133] = -3;
		PACKET_LENGTHS[134] = -3;
		PACKET_LENGTHS[135] = -3;
		PACKET_LENGTHS[136] = -3;
		PACKET_LENGTHS[137] = -3;
		PACKET_LENGTHS[138] = -1; // Other walk clicking, such as items on
		// ground.
		PACKET_LENGTHS[139] = -3;
		PACKET_LENGTHS[140] = -3;
		PACKET_LENGTHS[141] = -3;
		PACKET_LENGTHS[142] = -3;
		PACKET_LENGTHS[143] = -3;
		PACKET_LENGTHS[144] = -3;
		PACKET_LENGTHS[145] = -3;
		PACKET_LENGTHS[146] = -3;
		PACKET_LENGTHS[147] = -3;
		PACKET_LENGTHS[148] = -3;
		PACKET_LENGTHS[149] = -3;
		PACKET_LENGTHS[150] = -3;
		PACKET_LENGTHS[151] = -3;
		PACKET_LENGTHS[152] = -3;
		PACKET_LENGTHS[153] = -3;
		PACKET_LENGTHS[154] = -3;
		PACKET_LENGTHS[155] = -3;
		PACKET_LENGTHS[156] = -3;
		PACKET_LENGTHS[157] = -3;
		PACKET_LENGTHS[158] = 6; // First object option.
		PACKET_LENGTHS[159] = -3;
		PACKET_LENGTHS[160] = 2; // First player option.
		PACKET_LENGTHS[161] = -3;
		PACKET_LENGTHS[162] = -3;
		PACKET_LENGTHS[163] = -3;
		PACKET_LENGTHS[164] = -3;
		PACKET_LENGTHS[165] = 4; // Settings buttons.
		PACKET_LENGTHS[166] = -3;
		PACKET_LENGTHS[167] = 9; // Switching items around in your inventory,
		// banking, etc.
		PACKET_LENGTHS[168] = -3;
		PACKET_LENGTHS[169] = 6; // Buttons.
		PACKET_LENGTHS[170] = -3;
		PACKET_LENGTHS[171] = -3;
		PACKET_LENGTHS[172] = -3;
		PACKET_LENGTHS[173] = 6;
		PACKET_LENGTHS[174] = -3;
		PACKET_LENGTHS[175] = -3;
		PACKET_LENGTHS[176] = -3;
		PACKET_LENGTHS[177] = -3;
		PACKET_LENGTHS[178] = -1;
		PACKET_LENGTHS[179] = 12; // Item index switching.
		PACKET_LENGTHS[180] = -3;
		PACKET_LENGTHS[181] = -3;
		PACKET_LENGTHS[182] = -3;
		PACKET_LENGTHS[183] = -3;
		PACKET_LENGTHS[184] = -3;
		PACKET_LENGTHS[185] = -3;
		PACKET_LENGTHS[186] = 8; // Item operate.
		PACKET_LENGTHS[187] = -3;
		PACKET_LENGTHS[188] = -3;
		PACKET_LENGTHS[189] = -3;
		PACKET_LENGTHS[190] = -3;
		PACKET_LENGTHS[191] = -3;
		PACKET_LENGTHS[192] = -3;
		PACKET_LENGTHS[193] = -3;
		PACKET_LENGTHS[194] = -3;
		PACKET_LENGTHS[195] = -3;
		PACKET_LENGTHS[196] = -3;
		PACKET_LENGTHS[197] = -3;
		PACKET_LENGTHS[198] = -3;
		PACKET_LENGTHS[199] = 2;
		PACKET_LENGTHS[200] = -3;
		PACKET_LENGTHS[201] = 6; // Ground item picking up.
		PACKET_LENGTHS[202] = -3;
		PACKET_LENGTHS[203] = 8; // Item options 1.
		PACKET_LENGTHS[204] = -3;
		PACKET_LENGTHS[205] = -3;
		PACKET_LENGTHS[206] = -3;
		PACKET_LENGTHS[207] = -3;
		PACKET_LENGTHS[208] = -3;
		PACKET_LENGTHS[209] = -3;
		PACKET_LENGTHS[210] = -3;
		PACKET_LENGTHS[211] = 8; // Item dropping.
		PACKET_LENGTHS[212] = 6;
		PACKET_LENGTHS[213] = -3;
		PACKET_LENGTHS[214] = 6;
		PACKET_LENGTHS[215] = -3;
		PACKET_LENGTHS[216] = -3;
		PACKET_LENGTHS[217] = -3;
		PACKET_LENGTHS[218] = -3;
		PACKET_LENGTHS[219] = -3;
		PACKET_LENGTHS[220] = 8; // Item eating, drinking, etc.
		PACKET_LENGTHS[221] = -3;
		PACKET_LENGTHS[222] = -1; // Public chat text.
		PACKET_LENGTHS[223] = -3;
		PACKET_LENGTHS[224] = 14;
		PACKET_LENGTHS[225] = -3;
		PACKET_LENGTHS[226] = -3;
		PACKET_LENGTHS[227] = 2; // Third player option.
		PACKET_LENGTHS[228] = 6; // Second object option.
		PACKET_LENGTHS[229] = -3;
		PACKET_LENGTHS[230] = -3;
		PACKET_LENGTHS[231] = -3;
		PACKET_LENGTHS[232] = 6; // Buttons.
		PACKET_LENGTHS[233] = 6; // Buttons.
		PACKET_LENGTHS[234] = -3;
		PACKET_LENGTHS[235] = -3;
		PACKET_LENGTHS[236] = -3;
		PACKET_LENGTHS[237] = -3;
		PACKET_LENGTHS[238] = -3;
		PACKET_LENGTHS[239] = -3;
		PACKET_LENGTHS[240] = -3;
		PACKET_LENGTHS[241] = -3;
		PACKET_LENGTHS[242] = -3;
		PACKET_LENGTHS[243] = -3;
		PACKET_LENGTHS[244] = -3;
		PACKET_LENGTHS[245] = -3;
		PACKET_LENGTHS[246] = -3;
		PACKET_LENGTHS[247] = 4; // Unknown.
		PACKET_LENGTHS[248] = 1; // Unknown.
		PACKET_LENGTHS[249] = -3;
		PACKET_LENGTHS[250] = 4;
		PACKET_LENGTHS[251] = -3;
		PACKET_LENGTHS[252] = -3;
		PACKET_LENGTHS[253] = 2;
		PACKET_LENGTHS[254] = -3;
		PACKET_LENGTHS[255] = -3;
	}

	/** The opcode. */
	private int opcode;

	/** The length. */
	private int length;

	/** The payload. */
	private ChannelBuffer payload;

	/**
	 * Instantiates a new osiris packet decoder.
	 */
	public OsirisPacketDecoder() {
		super(OsirisDecoderState.READ_OPCODE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.handler.codec.replay.ReplayingDecoder#decode(org.jboss
	 * .netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel,
	 * org.jboss.netty.buffer.ChannelBuffer, java.lang.Enum)
	 */

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer in, OsirisDecoderState state) throws Exception {
		switch (state) {
		case READ_OPCODE:
			opcode = in.readByte() & 0xff;
			checkpoint(OsirisDecoderState.READ_LENGTH);
		case READ_LENGTH:
			length = PACKET_LENGTHS[opcode];
			if (length == -1) {
				length = in.readByte() & 0xff;
			}

			// Unknown length.
			if (length == -3) {

				/*
				 * Here we will attempt to salvage the stream by skipping all
				 * available bytes in hopes that this frame has not arrived
				 * fragmented and is contained completely within this one
				 * buffer. If this is the case, we can skip the entire frame and
				 * the next frame can be read properly. If not, the stream has
				 * been offset and the game will break.
				 * 
				 * TODO: Possibly implement a more adaptive system?
				 */
				int skippedBytes = 0;
				try {
					// Attempt to drain all of the data out of the buffer.
					for (; in.readable(); skippedBytes++)
						in.readByte();
				} catch (Throwable throwable) {
					/*
					 * This type of (replaying) buffer will throw a Throwable
					 * when it has no more available data. We will catch this
					 * Throwable here so that it doesn't fall back to the
					 * decoder and get handled by the replaying system. In
					 * effect, we are draining all available data out of this
					 * buffer (and from the stream) and hopefully skipping all
					 * of the data this unknown frame has.
					 */

					// Now go and attempt to read the next frame.
					checkpoint(OsirisDecoderState.READ_OPCODE);
					break;
				}

			}
			checkpoint(OsirisDecoderState.READ_PAYLOAD);
		case READ_PAYLOAD:
			payload = ChannelBuffers.dynamicBuffer();
			payload.writeBytes(in.readBytes(length));
			checkpoint(OsirisDecoderState.READ_OPCODE);
			return new Packet(new PacketHeader(opcode, length), payload);
		}
		return null;
	}

}