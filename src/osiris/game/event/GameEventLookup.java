package osiris.game.event;

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

import osiris.game.event.impl.ActionButtonEvent;
import osiris.game.event.impl.CastSpellEvent;
import osiris.game.event.impl.ChatEvent;
import osiris.game.event.impl.ChatOptionsEvent;
import osiris.game.event.impl.CommandEvent;
import osiris.game.event.impl.ContactsEvent;
import osiris.game.event.impl.ExamineEvent;
import osiris.game.event.impl.HdNotificationEvent;
import osiris.game.event.impl.IgnoredEvent;
import osiris.game.event.impl.ItemOnItemEvent;
import osiris.game.event.impl.ItemOnObjectEvent;
import osiris.game.event.impl.ItemSystemsEvent;
import osiris.game.event.impl.LoginRequestEvent;
import osiris.game.event.impl.LogoutEvent;
import osiris.game.event.impl.MovementEvent;
import osiris.game.event.impl.NpcAttackEvent;
import osiris.game.event.impl.NpcOptionEvent;
import osiris.game.event.impl.ObjectEvent;
import osiris.game.event.impl.OptionClickEvent;
import osiris.game.event.impl.PlayerOptionEvent;
import osiris.game.event.impl.RegionLoadedEvent;
import osiris.game.event.impl.ServiceRequestEvent;
import osiris.game.event.impl.UnhandledEvent;
import osiris.game.event.impl.UpdateRequestEvent;
import osiris.game.event.impl.ValueXEvent;

// TODO: Auto-generated Javadoc
/**
 * Provides lookup utilities for game events.
 * 
 * @author Blake
 * 
 */
public class GameEventLookup {

	/**
	 * The events.
	 */
	private static Class<?>[] events = new Class<?>[259];

	/**
	 * Lookup.
	 * 
	 * @param opcode
	 *            the opcode
	 * @return the class
	 */
	@SuppressWarnings("unchecked")
	public static Class<GameEvent> lookup(int opcode) {
		return (Class<GameEvent>) events[opcode];
	}

	static {
		// Request events.
		events[256] = ServiceRequestEvent.class;
		events[257] = UpdateRequestEvent.class;
		events[258] = LoginRequestEvent.class;

		// Ignored events
		events[115] = events[247] = events[22] = events[117] = events[59] = events[99] = events[248] = IgnoredEvent.class;

		// Game events.
		events[224] = ItemOnObjectEvent.class;
		events[43] = ValueXEvent.class;
		events[24] = events[70] = CastSpellEvent.class;
		events[49] = MovementEvent.class;
		events[119] = MovementEvent.class;
		events[138] = MovementEvent.class;
		events[138] = MovementEvent.class;
		events[107] = CommandEvent.class;
		events[3] = events[167] = events[179] = events[203] = events[211] = events[201] = events[220] = ItemSystemsEvent.class;
		events[203] = ItemSystemsEvent.class;
		events[160] = events[253] = PlayerOptionEvent.class;
		events[123] = NpcAttackEvent.class;
		events[21] = events[113] = events[169] = events[232] = events[133] = events[250] = ActionButtonEvent.class;
		events[233] = events[214] = events[173] = ActionButtonEvent.class;
		events[212] = ChatOptionsEvent.class;
		events[222] = ChatEvent.class;
		events[129] = HdNotificationEvent.class;
		events[158] = events[228] = ObjectEvent.class;
		events[47] = LogoutEvent.class;
		events[40] = ItemOnItemEvent.class;
		events[30] = events[61] = events[132] = events[2] = events[178] = ContactsEvent.class;
		events[60] = RegionLoadedEvent.class;
		events[38] = events[84] = events[88] = ExamineEvent.class;
		events[52] = events[7] = events[199] = NpcOptionEvent.class;
		events[63] = OptionClickEvent.class;

		for (int i = 0; i < events.length; i++) {
			if (events[i] == null) {
				events[i] = UnhandledEvent.class;
			}
		}
	}

}
