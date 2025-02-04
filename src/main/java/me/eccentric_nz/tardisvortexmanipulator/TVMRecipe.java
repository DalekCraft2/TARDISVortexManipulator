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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TVMRecipe {

    private final TARDISVortexManipulatorPlugin plugin;

    public TVMRecipe(TARDISVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    public ShapedRecipe makeRecipe() {
        String[] resultIdData = plugin.getConfig().getString("recipe.result").split(":");
        Material material = Material.valueOf(resultIdData[0]);
        int amount = plugin.getConfig().getInt("recipe.amount");
        ItemStack itemStack;
        if (resultIdData.length == 2) {
            short resultData = Short.parseShort(resultIdData[1]);
            itemStack = new ItemStack(material, amount);
            ItemMeta itemMeta = itemStack.getItemMeta();
            ((Damageable) itemMeta).setDamage(resultData);
            itemStack.setItemMeta(itemMeta);
        } else {
            itemStack = new ItemStack(material, amount);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Vortex Manipulator");
        if (!plugin.getConfig().getString("recipe.lore").equals("")) {
            List<String> lore = plugin.getConfig().getStringList("recipe.lore");
            lore.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
            itemMeta.setLore(lore);
        }
        itemMeta.setCustomModelData(10000002);
        // TODO Undo the PBV stuff.
        itemMeta.getPersistentDataContainer().set(plugin.getItemKey(), PersistentDataType.STRING, "vortex_manipulator");
        itemStack.setItemMeta(itemMeta);
        NamespacedKey key = new NamespacedKey(plugin, "Vortex_Manipulator");
        ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
        // get shape
        try {
            String[] shapeTemp = plugin.getConfig().getString("recipe.shape").split(",");
            String[] shape = new String[3];
            for (int i = 0; i < 3; i++) {
                shape[i] = shapeTemp[i].replaceAll("-", " ");
            }
            recipe.shape(shape[0], shape[1], shape[2]);
            Set<String> ingredients = plugin.getConfig().getConfigurationSection("recipe.ingredients").getKeys(false);
            ingredients.forEach((ingredient) -> {
                char c = ingredient.charAt(0);
                Material material1 = Material.valueOf(plugin.getConfig().getString("recipe.ingredients." + ingredient));
                recipe.setIngredient(c, material1);
            });
        } catch (IllegalArgumentException e) {
            plugin.getLogger().log(Level.SEVERE, "Vortex Manipulator recipe failed! Check the config file!");
        }
        // add the recipe to TARDIS' list
        plugin.getTardisApi().getShapedRecipes().put("Vortex Manipulator", recipe);
        // return the recipe
        return recipe;
    }
}
