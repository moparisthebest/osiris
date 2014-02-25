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

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class ConsoleProxy.
 * 
 * @author Blake
 */
public class ConsoleProxy extends PrintStream {

	/**
	 * Instantiates a new console proxy.
	 * 
	 * @param out
	 *            the out
	 */
	public ConsoleProxy(OutputStream out) {
		super(out);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(java.lang.String)
	 */
	@Override
	public void print(String msg) {
		msg = format(msg);
		super.print(msg);
	}

	/**
	 * discombobulated corpse termination urbanization governmentalibation
	 * explosive montezuma's blasting bowel revenge!
	 * 
	 * yeah, that sounded good
	 * 
	 * @param msg
	 *            the msg
	 * @return the string
	 */
	private String format(String msg) {
		return "[" + sdf.format(new Date()) + "]: " + msg;
	}

	/** The sdf. */
	private SimpleDateFormat sdf = new SimpleDateFormat();

}
