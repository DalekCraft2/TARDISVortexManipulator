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

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * TabCompleter for /vmm
 */
public class TVMMessageTabCompleter extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> rootSubs = ImmutableList.of("msg", "list", "read", "delete", "clear");
    private final ImmutableList<String> inOutSubs = ImmutableList.of("in", "out");

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length <= 1) {
            return partial(args[0], rootSubs);
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("msg")) {
                return null;
            }
            if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("clear")) {
                return partial(args[1], inOutSubs);
            }
        }
        return ImmutableList.of();
    }
}
