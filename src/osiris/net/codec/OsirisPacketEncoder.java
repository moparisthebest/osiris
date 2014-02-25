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
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import osiris.io.Packet;
import osiris.io.PacketHeader.LengthType;

/**
 * The default Osiris packet encoder.
 * 
 * @author Blake
 * 
 */
@ChannelPipelineCoverage("all")
public class OsirisPacketEncoder extends OneToOneEncoder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss
	 * .netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel,
	 * java.lang.Object)
	 */

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		// Write the payload.
		Packet packet = (Packet) msg;
		if (packet.isRaw()) {
			ChannelBuffer out = ChannelBuffers.buffer(packet.getBuffer().writerIndex());
			out.writeBytes(packet.getBuffer());
			return out;
		}

		// Write the full packet.
		LengthType type = packet.getHeader().getLengthType();
		int headerLength = 1;
		int payloadLength = packet.getBuffer().writerIndex();
		if (type == LengthType.VARIABLE_BYTE) {
			headerLength += 1;
		} else if (type == LengthType.VARIABLE_SHORT) {
			headerLength += 2;
		}
		ChannelBuffer buffer = ChannelBuffers.buffer(headerLength + payloadLength);
		buffer.writeByte((byte) packet.getHeader().getOpcode());
		if (type == LengthType.VARIABLE_BYTE) {
			buffer.writeByte((byte) packet.getBuffer().writerIndex());
		} else if (type == LengthType.VARIABLE_SHORT) {
			buffer.writeShort((short) packet.getBuffer().writerIndex());
		}
		buffer.writeBytes(packet.getBuffer());

		packet = null;
		msg = null;
		return buffer;
	}

}
