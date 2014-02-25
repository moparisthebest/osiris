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

import osiris.game.action.Action;
import osiris.game.model.Player;
import osiris.game.model.item.Item;
import osiris.game.model.magic.MagicManager;
import osiris.game.model.magic.SpellBook;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class SwitchAutoCastAction.
 * 
 * @author Boomer
 */
public class SwitchAutoCastAction extends Action {

	/** The spell id. */
	private int spellId;

	/** The defence. */
	private boolean defence;

	/** The completed. */
	private boolean completed = false;

	/**
	 * Instantiates a new action.
	 * 
	 * @param player
	 *            the player
	 * @param defence
	 *            the defence
	 */
	public SwitchAutoCastAction(Player player, boolean defence) {
		super(player);
		this.spellId = -1;
		this.defence = defence;
		SpellBook spellBook = player.getSpellBook();
		if (spellBook == SpellBook.LUNAR) {
			cancel();
			return;
		}
		Item weapon = player.getEquipment().getItem(Settings.SLOT_WEAPON);
		if (weapon == null) {
			cancel();
			return;
		}
		if ((spellBook == SpellBook.ANCIENT && weapon.getId() != 4675) || (spellBook == SpellBook.NORMAL && weapon.getId() == 4675)) {
			player.getEventWriter().sendMessage("You can only autocast ANCIENT spells with an ANCIENT staff!");
			cancel();
			return;
		}
		player.getEventWriter().sendTab(73, spellBook == SpellBook.ANCIENT ? 388 : 319);
	}

	/**
	 * Sets the auto spell id.
	 * 
	 * @param spellId
	 *            the spell id
	 * @return the switch auto cast action
	 */
	public SwitchAutoCastAction setAutoSpellId(int spellId) {
		this.spellId = spellId;

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
		if (!completed)
			getPlayer().getCombatManager().setAutoSpell(-1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
		if (spellId != -1) {
			int[] spells = null;
			switch (getPlayer().getSpellBook()) {
			case NORMAL:
				spells = MagicManager.NORMAL_AUTOCAST_SPELLS;
				break;
			case ANCIENT:
				spells = MagicManager.ANCIENT_AUTOCAST_SPELLS;
				break;
			}
			if (spells == null) {
				cancel();
				return;
			}
			int spellSprite = 45;
			if (getPlayer().getSpellBook() == SpellBook.ANCIENT)
				spellSprite = 13;
			if (defence)
				spellSprite += 100;
			spellSprite += spellId * 2;
			getPlayer().getCombatManager().setAutoSpell(spells[spellId], getPlayer().getSpellBook(), defence, spellSprite);
			completed = true;
		}
		getPlayer().refreshWeaponTab();

	}
}
