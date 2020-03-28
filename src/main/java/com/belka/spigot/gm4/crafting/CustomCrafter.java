package com.belka.spigot.gm4.crafting;

import api.CustomBlock;
import api.CustomBlockType;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import com.belka.spigot.gm4.modules.Advancements;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class CustomCrafter implements Module, Listener {

	private MainClass mc;
	private RecipeHandler rh;
	private ArrayList<String> enabled = new ArrayList<>();

	public CustomCrafter(MainClass mc, RecipeHandler rh) {
		this.mc = mc;
		this.rh = rh;
	}

	@Override
	public void init(MainClass mc) {
		enabled.add("CustomCrafter");
		if (mc.getStorage().config().getBoolean("CustomCrafter.MasterCrafting")) enabled.add("MasterCrafter");
		if (mc.getStorage().config().getBoolean("CustomCrafter.BlastFurnace")) enabled.add("BlastFurnace");
		if (mc.getStorage().config().getBoolean("CustomCrafter.Disassembler")) enabled.add("Disassembler");
		if (mc.getStorage().config().getBoolean("CustomCrafter.EquivalentExchange")) enabled.add("AlchemicalCrafter");
	}

	@EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
		if (!mc.getStorage().config().getBoolean("CustomCrafter.enabled")) return;
		Item i = e.getItemDrop();
		final int[] task = new int[]{-1};
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
            if (i.isOnGround()) {
                Bukkit.getScheduler().cancelTask(task[0]);
                Location loc = i.getLocation();
                if (i.getItemStack().getType() == Material.CRAFTING_TABLE) {
                    Block b = loc.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                    if (b.getBlockData().getMaterial() == Material.DROPPER) {
                        Dropper dr = (Dropper) b.getState();
						if (rh.equalsRecipe(dr, CustomRecipes.create()) && dr.getInventory().getItem(0).getAmount() == 1) {
							if (CustomBlock.get(b.getLocation()) == null) {
								CustomBlock.create(CustomBlockType.CUSTOM_CRAFTER, b.getLocation());
								ItemStack item = e.getItemDrop().getItemStack();
								int amount = e.getItemDrop().getItemStack().getAmount() - 1;
								if (amount > 0) {
									item.setAmount(amount);
									e.getItemDrop().setItemStack(item);
								}
								else e.getItemDrop().remove();
								Advancements.grantAdvancement("clever_crafting", e.getPlayer());
							}
						}
                    }
                }
			}
		}, 0, 1L);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) { // Remove Custom Blocks
		Block b = event.getBlock();
		CustomBlock cb = CustomBlock.get(b.getLocation());
		if (cb != null && cb.getType() != CustomBlockType.ENDER_HOPPER) {
			cb.destroy(true);
//			event.getPlayer().sendMessage(ChatColor.GREEN + "Successfully destroyed a " + cb.getType().getName() + " at " + b.getX() + " " + b.getY() + " " + b.getZ() + ".");
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		updateInv(e.getInventory());
	}
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		updateInv(e.getInventory());
	}
	private void updateInv(Inventory inv) {
		if (inv.getType().equals(InventoryType.DROPPER) && inv.getLocation() != null) {
			Block b = inv.getLocation().getBlock();
			CustomBlock cb = CustomBlock.get(b.getLocation());
			if (cb != null) {
				Objects.requireNonNull(Bukkit.getEntity(cb.getUuid())).setFireTicks(Integer.MAX_VALUE);
				mc.getServer().getScheduler().runTaskLater(mc, () -> {
					Dropper dropper = (Dropper) b.getState();
					rh.craft(dropper, (Player) inv.getViewers().get(0));
				}, 1L);
			}
		}
		else if (inv.getType().equals(InventoryType.HOPPER)) {

		}
	}

	@EventHandler
	public void onInteract(PlayerArmorStandManipulateEvent e) { // Disable item removal armor stand
		if (e.getRightClicked().getCustomName() != null && enabled.contains(e.getRightClicked().getCustomName())) {
			e.setCancelled(true);
		}
	}
}
