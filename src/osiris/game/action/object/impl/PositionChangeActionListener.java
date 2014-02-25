package osiris.game.action.object.impl;

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

import osiris.data.parser.impl.PositionChangeParser;
import osiris.data.parser.impl.WorldObjectParser;
import osiris.game.action.ObjectActionListener;
import osiris.game.action.impl.DialogueAction;
import osiris.game.action.object.ObjectAction;
import osiris.game.model.Player;
import osiris.game.model.WorldObject;
import osiris.game.model.WorldObjects;
import osiris.game.model.dialogues.Dialogue;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving positionChangeAction events. The class
 * that is interested in processing a positionChangeAction event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addPositionChangeActionListener<code> method. When
 * the positionChangeAction event occurs, that object's appropriate
 * method is invoked.
 * 
 * @author Boomer
 */
public class PositionChangeActionListener implements ObjectActionListener {

	/** The info. */
	private PositionChangeParser.PositionChangeInfo info;

	/**
	 * Instantiates a new position change action listener.
	 * 
	 * @param info
	 *            the info
	 */
	public PositionChangeActionListener(PositionChangeParser.PositionChangeInfo info) {
		this.info = info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.action.ObjectActionListener#onObjectAction(osiris.game.action
	 * .object.ObjectAction)
	 */
	public void onObjectAction(ObjectAction objectAction) {
		Player player = objectAction.getPlayer();
		if (info.getType() == PositionChangeParser.Type.DOOR) {
			WorldObject object = WorldObjects.getObject(info.getLocation());
			if (object == null) {
				object = new WorldObject(info.getId(), info.getFrom().getId(), 0, info.getLocation());
			}
			WorldObjectParser.Face switchTo = info.getCurrent() == info.getTo() ? info.getFrom() : info.getTo();
			WorldObjects.removeObject(object);
			WorldObjects.addObject(new WorldObject(info.getId(), switchTo.getId(), 0, info.getLocation()));
			WorldObjects.refresh(player);
			info.setCurrent(switchTo);
		} else if (info.getType() == PositionChangeParser.Type.STAIR) {
			if (info.getMove(1) != null && info.getMove(0) != null) {
				Dialogue dialogue = new Dialogue(Dialogue.Type.OPTION, 1);
				List<String> lines = new ArrayList<String>();
				lines.add("Go up.");
				lines.add("Go down.");
				dialogue.setLines(lines);
				dialogue.setNpc(-2);
				dialogue.setNext(info.getId());
				new DialogueAction(player, dialogue).run();
			} else {
				if (info.getMove(1) != null) {
					player.teleport(info.getMove(1));
				} else if (info.getMove(0) != null) {
					player.teleport(info.getMove(0));
				}
			}
		}
	}

}
