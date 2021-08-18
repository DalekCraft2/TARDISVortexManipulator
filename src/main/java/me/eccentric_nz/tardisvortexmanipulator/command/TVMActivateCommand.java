/*
 * Copyright (C) 2021 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TVMActivateCommand implements CommandExecutor {

    private final TARDISVortexManipulatorPlugin plugin;

    public TVMActivateCommand(TARDISVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vmactivate")) {
            if (!sender.hasPermission("tardis.admin")) {
                sender.sendMessage(plugin.getMessagePrefix() + "You don't have permission to use that command!");
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage(plugin.getMessagePrefix() + "You need to specify a player name!");
                return true;
            }
            Player player = plugin.getServer().getPlayer(args[0]);
            if (player == null || !player.isOnline()) {
                sender.sendMessage(plugin.getMessagePrefix() + "Could not find player! Are they online?");
                return true;
            }
            String uuid = player.getUniqueId().toString();
            // check for existing record
            TVMResultSetManipulator resultSetManipulator = new TVMResultSetManipulator(plugin, uuid);
            if (!resultSetManipulator.resultSet()) {
                HashMap<String, Object> set = new HashMap<>();
                set.put("uuid", uuid);
                new TVMQueryFactory(plugin).doInsert("manipulator", set);
                sender.sendMessage(plugin.getMessagePrefix() + "Vortex Manipulator activated!");
            } else {
                sender.sendMessage(plugin.getMessagePrefix() + "The Vortex Manipulator is already activated!");
            }
            return true;
        }
        return false;
    }
}
