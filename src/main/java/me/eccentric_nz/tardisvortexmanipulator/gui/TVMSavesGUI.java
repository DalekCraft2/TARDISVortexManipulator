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
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMSave;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMSavesGUI {

    private final TARDISVortexManipulatorPlugin plugin;
    private final int start, finish;
    private final String uuid;
    private final ItemStack[] items;
    private final HashMap<String, Material> blocks = new HashMap<>();

    public TVMSavesGUI(TARDISVortexManipulatorPlugin plugin, int start, int finish, String uuid) {
        this.plugin = plugin;
        this.start = start;
        this.finish = finish;
        this.uuid = uuid;
        blocks.put("NORMAL", Material.DIRT);
        blocks.put("NETHER", Material.NETHERRACK);
        blocks.put("THE_END", Material.END_STONE);
        items = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator Saves GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] itemStacks = new ItemStack[54];
        int i = 0;
        // get the player's messages
        TVMResultSetSaves resultSetSaves = new TVMResultSetSaves(plugin, uuid, start, 44);
        if (resultSetSaves.resultSet()) {
            List<TVMSave> saves = resultSetSaves.getSaves();
            for (TVMSave save : saves) {
                // save
                ItemStack saveItem = new ItemStack(blocks.get(save.getEnv()), 1);
                ItemMeta saveMeta = saveItem.getItemMeta();
                saveMeta.setDisplayName(ChatColor.RESET + save.getName());
                saveMeta.setLore(Arrays.asList(ChatColor.GRAY + "World: " + save.getWorld(), ChatColor.GRAY + "x: " + oneDecimal(save.getX()), ChatColor.GRAY + "y: " + save.getY(), ChatColor.GRAY + "z: " + oneDecimal(save.getZ())));
                saveItem.setItemMeta(saveMeta);
                itemStacks[i] = saveItem;
                i++;
            }
        }

        int n = start / 44 + 1;
        // page number
        ItemStack page = new ItemStack(Material.BOWL, 1);
        ItemMeta pageMeta = page.getItemMeta();
        pageMeta.setDisplayName(ChatColor.RESET + "Page " + n);
        pageMeta.setCustomModelData(119);
        page.setItemMeta(pageMeta);
        itemStacks[45] = page;
        // close
        ItemStack closeButton = new ItemStack(Material.BOWL, 1);
        ItemMeta closeButtonMeta = closeButton.getItemMeta();
        closeButtonMeta.setDisplayName(ChatColor.RESET + "Close");
        closeButtonMeta.setCustomModelData(1);
        closeButton.setItemMeta(closeButtonMeta);
        itemStacks[46] = closeButton;
        // previous screen (only if needed)
        if (start > 0) {
            ItemStack previousButton = new ItemStack(Material.BOWL, 1);
            ItemMeta previousButtonMeta = previousButton.getItemMeta();
            previousButtonMeta.setDisplayName(ChatColor.RESET + "Previous Page");
            previousButtonMeta.setCustomModelData(120);
            previousButton.setItemMeta(previousButtonMeta);
            itemStacks[48] = previousButton;
        }
        // next screen (only if needed)
        if (finish > 44) {
            ItemStack nextButton = new ItemStack(Material.BOWL, 1);
            ItemMeta nextButtonMeta = nextButton.getItemMeta();
            nextButtonMeta.setDisplayName(ChatColor.RESET + "Next Page");
            nextButtonMeta.setCustomModelData(116);
            nextButton.setItemMeta(nextButtonMeta);
            itemStacks[49] = nextButton;
        }
        // delete
        ItemStack deleteButton = new ItemStack(Material.BOWL, 1);
        ItemMeta deleteButtonMeta = deleteButton.getItemMeta();
        deleteButtonMeta.setDisplayName(ChatColor.RESET + "Delete");
        deleteButtonMeta.setCustomModelData(107);
        deleteButton.setItemMeta(deleteButtonMeta);
        itemStacks[51] = deleteButton;
        // warp
        ItemStack goButton = new ItemStack(Material.BOWL, 1);
        ItemMeta goButtonMeta = goButton.getItemMeta();
        goButtonMeta.setDisplayName(ChatColor.RESET + "Enter Vortex");
        goButtonMeta.setCustomModelData(127);
        goButton.setItemMeta(goButtonMeta);
        itemStacks[53] = goButton;

        return itemStacks;
    }

    public ItemStack[] getItems() {
        return items;
    }

    private String oneDecimal(double d) {
        return String.format("%f.1", d);
    }
}
