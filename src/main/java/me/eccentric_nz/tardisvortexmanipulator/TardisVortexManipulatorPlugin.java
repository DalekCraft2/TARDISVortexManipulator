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
import me.eccentric_nz.tardisvortexmanipulator.database.TvmDatabase;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmMySql;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmSqlite;
import me.eccentric_nz.tardisvortexmanipulator.gui.TvmGuiListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TvmMessageGuiListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TvmSavesGuiListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TardisVortexManipulatorPlugin extends JavaPlugin {

    public static TardisVortexManipulatorPlugin plugin;
    private final TvmDatabase service = TvmDatabase.getInstance();
    private final List<Location> blocks = new ArrayList<>();
    private final List<UUID> beaconSetters = new ArrayList<>();
    private final List<UUID> travellers = new ArrayList<>();
    private String pluginName;
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
        PluginDescriptionFile pluginDescriptionFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pluginDescriptionFile.getName() + "]" + ChatColor.RESET + " ";
        PluginDescriptionFile pdfFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
        saveDefaultConfig();
        new TvmConfig(this).checkConfig();
        pluginManager = getServer().getPluginManager();
        /*
         * Get TARDIS
         */
        Plugin tardis = pluginManager.getPlugin("TARDIS");
        if (tardis == null || !pluginManager.isPluginEnabled("TARDIS")) {
            System.err.println("[TARDISVortexManipulator] Cannot find TARDIS!");
            pluginManager.disablePlugin(this);
            return;
        }
        this.tardis = (TARDIS) tardis;
        Version minVersion = new Version("4.7.5");
        // TARDIS version = something like 4.7.5-b2339 or 4.7.5-b11.07.21-5:24
        String version = this.tardis.getDescription().getVersion().split("-")[0];
        Version tardisVersion = new Version(version);
        if (tardisVersion.compareTo(minVersion) < 0) {
            System.err.println("[TARDISVortexManipulator] You need a newer version of TARDIS (v4.7.5)!");
            pluginManager.disablePlugin(this);
            return;
        }
        tardisApi = this.tardis.getTardisAPI();
        prefix = getConfig().getString("storage.mysql.prefix");
        loadDatabase();
        registerListeners();
        registerCommands();
        ShapedRecipe recipe = new TvmRecipe(this).makeRecipe();
        getServer().addRecipe(recipe);
        tardisApi.addShapedRecipe("vortex-manipulator", recipe);
        startRecharger();
    }

    public String getPluginName() {
        return pluginName;
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
                TvmSqlite sqlite = new TvmSqlite(this);
                sqlite.createTables();
            } else {
                service.setConnection();
                TvmMySql mysql = new TvmMySql(this);
                mysql.createTables();
            }
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(pluginName + "Connection and Tables Error: " + e);
        }
    }

    private void registerListeners() {
        pluginManager.registerEvents(new TvmBlockListener(this), this);
        pluginManager.registerEvents(new TvmCraftListener(this), this);
        pluginManager.registerEvents(new TvmDeathListener(this), this);
        pluginManager.registerEvents(new TvmEquipListener(this), this);
        pluginManager.registerEvents(new TvmGuiListener(this), this);
        pluginManager.registerEvents(new TvmMessageGuiListener(this), this);
        pluginManager.registerEvents(new TvmMoveListener(this), this);
        pluginManager.registerEvents(new TvmSavesGuiListener(this), this);
    }

    private void registerCommands() {
        getCommand("vortexmanipulator").setExecutor(new TvmCommand(this));
        getCommand("vmactivate").setExecutor(new TvmCommandActivate(this));
        getCommand("vmbeacon").setExecutor(new TvmCommandBeacon(this));
        getCommand("vmhelp").setExecutor(new TvmCommandHelp(this));
        getCommand("vmhelp").setTabCompleter(new TvmTabCompleteHelp());
        getCommand("vmlifesigns").setExecutor(new TvmCommandLifesigns(this));
        getCommand("vmmessage").setExecutor(new TvmCommandMessage(this));
        getCommand("vmmessage").setTabCompleter(new TvmTabCompleteMessage());
        getCommand("vmremove").setExecutor(new TvmCommandRemove(this));
        getCommand("vmsave").setExecutor(new TvmCommandSave(this));
        getCommand("vmgive").setExecutor(new TvmCommandGive(this));
        getCommand("vmdatabase").setExecutor(new TvmCommandConvert(this));
    }

    private void startRecharger() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TvmTachyonRunnable(this), 1200L, getConfig().getLong("tachyon_use.recharge_interval"));
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
            getServer().getConsoleSender().sendMessage(pluginName + "Debug: " + object);
        }
    }
}
