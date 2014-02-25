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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import osiris.data.NpcDropLoader;
import osiris.data.parser.Parser;
import osiris.game.model.NpcDrop;
import osiris.game.model.NpcDropContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class NpcDropParser.
 * 
 * @author Blake Beaupain
 * @author Boomer
 * @author samuraiblood2
 * 
 */
public class NpcDropParser extends Parser {

	/**
	 * Instantiates a new npc drop parser.
	 * 
	 * @param file
	 *            the file
	 */
	public NpcDropParser(String file) {
		super(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.data.parser.Parser#onParse(org.w3c.dom.Document)
	 */
	@Override
	public void onParse(Document doc) throws Exception {
		NodeList dropList = doc.getElementsByTagName("npcDrop");
		for (int i = 0; i < dropList.getLength(); i++) {
			Element dropElement = (Element) dropList.item(i);
			NpcDropContainer container;
			List<Integer> npcIds = new ArrayList<Integer>();
			List<NpcDrop> npcDrops = new ArrayList<NpcDrop>();

			// Get the NPC IDs.
			Element npcIdsElement = (Element) dropElement.getElementsByTagName("npcIds").item(0);
			if (npcIdsElement != null) {
				String value = npcIdsElement.getFirstChild().getNodeValue();
				StringTokenizer st = new StringTokenizer(value, ", ");
				while (st.hasMoreTokens()) {
					Integer id = Integer.parseInt(st.nextToken());
					npcIds.add(id);
				}
			}

			// Get the items.
			NodeList itemsList = (NodeList) dropElement.getElementsByTagName("item");
			for (int n = 0; n < itemsList.getLength(); n++) {
				Element itemElement = (Element) itemsList.item(n);
				int itemId = Integer.parseInt(itemElement.getAttribute("id"));
				String amount = itemElement.getAttribute("amount");
				double chance = Double.parseDouble(itemElement.getAttribute("chance").replaceAll("%", "")) / 100;
				StringTokenizer st = new StringTokenizer(amount, "-");
				if (st.countTokens() == 1)
					npcDrops.add(new NpcDrop(itemId, Integer.parseInt(st.nextToken()), chance));
				else
					npcDrops.add(new NpcDrop(itemId, Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), chance));
			}

			container = new NpcDropContainer(npcIds, npcDrops);
			NpcDropLoader.add(container);
		}
	}
}
