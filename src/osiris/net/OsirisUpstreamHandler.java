package osiris.net;

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

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import osiris.Main;
import osiris.ServerEngine;
import osiris.game.event.GameEvent;
import osiris.game.event.GameEventLookup;
import osiris.game.event.impl.ServiceRequestEvent;
import osiris.game.model.Player;
import osiris.io.Packet;

/**
 * The default Osiris upstream handler.
 * 
 * @author Blake
 * 
 */
@ChannelPipelineCoverage("all")
public class OsirisUpstreamHandler extends SimpleChannelUpstreamHandler {

	/**
	 * The player map.
	 */
	private static Map<Integer, Player> playerMap = new HashMap<Integer, Player>();

	/**
	 * Gets the player map.
	 * 
	 * @return the player map
	 */
	public static Map<Integer, Player> getPlayerMap() {
		return playerMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(
	 * org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Packet packet = (Packet) e.getMessage();
		Class<GameEvent> clazz = GameEventLookup.lookup(packet.getHeader().getOpcode());

		// Standard game event.
		if (clazz.getSuperclass().equals(GameEvent.class) && !clazz.equals(ServiceRequestEvent.class)) {
			GameEvent event;
			synchronized (playerMap) {
				Player player = playerMap.get(e.getChannel().getId());
				event = clazz.getConstructor(Player.class, Packet.class).newInstance(player, packet);
			}
			ServerEngine.queueGameEvent(event);
		} else {
			// Service request.
			GameEvent event = clazz.getConstructor(Channel.class, Packet.class).newInstance(e.getChannel(), packet);
			event.process();
			event = null;
		}
		clazz = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelOpen(org.
	 * jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelClosed(org
	 * .jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Player player = playerMap.get(e.getChannel().getId());
		if (player != null) {
			player.logout();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelDisconnected
	 * (org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Player player = playerMap.get(e.getChannel().getId());
		if (player != null) {
			player.logout();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#exceptionCaught(
	 * org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ExceptionEvent)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		if (Main.isLocal()) {
			e.getCause().printStackTrace();
		}
		e.getChannel().close();
	}

}
