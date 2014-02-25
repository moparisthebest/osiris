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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemDef.
 * 
 * @author Boomer
 */
@SuppressWarnings("unused")
public class ItemDef extends GameDef {

	/** The definitions. */
	public static GameDef[] definitions;

	/** The female id. */
	private int equipId, specialtyStorePrice, price, femaleId;

	/** The weight. */
	private double weight;

	/** The bonuses. */
	private int[] requirements, bonuses;

	/** The tradeable. */
	private boolean noted, stackable, members, tradeable;

	/** The Constant ITEM_DEFINITIONS. */
	public static final int ITEM_DEFINITIONS = 13559;

	/**
	 * Instantiates a new game def.
	 * 
	 * @param id
	 *            the id
	 * @param equipId
	 *            the equip id
	 * @param bonuses
	 *            the bonuses
	 * @param noted
	 *            the noted
	 * @param stackable
	 *            the stackable
	 * @param members
	 *            the members
	 * @param weight
	 *            the weight
	 * @param price
	 *            the price
	 * @param name
	 *            the name
	 * @param examine
	 *            the examine
	 * @param levelRequirements
	 *            the level requirements
	 */

	public ItemDef(final int id, final int equipId, int[] bonuses, boolean noted, boolean stackable, boolean members, double weight, int price, String name, String examine, int[] levelRequirements) {
		super(id, name, examine, Settings.SINGULAR_DIMENSION);
		this.equipId = equipId;
		this.bonuses = bonuses;
		this.noted = noted;
		this.stackable = stackable;
		this.members = members;
		this.femaleId = -1;
		this.tradeable = true;
		this.weight = weight;
		this.price = price;
		this.requirements = levelRequirements;
	}

	/**
	 * Sets the stackable.
	 * 
	 * @param stackable
	 *            the new stackable
	 */
	public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}

	/**
	 * Sets the tradeable.
	 * 
	 * @param tradeable
	 *            the new tradeable
	 */
	public void setTradeable(boolean tradeable) {
		this.tradeable = tradeable;
	}

	/**
	 * Sets the female id.
	 * 
	 * @param femaleId
	 *            the new female id
	 */
	public void setFemaleId(int femaleId) {
		this.femaleId = femaleId;
	}

	/**
	 * Sets the bonuses.
	 * 
	 * @param bonuses
	 *            the new bonuses
	 */
	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	/**
	 * Sets the price.
	 * 
	 * @param price
	 *            the new price
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * Gets the female id.
	 * 
	 * @return the female id
	 */
	public int getFemaleId() {
		return femaleId;
	}

	/**
	 * Gets the requirements.
	 * 
	 * @return the requirements
	 */
	public int[] getRequirements() {
		return requirements;
	}

	/**
	 * Checks if is tradeable.
	 * 
	 * @return true, if is tradeable
	 */
	public boolean isTradeable() {
		if (noted)
			return ItemDef.forId(getId() - 1).isTradeable();
		else
			return tradeable;
	}

	/**
	 * Checks if is members.
	 * 
	 * @return true, if is members
	 */
	public boolean isMembers() {
		return members;
	}

	/**
	 * Gets the weight.
	 * 
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Gets the price.
	 * 
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Gets the equip id.
	 * 
	 * @return the equip id
	 */
	public int getEquipId() {
		return equipId;
	}

	/**
	 * Gets the bonuses.
	 * 
	 * @return the bonuses
	 */
	public int[] getBonuses() {
		return bonuses;
	}

	/**
	 * Gets the bonus.
	 * 
	 * @param id
	 *            the id
	 * @return the bonus
	 */
	public int getBonus(int id) {
		return bonuses[id];
	}

	/**
	 * Checks if is noted.
	 * 
	 * @return true, if is noted
	 */
	public boolean isNoted() {
		return noted;
	}

	/**
	 * Checks if is stackable.
	 * 
	 * @return true, if is stackable
	 */
	public boolean isStackable() {
		return stackable || noted;
	}

	/**
	 * Checks if is stackable.
	 * 
	 * @return true, if is stackable
	 */
	public boolean isRawStackable() {
		return stackable;
	}

	/**
	 * Checks if is full body.
	 * 
	 * @return true, if is full body
	 */
	public boolean isFullBody() {
		String weapon = getName();
		for (int i = 0; i < Settings.FULL_BODY.length; i++) {
			if (weapon.contains(Settings.FULL_BODY[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is full helm.
	 * 
	 * @return true, if is full helm
	 */
	public boolean isFullHelm() {
		String weapon = getName();
		for (int i = 0; i < Settings.FULL_HAT.length; i++) {
			if (weapon.endsWith(Settings.FULL_HAT[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is full mask.
	 * 
	 * @return true, if is full mask
	 */
	public boolean isFullMask() {
		String weapon = getName();
		for (int i = 0; i < Settings.FULL_MASK.length; i++) {
			if (weapon.endsWith(Settings.FULL_MASK[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is two handed.
	 * 
	 * @return true, if is two handed
	 */
	public boolean isTwoHanded() {
		String wepEquiped = getName();
		int itemId = getId();
		if (itemId == 4212)
			return true;
		else if (itemId == 4214)
			return true;
		else if (wepEquiped.endsWith("2h sword"))
			return true;
		else if (wepEquiped.endsWith("longbow"))
			return true;
		else if (wepEquiped.equals("Seercull"))
			return true;
		else if (wepEquiped.endsWith("shortbow"))
			return true;
		else if (wepEquiped.endsWith("Longbow"))
			return true;
		else if (wepEquiped.endsWith("Shortbow"))
			return true;
		else if (wepEquiped.endsWith("bow full"))
			return true;
		else if (wepEquiped.endsWith("halberd"))
			return true;
		else if (wepEquiped.equals("Granite maul"))
			return true;
		else if (wepEquiped.equals("Karils crossbow"))
			return true;
		else if (wepEquiped.equals("Torags hammers"))
			return true;
		else if (wepEquiped.equals("Veracs flail"))
			return true;
		else if (wepEquiped.equals("Dharoks greataxe"))
			return true;
		else if (wepEquiped.equals("Guthans warspear"))
			return true;
		else if (wepEquiped.equals("Tzhaar-ket-om"))
			return true;
		else if (wepEquiped.endsWith("godsword"))
			return true;
		else if (wepEquiped.equals("Saradomin sword"))
			return true;
		else if (wepEquiped.equalsIgnoreCase("dark bow"))
			return true;
		else if (wepEquiped.startsWith("Anger"))
			return true;
		else
			return false;
	}

	/**
	 * Checks if is shield.
	 * 
	 * @return true, if is shield
	 */
	public boolean isShield() {
		String name = getName();
		for (int i = 0; i < Settings.SHIELDS.length; i++) {
			if (name.contains(Settings.SHIELDS[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the item type.
	 * 
	 * @return the item type
	 */
	public int getItemType() {
		String name = getName().toLowerCase();
		for (int i = 0; i < Settings.CAPES.length; i++) {
			if (name.contains(Settings.CAPES[i].toLowerCase()))
				return 1;
		}
		for (int i = 0; i < Settings.HATS.length; i++) {
			if (name.contains(Settings.HATS[i].toLowerCase()))
				return 0;
		}
		for (int i = 0; i < Settings.BOOTS.length; i++) {
			if (name.endsWith(Settings.BOOTS[i]) || name.startsWith(Settings.BOOTS[i]))
				return 10;
		}
		for (int i = 0; i < Settings.GLOVES.length; i++) {
			if (name.endsWith(Settings.GLOVES[i]) || name.startsWith(Settings.GLOVES[i]))
				return 9;
		}
		for (int i = 0; i < Settings.SHIELDS.length; i++) {
			if (name.contains(Settings.SHIELDS[i].toLowerCase()))
				return 5;
		}
		for (int i = 0; i < Settings.AMULETS.length; i++) {
			if (name.endsWith(Settings.AMULETS[i]) || name.startsWith(Settings.AMULETS[i]) || name.contains(Settings.AMULETS[i]))
				return 2;
		}
		for (int i = 0; i < Settings.ARROWS.length; i++) {
			if (name.toLowerCase().contains(Settings.ARROWS[i].toLowerCase()) || name.startsWith(Settings.ARROWS[i]))
				return 13;
		}
		for (int i = 0; i < Settings.RINGS.length; i++) {
			if (name.endsWith(Settings.RINGS[i]) || name.startsWith(Settings.RINGS[i]))
				return 12;
		}
		for (int i = 0; i < Settings.BODY.length; i++) {
			if (name.contains(Settings.BODY[i].toLowerCase()))
				return 4;
		}
		for (int i = 0; i < Settings.LEGS.length; i++) {
			if (name.contains(Settings.LEGS[i].toLowerCase()))
				return 7;
		}
		return 3;
	}

	/**
	 * Load.
	 */
	public static void load() {
		try {
			definitions = new GameDef[ITEM_DEFINITIONS];
			FileInputStream input = new FileInputStream("./data/itemdefs/itemdefinitions.bin");
			ObjectInputStream in = new ObjectInputStream(input);
			for (int i = 0; i < ITEM_DEFINITIONS; i++) {
				int id = in.readInt();
				int equipId = in.readInt();
				int[] bonus = (int[]) in.readObject();
				boolean noted = in.readBoolean();
				boolean stackable = in.readBoolean();
				boolean members = in.readBoolean();
				double weight = in.readDouble();
				int price = in.readInt();
				String name = (String) in.readObject();
				String examine = (String) in.readObject();
				int[] requirements = (int[]) in.readObject();
				ItemDef def = new ItemDef(id, equipId, bonus, noted, stackable, members, weight, price, name, examine, requirements);
				definitions[def.getId()] = def;
			}
			BufferedReader reader = new BufferedReader(new FileReader("./data/itemdefs/untradeable.txt"));
			String line;
			while ((line = reader.readLine()) != null)
				if (!line.startsWith("//"))
					ItemDef.forId(Integer.parseInt(line)).setTradeable(false);
			ItemDef.forId(313).setStackable(true);
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * For id.
	 * 
	 * @param id
	 *            the id
	 * @return the item def
	 */
	public static ItemDef forId(int id) {
		if (id < 0 || id > definitions.length)
			return produceDefinition(id);
		ItemDef def = (ItemDef) definitions[id];
		if (def == null)
			return produceDefinition(id);
		else
			return def;
	}

	/**
	 * Produce definition.
	 * 
	 * @param itemId
	 *            the item id
	 * @return the item def
	 */
	public static ItemDef produceDefinition(int itemId) {
		return new ItemDef(itemId, -1, null, false, false, true, 0, 0, "Null", "this item has no definition", null);
	}

}
