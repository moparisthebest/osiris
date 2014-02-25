package osiris.game.model.def;

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

import java.awt.Dimension;

// TODO: Auto-generated Javadoc

/**
 * The Class GameDef.
 * 
 * @author Boomer
 * 
 */
public class GameDef {

	/**
	 * The id.
	 */
	private final int id;

	/**
	 * The examine.
	 */
	private final String name, examine;

	/**
	 * The dim.
	 */
	private final Dimension dim;

	/**
	 * Instantiates a new game def.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param examine
	 *            the examine
	 * @param dim
	 *            the dim
	 */
	public GameDef(final int id, String name, String examine, final Dimension dim) {
		this.id = id;
		this.name = name;
		this.examine = examine;
		this.dim = dim;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Gets the dimension.
	 * 
	 * @return the dimension
	 */
	public final Dimension getDimension() {
		return dim;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the examine.
	 * 
	 * @return the examine
	 */
	public String getExamine() {
		return examine;
	}

}
