package osiris.game.update.block;

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

import static osiris.util.Settings.SLOT_CHEST;
import static osiris.util.Settings.SLOT_FEET;
import static osiris.util.Settings.SLOT_HANDS;
import static osiris.util.Settings.SLOT_HAT;
import static osiris.util.Settings.SLOT_LEGS;
import static osiris.util.Settings.SLOT_SHIELD;
import osiris.game.model.Appearance;
import osiris.game.model.Player;
import osiris.game.model.def.ItemDef;
import osiris.game.model.item.ItemContainer;
import osiris.game.update.UpdateBlock;
import osiris.io.PacketWriter;
import osiris.util.Utilities;

/**
 * An appearance update block.
 * 
 * @author Blake
 * 
 */
public class AppearanceBlock extends UpdateBlock {

	/** The Constant STAND_INDICE. */
	public static final int STAND_INDICE = 0x328;

	/** The Constant WALK_INDICE. */
	public static final int WALK_INDICE = 0x333;

	/** The Constant RUN_INDICE. */
	public static final int RUN_INDICE = 0x338;

	/**
	 * Instantiates a new appearance block.
	 * 
	 * @param player
	 *            the player
	 */
	public AppearanceBlock(Player player) {
		super(player, 0x80, 8);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.update.UpdateBlock#append(osiris.io.PacketWriter)
	 */

	@Override
	public void append(PacketWriter writer) throws Exception {
		Player player = (Player) getCharacter();
		ItemContainer e = player.getEquipment();
		Appearance a = player.getAppearance();
		PacketWriter p = new PacketWriter();

		// Send the initial attributes.
		p.writeByte(a.getGender());
		if ((a.getGender() & 0x2) == 2) {
			p.writeByte(0);
			p.writeByte(0);
		}
		p.writeByte(player.getSkullIcon()); // skull
		p.writeByte(player.getHeadIcon()); // prayer

		if (player.getNpcId() == -1) {
			for (int i = 0; i < 4; i++) {
				if (e.getItem(i) != null) {
					p.writeShort(32768 + ItemDef.forId(e.getItem(i).getId()).getEquipId());
				} else {
					p.writeByte(0);
				}
			}

			// Chest slot
			if (e.getItem(SLOT_CHEST) != null) {
				p.writeShort(32768 + ItemDef.forId(e.getItem(SLOT_CHEST).getId()).getEquipId());
			} else {
				p.writeShort(0x100 + a.getLooks()[Appearance.LOOK_CHEST]);
			}

			// Shield slot
			if (e.getItem(SLOT_SHIELD) != null) {
				p.writeShort(32768 + ItemDef.forId(e.getItem(SLOT_SHIELD).getId()).getEquipId());
			} else {
				p.writeByte(0);
			}

			// Arms slot
			if (e.getItem(SLOT_CHEST) != null) {
				if (!ItemDef.forId(e.getItem(SLOT_CHEST).getId()).isFullBody()) {
					p.writeShort(0x100 + a.getLooks()[Appearance.LOOK_ARMS]);
				} else {
					p.writeByte(0);
				}
			} else {
				p.writeShort(0x100 + a.getLooks()[Appearance.LOOK_ARMS]);
			}

			// Legs slot
			if (e.getItem(SLOT_LEGS) != null) {
				p.writeShort(32768 + ItemDef.forId(e.getItem(SLOT_LEGS).getId()).getEquipId());
			} else {
				p.writeShort(0x100 + a.getLooks()[Appearance.LOOK_LEGS]);
			}

			// Head slot
			if (e.getItem(SLOT_HAT) != null) {
				if (ItemDef.forId(e.getItem(SLOT_HAT).getId()).isFullHelm() || ItemDef.forId(e.getItem(SLOT_HAT).getId()).isFullMask()) {
					p.writeByte(0);
				} else {
					p.writeShort(0x100 + a.getLooks()[Appearance.LOOK_HEAD]);
				}
			} else {
				p.writeShort(0x100 + a.getLooks()[Appearance.LOOK_HEAD]);
			}

			// Hands slot
			if (e.getItem(SLOT_HANDS) != null) {
				p.writeShort(32768 + ItemDef.forId(e.getItem(SLOT_HANDS).getId()).getEquipId());
			} else {
				p.writeShort(0x100 + a.getLooks()[Appearance.LOOK_HANDS]);
			}

			// Feet slot
			if (e.getItem(SLOT_FEET) != null) {
				p.writeShort(32768 + ItemDef.forId(e.getItem(SLOT_FEET).getId()).getEquipId());
			} else {
				p.writeShort(0x100 + a.getLooks()[Appearance.LOOK_FEET]);
			}

			// Beard slot
			if (e.getItem(SLOT_HAT) != null) {
				if (ItemDef.forId(e.getItem(SLOT_HAT).getId()).isFullMask()) {
					p.writeByte(0);
				} else {
					p.writeShort(0x100 + a.getLooks()[Appearance.LOOK_BEARD]);
				}
			} else {
				p.writeShort(0x100 + a.getLooks()[Appearance.LOOK_BEARD]);
			}
		} else {
			p.writeShort(-1);
			p.writeShort(player.getNpcId());
		}

		// Colors
		for (int color : a.getColors()) {
			p.writeByte(color);
		}

		// Animation indices
		p.writeShort(player.getStandAnimation());
		p.writeShort(0x337);
		p.writeShort(player.getWalkAnimation());
		p.writeShort(0x334);
		p.writeShort(0x335);
		p.writeShort(0x336);
		p.writeShort(player.getRunAnimation());

		// Other misc attributes
		p.writeLong(Utilities.stringToLong(player.getUsername()));
		p.writeByte(player.getSkills().getCombatLevel()); // Combat
		// level.
		p.writeShort(0);

		// Write the props packet.
		writer.writeByte(p.getPacket().getBuffer().writerIndex());
		writer.getPacket().getBuffer().writeBytes(p.getPacket().getBuffer());
	}
}
