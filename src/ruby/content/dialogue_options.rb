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

java_import 'osiris.Main'
java_import 'osiris.game.action.impl.DialogueAction'
java_import 'osiris.game.action.impl.BankAction'

def dialogueOption(player, id)
	dialogue = player.get_currently_open_dialogue
	if id == 2
		if dialogue.get_npc == 494
			BankAction.new(player)
		elsif dialogue.get_npc == -2
			move = get_position_change(dialogue.get_next)
			player.teleport(move.get_up)
			end_conversation(player, dialogue)
		else continue_conversation(player, dialogue) end
	else
		if dialogue.get_npc == 304
			player.get_current_action.cancel
			return
		elsif dialogue.get_npc == -2
			move = get_position_change(dialogue.get_next)
			player.teleport(move.get_down)
			end_conversation(player, dialogue)
		else continue_conversation(player, dialogue) end
	end
end

def end_conversation(player, dialogue)
	if player.get_current_action.kind_of? DialogueAction then
		player.get_current_action.cancel
	else return end
end

def continue_conversation(player, dialogue)
	if dialogue.get_next == -1
		end_conversation(player, dialogue)
	else
		DialogueAction.new(player, get_next_dialogue(dialogue.get_next, dialogue.get_npc)).run
	end
end

def get_next_dialogue(id, npc)
	Main.get_dialogues.length.times do |i|
		dialogue = Main.get_dialogues.get(i)
		if dialogue.get_id == id and dialogue.get_npc == npc
			return Main.get_dialogues.get(i)
		end
	end
end

def get_position_change(id)
	Main.get_stairs.length.times do |i|
		info = Main.get_stairs.get(i)
		if info.get_id == id
			return Main.get_stairs.get(i)
		end
	end
end
