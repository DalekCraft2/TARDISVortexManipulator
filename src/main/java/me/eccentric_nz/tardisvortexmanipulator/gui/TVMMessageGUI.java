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
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetInbox;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMMessageGUI {

    private final TARDISVortexManipulatorPlugin plugin;
    private final int start, finish;
    private final String uuid;
    private final ItemStack[] items;

    public TVMMessageGUI(TARDISVortexManipulatorPlugin plugin, int start, int finish, String uuid) {
        this.plugin = plugin;
        this.start = start;
        this.finish = finish;
        this.uuid = uuid;
        items = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator Messages GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] itemStacks = new ItemStack[54];
        int i = 0;
        // get the player's messages
        TVMResultSetInbox resultSetInbox = new TVMResultSetInbox(plugin, uuid, start, 44);
        if (resultSetInbox.resultSet()) {
            List<TVMMessage> messages = resultSetInbox.getMail();
            for (TVMMessage message : messages) {
                // message
                ItemStack messageItem;
                if (message.isRead()) {
                    messageItem = new ItemStack(Material.BOOK, 1);
                } else {
                    messageItem = new ItemStack(Material.WRITABLE_BOOK, 1);
                }
                ItemMeta messageMeta = messageItem.getItemMeta();
                messageMeta.setDisplayName(ChatColor.RESET + "#" + (i + start + 1));
                String from = plugin.getServer().getOfflinePlayer(message.getWho()).getName();
                messageMeta.setLore(Arrays.asList(ChatColor.GRAY + "From: " + from, ChatColor.GRAY + "Date: " + message.getDate(), ChatColor.GRAY + "" + message.getId()));
                messageItem.setItemMeta(messageMeta);
                itemStacks[i] = messageItem;
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
        // read
        ItemStack readButton = new ItemStack(Material.BOWL, 1);
        ItemMeta readButtonMeta = readButton.getItemMeta();
        readButtonMeta.setDisplayName(ChatColor.RESET + "Read");
        readButtonMeta.setCustomModelData(121);
        readButton.setItemMeta(readButtonMeta);
        itemStacks[51] = readButton;
        // delete
        ItemStack deleteButton = new ItemStack(Material.BOWL, 1);
        ItemMeta deleteButtonMeta = deleteButton.getItemMeta();
        deleteButtonMeta.setDisplayName(ChatColor.RESET + "Delete");
        deleteButtonMeta.setCustomModelData(107);
        deleteButton.setItemMeta(deleteButtonMeta);
        itemStacks[53] = deleteButton;

        return itemStacks;
    }

    public ItemStack[] getItems() {
        return items;
    }
}
