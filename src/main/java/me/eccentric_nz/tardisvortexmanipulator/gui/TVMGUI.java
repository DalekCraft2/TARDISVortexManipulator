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
    private final ItemStack[] gui;

    public TVMGUI(TARDISVortexManipulatorPlugin plugin, int tachyonLevel) {
        this.plugin = plugin;
        this.tachyonLevel = tachyonLevel;
        gui = getItemStack();
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
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta none = one.getItemMeta();
        none.setDisplayName(ChatColor.RESET + "1");
        none.setCustomModelData(118);
        one.setItemMeta(none);
        // 2 abc
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta abc = two.getItemMeta();
        abc.setDisplayName(ChatColor.RESET + "2");
        abc.setLore(Collections.singletonList(ChatColor.GRAY + "ABC"));
        abc.setCustomModelData(126);
        two.setItemMeta(abc);
        // 3 def
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta def = three.getItemMeta();
        def.setDisplayName(ChatColor.RESET + "3");
        def.setLore(Collections.singletonList(ChatColor.GRAY + "DEF"));
        def.setCustomModelData(125);
        three.setItemMeta(def);
        // 4 ghi
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta ghi = four.getItemMeta();
        ghi.setDisplayName(ChatColor.RESET + "4");
        ghi.setLore(Collections.singletonList(ChatColor.GRAY + "GHI"));
        ghi.setCustomModelData(111);
        four.setItemMeta(ghi);
        // 5 jkl
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta jkl = five.getItemMeta();
        jkl.setDisplayName(ChatColor.RESET + "5");
        jkl.setLore(Collections.singletonList(ChatColor.GRAY + "JKL"));
        jkl.setCustomModelData(110);
        five.setItemMeta(jkl);
        // 6 mno
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta mno = six.getItemMeta();
        mno.setDisplayName(ChatColor.RESET + "6");
        mno.setLore(Collections.singletonList(ChatColor.GRAY + "MNO"));
        mno.setCustomModelData(123);
        six.setItemMeta(mno);
        // 7 pqrs
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta pqrs = seven.getItemMeta();
        pqrs.setDisplayName(ChatColor.RESET + "7");
        pqrs.setLore(Collections.singletonList(ChatColor.GRAY + "PQRS"));
        pqrs.setCustomModelData(122);
        seven.setItemMeta(pqrs);
        // 8 tuv
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta tuv = eight.getItemMeta();
        tuv.setDisplayName(ChatColor.RESET + "8");
        tuv.setLore(Collections.singletonList(ChatColor.GRAY + "TUV"));
        tuv.setCustomModelData(109);
        eight.setItemMeta(tuv);
        // 9 wxyz
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta wxyz = nine.getItemMeta();
        wxyz.setDisplayName(ChatColor.RESET + "9");
        wxyz.setLore(Collections.singletonList(ChatColor.GRAY + "WXYZ"));
        wxyz.setCustomModelData(117);
        nine.setItemMeta(wxyz);
        // 0
        ItemStack zero = new ItemStack(Material.BOWL, 1);
        ItemMeta nada = zero.getItemMeta();
        nada.setDisplayName(ChatColor.RESET + "0");
        nada.setCustomModelData(132);
        zero.setItemMeta(nada);
        // symbols -_*~
        ItemStack hash = new ItemStack(Material.BOWL, 1);
        ItemMeta symbols = hash.getItemMeta();
        symbols.setDisplayName(ChatColor.RESET + "#");
        symbols.setLore(Collections.singletonList(ChatColor.GRAY + "~_-"));
        symbols.setCustomModelData(112);
        hash.setItemMeta(symbols);
        // space
        ItemStack star = new ItemStack(Material.BOWL, 1);
        ItemMeta space = star.getItemMeta();
        space.setDisplayName(ChatColor.RESET + "*");
        space.setLore(Collections.singletonList(ChatColor.GRAY + "Space"));
        space.setCustomModelData(124);
        star.setItemMeta(space);
        // world
        ItemStack world = new ItemStack(Material.BOWL, 1);
        ItemMeta but = world.getItemMeta();
        but.setDisplayName(ChatColor.RESET + "World");
        but.setCustomModelData(128);
        world.setItemMeta(but);
        // x
        ItemStack x = new ItemStack(Material.BOWL, 1);
        ItemMeta sel = x.getItemMeta();
        sel.setDisplayName(ChatColor.RESET + "X");
        sel.setCustomModelData(129);
        x.setItemMeta(sel);
        // y
        ItemStack y = new ItemStack(Material.BOWL, 1);
        ItemMeta hei = y.getItemMeta();
        hei.setDisplayName(ChatColor.RESET + "Y");
        hei.setCustomModelData(130);
        y.setItemMeta(hei);
        // z
        ItemStack z = new ItemStack(Material.BOWL, 1);
        ItemMeta coord = z.getItemMeta();
        coord.setDisplayName(ChatColor.RESET + "Z");
        coord.setCustomModelData(131);
        z.setItemMeta(coord);
        // tachyon level
        double percent = tachyonLevel / plugin.getConfig().getDouble("tachyon_use.max");
        short durability = (short) (1562 - (percent * 1562));
        ItemStack tachyon = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta tachyonMeta = tachyon.getItemMeta();
        tachyonMeta.setDisplayName(ChatColor.RESET + "Tachyon Level");
        int level = (int) (percent * 100);
        List<String> lore = Collections.singletonList(ChatColor.GRAY + "" + level + "%");
        int customModelData = 105;
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
        ItemStack warp = new ItemStack(Material.BOWL, 1);
        ItemMeta tol = warp.getItemMeta();
        tol.setDisplayName(ChatColor.RESET + "Enter Vortex / Save location / Check lifesigns");
        tol.setCustomModelData(127);
        warp.setItemMeta(tol);
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
        ItemMeta curr = save.getItemMeta();
        curr.setDisplayName(ChatColor.RESET + "Save current location");
        curr.setCustomModelData(74);
        save.setItemMeta(curr);
        // load
        ItemStack load = new ItemStack(Material.BOWL, 1);
        ItemMeta disk = load.getItemMeta();
        disk.setDisplayName(ChatColor.RESET + "Load saved location");
        disk.setCustomModelData(114);
        load.setItemMeta(disk);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName(ChatColor.RESET + "Close");
        win.setCustomModelData(1);
        close.setItemMeta(win);
        // next
        ItemStack next = new ItemStack(Material.BOWL, 1);
        ItemMeta cha = next.getItemMeta();
        cha.setDisplayName(ChatColor.RESET + "Next character");
        cha.setCustomModelData(116);
        next.setItemMeta(cha);
        // back
        ItemStack prev = new ItemStack(Material.BOWL, 1);
        ItemMeta let = prev.getItemMeta();
        let.setDisplayName(ChatColor.RESET + "Previous character");
        let.setCustomModelData(120);
        prev.setItemMeta(let);

        return new ItemStack[]{null, null, null, null, display, null, null, null, null, tachyon, null, world, one, two, three, null, save, null, lifesigns, null, x, four, five, six, null, load, null, null, null, y, seven, eight, nine, null, message, null, null, null, z, star, zero, hash, null, beacon, null, close, null, null, prev, null, next, null, null, warp};
    }

    public ItemStack[] getGui() {
        return gui;
    }
}
