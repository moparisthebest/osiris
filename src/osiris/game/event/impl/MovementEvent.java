package osiris.game.event.impl;

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

import osiris.game.event.GameEvent;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.io.ByteForm;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.ValueType;

/**
 * The Class MovementEvent.
 * 
 * @author Blake
 * 
 */
public class MovementEvent extends GameEvent {

	/**
	 * Instantiates a new movement event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public MovementEvent(Player player, Packet packet) {
		super(player, packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.event.GameEvent#process()
	 */

	@Override
	public void process() throws Exception {
		if (getPlayer().isMovementLocked())
			return;
		if (getPacket().getHeader().getOpcode() != 138) {
			if (getPlayer().getCurrentAction() != null) {
				getPlayer().getCurrentAction().cancel();
				getPlayer().stopAnimation();
			}
			getPlayer().setInteractingCharacter(null);
		}

		if (!getPlayer().canMove())
			return;

		getPlayer().getEventWriter().sendCloseInterface();
		int size = getPacket().getHeader().getLength();
		if (getPacket().getHeader().getOpcode() == 119) {
			size -= 14;
		}

		int steps = (size - 5) / 2;
		PacketReader reader = new PacketReader(getPacket());

		// Get the first step.
		int firstX = reader.readShort(ValueType.A, ByteForm.LITTLE);
		int firstY = reader.readShort(ValueType.A);

		// Check if we can make this first step.
		if (!getPlayer().getMovementQueue().addFirstStep(new Position(firstX, firstY))) {
			return;
		}

		// Check if this path needs to be ran instead of walked.
		boolean runningQueue = reader.readByte(ValueType.C) == 1;
		getPlayer().getMovementQueue().setRunningQueue(runningQueue);

		// Get the steps.
		for (int i = 0; i < steps; i++) {
			int deltaX = (byte) reader.readByte();
			int deltaY = (byte) reader.readByte(ValueType.S);
			int stepX = deltaX + firstX;
			int stepY = deltaY + firstY;
			getPlayer().getMovementQueue().addStep(new Position(stepX, stepY));
		}
	}
}
