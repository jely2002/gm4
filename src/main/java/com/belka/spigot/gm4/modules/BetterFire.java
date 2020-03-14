package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BetterFire implements Listener, Initializable {
	private MainClass mc;

	public BetterFire(MainClass mc) {
		this.mc = mc;
	}

	private ArrayList<Arrow> fireArrows = new ArrayList<>();
	private HashMap<UUID, Integer> checkArrows = new HashMap<>();
	private ArrayList<Arrow> deleteArrows = new ArrayList<>();


	public void init(MainClass mc) {
		if(!mc.getStorage().config().getBoolean("BetterFire.enabled")) return;
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			for (Arrow arrow : fireArrows) {
				if(arrow.getFireTicks() > 0) {
					if (checkArrows.containsKey(arrow.getUniqueId())) {
						if (checkArrows.get(arrow.getUniqueId()) == arrow.getTicksLived()) {
							deleteArrows.add(arrow);
							checkArrows.remove(arrow.getUniqueId());
						} else {
							checkArrows.put(arrow.getUniqueId(), arrow.getTicksLived());
						}
					} else {
						checkArrows.put(arrow.getUniqueId(), arrow.getTicksLived());
					}
					setFire(arrow);
				} else {
					deleteArrows.add(arrow);
				}
			}
			if(deleteArrows.size() > 0) {
				for (Arrow arrow : deleteArrows) {
					fireArrows.remove(arrow);
				}
			}
		}, 0, 5L);
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		if(!mc.getStorage().config().getBoolean("BetterFire.enabled")) return;
		if(e.getHitEntity() != null) return;
		if (e.getEntity() instanceof Arrow) {
			final Arrow arrow = (Arrow)e.getEntity();
			if(!fireArrows.contains(arrow)) {
				fireArrows.add(arrow);
			}
			if (arrow.getFireTicks() > 0) {
				setFire(arrow);
			}
		}
	}

	@EventHandler
	public void onCreeperDamageByPlayer(EntityDamageByEntityEvent e) {
		if(!mc.getStorage().config().getBoolean("BetterFire.enabled")) return;
		if(e.getEntity() instanceof Creeper) {
			Creeper creeper = (Creeper) e.getEntity();
			if (creeper.getHealth() <= 2) {
				if (e.getDamager() instanceof Projectile) {
					Projectile projectile = (Projectile) e.getDamager();
					if (projectile.getShooter() instanceof Player) {
						if (projectile.getFireTicks() > 0) {
							creeper.getWorld().createExplosion(creeper.getLocation(), 2.5f, true);
							creeper.setHealth(0.0);
						}
					}
				} else if (e.getDamager() instanceof Player) {
					Player damager = (Player) e.getDamager();
					ItemStack damageItem = damager.getInventory().getItemInMainHand();
					if (damageItem.hasItemMeta()) {
						ItemMeta meta = damageItem.getItemMeta();
						if (meta.hasEnchant(Enchantment.FIRE_ASPECT)) {
							creeper.getWorld().createExplosion(creeper.getLocation(), 2.5f, true);
							creeper.setHealth(0.0);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onCreeperTakeDamage(EntityDamageEvent e) {
		if(!mc.getStorage().config().getBoolean("BetterFire.enabled")) return;
		if(e.getEntity() instanceof Creeper && (e.getCause() == DamageCause.FIRE_TICK || e.getCause() == DamageCause.FIRE)) {
			Creeper creeper = (Creeper)e.getEntity();
			if(creeper.getHealth() <= 2) {
				creeper.getWorld().createExplosion(creeper.getLocation(), 2.5f, true);
				creeper.setHealth(0.0);
			}
		}
	}

	private void setFire(Arrow a) {
		if(a.getWorld().getBlockAt(a.getLocation()).getType() == Material.AIR) {
			a.getWorld().getBlockAt(a.getLocation()).setType(Material.FIRE);
		}
	}
}
