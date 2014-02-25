package osiris.game.model.dialogues;

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

// TODO: Auto-generated Javadoc
/**
 * The Class Dialogue.
 * 
 * @author samuraiblood2
 * 
 */
public class Dialogue {

	/** The npc. */
	private int npc;

	/** The id. */
	private int id;

	/** The next. */
	private int next;

	/** The title. */
	private String title;

	/** The lines. */
	private List<String> lines = new ArrayList<String>();

	/** The type. */
	private Type type = Type.NPC;

	/**
	 * The Enum Type.
	 */
	public enum Type {
		/** The OPTION. */
		OPTION,

		/** The NPC. */
		NPC,

		/** The PLAYER. */
		PLAYER
	}

	/**
	 * Value of.
	 * 
	 * @param dialogue
	 *            the dialogue
	 * @return the dialogue
	 */
	public Dialogue(Dialogue dialogue) {
		this(dialogue.getType(), dialogue.getId());
	}

	/**
	 * Instantiates a new dialogue.
	 * 
	 * @param type
	 *            the type
	 * @param id
	 *            the id
	 */
	public Dialogue(Type type, int id) {
		this.type = type;
		this.id = id;
	}

	/**
	 * Sets the lines.
	 * 
	 * @param lines
	 *            the new lines
	 */
	public void setLines(List<String> lines) {
		this.lines.addAll(lines);
	}

	/**
	 * Gets the lines.
	 * 
	 * @return the lines
	 */
	public String[] getLines() {
		return lines.toArray(new String[0]);
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the npc.
	 * 
	 * @param npc
	 *            the new npc
	 */
	public void setNpc(int npc) {
		this.npc = npc;
	}

	/**
	 * Gets the npc.
	 * 
	 * @return the npc
	 */
	public int getNpc() {
		return npc;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the next.
	 * 
	 * @param next
	 *            the new next
	 */
	public void setNext(int next) {
		this.next = next;
	}

	/**
	 * Gets the next.
	 * 
	 * @return the next
	 */
	public int getNext() {
		return next;
	}
}