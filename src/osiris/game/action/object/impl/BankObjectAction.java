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

import osiris.game.action.DistancedAction;
import osiris.game.action.ObjectActionListener;
import osiris.game.action.impl.BankAction;
import osiris.game.action.object.ObjectAction;
import osiris.game.model.Player;

/**
 * The Class BankObjectAction.
 * 
 * @author samuraiblood2
 * 
 */
public class BankObjectAction implements ObjectActionListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.action.ObjectActionListener#onObjectAction(osiris.game.action
	 * .object.ObjectAction)
	 */
	@Override
	public void onObjectAction(ObjectAction action) {
		final Player player = (Player) action.getCharacter();

		new DistancedAction(player, action.getObjectPosition(), 1) {

			@Override
			public void execute() {
				new BankAction(player);
			}

		}.run();
	}

}
