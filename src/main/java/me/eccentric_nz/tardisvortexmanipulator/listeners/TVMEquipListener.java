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
package me.eccentric_nz.tardisvortexmanipulator.listeners;

import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TVMEquipListener implements Listener {

    private final TARDISVortexManipulatorPlugin plugin;
    private final HashSet<Material> transparent = new HashSet<>();

    public TVMEquipListener(TARDISVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
        transparent.add(Material.AIR);
        transparent.add(Material.ALLIUM);
        transparent.add(Material.AZURE_BLUET);
        transparent.add(Material.BLUE_ORCHID);
        transparent.add(Material.BROWN_MUSHROOM);
        transparent.add(Material.DANDELION);
        transparent.add(Material.DEAD_BUSH);
        transparent.add(Material.FERN);
        transparent.add(Material.GRASS);
        transparent.add(Material.LARGE_FERN);
        transparent.add(Material.LILAC);
        transparent.add(Material.ORANGE_TULIP);
        transparent.add(Material.OXEYE_DAISY);
        transparent.add(Material.PEONY);
        transparent.add(Material.PINK_TULIP);
        transparent.add(Material.POPPY);
        transparent.add(Material.REDSTONE_WIRE);
        transparent.add(Material.RED_MUSHROOM);
        transparent.add(Material.RED_TULIP);
        transparent.add(Material.ROSE_BUSH);
        transparent.add(Material.SNOW);
        transparent.add(Material.SUNFLOWER);
        transparent.add(Material.TALL_GRASS);
        transparent.add(Material.VINE);
        transparent.add(Material.WHITE_TULIP);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.LEFT_CLICK_AIR)) {
            return;
        }
        Player player = event.getPlayer();
        if (!player.hasPermission("vm.teleport")) {
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(TARDISVortexManipulatorPlugin.plugin.getItemKey(), PersistentDataType.STRING) && itemStack.getItemMeta().getPersistentDataContainer().get(TARDISVortexManipulatorPlugin.plugin.getItemKey(), PersistentDataType.STRING).equals("vortex_manipulator")) {
            // get tachyon level
            TVMResultSetManipulator resultSetManipulator = new TVMResultSetManipulator(plugin, player.getUniqueId().toString());
            if (resultSetManipulator.resultSet()) {
                if (action.equals(Action.RIGHT_CLICK_AIR)) {
                    // open gui
                    ItemStack[] guiItems = new TVMGUI(plugin, resultSetManipulator.getTachyonLevel()).getItems();
                    Inventory vortexManipulatorInventory = plugin.getServer().createInventory(player, 54, "§4Vortex Manipulator");
                    vortexManipulatorInventory.setContents(guiItems);
                    player.openInventory(vortexManipulatorInventory);
                } else if (plugin.getConfig().getBoolean("allow.look_at_block") && player.hasPermission("vm.lookatblock")) {
                    UUID uuid = player.getUniqueId();
                    int maxDistance = plugin.getConfig().getInt("max_look_at_distance");
                    Location targetBlockLocation = player.getTargetBlock(transparent, maxDistance).getLocation();
                    targetBlockLocation.add(0.0d, 1.0d, 0.0d);
                    List<Player> players = new ArrayList<>();
                    players.add(player);
                    if (plugin.getConfig().getBoolean("allow.multiple")) {
                        player.getNearbyEntities(0.5d, 0.5d, 0.5d).forEach((entity) -> {
                            if (entity instanceof Player && !entity.getUniqueId().equals(uuid)) {
                                players.add((Player) entity);
                            }
                        });
                    }
                    int required = plugin.getConfig().getInt("tachyon_use.travel.to_block");
                    int actual = required * players.size();
                    if (!TVMUtils.checkTachyonLevel(uuid.toString(), actual)) {
                        player.sendMessage(plugin.getMessagePrefix() + "You need at least " + actual + " tachyons to travel!");
                        return;
                    }
                    player.sendMessage(plugin.getMessagePrefix() + "Standby for Vortex travel...");
                    // Random malfunction
                    Random random = new Random();
                    if (random.nextInt(100) < plugin.getConfig().getInt("block_travel_malfunction_chance")) {
                        plugin.debug(player.getDisplayName() + " has malfunctioned");
                        Parameters params = new Parameters(player, TVMUtils.getProtectionFlags());
                        Location randomLocation = null;
                        // since the TARDIS api is a little funky at times, retry up to ten times if a location isn't found
                        // this will exponentially increase the accuracy of the configured chance
                        int retries = 0;
                        while (randomLocation == null) {
                            randomLocation = plugin.getTardisApi().getRandomLocation(plugin.getTardisApi().getWorlds(), null, params);
                            retries++;
                            if (retries >= 10) {
                                break;
                            }
                        }
                        // check to ensure we have a valid alternate location before triggering the malfunction
                        // for this reason the actual malfunction rate may be lower than configured
                        if (randomLocation != null) {
                            player.sendMessage(plugin.getMessagePrefix() + "Vortex travel malfunction. Attempting to land in safe location..");
                            targetBlockLocation = randomLocation;
                        }
                    }
                    TVMUtils.movePlayers(players, targetBlockLocation, player.getLocation().getWorld());
                    // remove tachyons
                    new TVMQueryFactory(plugin).alterTachyons(uuid.toString(), -actual);
                }
            }
        }
    }
}
