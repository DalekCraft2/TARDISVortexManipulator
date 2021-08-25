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

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TVMGUIListener extends TVMGUICommon implements Listener {

    private final TARDISVortexManipulatorPlugin plugin;
    List<String> titles = Arrays.asList("§4Vortex Manipulator", "§4VM Messages", "§4VM Saves");
    List<String> components = Arrays.asList("", "", "", "", "", "");
    List<Integer> letters = Arrays.asList(0, 4, 5);
    char[] twoChars = new char[]{'2', 'a', 'b', 'c'};
    char[] threeChars = new char[]{'3', 'd', 'e', 'f'};
    char[] fourChars = new char[]{'4', 'g', 'h', 'i'};
    char[] fiveChars = new char[]{'5', 'j', 'k', 'l'};
    char[] sixChars = new char[]{'6', 'm', 'n', 'o'};
    char[] sevenChars = new char[]{'7', 'p', 'q', 'r', 's'};
    char[] eightChars = new char[]{'8', 't', 'u', 'v'};
    char[] nineChars = new char[]{'9', 'w', 'x', 'y', 'z'};
    char[] starChars = new char[]{'*', ' '};
    char[] hashChars = new char[]{'#', '~', '_', '-'};
    int which = 0;
    int[] pos;
    int twoTracker = 0;
    int threeTracker = 0;
    int fourTracker = 0;
    int fiveTracker = 0;
    int sixTracker = 0;
    int sevenTracker = 0;
    int eightTracker = 0;
    int nineTracker = 0;
    int starTracker = 0;
    int hashTracker = 0;
    TVMQueryFactory queryFactory;

    public TVMGUIListener(TARDISVortexManipulatorPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        // init string positions
        pos = new int[6];
        for (int i = 0; i < 6; i++) {
            pos[i] = 0;
        }
        queryFactory = new TVMQueryFactory(this.plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGuiClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals("§4Vortex Manipulator")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                switch (slot) {
                    case 11 -> {
                        // world
                        which = 0;
                        resetTrackers();
                    }
                    case 12 -> // one
                            updateDisplay(view, '1');
                    case 13 -> {
                        // two
                        if (letters.contains(which)) {
                            updateDisplay(view, twoChars[twoTracker]);
                            twoTracker++;
                            if (twoTracker == twoChars.length) {
                                twoTracker = 0;
                            }
                        } else {
                            updateDisplay(view, '2');
                        }
                    }
                    case 14 -> {
                        // three
                        if (letters.contains(which)) {
                            updateDisplay(view, threeChars[threeTracker]);
                            threeTracker++;
                            if (threeTracker == threeChars.length) {
                                threeTracker = 0;
                            }
                        } else {
                            updateDisplay(view, '3');
                        }
                    }
                    case 16 -> {
                        // save
                        which = 4;
                        resetTrackers();
                    }
                    case 18 -> {
                        // lifesigns
                        which = 5;
                        resetTrackers();
                    }
                    case 20 -> {
                        // x
                        which = 1;
                        resetTrackers();
                    }
                    case 21 -> {
                        // four
                        if (letters.contains(which)) {
                            updateDisplay(view, fourChars[fourTracker]);
                            fourTracker++;
                            if (fourTracker == fourChars.length) {
                                fourTracker = 0;
                            }
                        } else {
                            updateDisplay(view, '4');
                        }
                    }
                    case 22 -> {
                        // five
                        if (letters.contains(which)) {
                            updateDisplay(view, fiveChars[fiveTracker]);
                            fiveTracker++;
                            if (fiveTracker == fiveChars.length) {
                                fiveTracker = 0;
                            }
                        } else {
                            updateDisplay(view, '5');
                        }
                    }
                    case 23 -> {
                        // six
                        if (letters.contains(which)) {
                            updateDisplay(view, sixChars[sixTracker]);
                            sixTracker++;
                            if (sixTracker == sixChars.length) {
                                sixTracker = 0;
                            }
                        } else {
                            updateDisplay(view, '6');
                        }
                    }
                    case 25 -> // load
                            // open saves GUI
                            loadSaves(player);
                    case 29 -> {
                        // y
                        which = 2;
                        resetTrackers();
                    }
                    case 30 -> {
                        // seven
                        if (letters.contains(which)) {
                            updateDisplay(view, sevenChars[sevenTracker]);
                            sevenTracker++;
                            if (sevenTracker == sevenChars.length) {
                                sevenTracker = 0;
                            }
                        } else {
                            updateDisplay(view, '7');
                        }
                    }
                    case 31 -> {
                        // eight
                        if (letters.contains(which)) {
                            updateDisplay(view, eightChars[eightTracker]);
                            eightTracker++;
                            if (eightTracker == eightChars.length) {
                                eightTracker = 0;
                            }
                        } else {
                            updateDisplay(view, '8');
                        }
                    }
                    case 32 -> {
                        // nine
                        if (letters.contains(which)) {
                            updateDisplay(view, nineChars[nineTracker]);
                            nineTracker++;
                            if (nineTracker == nineChars.length) {
                                nineTracker = 0;
                            }
                        } else {
                            updateDisplay(view, '9');
                        }
                    }
                    case 34 -> // message
                            message(player);
                    case 38 -> {
                        // z
                        which = 3;
                        resetTrackers();
                    }
                    case 39 -> {
                        // star
                        updateDisplay(view, starChars[starTracker]);
                        starTracker++;
                        if (starTracker == starChars.length) {
                            starTracker = 0;
                        }
                    }
                    case 40 -> //zero
                            updateDisplay(view, '0');
                    case 41 -> {
                        // hash
                        if (letters.contains(which) || components.get(0).startsWith("~")) {
                            updateDisplay(view, hashChars[hashTracker]);
                            hashTracker++;
                            if (hashTracker == hashChars.length) {
                                hashTracker = 0;
                            }
                        } else {
                            updateDisplay(view, '-');
                        }
                    }
                    case 43 -> // beacon
                            setBeacon(player);
                    case 45 -> {
                        // close
                        close(player);
                        components = Arrays.asList("", "", "", "", "", "");
                    }
                    case 48 -> {
                        // previous cursor
                        if (pos[which] > 0) {
                            pos[which]--;
                        }
                        resetTrackers();
                    }
                    case 50 -> {
                        // next cursor
                        int next = components.get(which).length() + 1;
                        if (pos[which] < next) {
                            pos[which]++;
                        }
                        resetTrackers();
                    }
                    case 53 -> {
                        switch (which) {
                            case 4 -> saveCurrentLocation(player, view); // save
                            case 5 -> scanLifesigns(player, view); // scan
                            default -> doWarp(player, view); // warp
                        }
                    }
                    default -> {
                    }
                }
            }
        }
    }

    private void updateDisplay(InventoryView view, char c) {
        ItemStack display = view.getItem(4);
        ItemMeta displayMeta = display.getItemMeta();
        char[] chars = (components.get(which).isEmpty()) ? new char[1] : components.get(which).toCharArray();
        if (pos[which] >= chars.length) {
            char[] temp = chars.clone();
            chars = new char[pos[which] + 1];
            int i = 0;
            for (char ignored : temp) {
                chars[i] = temp[i];
                i++;
            }
        }
        chars[pos[which]] = c;
        String comp = new String(chars);
        String combined;
        switch (which) {
            case 0 -> combined = comp + " " + components.get(1) + " " + components.get(2) + " " + components.get(3);
            case 1 -> combined = components.get(0) + " " + comp + " " + components.get(2) + " " + components.get(3);
            case 2 -> combined = components.get(0) + " " + components.get(1) + " " + comp + " " + components.get(3);
            case 3 -> combined = components.get(0) + " " + components.get(1) + " " + components.get(2) + " " + comp;
            default -> combined = comp;
        }
        components.set(which, comp);
        List<String> displayLore = List.of(ChatColor.GRAY + combined);
        displayMeta.setLore(displayLore);
        display.setItemMeta(displayMeta);
    }

    private void resetTrackers() {
        twoTracker = 0;
        threeTracker = 0;
        fourTracker = 0;
        fiveTracker = 0;
        sixTracker = 0;
        sevenTracker = 0;
        eightTracker = 0;
        nineTracker = 0;
        starTracker = 0;
        hashTracker = 0;
    }

    private void saveCurrentLocation(Player player, InventoryView view) {
        ItemStack display = view.getItem(4);
        ItemMeta displayMeta = display.getItemMeta();
        List<String> lore = displayMeta.getLore();
        String name = lore.get(0);
        if (name.isEmpty()) {
            player.sendMessage(plugin.getMessagePrefix() + "You need to enter a save name!");
            return;
        }
        Location location = player.getLocation();
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", player.getUniqueId().toString());
        set.put("save_name", lore.get(0));
        set.put("world", location.getWorld().getName());
        set.put("x", location.getX());
        set.put("y", location.getY());
        set.put("z", location.getZ());
        set.put("yaw", location.getYaw());
        set.put("pitch", location.getPitch());
        queryFactory.doInsert("saves", set);
        close(player);
        player.sendMessage(plugin.getMessagePrefix() + "Current location saved.");
    }

    private void scanLifesigns(Player player, InventoryView view) {
        close(player);
        if (!player.hasPermission("vm.lifesigns")) {
            player.sendMessage(plugin.getMessagePrefix() + "You don't have permission to use the lifesigns scanner!");
            return;
        }
        int required = plugin.getConfig().getInt("tachyon_use.lifesigns");
        if (!TVMUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
            player.sendMessage(plugin.getMessagePrefix() + "You don't have enough tachyons to use the lifesigns scanner!");
            return;
        }
        // remove tachyons
        queryFactory.alterTachyons(player.getUniqueId().toString(), -required);
        // process GUI
        ItemStack display = view.getItem(4);
        ItemMeta displayMeta = display.getItemMeta();
        List<String> lore = displayMeta.getLore();
        String playerName = lore.get(0).trim();
        if (playerName.isEmpty()) {
            player.sendMessage(plugin.getMessagePrefix() + "Nearby entities:");
            // scan nearby entities
            double scanDistance = plugin.getConfig().getDouble("lifesign_scan_distance");
            List<Entity> nearbyEntities = player.getNearbyEntities(scanDistance, scanDistance, scanDistance);
            if (nearbyEntities.size() > 0) {
                // record nearby entities
                HashMap<EntityType, Integer> scannedEntities = new HashMap<>();
                List<String> playerNames = new ArrayList<>();
                nearbyEntities.forEach((entity) -> {
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
                });
                scannedEntities.forEach((key, value) -> {
                    String message = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    if (key.equals(EntityType.PLAYER) && playerNames.size() > 0) {
                        playerNames.forEach((pn) -> stringBuilder.append(", ").append(pn));
                        message = " (" + stringBuilder.substring(2) + ")";
                    }
                    player.sendMessage("    " + key + ": " + value + message);
                });
                scannedEntities.clear();
            } else {
                player.sendMessage("No nearby entities.");
            }
        } else {
            Player scanned = plugin.getServer().getPlayer(playerName);
            if (scanned == null) {
                player.sendMessage(plugin.getMessagePrefix() + "Could not find a player with that name!");
                return;
            }
            if (!scanned.isOnline()) {
                player.sendMessage(plugin.getMessagePrefix() + playerName + " is not online!");
                return;
            }
            // getHealth() / getMaxHealth() * getHealthScale()
            double maxHealth = scanned.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double health = scanned.getHealth() / maxHealth * scanned.getHealthScale();
            float hunger = (scanned.getFoodLevel() / 20F) * 100;
            int air = scanned.getRemainingAir();
            player.sendMessage(plugin.getMessagePrefix() + playerName + "'s lifesigns:");
            player.sendMessage("Has been alive for: " + TVMUtils.convertTicksToTime(scanned.getTicksLived()));
            player.sendMessage("Health: " + String.format("%.1f", health / 2) + " hearts");
            player.sendMessage("Hunger bar: " + String.format("%.2f", hunger) + "%");
            player.sendMessage("Air: ~" + (air / 20) + " seconds remaining");
        }
    }

    private void loadSaves(Player player) {
        close(player);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMSavesGUI savesGui = new TVMSavesGUI(plugin, 0, 44, player.getUniqueId().toString());
            ItemStack[] savesGuiItems = savesGui.getItems();
            Inventory savesInventory = plugin.getServer().createInventory(player, 54, "§4VM Saves");
            savesInventory.setContents(savesGuiItems);
            player.openInventory(savesInventory);
        }, 2L);
    }

    private void message(Player player) {
        close(player);
        if (!player.hasPermission("vm.message")) {
            player.sendMessage(plugin.getMessagePrefix() + "You don't have permission to use Vortex messages!");
            return;
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMMessageGUI messageGui = new TVMMessageGUI(plugin, 0, 44, player.getUniqueId().toString());
            ItemStack[] messageGuiItems = messageGui.getItems();
            Inventory messageInventory = plugin.getServer().createInventory(player, 54, "§4VM Messages");
            messageInventory.setContents(messageGuiItems);
            player.openInventory(messageInventory);
        }, 2L);
    }

    private void setBeacon(Player player) {
        if (!player.hasPermission("vm.beacon")) {
            close(player);
            player.sendMessage(plugin.getMessagePrefix() + "You don't have permission to set a beacon signal!");
            return;
        }
        UUID uuid = player.getUniqueId();
        String message = "You don't have enough tachyons to set a beacon signal!";
        int required = plugin.getConfig().getInt("tachyon_use.beacon");
        if (TVMUtils.checkTachyonLevel(uuid.toString(), required)) {
            String uuidString = uuid.toString();
            Location location = player.getLocation();
            // potential griefing, we need to check the location first!
            List<Flag> flags = new ArrayList<>();
            if (plugin.getConfig().getBoolean("respect.factions")) {
                flags.add(Flag.RESPECT_FACTIONS);
            }
            if (plugin.getConfig().getBoolean("respect.griefprevention")) {
                flags.add(Flag.RESPECT_GRIEFPREVENTION);
            }
            if (plugin.getConfig().getBoolean("respect.towny")) {
                flags.add(Flag.RESPECT_TOWNY);
            }
            if (plugin.getConfig().getBoolean("respect.worldborder")) {
                flags.add(Flag.RESPECT_WORLDBORDER);
            }
            if (plugin.getConfig().getBoolean("respect.worldguard")) {
                flags.add(Flag.RESPECT_WORLDGUARD);
            }
            Parameters parameters = new Parameters(player, flags);
            if (!plugin.getTardisApi().getRespect().getRespect(location, parameters)) {
                close(player);
                player.sendMessage(plugin.getMessagePrefix() + "You are not permitted to set a beacon signal here!");
                return;
            }
            Block block = location.getBlock().getRelative(BlockFace.DOWN);
            queryFactory.saveBeaconBlock(uuidString, block);
            block.setBlockData(Material.BEACON.createBlockData());
            Block down = block.getRelative(BlockFace.DOWN);
            queryFactory.saveBeaconBlock(uuidString, down);
            BlockData ironBlock = Material.IRON_BLOCK.createBlockData();
            down.setBlockData(ironBlock);
            List<BlockFace> faces = Arrays.asList(BlockFace.EAST, BlockFace.NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_WEST, BlockFace.WEST, BlockFace.SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_EAST);
            faces.forEach((face) -> {
                queryFactory.saveBeaconBlock(uuidString, down.getRelative(face));
                down.getRelative(face).setBlockData(ironBlock);
            });
            plugin.getBeaconSetters().add(uuid);
            message = "Beacon signal set, don't move!";
            // remove tachyons
            queryFactory.alterTachyons(player.getUniqueId().toString(), -required);
        }
        close(player);
        player.sendMessage(plugin.getMessagePrefix() + message);
    }

    private void doWarp(Player player, InventoryView view) {
        ItemStack display = view.getItem(4);
        ItemMeta displayMeta = display.getItemMeta();
        List<String> displayLore = displayMeta.getLore();
        List<String> destination;
        if (!displayLore.get(0).trim().isEmpty()) {
            destination = Arrays.asList(displayLore.get(0).trim().split(" "));
        } else {
            destination = new ArrayList<>();
        }
        List<String> worlds = new ArrayList<>();
        Location location;
        // set parameters
        List<Flag> flags = new ArrayList<>();
        flags.add(Flag.PERMS_AREA);
        flags.add(Flag.PERMS_NETHER);
        flags.add(Flag.PERMS_THEEND);
        flags.add(Flag.PERMS_WORLD);
        if (plugin.getConfig().getBoolean("respect.factions")) {
            flags.add(Flag.RESPECT_FACTIONS);
        }
        if (plugin.getConfig().getBoolean("respect.griefprevention")) {
            flags.add(Flag.RESPECT_GRIEFPREVENTION);
        }
        if (plugin.getConfig().getBoolean("respect.towny")) {
            flags.add(Flag.RESPECT_TOWNY);
        }
        if (plugin.getConfig().getBoolean("respect.worldborder")) {
            flags.add(Flag.RESPECT_WORLDBORDER);
        }
        if (plugin.getConfig().getBoolean("respect.worldguard")) {
            flags.add(Flag.RESPECT_WORLDGUARD);
        }
        Parameters parameters = new Parameters(player, flags);
        int required;
        switch (destination.size()) {
            case 1, 2, 3 -> {
                required = plugin.getConfig().getInt("tachyon_use.travel.world");
                // only world specified (or incomplete setting)
                // check world is an actual world
                if (plugin.getServer().getWorld(destination.get(0)) == null) {
                    close(player);
                    player.sendMessage(plugin.getMessagePrefix() + "World does not exist!");
                    return;
                }
                // check world is enabled for travel
                if (!plugin.getTardisApi().getWorlds().contains(destination.get(0))) {
                    close(player);
                    player.sendMessage(plugin.getMessagePrefix() + "You cannot travel to this world using the Vortex Manipulator!");
                    return;
                }
                worlds.add(destination.get(0));
                location = plugin.getTardisApi().getRandomLocation(worlds, null, parameters);
            }
            case 4 -> {
                required = plugin.getConfig().getInt("tachyon_use.travel.coords");
                // world, x, y, z specified
                World world;
                if (destination.get(0).contains("~")) {
                    // relative location
                    world = player.getLocation().getWorld();
                } else {
                    world = plugin.getServer().getWorld(destination.get(0));
                    if (world == null) {
                        close(player);
                        player.sendMessage(plugin.getMessagePrefix() + "World does not exist!");
                        return;
                    }
                    // check world is enabled for travel
                    if (!plugin.getTardisApi().getWorlds().contains(destination.get(0))) {
                        close(player);
                        player.sendMessage(plugin.getMessagePrefix() + "You cannot travel to this world using the Vortex Manipulator!");
                        return;
                    }
                }
                double x;
                double y;
                double z;
                try {
                    if (destination.get(1).startsWith("~")) {
                        // get players current location
                        Location playerLocation = player.getLocation();
                        double playerX = playerLocation.getX();
                        double playerY = playerLocation.getY();
                        double playerZ = playerLocation.getZ();
                        // strip off the initial "~" and add to current position
                        x = playerX + Double.parseDouble(destination.get(1).substring(1));
                        y = playerY + Double.parseDouble(destination.get(2).substring(1));
                        z = playerZ + Double.parseDouble(destination.get(3).substring(1));
                    } else {
                        x = Double.parseDouble(destination.get(1));
                        y = Double.parseDouble(destination.get(2));
                        z = Double.parseDouble(destination.get(3));
                    }
                } catch (NumberFormatException e) {
                    close(player);
                    player.sendMessage(plugin.getMessagePrefix() + "Could not parse coordinates!");
                    return;
                }
                location = new Location(world, x, y, z);
                // check block has space for player
                if (!location.getBlock().getType().equals(Material.AIR)) {
                    player.sendMessage(plugin.getMessagePrefix() + "Destination block is not AIR! Adjusting...");
                    // get the highest block at these coords
                    int highest = location.getWorld().getHighestBlockYAt(location);
                    location.setY(highest);
                }
            }
            default -> {
                required = plugin.getConfig().getInt("tachyon_use.travel.random");
                // random
                location = plugin.getTardisApi().getRandomLocation(plugin.getTardisApi().getWorlds(), null, parameters);
            }
        }
        UUID uuid = player.getUniqueId();
        if (!TVMUtils.checkTachyonLevel(uuid.toString(), required)) {
            close(player);
            player.sendMessage(plugin.getMessagePrefix() + "You need at least " + required + " tachyons to travel!");
            return;
        }
        if (location != null) {
            close(player);
            List<Player> players = new ArrayList<>();
            players.add(player);
            if (plugin.getConfig().getBoolean("allow.multiple")) {
                player.getNearbyEntities(0.5d, 0.5d, 0.5d).forEach((entity) -> {
                    if (entity instanceof Player && !entity.getUniqueId().equals(uuid)) {
                        players.add((Player) entity);
                    }
                });
            }
            int actual = required * players.size();
            if (!TVMUtils.checkTachyonLevel(uuid.toString(), actual)) {
                player.sendMessage(plugin.getMessagePrefix() + "You need at least " + actual + " tachyons to travel!");
                return;
            }
            player.sendMessage(plugin.getMessagePrefix() + "Standby for Vortex travel...");
            while (!location.getChunk().isLoaded()) {
                location.getChunk().load();
            }
            TVMUtils.movePlayers(players, location, player.getLocation().getWorld());
            // remove tachyons
            queryFactory.alterTachyons(uuid.toString(), -actual);
        } else {
            player.sendMessage(plugin.getMessagePrefix() + "No location could be found within those parameters.");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMenuDrag(InventoryDragEvent event) {
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (!titles.contains(title)) {
            return;
        }
        Set<Integer> slots = event.getRawSlots();
        slots.forEach((slot) -> {
            if ((slot >= 0 && slot < 81)) {
                event.setCancelled(true);
            }
        });
    }
}
