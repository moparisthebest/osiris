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

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * Keys for landscape unpacking.
 * 
 * @author Blake
 * 
 */
public class LandscapeKeys {

	/** The Constant keyMap. */
	private static final Map<Integer, int[]> keyMap = new HashMap<Integer, int[]>();

	/**
	 * Load keys.
	 * 
	 * @param fileName
	 *            the file name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void loadKeys(String fileName) throws IOException {
		DataInputStream in = new DataInputStream(new FileInputStream(fileName));
		while (in.available() > 20) {
			int regionId = in.readInt();
			int[] regionKeys = new int[4];
			for (int i = 0; i < regionKeys.length; i++) {
				regionKeys[i] = in.readInt();
			}
			keyMap.put(regionId, regionKeys);
		}
	}

	/**
	 * Gets the keys.
	 * 
	 * @param regionId
	 *            the region id
	 * @return the keys
	 */
	public static int[] getKeys(int regionId) {
		return keyMap.get(regionId);
	}

	/**
	 * Dump.
	 */
	public static void dump() {
		for (Map.Entry<Integer, int[]> entry : keyMap.entrySet()) {
			int regionId = entry.getKey();
			int[] keys = entry.getValue();
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter("./mapdata/" + regionId + ".txt"));
				for (int key : keys) {
					writer.write(key + "");
					writer.newLine();
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
