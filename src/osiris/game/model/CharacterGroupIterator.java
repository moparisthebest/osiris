package osiris.game.model;

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

import java.util.Iterator;
import java.util.NoSuchElementException;

// TODO: Auto-generated Javadoc

/**
 * The Class EntityListIterator.
 * 
 * @author Blake
 * @param <C>
 *            the element type
 * 
 */
public class CharacterGroupIterator<C extends Character> implements Iterator<C> {

	/**
	 * The entities.
	 */
	private Character[] entities;

	/**
	 * The entity list.
	 */
	private CharacterGroup<C> entityList;

	/**
	 * The previous index.
	 */
	private int lastIndex = -1;

	/**
	 * The current index.
	 */
	private int cursor = 0;

	/**
	 * The size of the list.
	 */
	private int size;

	/**
	 * Creates an entity list iterator.
	 * 
	 * @param entityList
	 *            The entity list.
	 */
	public CharacterGroupIterator(CharacterGroup<C> entityList) {
		this.entityList = entityList;
		entities = entityList.toArray(new Character[0]);
		size = entities.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */

	@Override
	public boolean hasNext() {
		return cursor < size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */

	@SuppressWarnings("unchecked")
	@Override
	public C next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		lastIndex = cursor++;
		return (C) entities[lastIndex];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */

	@Override
	public void remove() {
		if (lastIndex == -1) {
			throw new IllegalStateException();
		}
		entityList.remove(entities[lastIndex]);
	}

}