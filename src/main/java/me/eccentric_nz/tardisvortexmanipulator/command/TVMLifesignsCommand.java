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

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TVMLifesignsCommand implements CommandExecutor {

    private final TARDISVortexManipulatorPlugin plugin;

    public TVMLifesignsCommand(TARDISVortexManipulatorPlugin plugin) {
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
        if (!player.hasPermission("vm.lifesigns")) {
            player.sendMessage(plugin.getMessagePrefix() + "You don't have permission to use that command!");
            return true;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(plugin.getItemKey(), PersistentDataType.STRING) && itemStack.getItemMeta().getPersistentDataContainer().get(plugin.getItemKey(), PersistentDataType.STRING).equals("vortex_manipulator")) {
            int required = plugin.getConfig().getInt("tachyon_use.lifesigns");
            if (!TVMUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
                player.sendMessage(plugin.getMessagePrefix() + "You don't have enough tachyons to use the lifesigns scanner!");
                return true;
            }
            // remove tachyons
            new TVMQueryFactory(plugin).alterTachyons(player.getUniqueId().toString(), -required);
            if (args.length == 0) {
                player.sendMessage(plugin.getMessagePrefix() + "Nearby entities:");
                // scan nearby entities
                double distance = plugin.getConfig().getDouble("lifesign_scan_distance");
                List<Entity> nearbyEntities = player.getNearbyEntities(distance, distance, distance);
                if (nearbyEntities.size() > 0) {
                    // record nearby entities
                    HashMap<EntityType, Integer> scannedEntities = new HashMap<>();
                    List<String> playerNames = new ArrayList<>();
                    for (Entity entity : nearbyEntities) {
                        EntityType entityType = entity.getType();
                        if (TARDISConstants.ENTITY_TYPES.contains(entityType)) {
                            Integer entityCount = scannedEntities.getOrDefault(entityType, 0);
                            boolean visible = true;
                            if (entityType.equals(EntityType.PLAYER)) {
                                Player entityPlayer = (Player) entity;
                                if (player.canSee(entityPlayer)) {
                                    playerNames.add(entityPlayer.getName());
                                } else {
                                    visible = false;
                                }
                            }
                            if (visible) {
                                scannedEntities.put(entityType, entityCount + 1);
                            }
                        }
                    }
                    for (Map.Entry<EntityType, Integer> entry : scannedEntities.entrySet()) {
                        String message = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        if (entry.getKey().equals(EntityType.PLAYER) && playerNames.size() > 0) {
                            playerNames.forEach((pn) -> stringBuilder.append(", ").append(pn));
                            message = " (" + stringBuilder.substring(2) + ")";
                        }
                        player.sendMessage("    " + entry.getKey() + ": " + entry.getValue() + message);
                    }
                    scannedEntities.clear();
                } else {
                    player.sendMessage("No nearby entities.");
                }
            }
            if (args.length < 1) {
                player.sendMessage(plugin.getMessagePrefix() + "You need to specify a player name!");
                return true;
            }
            Player scanned = plugin.getServer().getPlayer(args[0]);
            if (scanned == null) {
                player.sendMessage(plugin.getMessagePrefix() + "Could not find a player with that name!");
                return true;
            }
            if (!scanned.isOnline()) {
                player.sendMessage(plugin.getMessagePrefix() + args[0] + " is not online!");
                return true;
            }
            // getHealth() / getMaxHealth() * getHealthScale()
            double health = scanned.getHealth() / scanned.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * scanned.getHealthScale();
            float hunger = (scanned.getFoodLevel() / 20F) * 100;
            int air = scanned.getRemainingAir();
            player.sendMessage(plugin.getMessagePrefix() + args[0] + "'s lifesigns:");
            player.sendMessage("Has been alive for: " + TVMUtils.convertTicksToTime(scanned.getTicksLived()));
            player.sendMessage("Health: " + String.format("%.1f", health / 2) + " hearts");
            player.sendMessage("Hunger bar: " + String.format("%.2f", hunger) + "%");
            player.sendMessage("Air: ~" + (air / 20) + " seconds remaining");
        } else {
            player.sendMessage(plugin.getMessagePrefix() + "You don't have a Vortex Manipulator in your hand!");
        }
        return true;
    }
}
