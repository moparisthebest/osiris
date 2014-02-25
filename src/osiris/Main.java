package osiris;

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

import static osiris.util.Settings.LANDSCAPE_KEYS_FILE;
import static osiris.util.Settings.MAX_NPCS;
import static osiris.util.Settings.MAX_PLAYERS;
import static osiris.util.Settings.PORT;
import static osiris.util.Settings.SCRIPTS_DIRECTORY;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import osiris.data.SettingsLoader;
import osiris.data.parser.ParserLookup;
import osiris.data.parser.impl.PositionChangeParser.PositionChangeInfo;
import osiris.data.sql.SqlManager;
import osiris.game.model.CharacterGroup;
import osiris.game.model.Npc;
import osiris.game.model.Player;
import osiris.game.model.def.ItemDef;
import osiris.game.model.def.NpcCombatDef;
import osiris.game.model.dialogues.Dialogue;
import osiris.game.model.ground.GroundManager;
import osiris.game.model.magic.MagicManager;
import osiris.net.OsirisPipelineFactory;
import osiris.util.ConsoleProxy;
import osiris.util.LandscapeKeys;
import osiris.util.RubyInterpreter;
import osiris.util.Settings;

/**
 * The main class.
 * 
 * @author Blake
 * @author samuraiblood2
 * @author Boomer
 * 
 */
public class Main {
	/**
	 * The Constant players.
	 */
	private static final CharacterGroup<Player> players = new CharacterGroup<Player>(MAX_PLAYERS);

	/**
	 * The Constant npcs.
	 */
	private static final CharacterGroup<Npc> npcs = new CharacterGroup<Npc>(MAX_NPCS);

	/** The Constant quickChatMessages. */
	private static final Hashtable<Integer, String> quickChatMessages = new Hashtable<Integer, String>();

	/** The local. */
	private static boolean local;

	/** The Constant dialogues. */
	private static final List<Dialogue> dialogues = new ArrayList<Dialogue>();

	/** The Constant stairs. */
	private static final List<PositionChangeInfo> stairs = new ArrayList<PositionChangeInfo>();

	/** The most players online. */
	private static int mostPlayersOnline = 0;

	/** The start time. */
	public static long startTime;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		printTag();

		System.setOut(new ConsoleProxy(System.out));
		System.setErr(new ConsoleProxy(System.err));
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("-verbose") || args[0].equalsIgnoreCase("-v") || args[0].equalsIgnoreCase("-local") || args[0].equalsIgnoreCase("-l")) {
				local = true;
			}
		}

		// 1. Load up any configuration.
		try {
			SettingsLoader.load();
			if (SettingsLoader.contains("verbose_mode")) {
				local = SettingsLoader.getValueAsBoolean("verbose_mode");
			}

			System.out.println("Bootstrapping " + Settings.SERVER_NAME + " emulator...");

			LandscapeKeys.loadKeys(LANDSCAPE_KEYS_FILE);
			GroundManager.create();
			ItemDef.load();
			SqlManager.load();
			MagicManager.load();
			NpcCombatDef.load();

			// XXX: Run the various configuration parsers.
			ParserLookup.doRun();

			RubyInterpreter.loadScripts(new File(SCRIPTS_DIRECTORY));
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		// 2. Start the server engine.
		ServerEngine.start();

		// Start the networking system.
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.setPipelineFactory(new OsirisPipelineFactory());
		ExecutorService e = Executors.newCachedThreadPool();
		ChannelFactory factory = new NioServerSocketChannelFactory(e, e);
		bootstrap.setFactory(factory);
		bootstrap.bind(new InetSocketAddress(PORT));
		startTime = System.currentTimeMillis();
		System.out.println("Bootstrap complete!");
	}

	/**
	 * Prints the tag.
	 */
	private static void printTag() {
		System.out.println();
		System.out.println("		 ,-----.        ,--.       ,--.   ");
		System.out.println("		 '  .-.  ' ,---. `--',--.--.`--' ,---. ");
		System.out.println("		 |  | |  |(  .-' ,--.|  .--',--.(  .-' ");
		System.out.println("		 '  '-'  '.-'  `)|  ||  |   |  |.-'  `)");
		System.out.println("		  `-----' `----' `--'`--'   `--'`----'");
		System.out.println();
	}

	/**
	 * Gets the players.
	 * 
	 * @return the players
	 */
	public static CharacterGroup<Player> getPlayers() {
		return players;
	}

	/**
	 * Gets the npcs.
	 * 
	 * @return the npcs
	 */
	public static CharacterGroup<Npc> getNpcs() {
		return npcs;
	}

	/**
	 * Gets the quick chat messages.
	 * 
	 * @return the quick chat messages
	 */
	public static Hashtable<Integer, String> getQuickChatMessages() {
		return quickChatMessages;
	}

	/**
	 * Find player.
	 * 
	 * @param uid
	 *            the uid
	 * @return the character
	 */
	public static Player findPlayer(int uid) {
		for (Player player : getPlayers())
			if (player.getUniqueId() == uid)
				return player;
		return null;
	}

	/**
	 * Find player.
	 * 
	 * @param name
	 *            the name
	 * @return the player
	 */
	public static Player findPlayer(String name) {
		for (Player player : getPlayers())
			if (player.getUsername().equalsIgnoreCase(name))
				return player;
		return null;
	}

	/**
	 * Sets the most players online.
	 * 
	 * @param most
	 *            the new most players online
	 */
	public static void setMostPlayersOnline(int most) {
		mostPlayersOnline = most;
	}

	/**
	 * Gets the most players online.
	 * 
	 * @return the most players online
	 */
	public static int getMostPlayersOnline() {
		return mostPlayersOnline;
	}

	/**
	 * Checks if is local.
	 * 
	 * @return true, if is local
	 */
	public static boolean isLocal() {
		return local;
	}

	/**
	 * Gets the dialogues.
	 * 
	 * @return the dialogues
	 */
	public static List<Dialogue> getDialogues() {
		return dialogues;
	}

	/**
	 * Gets the stairs.
	 * 
	 * @return the stairs
	 */
	public static List<PositionChangeInfo> getStairs() {
		return stairs;
	}

}
