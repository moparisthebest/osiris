# Osiris Emulator
# Copyright (C) 2011  Garrett Woodard, Blake Beaupain, Travis Burtrum
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU Affero General Public License for more details.
# 
#  You should have received a copy of the GNU Affero General Public License
#  along with this program.  If not, see <http://www.gnu.org/licenses/>.

require 'java'
java_import 'osiris.game.model.item.Item'
java_import 'osiris.game.model.def.ItemDef'
java_import 'osiris.game.model.effect.PoisonEffect'
java_import 'osiris.game.action.ItemActionListener'
java_import 'osiris.game.action.ItemActionListeners'
java_import 'osiris.game.action.item.ItemClickAction'
java_import 'osiris.game.update.block.AnimationBlock'

# Next potion ID's.
POTIONS = Hash.new

# Strength potions
POTIONS[113] = 115
POTIONS[115] = 117
POTIONS[117] = 119

# Attack potions
POTIONS[2428] = 121
POTIONS[121] = 123
POTIONS[123] = 125

# Restore potions
POTIONS[2430] = 127
POTIONS[127] = 129
POTIONS[129] = 131

# Defence potions
POTIONS[2432] = 133
POTIONS[133] = 135
POTIONS[135] = 137

# Prayer potions
POTIONS[2434] = 139
POTIONS[139] = 141
POTIONS[141] = 143

# Ranging potions
POTIONS[2444] = 169
POTIONS[169] = 171
POTIONS[171] = 173

# Energy potions
POTIONS[3008] = 3010
POTIONS[3010] = 3012
POTIONS[3012] = 3014

# Antipoison potions
POTIONS[2446] = 175
POTIONS[175] = 177
POTIONS[177] = 179

# Magic potions
POTIONS[3040] = 3042
POTIONS[3042] = 3044
POTIONS[3044] = 3046

# Super attack potions
POTIONS[2436] = 145
POTIONS[145] = 147
POTIONS[147] = 149

# Super strength potions
POTIONS[2440] = 157
POTIONS[157] = 159
POTIONS[159] = 161

# Super defence potions
POTIONS[2442] = 163
POTIONS[163] = 165
POTIONS[165] = 167

class PotionListener
	include ItemActionListener
	
	# Gets the next (i.e. 1 lower dosage) potion ID for an item.
	def get_next_id item
		if ItemDef.for_id(item.get_id).get_name.include? '(1)' then return 229
		else return POTIONS[item.get_id] end
	end
	
	def apply_effect player, id
		name = ItemDef.for_id(id).get_name.downcase!
		if name.include? 'poison'
			poisoned = true
		elsif name.include? 'defence'
			stat = 1
		elsif name.include? 'attack'
			stat = 0
		elsif name.include? 'strength'
			stat = 2
		elsif name.include? 'magic'
			stat = 6
		elsif name.include? 'ranging'
			stat = 4
		elsif name.include? 'prayer'
			stat = 5
		end
		
		skills = player.get_skills
		if poisoned
			player.get_effects.size.times do |i|
				if player.get_effects.get(i).kind_of? PoisonEffect
					player.get_effects.get(i).cancel		
				end
			end
		else
			if name.include? 'super'
				modifier = ((skills.max_level(stat) * 15) / 100)
				adjustment = 5 + modifier
			else
				if stat == 5
					modifier = (skills.max_level(stat) / 4)
					adjustment = 7 + modifier
				else
					modifier = ((skills.max_level(stat) * 10) / 100)
					adjustment = 3 + modifier
				end
			end
		
			if skills.current_level(stat) >= 99
				if stat == 5
					skills.set_cur_level(stat, 99)
				return end
				skills.set_cur_level(stat, (skills.max_level(stat) + adjustment))
			else
				if stat == 5 and (skills.current_level(stat) + adjustment) >= skills.max_level(stat)
					skills.set_cur_level(stat, skills.max_level(stat))
				else
					skills.set_cur_level(stat, (skills.current_level(stat) + adjustment))
				end
			end
		end
	end
	
	# Called when a player drinks a potion.
	def on_item_action action
		player = action.get_character
		if action.kind_of? ItemClickAction then
			player.get_event_writer.send_message 'You drink some of the potion.'
			player.add_update_block(AnimationBlock.new(player, 829, 0));   
			player.get_inventory.remove_by_slot action.get_slot, 1
			player.get_inventory.add Item.new get_next_id(action.get_item)
			apply_effect action.get_player, action.get_item.get_id
		end
	end
end

# Register the potion listener.
ItemActionListeners.add_listener PotionListener.new,
113, 115, 117, 119, 2428, 121, 123, 125, 2430, 127, 129, 131, 2432, 133, 135, 137, 2434, 139, 141, 143, 2444, 169, 171, 173, 3008, 3010, 3012, 3014, 3040, 3042, 3044, 3046, 2436, 145, 147, 149, 2440, 157, 159, 161, 2442, 163, 165, 167, 2446, 175, 177, 179
