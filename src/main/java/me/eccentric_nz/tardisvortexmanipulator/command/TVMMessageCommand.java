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
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
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

public class TVMMessageCommand implements CommandExecutor {

    private final TARDISVortexManipulatorPlugin plugin;

    public TVMMessageCommand(TARDISVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            sender.sendMessage(plugin.getMessagePrefix() + "That command cannot be used from the console!");
            return true;
        }
        if (!player.hasPermission("vm.message")) {
            player.sendMessage(plugin.getMessagePrefix() + "You don't have permission to use that command!");
            return true;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(TARDISVortexManipulatorPlugin.plugin.getItemKey(), PersistentDataType.STRING) && itemStack.getItemMeta().getPersistentDataContainer().get(TARDISVortexManipulatorPlugin.plugin.getItemKey(), PersistentDataType.STRING).equals("vortex_manipulator")) {
            if (args.length < 2) {
                player.sendMessage(plugin.getMessagePrefix() + "Incorrect command usage!");
                return false;
            }
            String sub = args[0].toLowerCase();
            try {
                FIRST first = FIRST.valueOf(sub);
                switch (first) {
                    case MSG -> {
                        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[1]);
                        String offlinePlayerUuid = offlinePlayer.getUniqueId().toString();
                        // check they have a Vortex Manipulator
                        TVMResultSetManipulator resultSetManipulator = new TVMResultSetManipulator(plugin, offlinePlayerUuid);
                        if (!resultSetManipulator.resultSet()) {
                            player.sendMessage(plugin.getMessagePrefix() + args[1] + " does not have a Vortex Manipulator!");
                            return true;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            stringBuilder.append(args[i]).append(" ");
                        }
                        String message = stringBuilder.toString();
                        HashMap<String, Object> whereOfflinePlayer = new HashMap<>();
                        whereOfflinePlayer.put("uuid_to", offlinePlayerUuid);
                        whereOfflinePlayer.put("uuid_from", player.getUniqueId().toString());
                        whereOfflinePlayer.put("message", message);
                        whereOfflinePlayer.put("date", System.currentTimeMillis());
                        new TVMQueryFactory(plugin).doInsert("messages", whereOfflinePlayer);
                        player.sendMessage(plugin.getMessagePrefix() + "Message sent.");
                    }
                    case LIST -> {
                        String uuid = player.getUniqueId().toString();
                        if (args.length == 2) {
                            if (args[1].equalsIgnoreCase("out")) {
                                // list outbox
                                TVMResultSetOutbox resultSetOutbox = new TVMResultSetOutbox(plugin, uuid, 0, 10);
                                if (resultSetOutbox.resultSet()) {
                                    TVMUtils.sendOutboxList(player, resultSetOutbox, 1);
                                } else {
                                    player.sendMessage(plugin.getMessagePrefix() + "There are no messages in your outbox.");
                                    return true;
                                }
                            } else {
                                // list inbox
                                TVMResultSetInbox resultSetInbox = new TVMResultSetInbox(plugin, uuid, 0, 10);
                                if (resultSetInbox.resultSet()) {
                                    TVMUtils.sendInboxList(player, resultSetInbox, 1);
                                } else {
                                    player.sendMessage(plugin.getMessagePrefix() + "There are no messages in your inbox.");
                                    return true;
                                }
                            }
                        }
                        if (args.length < 3) {
                            player.sendMessage(plugin.getMessagePrefix() + "You need to specify a page number!");
                            return true;
                        }
                        int page = parseNum(args[2]);
                        if (page == -1) {
                            player.sendMessage(plugin.getMessagePrefix() + "Invalid page number!");
                            return true;
                        }
                        int start = (page * 10) - 10;
                        int limit = page * 10;
                        if (args[1].equalsIgnoreCase("out")) {
                            // outbox
                            TVMResultSetOutbox resultSetOutbox = new TVMResultSetOutbox(plugin, uuid, start, limit);
                            if (resultSetOutbox.resultSet()) {
                                TVMUtils.sendOutboxList(player, resultSetOutbox, page);
                            } else {
                                player.sendMessage(plugin.getMessagePrefix() + "There are no messages in your outbox.");
                                return true;
                            }
                        } else {
                            // inbox
                            TVMResultSetInbox resultSetInbox = new TVMResultSetInbox(plugin, uuid, start, limit);
                            if (resultSetInbox.resultSet()) {
                                TVMUtils.sendInboxList(player, resultSetInbox, page);
                            } else {
                                player.sendMessage(plugin.getMessagePrefix() + "There are no messages in your inbox.");
                                return true;
                            }
                        }
                    }
                    case READ -> {
                        int readId = parseNum(args[1]);
                        if (readId != -1) {
                            TVMResultSetMessageByID tvmResultSetMessageById = new TVMResultSetMessageByID(plugin, readId);
                            if (tvmResultSetMessageById.resultSet()) {
                                TVMUtils.readMessage(player, tvmResultSetMessageById.getMessage());
                                // update read status
                                new TVMQueryFactory(plugin).setReadStatus(readId);
                            } else {
                                player.sendMessage(plugin.getMessagePrefix() + "No message exists with that id, use /vmm list [in|out] first!");
                                return true;
                            }
                        }
                    }
                    case DELETE -> {
                        int deleteId = parseNum(args[1]);
                        if (deleteId != -1) {
                            TVMResultSetMessageByID resultSetMessageById = new TVMResultSetMessageByID(plugin, deleteId);
                            if (resultSetMessageById.resultSet()) {
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("message_id", deleteId);
                                new TVMQueryFactory(plugin).doDelete("messages", where);
                                player.sendMessage(plugin.getMessagePrefix() + "Message deleted.");
                            }
                        } else {
                            player.sendMessage(plugin.getMessagePrefix() + "No message exists with that id, use /vmm list [in|out] first!");
                            return true;
                        }
                    }
                    default -> {
                        // clear
                        if (!args[1].equalsIgnoreCase("in") && !args[1].equalsIgnoreCase("out")) {
                            player.sendMessage(plugin.getMessagePrefix() + "You need to specify which mail box you want to clear (in or out)!");
                            return true;
                        }
                        TVMQueryFactory queryFactory = new TVMQueryFactory(plugin);
                        HashMap<String, Object> where = new HashMap<>();
                        String which = "Outbox";
                        if (args[1].equalsIgnoreCase("out")) {
                            where.put("uuid_from", player.getUniqueId().toString());
                        } else {
                            where.put("uuid_to", player.getUniqueId().toString());
                            which = "Inbox";
                        }
                        queryFactory.doDelete("messages", where);
                        player.sendMessage(plugin.getMessagePrefix() + which + " cleared.");
                    }
                }
            } catch (IllegalArgumentException e) {
                player.sendMessage(plugin.getMessagePrefix() + "Incorrect command usage!");
                return false;
            }
        } else {
            player.sendMessage(plugin.getMessagePrefix() + "You don't have a Vortex Manipulator in your hand!");
        }
        return true;
    }

    private int parseNum(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
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
