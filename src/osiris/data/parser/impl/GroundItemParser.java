package osiris.data.parser.impl;

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

import static osiris.util.Settings.DEFAULT_Z;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import osiris.data.parser.Parser;
import osiris.game.model.Position;
import osiris.game.model.def.ItemDef;
import osiris.game.model.ground.GroundItem;
import osiris.game.model.ground.GroundManager;
import osiris.game.model.item.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class GroundItemParser.
 * 
 * @author samuraiblood2
 * 
 */
public class GroundItemParser extends Parser {

	/**
	 * Instantiates a new ground item parser.
	 * 
	 * @param file
	 *            the file
	 */
	public GroundItemParser(String file) {
		super(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.data.parser.Parser#onParse(org.w3c.dom.Document)
	 */
	@Override
	public void onParse(Document doc) throws Exception {
		NodeList itemsList = (NodeList) doc.getElementsByTagName("ground");
		if (itemsList == null) {
			return;
		}

		for (int i = 0; i < itemsList.getLength(); i++) {
			Element itemElement = (Element) itemsList.item(i);
			int id = Integer.parseInt(itemElement.getAttribute("id"));

			int amount = 1;
			if (ItemDef.forId(id).isStackable()) {
				String amountAttribute = itemElement.getAttribute("amount").trim();
				if (amountAttribute != null) {
					amount = Integer.parseInt(amountAttribute);
				}
			}

			String respawnAttribute = itemElement.getAttribute("respawn");
			boolean respawn = respawnAttribute != null && Boolean.parseBoolean(respawnAttribute);

			int x = Integer.parseInt(getTextValue(itemElement, "X").trim());
			int y = Integer.parseInt(getTextValue(itemElement, "Y").trim());

			String zNode = getTextValue(itemElement, "Z");
			int z = DEFAULT_Z;
			if (zNode != null) {
				z = Integer.parseInt(zNode.trim());
			}
			GroundItem item = new GroundItem(Item.create(id, amount).setRespawns(respawn), new Position(x, y, z));
			GroundManager.getManager().dropItem(item);
		}
	}
}
