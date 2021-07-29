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

    private void doPrev(InventoryView view, Player p) {
        int page = getPageNumber(view);
        if (page > 1) {
            int start = (page * 44) - 44;
            close(p);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                TVMMessageGUI tvmm = new TVMMessageGUI(plugin, start, start + 44, p.getUniqueId().toString());
                ItemStack[] gui = tvmm.getGui();
                Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Messages");
                vmg.setContents(gui);
                p.openInventory(vmg);
            }, 2L);
        }
    }

    private void doNext(InventoryView view, Player p) {
        int page = getPageNumber(view);
        int start = (page * 44) + 44;
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMMessageGUI tvmm = new TVMMessageGUI(plugin, start, start + 44, p.getUniqueId().toString());
            ItemStack[] gui = tvmm.getGui();
            Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Messages");
            vmg.setContents(gui);
            p.openInventory(vmg);
        }, 2L);
    }

    private void doRead(InventoryView view, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = view.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            List<String> lore = im.getLore();
            int message_id = TARDISNumberParsers.parseInt(lore.get(2));
            TVMResultSetMessageByID rsm = new TVMResultSetMessageByID(plugin, message_id);
            if (rsm.resultSet()) {
                close(p);
                TVMUtils.readMessage(p, rsm.getMessage());
                // update read status
                new TVMQueryFactory(plugin).setReadStatus(message_id);
            }
        } else {
            p.sendMessage(plugin.getMessagePrefix() + "Select a message!");
        }
    }

    private void doDelete(InventoryView view, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = view.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            List<String> lore = im.getLore();
            int message_id = TARDISNumberParsers.parseInt(lore.get(2));
            TVMResultSetMessageByID rsm = new TVMResultSetMessageByID(plugin, message_id);
            if (rsm.resultSet()) {
                close(p);
                HashMap<String, Object> where = new HashMap<>();
                where.put("message_id", message_id);
                new TVMQueryFactory(plugin).doDelete("messages", where);
                p.sendMessage(plugin.getMessagePrefix() + "Message deleted.");
            }
        } else {
            p.sendMessage(plugin.getMessagePrefix() + "Select a message!");
        }
    }
}
