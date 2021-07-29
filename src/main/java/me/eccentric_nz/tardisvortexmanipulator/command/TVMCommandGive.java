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

import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class TVMCommandGive implements CommandExecutor {

    private final TARDISVortexManipulatorPlugin plugin;
    private final int full;

    public TVMCommandGive(TARDISVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
        full = this.plugin.getConfig().getInt("tachyon_use.max");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vmgive")) {
            if (!sender.hasPermission("tardis.admin")) {
                sender.sendMessage(plugin.getMessagePrefix() + "You don't have permission to use that command!");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(plugin.getMessagePrefix() + "You need to specify a player and amount!");
                return true;
            }
            Player player = plugin.getServer().getPlayer(args[0]);
            if (player == null || !player.isOnline()) { // player must be online
                TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                return true;
            }
            // Look up this player's UUID
            UUID uuid = player.getUniqueId();
            // check for existing record
            TVMResultSetManipulator resultSetManipulator = new TVMResultSetManipulator(plugin, uuid.toString());
            if (resultSetManipulator.resultSet()) {
                int tachyonLevel = resultSetManipulator.getTachyonLevel();
                int amount;
                if (args[1].equalsIgnoreCase("full")) {
                    amount = full;
                } else if (args[1].equalsIgnoreCase("empty")) {
                    amount = 0;
                } else {
                    try {
                        amount = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getMessagePrefix() + "The last argument must be a number, 'full', or 'empty'");
                        return true;
                    }
                    if (tachyonLevel + amount > full) {
                        amount = full;
                    } else {
                        amount += tachyonLevel;
                    }
                }
                HashMap<String, Object> set = new HashMap<>();
                set.put("tachyon_level", amount);
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                new TVMQueryFactory(plugin).doUpdate("manipulator", set, where);
                sender.sendMessage(plugin.getMessagePrefix() + "Tachyon level set to " + amount);
            } else {
                sender.sendMessage(plugin.getMessagePrefix() + "Player does not have a Vortex Manipulator!");
            }
            return true;
        }
        return false;
    }
}
