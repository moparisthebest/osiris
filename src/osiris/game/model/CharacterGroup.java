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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

// TODO: Auto-generated Javadoc

/**
 * A group of characters.
 * 
 * @author Blake
 * @param <C>
 *            the element type
 * 
 */
public class CharacterGroup<C extends Character> implements Collection<C>, Iterable<C> {

	/**
	 * Internal entities array.
	 */
	private Character[] characters;

	/**
	 * Current size.
	 */
	private int size = 0;

	/**
	 * Creates an entity list with the specified capacity.
	 * 
	 * @param capacity
	 *            The capacity.
	 */
	public CharacterGroup(int capacity) {
		characters = new Character[capacity + 1]; // do not use idx 0
	}

	/**
	 * Gets an entity.
	 * 
	 * @param index
	 *            The index.
	 * @return The entity.
	 */
	public Character get(int index) {
		if (index <= 0 || index >= characters.length) {
			throw new IndexOutOfBoundsException();
		}
		return characters[index];
	}

	/**
	 * Gets the index of an entity.
	 * 
	 * @param entity
	 *            The entity.
	 * @return The index in the list.
	 */
	public int indexOf(C entity) {
		return entity.getSlot();
	}

	/**
	 * Gets the next free id.
	 * 
	 * @return The next free id.
	 */
	private int getNextId() {
		for (int i = 1; i < characters.length; i++) {
			if (characters[i] == null) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#add(java.lang.Object)
	 */

	@Override
	public boolean add(C arg0) {
		int id = getNextId();
		if (id == -1) {
			return false;
		}
		characters[id] = arg0;
		arg0.setSlot(id);
		size++;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */

	@Override
	public boolean addAll(Collection<? extends C> arg0) {
		boolean changed = false;
		for (C entity : arg0) {
			if (add(entity)) {
				changed = true;
			}
		}
		return changed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#clear()
	 */

	@Override
	public void clear() {
		for (int i = 1; i < characters.length; i++) {
			characters[i] = null;
		}
		size = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#contains(java.lang.Object)
	 */

	@Override
	public boolean contains(Object arg0) {
		for (int i = 1; i < characters.length; i++) {
			if (characters[i] == arg0) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */

	@Override
	public boolean containsAll(Collection<?> arg0) {
		boolean failed = false;
		for (Object o : arg0) {
			if (!contains(o)) {
				failed = true;
			}
		}
		return !failed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#isEmpty()
	 */

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#iterator()
	 */

	@Override
	public Iterator<C> iterator() {
		return new CharacterGroupIterator<C>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#remove(java.lang.Object)
	 */

	@Override
	public boolean remove(Object arg0) {
		for (int i = 1; i < characters.length; i++) {
			if (characters[i] == arg0) {
				characters[i] = null;
				size--;
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */

	@Override
	public boolean removeAll(Collection<?> arg0) {
		boolean changed = false;
		for (Object o : arg0) {
			if (remove(o)) {
				changed = true;
			}
		}
		return changed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */

	@Override
	public boolean retainAll(Collection<?> arg0) {
		boolean changed = false;
		for (int i = 1; i < characters.length; i++) {
			if (characters[i] != null) {
				if (!arg0.contains(characters[i])) {
					characters[i] = null;
					size--;
					changed = true;
				}
			}
		}
		return changed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#size()
	 */

	@Override
	public int size() {
		return size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#toArray()
	 */

	@Override
	public Character[] toArray() {
		int size = size();
		Character[] array = new Character[size];
		int ptr = 0;
		for (int i = 1; i < characters.length; i++) {
			if (characters[i] != null) {
				array[ptr++] = characters[i];
			}
		}
		return array;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#toArray(T[])
	 */

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] arg0) {
		Character[] arr = toArray();
		return (T[]) Arrays.copyOf(arr, arr.length, arg0.getClass());
	}

}
