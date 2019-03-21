package com.belka.spigot.gm4.config;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SettingsGUI implements Listener {

    private Inventory inv;
    private MainClass mc;

    public SettingsGUI(MainClass mc) {
        this.mc = mc;
    }

    public void openInventory(Player p) {
        inv = Bukkit.createInventory(null, getInventorySize(), ChatColor.DARK_GRAY + "Gamemode 4 modules");
        addModules();
        p.openInventory(inv);
    }

    private void addModules() {
        int count = 0;
        for(String module : mc.getConfig().getConfigurationSection("").getKeys(false)) {
            if(mc.getConfig().getBoolean(module + ".enabled")) {
                inv.setItem(count, createItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + module.replaceAll("\\d+", "").replaceAll("(.)([A-Z])", "$1 $2")));
            } else {
                inv.setItem(count, createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + module.replaceAll("\\d+", "").replaceAll("(.)([A-Z])", "$1 $2")));
            }
            count++;
        }
        inv.setItem(inv.getSize() - 1, createItem(Material.BARRIER, ChatColor.RED + "Close menu"));
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<>();
        if(material == Material.BARRIER) {
            Lore.add("Click to close the menu");
        } else {
            if(mc.getConfig().getBoolean(name.replaceAll("\\s+","").replace("§c", "").replace("§a", "") + ".enabled")) {
                Lore.add("Click to disable this module");
            } else {
                Lore.add("Click to enable this module");
            }
        }
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack updateItem(String name, boolean state) {
        if(state) {
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name.replace("§a", "§c"));
            ArrayList<String> Lore = new ArrayList<>();
            Lore.add("Click to enable this module");
            meta.setLore(Lore);
            item.setItemMeta(meta);
            return item;
        } else {
            ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name.replace("§c", "§a"));
            ArrayList<String> Lore = new ArrayList<>();
            Lore.add("Click to disable this module");
            meta.setLore(Lore);
            item.setItemMeta(meta);
            return item;
        }
    }

    private int getInventorySize() {
        int max = mc.getConfig().getConfigurationSection("").getKeys(false).size();
        if (max <= 0) return 9;
        int quotient = (int)Math.ceil(max / 9.0);
        return quotient * 9 + 9;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) return;
        if(e.getWhoClicked() == null) return;
        Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        Inventory inventory = e.getInventory();
        if (inventory.getName().equals(inv.getName())) {
            if(clicked.getItemMeta() == null) return;
            if (mc.getConfig().getConfigurationSection("").getKeys(false).contains(clicked.getItemMeta().getDisplayName().replaceAll("\\s+","").replace("§c", "").replace("§a", ""))) {
                if(mc.getConfig().getBoolean(clicked.getItemMeta().getDisplayName().replaceAll("\\s+","").replace("§c", "").replace("§a", "") + ".enabled")) {
                    p.sendMessage(ChatColor.RED + "Disabled " + clicked.getItemMeta().getDisplayName().replace("§c", "").replace("§a", "") + ".");
                    inv.setItem(e.getSlot(),updateItem(clicked.getItemMeta().getDisplayName(), true));
                } else {
                    p.sendMessage(ChatColor.GREEN + "Enabled " + clicked.getItemMeta().getDisplayName().replace("§c", "").replace("§a", "") + ".");
                    inv.setItem(e.getSlot() ,updateItem(clicked.getItemMeta().getDisplayName(), false));
                }
                mc.getConfig().set(clicked.getItemMeta().getDisplayName().replaceAll("\\s+","").replace("§c", "").replace("§a", "") + ".enabled", !mc.getConfig().getBoolean(clicked.getItemMeta().getDisplayName().replaceAll("\\s+","").replace("§c", "").replace("§a", "") + ".enabled"));
                mc.saveConfig();
            } else if(clicked.getType() == Material.BARRIER) {
                p.closeInventory();
            }
            e.setCancelled(true);
        }
    }
}
