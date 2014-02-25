package osiris.game.action.impl;

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
import osiris.game.model.Character;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.game.model.def.ItemDef;
import osiris.game.model.ground.GroundItem;
import osiris.game.model.ground.GroundManager;

/**
 * The Class GroundItemAction.
 * 
 * @author samuraiblood2
 * 
 */
public class PickupItemAction extends DistancedAction {

	/** The id. */
	private int id;

	/** The position. */
	private Position position;

	/**
	 * Instantiates a new ground item action.
	 * 
	 * @param character
	 *            the character
	 * @param position
	 *            the position
	 * @param id
	 *            the id
	 */
	public PickupItemAction(Character character, Position position, int id) {
		super(character, position, 1);
		this.id = id;
		this.position = position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.DistancedAction#execute()
	 */
	@Override
	public void execute() {
		if (getCharacter() instanceof Player) {
			Player player = (Player) getCharacter();
			if (player == null) {
				return;
			}

			if (player.getInventory().isFull() && (!ItemDef.forId(id).isStackable() || (ItemDef.forId(id).isStackable() && player.getInventory().getItemById(id) == null))) {
				player.getEventWriter().sendMessage("You don't have enough room in your inventory!");
				return;
			}

			for (GroundItem item : player.getGroundItems()) {
				if (item.getPosition().equals(position) && item.getItem().getId() == id) {
					if (!getPlayer().getInventory().add(item.getItem()))
						getPlayer().getEventWriter().sendMessage("You have no room open in your inventory!");
					else {
						GroundManager.getManager().pickupItem(item);
					}
					break;
				}
			}
		}
	}

}
