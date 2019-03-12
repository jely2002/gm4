package com.belka.spigot.gm4.modules;

import java.util.Random;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class VorpelSwords implements Listener {
	//Based of inventivetalent's VorpelSwords
	private MainClass mc;

	public VorpelSwords(MainClass mc){
		this.mc = mc;
	}

    public int max = 10;
    public int amount = 3;
	public Random random = new Random();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(mc.getConfig().getBoolean("VorpelSwords") == true) {
			max = mc.getConfig().getInt("options.VorpelSwords.max");
			amount = mc.getConfig().getInt("options.VorpelSwords.amount");
			Player killer = e.getEntity().getKiller();
			if(killer != null) {
				if(isSimilar(CustomItems.VORPEL_SWORD(), killer.getInventory().getItemInMainHand())) {
					if(!(e.getEntity() instanceof Player) && random.nextInt(max) <= amount) {
						ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
						SkullMeta meta = (SkullMeta) skull.getItemMeta();
						meta.setOwner(getHeadOwner(e.getEntity()));
						skull.setItemMeta(meta);
						killer.getWorld().dropItemNaturally(e.getEntity().getLocation(), skull);
					}
					else if(e.getEntity() instanceof Player) {
						ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
						SkullMeta meta = (SkullMeta) skull.getItemMeta();
						meta.setOwner(e.getEntity().getName());
						skull.setItemMeta(meta);
						killer.getWorld().dropItemNaturally(e.getEntity().getLocation(), skull);
					}
				}
			}
		}
	}

	boolean isSimilar(ItemStack stack, ItemStack other) {
		if (stack == null || other == null) {
			return false;
		}
		if (stack == other || stack.getType() == other.getType() && stack.hasItemMeta() && other.hasItemMeta() && stack.getItemMeta().getDisplayName().equals(other.getItemMeta().getDisplayName()) && stack.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_MOBS) && stack.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_MOBS) == 3) {
			return true;
		}
		return false;
	}
	
	public String getHeadOwner(Entity type) {
		if (type instanceof Player) {
			return type.getName();
		}
		else if(type instanceof Blaze) {
			return "MHF_Blaze";
		}
		else if(type instanceof Spider) {
			return "MHF_Spider";
		}
		else if(type instanceof CaveSpider) {
			return "MHF_CaveSpider";
		}
		else if(type instanceof Chicken) {
			return "MHF_Chicken";
		}
		else if(type instanceof Cow) {
			return "MHF_Cow";
		}
		else if(type instanceof Enderman) {
			return "MHF_Enderman";
		}
		else if(type instanceof Ghast) {
			return "MHF_Ghast";
		}
		else if(type instanceof Golem) {
			return "MHF_Golem";
		}
		else if(type instanceof MagmaCube) {
			return "MHF_LavaSlime";
		}
		else if(type instanceof MushroomCow) {
			return "MHF_MushroomCow";
		}
		else if(type instanceof Ocelot) {
			return "MHF_Ocelot";
		}
		else if(type instanceof Pig) {
			return "MHF_Pig";
		}
		else if(type instanceof PigZombie) {
			return "MHF_PigZombie";
		}
		else if(type instanceof Sheep) {
			return "MHF_Sheep";
		}
		else if(type instanceof Slime) {
			return "MHF_Slime";
		}
		else if(type instanceof Squid) {
			return "MHF_Squid";
		}
		else if(type instanceof Villager) {
			return "MHF_Villager";
		}
		else if(type instanceof Skeleton) {
			return "MHF_Skeleton";
		}
		else if(type instanceof Zombie) {
			return "MHF_Zombie";
		}
		else if(type instanceof Creeper) {
			return "MHF_Creeper";
		}
		else if(type instanceof Witch) {
			return "MHF_Witch";
		}
		else if(type instanceof Wither) {
			return "MHF_Wither";
		}
		else if(type instanceof Wolf) {
			return "MHF_Wolf";
		}
		else if(type instanceof Guardian) {
			return "MHF_Guardian";
		}
		else if(type instanceof Horse) {
			return "gavertoso";
		}
		else if(type instanceof Bat) {
			return "bozzobrain";
		}
		else if(type instanceof Snowman) {
			return "Koebasti";
		}
		else if(type instanceof Rabbit) {
			return "rabbit2077";
		}
		return "MHF_Question";
	}
}
