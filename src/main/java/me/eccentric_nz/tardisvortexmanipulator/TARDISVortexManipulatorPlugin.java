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
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TardisAPI;
import me.eccentric_nz.TARDIS.utility.Version;
import me.eccentric_nz.tardisvortexmanipulator.command.*;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMDatabase;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMMySQL;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMSQLite;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMMessageGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMSavesGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class TARDISVortexManipulatorPlugin extends JavaPlugin {

    public static TARDISVortexManipulatorPlugin plugin;
    private final TVMDatabase service = TVMDatabase.getInstance();
    private final List<Location> blocks = new ArrayList<>();
    private final List<UUID> beaconSetters = new ArrayList<>();
    private final List<UUID> travellers = new ArrayList<>();
    private String messagePrefix;
    private NamespacedKey itemKey;
    private TardisAPI tardisApi;
    private TARDIS tardis;
    private PluginManager pluginManager;
    private String prefix;

    @Override
    public void onDisable() {
        // Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        plugin = this;
        itemKey = new NamespacedKey(this, "item");
        messagePrefix = ChatColor.GOLD + "[" + getDescription().getName() + "]" + ChatColor.RESET + " ";
        saveDefaultConfig();
        new TVMConfig(this).checkConfig();
        pluginManager = getServer().getPluginManager();
        /*
         * Get TARDIS
         */
        Plugin tardis = pluginManager.getPlugin("TARDIS");
        if (tardis == null || !pluginManager.isPluginEnabled("TARDIS")) {
            getLogger().log(java.util.logging.Level.SEVERE, "Cannot find TARDIS!");
            pluginManager.disablePlugin(this);
            return;
        }
        this.tardis = (TARDIS) tardis;
        Version minVersion = new Version("4.7.5");
        // TARDIS version = something like 4.7.5-b2339 or 4.7.5-b11.07.21-5:24
        String version = this.tardis.getDescription().getVersion().split("-")[0];
        Version tardisVersion = new Version(version);
        if (tardisVersion.compareTo(minVersion) < 0) {
            getLogger().log(java.util.logging.Level.SEVERE, "You need a newer version of TARDIS (v4.7.5)!");
            pluginManager.disablePlugin(this);
            return;
        }
        tardisApi = this.tardis.getTardisAPI();
        prefix = getConfig().getString("storage.mysql.prefix");
        loadDatabase();
        registerListeners();
        registerCommands();
        ShapedRecipe recipe = new TVMRecipe(this).makeRecipe();
        getServer().addRecipe(recipe);
        tardisApi.addShapedRecipe("vortex-manipulator", recipe);
        startRecharger();
    }

    public String getMessagePrefix() {
        return messagePrefix;
    }

    public NamespacedKey getItemKey() {
        return itemKey;
    }

    public TardisAPI getTardisApi() {
        return tardisApi;
    }

    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets up the database.
     */
    private void loadDatabase() {
        String databaseType = getConfig().getString("storage.database");
        try {
            if (databaseType.equals("sqlite")) {
                String path = getDataFolder() + File.separator + "Tvm.db";
                service.setConnection(path);
                TVMSQLite sqlite = new TVMSQLite(this);
                sqlite.createTables();
            } else {
                service.setConnection();
                TVMMySQL mysql = new TVMMySQL(this);
                mysql.createTables();
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Connection and Tables Error: " + e.getMessage());
        }
    }

    private void registerListeners() {
        pluginManager.registerEvents(new TVMBlockListener(this), this);
        pluginManager.registerEvents(new TVMCraftListener(this), this);
        pluginManager.registerEvents(new TVMDeathListener(this), this);
        pluginManager.registerEvents(new TVMEquipListener(this), this);
        pluginManager.registerEvents(new TVMGUIListener(this), this);
        pluginManager.registerEvents(new TVMMessageGUIListener(this), this);
        pluginManager.registerEvents(new TVMMoveListener(this), this);
        pluginManager.registerEvents(new TVMSavesGUIListener(this), this);
    }

    private void registerCommands() {
        getCommand("vortexmanipulator").setExecutor(new TVMCommand(this));
        getCommand("vmactivate").setExecutor(new TVMCommandActivate(this));
        getCommand("vmbeacon").setExecutor(new TVMCommandBeacon(this));
        getCommand("vmhelp").setExecutor(new TVMCommandHelp(this));
        getCommand("vmhelp").setTabCompleter(new TVMTabCompleteHelp());
        getCommand("vmlifesigns").setExecutor(new TVMCommandLifesigns(this));
        getCommand("vmmessage").setExecutor(new TVMCommandMessage(this));
        getCommand("vmmessage").setTabCompleter(new TVMTabCompleteMessage());
        getCommand("vmremove").setExecutor(new TVMCommandRemove(this));
        getCommand("vmsave").setExecutor(new TVMCommandSave(this));
        getCommand("vmgive").setExecutor(new TVMCommandGive(this));
        getCommand("vmdatabase").setExecutor(new TVMCommandConvert(this));
    }

    private void startRecharger() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TVMTachyonRunnable(this), 1200L, getConfig().getLong("tachyon_use.recharge_interval"));
    }

    public List<Location> getBlocks() {
        return blocks;
    }

    public List<UUID> getBeaconSetters() {
        return beaconSetters;
    }

    public List<UUID> getTravellers() {
        return travellers;
    }

    /**
     * Outputs a message to the console. Requires debug: true in config.yml
     *
     * @param object the Object to print to the console
     */
    public void debug(Object object) {
        if (getConfig().getBoolean("debug")) {
            getLogger().log(Level.CONFIG, (String) object);
        }
    }
}
