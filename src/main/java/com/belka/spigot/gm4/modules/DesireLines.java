package com.belka.spigot.gm4.modules;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class DesireLines implements Listener {

	private MainClass mc;

	public DesireLines(MainClass mc){
		this.mc = mc;
	}

    public int max = 50;
    public int amount = 2;
	public Random random = new Random();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(mc.getConfig().getBoolean("DesireLines") == true) {
			max = mc.getConfig().getInt("options.DesireLines.max");
			amount = mc.getConfig().getInt("options.DesireLines.amount");
			final Player p = e.getPlayer();
			if(e.getFrom().getBlock().equals(e.getTo().getBlock())) {
				return;
			}
			if(random.nextInt(max) <= amount) {
				Block below = p.getLocation().getBlock();
				if(below != null && (p.getGameMode() == GameMode.SURVIVAL ||  p.getGameMode() == GameMode.ADVENTURE)) {
					ItemStack replace = new ItemStack(below.getType(), 1, (short) below.getData());
					if(replace.getType().equals(Material.AIR)) {
						below = p.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
						if(replace.getType() == Material.GRASS || replace.getType() == Material.DIRT || replace.getType() == Material.SAND) {
							below.getWorld().spigot().playEffect(below.getLocation().add(0.5, 0.5, 0.5), Effect.TILE_BREAK, below.getTypeId(), (int)below.getData(), 0.0f, 0.0f, 0.0f, 0.0f, 10, 16);
						}
						replace = replacement(new ItemStack(below.getType(), 1, (short) below.getData()));
						below.setType(replace.getType());
						below.setData((byte) replace.getDurability());
						below.getState().update();
					}
					else if(!replace.getType().equals(Material.AIR)) {
						if(replace.getType() == Material.LONG_GRASS || replace.getType() == Material.DOUBLE_PLANT || replace.getType() == Material.BROWN_MUSHROOM || replace.getType() == Material.RED_MUSHROOM || replace.getType() == Material.YELLOW_FLOWER || replace.getType() == Material.DEAD_BUSH) {
							below.getWorld().spigot().playEffect(below.getLocation().add(0.5, 0.5, 0.5), Effect.TILE_BREAK, below.getTypeId(), (int)below.getData(), 0.0f, 0.0f, 0.0f, 0.0f, 10, 16);
							//below.getWorld().dropItem(below.getLocation(), replace);
						}
						replace = replacement(new ItemStack(below.getType(), 1, (short) below.getData()));
						below.setType(replace.getType());
						below.setData((byte) replace.getDurability());
						below.getState().update();
					}
				}
			}
		}
	}
	
	public ItemStack replacement(ItemStack i) {
		ItemStack returnItem = i;
		Material mat = i.getType();
		if(mat == Material.LONG_GRASS || mat == Material.DOUBLE_PLANT || mat == Material.BROWN_MUSHROOM || mat == Material.RED_MUSHROOM || mat == Material.YELLOW_FLOWER || mat == Material.DEAD_BUSH) {
			returnItem.setType(Material.AIR);
			returnItem.setDurability((short) 0);
		}
		else if(mat == Material.GRASS) {
			returnItem.setType(Material.DIRT);
			returnItem.setDurability((short) 0);
		}
		else if(mat == Material.DIRT && returnItem.getDurability() == 0) {
			returnItem.setType(Material.DIRT);
			returnItem.setDurability((short) 1);
		}
		else if(mat == Material.SAND) {
			returnItem.setType(Material.SANDSTONE);
			returnItem.setDurability((short) 0);
		}
		return returnItem;
	}
}
