package osiris.util;

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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import osiris.Main;

/**
 * The Class RubyInterpreter.
 * 
 * @author Blakeman8192
 */
public class RubyInterpreter {

	/** The Constant engine. */
	private static final ScriptEngine engine;

	static {
		if (Main.isLocal()) {
			System.out.println("Loading JRuby script engine...");
		}
		ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName("jruby");
		if (engine == null) {
			throw new RuntimeException("JRuby engine not found!");
		}
	}

	/**
	 * Invoke.
	 * 
	 * @param identifier
	 *            the identifier
	 * @param args
	 *            the args
	 */
	public static void invoke(String identifier, Object... args) {
		Invocable i = (Invocable) engine;
		try {
			i.invokeFunction(identifier, args);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the variable.
	 * 
	 * @param key
	 *            the key
	 * @return the variable
	 */
	public static Object getVariable(String key) {
		return engine.get(key);
	}

	/**
	 * Load scripts.
	 * 
	 * @param directory
	 *            the directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ScriptException
	 *             the script exception
	 */
	public static void loadScripts(File directory) throws IOException, ScriptException {
		if (!directory.exists() || !directory.isDirectory()) {
			throw new IllegalArgumentException("Invalid scripts directory!");
		}
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				loadScripts(file);
			} else {
				if (file.getName().endsWith(".rb")) {
					if (Main.isLocal()) {
						System.out.println("\tScript: " + file.getName());
					}
					engine.eval(new FileReader(file));
				}
			}
		}
	}

}
