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

import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TvmUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TvmCommandMessage implements CommandExecutor {

    private final TardisVortexManipulatorPlugin plugin;

    public TvmCommandMessage(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vmmessage")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "That command cannot be used from the console!");
                return true;
            }
            if (!player.hasPermission("vm.message")) {
                player.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(TardisVortexManipulatorPlugin.plugin.getItemKey(), PersistentDataType.STRING) && itemStack.getItemMeta().getPersistentDataContainer().get(TardisVortexManipulatorPlugin.plugin.getItemKey(), PersistentDataType.STRING).equals("vortex_manipulator")) {
                if (args.length < 2) {
                    player.sendMessage(plugin.getPluginName() + "Incorrect command usage!");
                    return false;
                }
                String first = args[0].toLowerCase(); // TODO Figure out how to name these three "first"s differently.
                try {
                    FIRST f = FIRST.valueOf(first);
                    switch (f) {
                        case MSG -> {
                            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[1]);
                            if (offlinePlayer == null) {
                                player.sendMessage(plugin.getPluginName() + "Could not find a player with that name!");
                                return true;
                            }
                            String ofp_uuid = offlinePlayer.getUniqueId().toString();
                            // check they have a Vortex Manipulator
                            TvmResultSetManipulator tvmResultSetManipulator = new TvmResultSetManipulator(plugin, ofp_uuid);
                            if (!tvmResultSetManipulator.resultSet()) {
                                player.sendMessage(plugin.getPluginName() + args[1] + " does not have a Vortex Manipulator!");
                                return true;
                            }
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                stringBuilder.append(args[i]).append(" ");
                            }
                            String message = stringBuilder.toString();
                            HashMap<String, Object> whereOfflinePlayer = new HashMap<>();
                            whereOfflinePlayer.put("uuid_to", ofp_uuid);
                            whereOfflinePlayer.put("uuid_from", player.getUniqueId().toString());
                            whereOfflinePlayer.put("message", message);
                            whereOfflinePlayer.put("date", System.currentTimeMillis());
                            new TvmQueryFactory(plugin).doInsert("messages", whereOfflinePlayer);
                            player.sendMessage(plugin.getPluginName() + "Message sent.");
                        }
                        case LIST -> {
                            String uuid = player.getUniqueId().toString();
                            if (args.length == 2) {
                                if (args[1].equalsIgnoreCase("out")) {
                                    // list outbox
                                    TvmResultSetOutbox tvmResultSetOutbox = new TvmResultSetOutbox(plugin, uuid, 0, 10);
                                    if (tvmResultSetOutbox.resultSet()) {
                                        TvmUtils.sendOutboxList(player, tvmResultSetOutbox, 1);
                                    } else {
                                        player.sendMessage(plugin.getPluginName() + "There are no messages in your outbox.");
                                        return true;
                                    }
                                } else {
                                    // list inbox
                                    TvmResultSetInbox tvmResultSetInbox = new TvmResultSetInbox(plugin, uuid, 0, 10);
                                    if (tvmResultSetInbox.resultSet()) {
                                        TvmUtils.sendInboxList(player, tvmResultSetInbox, 1);
                                    } else {
                                        player.sendMessage(plugin.getPluginName() + "There are no messages in your inbox.");
                                        return true;
                                    }
                                }
                            }
                            if (args.length < 3) {
                                player.sendMessage(plugin.getPluginName() + "You need to specify a page number!");
                                return true;
                            }
                            int page = parseNum(args[2]);
                            if (page == -1) {
                                player.sendMessage(plugin.getPluginName() + "Invalid page number!");
                                return true;
                            }
                            int start = (page * 10) - 10;
                            int limit = page * 10;
                            if (args[1].equalsIgnoreCase("out")) {
                                // outbox
                                TvmResultSetOutbox tvmResultSetOutbox = new TvmResultSetOutbox(plugin, uuid, start, limit);
                                if (tvmResultSetOutbox.resultSet()) {
                                    TvmUtils.sendOutboxList(player, tvmResultSetOutbox, page);
                                } else {
                                    player.sendMessage(plugin.getPluginName() + "There are no messages in your outbox.");
                                    return true;
                                }
                            } else {
                                // inbox
                                TvmResultSetInbox tvmResultSetInbox = new TvmResultSetInbox(plugin, uuid, start, limit);
                                if (tvmResultSetInbox.resultSet()) {
                                    TvmUtils.sendInboxList(player, tvmResultSetInbox, page);
                                } else {
                                    player.sendMessage(plugin.getPluginName() + "There are no messages in your inbox.");
                                    return true;
                                }
                            }
                        }
                        case READ -> {
                            int readId = parseNum(args[1]);
                            if (readId != -1) {
                                TvmResultSetMessageById tvmResultSetMessageById = new TvmResultSetMessageById(plugin, readId);
                                if (tvmResultSetMessageById.resultSet()) {
                                    TvmUtils.readMessage(player, tvmResultSetMessageById.getMessage());
                                    // update read status
                                    new TvmQueryFactory(plugin).setReadStatus(readId);
                                } else {
                                    player.sendMessage(plugin.getPluginName() + "No message exists with that id, use /vmm list [in|out] first!");
                                    return true;
                                }
                            }
                        }
                        case DELETE -> {
                            int deleteId = parseNum(args[1]);
                            if (deleteId != -1) {
                                TvmResultSetMessageById tvmResultSetMessageById = new TvmResultSetMessageById(plugin, deleteId);
                                if (tvmResultSetMessageById.resultSet()) {
                                    HashMap<String, Object> where = new HashMap<>();
                                    where.put("message_id", deleteId);
                                    new TvmQueryFactory(plugin).doDelete("messages", where);
                                    player.sendMessage(plugin.getPluginName() + "Message deleted.");
                                }
                            } else {
                                player.sendMessage(plugin.getPluginName() + "No message exists with that id, use /vmm list [in|out] first!");
                                return true;
                            }
                        }
                        default -> {
                            // clear
                            if (!args[1].equalsIgnoreCase("in") && !args[1].equalsIgnoreCase("out")) {
                                player.sendMessage(plugin.getPluginName() + "You need to specify which mail box you want to clear (in or out)!");
                                return true;
                            }
                            TvmQueryFactory tvmQueryFactory = new TvmQueryFactory(plugin);
                            HashMap<String, Object> where = new HashMap<>();
                            String which = "Outbox";
                            if (args[1].equalsIgnoreCase("out")) {
                                where.put("uuid_from", player.getUniqueId().toString());
                            } else {
                                where.put("uuid_to", player.getUniqueId().toString());
                                which = "Inbox";
                            }
                            tvmQueryFactory.doDelete("messages", where);
                            player.sendMessage(plugin.getPluginName() + which + " cleared.");
                        }
                    }
                } catch (IllegalArgumentException illegalArgumentException) {
                    player.sendMessage(plugin.getPluginName() + "Incorrect command usage!");
                    return false;
                }
                return true;
            } else {
                player.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }

    private int parseNum(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    private enum FIRST {

        MSG,
        LIST,
        READ,
        DELETE,
        CLEAR
    }
}
