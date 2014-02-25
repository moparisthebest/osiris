package osiris.data.parser;

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

import java.util.ArrayList;
import java.util.List;

import osiris.Main;
import osiris.data.parser.impl.DialogueParser;
import osiris.data.parser.impl.GroundItemParser;
import osiris.data.parser.impl.NpcDropParser;
import osiris.data.parser.impl.NpcSpawnParser;
import osiris.data.parser.impl.PositionChangeParser;
import osiris.data.parser.impl.WorldObjectParser;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * Runs all the various parsers at once. Basically for convenience only.
 * 
 * @author samuraiblood2
 * 
 */
public class ParserLookup {

	/** The parsers. */
	private static List<Parser> parsers = new ArrayList<Parser>();

	/**
	 * Adds the parsers.
	 * 
	 * TODO: Possibly make a parser for this as well.
	 * 
	 */
	private static void addParsers() {
		parsers.add(new DialogueParser(Settings.DIALOGUES));
		parsers.add(new PositionChangeParser(Settings.POSITION_CHANGE));
		parsers.add(new GroundItemParser(Settings.GROUND_ITEMS));
		parsers.add(new NpcDropParser(Settings.NPC_DROPS));
		parsers.add(new NpcSpawnParser(Settings.NPC_SPAWNS));
		parsers.add(new WorldObjectParser(Settings.WORLD_OBJECTS));
	}

	/**
	 * Do run.
	 */
	public static void doRun() {
		addParsers();
		for (Parser parser : parsers) {
			if (parser == null) {
				continue;
			}

			if (Main.isLocal()) {
				System.out.println("[Parser]: Running '" + parser.getFile().getName() + "' parser...");
			}
			new Thread(parser).run();
		}
	}
}
