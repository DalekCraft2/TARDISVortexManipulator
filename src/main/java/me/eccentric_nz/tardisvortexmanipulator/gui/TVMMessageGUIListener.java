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
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetMessageByID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMMessageGUIListener extends TVMGUICommon implements Listener {

    private final TARDISVortexManipulatorPlugin plugin;
    int selectedSlot = -1;

    public TVMMessageGUIListener(TARDISVortexManipulatorPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMessageGuiClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals("§4VM Messages")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                switch (slot) {
                    case 45 -> {
                    }
                    case 46 -> close(player); // close
                    case 48 -> doPrev(view, player); // previous page
                    case 49 -> doNext(view, player); // next page
                    case 51 -> doRead(view, player); // read
                    case 53 -> doDelete(view, player); // delete
                    default -> selectedSlot = slot; // select a message
                }
            }
        }
    }

    private void doPrev(InventoryView view, Player player) {
        int page = getPageNumber(view);
        if (page > 1) {
            int start = (page * 44) - 44;
            close(player);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                TVMMessageGUI messageGui = new TVMMessageGUI(plugin, start, start + 44, player.getUniqueId().toString());
                ItemStack[] messageGuiItems = messageGui.getItems();
                Inventory messageInventory = plugin.getServer().createInventory(player, 54, "§4VM Messages");
                messageInventory.setContents(messageGuiItems);
                player.openInventory(messageInventory);
            }, 2L);
        }
    }

    private void doNext(InventoryView view, Player player) {
        int page = getPageNumber(view);
        int start = (page * 44) + 44;
        close(player);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMMessageGUI messageGui = new TVMMessageGUI(plugin, start, start + 44, player.getUniqueId().toString());
            ItemStack[] messageGuiItems = messageGui.getItems();
            Inventory messageInventory = plugin.getServer().createInventory(player, 54, "§4VM Messages");
            messageInventory.setContents(messageGuiItems);
            player.openInventory(messageInventory);
        }, 2L);
    }

    private void doRead(InventoryView view, Player player) {
        if (selectedSlot != -1) {
            ItemStack message = view.getItem(selectedSlot);
            ItemMeta messageMeta = message.getItemMeta();
            List<String> messageLore = messageMeta.getLore();
            int messageId = TARDISNumberParsers.parseInt(messageLore.get(2));
            TVMResultSetMessageByID resultSetMessageById = new TVMResultSetMessageByID(plugin, messageId);
            if (resultSetMessageById.resultSet()) {
                close(player);
                TVMUtils.readMessage(player, resultSetMessageById.getMessage());
                // update read status
                new TVMQueryFactory(plugin).setReadStatus(messageId);
            }
        } else {
            player.sendMessage(plugin.getMessagePrefix() + "Select a message!");
        }
    }

    private void doDelete(InventoryView view, Player player) {
        if (selectedSlot != -1) {
            ItemStack message = view.getItem(selectedSlot);
            ItemMeta messageMeta = message.getItemMeta();
            List<String> messageLore = messageMeta.getLore();
            int messageId = TARDISNumberParsers.parseInt(messageLore.get(2));
            TVMResultSetMessageByID resultSetMessageById = new TVMResultSetMessageByID(plugin, messageId);
            if (resultSetMessageById.resultSet()) {
                close(player);
                HashMap<String, Object> where = new HashMap<>();
                where.put("message_id", messageId);
                new TVMQueryFactory(plugin).doDelete("messages", where);
                player.sendMessage(plugin.getMessagePrefix() + "Message deleted.");
            }
        } else {
            player.sendMessage(plugin.getMessagePrefix() + "Select a message!");
        }
    }
}
