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

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMSavesGUIListener extends TVMGUICommon implements Listener {

    private final TARDISVortexManipulatorPlugin plugin;
    int selectedSlot = -1;

    public TVMSavesGUIListener(TARDISVortexManipulatorPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onGuiClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals("§4VM Saves")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                if (view.getItem(slot) != null) {
                    switch (slot) {
                        case 45 -> {} // page number
                        case 46 -> close(player); // close
                        case 48 -> doPrev(view, player); // previous page
                        case 49 -> doNext(view, player); // next page
                        case 51 -> delete(view, player); // delete save
                        case 53 -> doWarp(view, player); // warp
                        default -> selectedSlot = slot;
                    }
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
                TVMSavesGUI savesGui = new TVMSavesGUI(plugin, start, start + 44, player.getUniqueId().toString());
                ItemStack[] savesGuiItems = savesGui.getItems();
                Inventory savesInventory = plugin.getServer().createInventory(player, 54, "§4VM Saves");
                savesInventory.setContents(savesGuiItems);
                player.openInventory(savesInventory);
            }, 2L);
        }
    }

    private void doNext(InventoryView view, Player player) {
        int page = getPageNumber(view);
        int start = (page * 44) + 44;
        close(player);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMSavesGUI savesGui = new TVMSavesGUI(plugin, start, start + 44, player.getUniqueId().toString());
            ItemStack[] savesGuiItems = savesGui.getItems();
            Inventory savesInventory = plugin.getServer().createInventory(player, 54, "§4VM Saves");
            savesInventory.setContents(savesGuiItems);
            player.openInventory(savesInventory);
        }, 2L);
    }

    private void delete(InventoryView view, Player player) {
        if (selectedSlot != -1) {
            ItemStack save = view.getItem(selectedSlot);
            ItemMeta saveMeta = save.getItemMeta();
            String saveName = saveMeta.getDisplayName();
            TVMResultSetWarpByName resultSetWarpByName = new TVMResultSetWarpByName(plugin, player.getUniqueId().toString(), saveName);
            if (resultSetWarpByName.resultSet()) {
                close(player);
                HashMap<String, Object> where = new HashMap<>();
                where.put("save_id", resultSetWarpByName.getId());
                new TVMQueryFactory(plugin).doDelete("saves", where);
                player.sendMessage(plugin.getMessagePrefix() + "Save deleted.");
            }
        } else {
            player.sendMessage(plugin.getMessagePrefix() + "Select a save!");
        }
    }

    private void doWarp(InventoryView view, Player player) {
        if (selectedSlot != -1) {
            ItemStack save = view.getItem(selectedSlot);
            ItemMeta saveMeta = save.getItemMeta();
            String saveName = saveMeta.getDisplayName();
            TVMResultSetWarpByName resultSetWarpByName = new TVMResultSetWarpByName(plugin, player.getUniqueId().toString(), saveName);
            if (resultSetWarpByName.resultSet()) {
                close(player);
                List<Player> players = new ArrayList<>();
                players.add(player);
                if (plugin.getConfig().getBoolean("allow.multiple")) {
                    player.getNearbyEntities(0.5d, 0.5d, 0.5d).forEach((entity) -> {
                        if (entity instanceof Player && !entity.getUniqueId().equals(player.getUniqueId())) {
                            players.add((Player) entity);
                        }
                    });
                }
                int required = plugin.getConfig().getInt("tachyon_use.travel.saved") * players.size();
                if (!TVMUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
                    close(player);
                    player.sendMessage(plugin.getMessagePrefix() + "You need at least " + required + " tachyons to travel!");
                    return;
                }
                Location location = resultSetWarpByName.getWarp();
                player.sendMessage(plugin.getMessagePrefix() + "Standby for Vortex travel...");
                while (!location.getChunk().isLoaded()) {
                    location.getChunk().load();
                }
                TVMUtils.movePlayers(players, location, player.getLocation().getWorld());
                // remove tachyons
                new TVMQueryFactory(plugin).alterTachyons(player.getUniqueId().toString(), -required);
            }
        } else {
            player.sendMessage(plugin.getMessagePrefix() + "Select a save!");
        }
    }
}
