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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMGUI {

    private final TARDISVortexManipulatorPlugin plugin;
    private final int tachyonLevel;
    private final ItemStack[] items;

    public TVMGUI(TARDISVortexManipulatorPlugin plugin, int tachyonLevel) {
        this.plugin = plugin;
        this.tachyonLevel = tachyonLevel;
        items = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // display
        ItemStack display = new ItemStack(Material.BOWL, 1);
        ItemMeta displayMeta = display.getItemMeta();
        displayMeta.setDisplayName(ChatColor.RESET + "Display");
        displayMeta.setLore(Collections.singletonList(ChatColor.GRAY + ""));
        displayMeta.setCustomModelData(108);
        display.setItemMeta(displayMeta);
        // keypad pad
        // 1
        ItemStack oneButton = new ItemStack(Material.BOWL, 1);
        ItemMeta oneButtonMeta = oneButton.getItemMeta();
        oneButtonMeta.setDisplayName(ChatColor.RESET + "1");
        oneButtonMeta.setCustomModelData(118);
        oneButton.setItemMeta(oneButtonMeta);
        // 2 abc
        ItemStack twoButton = new ItemStack(Material.BOWL, 1);
        ItemMeta twoButtonMeta = twoButton.getItemMeta();
        twoButtonMeta.setDisplayName(ChatColor.RESET + "2");
        twoButtonMeta.setLore(Collections.singletonList(ChatColor.GRAY + "ABC"));
        twoButtonMeta.setCustomModelData(126);
        twoButton.setItemMeta(twoButtonMeta);
        // 3 def
        ItemStack threeButton = new ItemStack(Material.BOWL, 1);
        ItemMeta threeButtonMeta = threeButton.getItemMeta();
        threeButtonMeta.setDisplayName(ChatColor.RESET + "3");
        threeButtonMeta.setLore(Collections.singletonList(ChatColor.GRAY + "DEF"));
        threeButtonMeta.setCustomModelData(125);
        threeButton.setItemMeta(threeButtonMeta);
        // 4 ghi
        ItemStack fourButton = new ItemStack(Material.BOWL, 1);
        ItemMeta fourButtonMeta = fourButton.getItemMeta();
        fourButtonMeta.setDisplayName(ChatColor.RESET + "4");
        fourButtonMeta.setLore(Collections.singletonList(ChatColor.GRAY + "GHI"));
        fourButtonMeta.setCustomModelData(111);
        fourButton.setItemMeta(fourButtonMeta);
        // 5 jkl
        ItemStack fiveButton = new ItemStack(Material.BOWL, 1);
        ItemMeta fiveButtonMeta = fiveButton.getItemMeta();
        fiveButtonMeta.setDisplayName(ChatColor.RESET + "5");
        fiveButtonMeta.setLore(Collections.singletonList(ChatColor.GRAY + "JKL"));
        fiveButtonMeta.setCustomModelData(110);
        fiveButton.setItemMeta(fiveButtonMeta);
        // 6 mno
        ItemStack sixButton = new ItemStack(Material.BOWL, 1);
        ItemMeta sixButtonMeta = sixButton.getItemMeta();
        sixButtonMeta.setDisplayName(ChatColor.RESET + "6");
        sixButtonMeta.setLore(Collections.singletonList(ChatColor.GRAY + "MNO"));
        sixButtonMeta.setCustomModelData(123);
        sixButton.setItemMeta(sixButtonMeta);
        // 7 pqrs
        ItemStack sevenButton = new ItemStack(Material.BOWL, 1);
        ItemMeta sevenButtonMeta = sevenButton.getItemMeta();
        sevenButtonMeta.setDisplayName(ChatColor.RESET + "7");
        sevenButtonMeta.setLore(Collections.singletonList(ChatColor.GRAY + "PQRS"));
        sevenButtonMeta.setCustomModelData(122);
        sevenButton.setItemMeta(sevenButtonMeta);
        // 8 tuv
        ItemStack eightButton = new ItemStack(Material.BOWL, 1);
        ItemMeta eightButtonMeta = eightButton.getItemMeta();
        eightButtonMeta.setDisplayName(ChatColor.RESET + "8");
        eightButtonMeta.setLore(Collections.singletonList(ChatColor.GRAY + "TUV"));
        eightButtonMeta.setCustomModelData(109);
        eightButton.setItemMeta(eightButtonMeta);
        // 9 wxyz
        ItemStack nineButton = new ItemStack(Material.BOWL, 1);
        ItemMeta nineButtonMeta = nineButton.getItemMeta();
        nineButtonMeta.setDisplayName(ChatColor.RESET + "9");
        nineButtonMeta.setLore(Collections.singletonList(ChatColor.GRAY + "WXYZ"));
        nineButtonMeta.setCustomModelData(117);
        nineButton.setItemMeta(nineButtonMeta);
        // 0
        ItemStack zeroButton = new ItemStack(Material.BOWL, 1);
        ItemMeta zeroButtonMeta = zeroButton.getItemMeta();
        zeroButtonMeta.setDisplayName(ChatColor.RESET + "0");
        zeroButtonMeta.setCustomModelData(132);
        zeroButton.setItemMeta(zeroButtonMeta);
        // symbols -_~
        ItemStack hashButton = new ItemStack(Material.BOWL, 1);
        ItemMeta hashButtonMeta = hashButton.getItemMeta();
        hashButtonMeta.setDisplayName(ChatColor.RESET + "#");
        hashButtonMeta.setLore(Collections.singletonList(ChatColor.GRAY + "~_-"));
        hashButtonMeta.setCustomModelData(112);
        hashButton.setItemMeta(hashButtonMeta);
        // space
        ItemStack starButton = new ItemStack(Material.BOWL, 1);
        ItemMeta starButtonMeta = starButton.getItemMeta();
        starButtonMeta.setDisplayName(ChatColor.RESET + "*");
        starButtonMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Space"));
        starButtonMeta.setCustomModelData(124);
        starButton.setItemMeta(starButtonMeta);
        // world
        ItemStack worldButton = new ItemStack(Material.BOWL, 1);
        ItemMeta worldButtonMeta = worldButton.getItemMeta();
        worldButtonMeta.setDisplayName(ChatColor.RESET + "World");
        worldButtonMeta.setCustomModelData(128);
        worldButton.setItemMeta(worldButtonMeta);
        // x
        ItemStack xButton = new ItemStack(Material.BOWL, 1);
        ItemMeta xButtonMeta = xButton.getItemMeta();
        xButtonMeta.setDisplayName(ChatColor.RESET + "X");
        xButtonMeta.setCustomModelData(129);
        xButton.setItemMeta(xButtonMeta);
        // y
        ItemStack yButton = new ItemStack(Material.BOWL, 1);
        ItemMeta yButtonMeta = yButton.getItemMeta();
        yButtonMeta.setDisplayName(ChatColor.RESET + "Y");
        yButtonMeta.setCustomModelData(130);
        yButton.setItemMeta(yButtonMeta);
        // z
        ItemStack zButton = new ItemStack(Material.BOWL, 1);
        ItemMeta zButtonMeta = zButton.getItemMeta();
        zButtonMeta.setDisplayName(ChatColor.RESET + "Z");
        zButtonMeta.setCustomModelData(131);
        zButton.setItemMeta(zButtonMeta);
        // tachyon level
        double percent = tachyonLevel / plugin.getConfig().getDouble("tachyon_use.max");
        short durability = (short) (1562 - (percent * 1562));
        ItemStack tachyon = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta tachyonMeta = tachyon.getItemMeta();
        tachyonMeta.setDisplayName(ChatColor.RESET + "Tachyon Level");
        int level = (int) (percent * 100);
        List<String> lore = Collections.singletonList(ChatColor.GRAY + "" + level + "%");
        int customModelData = 105;
        // TODO Change resource pack to use a single "custom_model_data" value with multiple "damage" values for tachyon level.
        if (level == 0) {
            customModelData = 95;
        } else if (level < 11) {
            customModelData = 96;
        } else if (level < 21) {
            customModelData = 97;
        } else if (level < 31) {
            customModelData = 98;
        } else if (level < 41) {
            customModelData = 99;
        } else if (level < 51) {
            customModelData = 100;
        } else if (level < 61) {
            customModelData = 101;
        } else if (level < 71) {
            customModelData = 102;
        } else if (level < 81) {
            customModelData = 103;
        } else if (level < 91) {
            customModelData = 104;
        }
        tachyonMeta.setCustomModelData(customModelData);
        tachyonMeta.setLore(lore);
        ((Damageable) tachyonMeta).setDamage(durability);
        tachyon.setItemMeta(tachyonMeta);
        // lifesigns
        ItemStack lifesigns = new ItemStack(Material.BOWL, 1);
        ItemMeta lifesignsMeta = lifesigns.getItemMeta();
        lifesignsMeta.setDisplayName(ChatColor.RESET + "Lifesigns");
        lifesignsMeta.setCustomModelData(113);
        lifesigns.setItemMeta(lifesignsMeta);
        // warp
        ItemStack goButton = new ItemStack(Material.BOWL, 1);
        ItemMeta goButtonMeta = goButton.getItemMeta();
        goButtonMeta.setDisplayName(ChatColor.RESET + "Enter Vortex / Save location / Check lifesigns");
        goButtonMeta.setCustomModelData(127);
        goButton.setItemMeta(goButtonMeta);
        // beacon
        ItemStack beacon = new ItemStack(Material.BOWL, 1);
        ItemMeta beaconMeta = beacon.getItemMeta();
        beaconMeta.setDisplayName(ChatColor.RESET + "Beacon signal");
        beaconMeta.setCustomModelData(106);
        beacon.setItemMeta(beaconMeta);
        // message
        ItemStack message = new ItemStack(Material.BOWL, 1);
        ItemMeta messageMeta = message.getItemMeta();
        messageMeta.setDisplayName(ChatColor.RESET + "Messages");
        messageMeta.setCustomModelData(115);
        message.setItemMeta(messageMeta);
        // save
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta saveMeta = save.getItemMeta();
        saveMeta.setDisplayName(ChatColor.RESET + "Save current location");
        saveMeta.setCustomModelData(74);
        save.setItemMeta(saveMeta);
        // load
        ItemStack load = new ItemStack(Material.BOWL, 1);
        ItemMeta loadMeta = load.getItemMeta();
        loadMeta.setDisplayName(ChatColor.RESET + "Load saved location");
        loadMeta.setCustomModelData(114);
        load.setItemMeta(loadMeta);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RESET + "Close");
        closeMeta.setCustomModelData(1);
        close.setItemMeta(closeMeta);
        // next
        ItemStack next = new ItemStack(Material.BOWL, 1);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(ChatColor.RESET + "Next character");
        nextMeta.setCustomModelData(116);
        next.setItemMeta(nextMeta);
        // back
        ItemStack previous = new ItemStack(Material.BOWL, 1);
        ItemMeta previousMeta = previous.getItemMeta();
        previousMeta.setDisplayName(ChatColor.RESET + "Previous character");
        previousMeta.setCustomModelData(120);
        previous.setItemMeta(previousMeta);

        return new ItemStack[]{null, null, null, null, display, null, null, null, null, tachyon, null, worldButton, oneButton, twoButton, threeButton, null, save, null, lifesigns, null, xButton, fourButton, fiveButton, sixButton, null, load, null, null, null, yButton, sevenButton, eightButton, nineButton, null, message, null, null, null, zButton, starButton, zeroButton, hashButton, null, beacon, null, close, null, null, previous, null, next, null, null, goButton};
    }

    public ItemStack[] getItems() {
        return items;
    }
}
