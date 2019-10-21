package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.material.PoweredRail;

import java.util.List;

public class SpawnerMinecarts implements Listener {

	private MainClass mc;

	public SpawnerMinecarts(MainClass mc) {
		this.mc = mc;
	}

	@SuppressWarnings("deprecation")
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
		if (!mc.getConfig().getBoolean("SpawnerMinecarts.enabled")) return;
        if (e.getBlock().getType() == Material.REDSTONE_BLOCK) {
            if (e.getBlockAgainst().getType() == Material.PISTON) {
                Piston piston = (Piston) e.getBlockAgainst().getBlockData();
                if (piston.getFacing().equals(BlockFace.DOWN)) {
                    if (e.getBlockAgainst().getRelative(BlockFace.DOWN).getType() == Material.SPAWNER) {
                        Block sp = e.getBlockAgainst().getRelative(BlockFace.DOWN);
                        CreatureSpawner spawner = (CreatureSpawner) sp.getState();
                        for (Entity entity : Helper.getNearbyEntities(sp.getLocation().subtract(0, 1, 0), 1)) {
                            if (entity instanceof Minecart) {
                                Minecart m = (Minecart) entity;
                                if (m.getPassengers().size() == 0) {
                                    if (m.getDisplayBlock().getItemType() == Material.AIR || m.getDisplayBlock().getItemType() == Material.LEGACY_AIR) {
										String mob = spawner.getSpawnedType().toString().toLowerCase();
                                        sp.setType(Material.AIR);

                                        Location loc = m.getLocation();
										m.remove();
										Entity mms = loc.getWorld().spawnEntity(loc, EntityType.MINECART_MOB_SPAWNER);
										mms.setCustomNameVisible(true);
										mms.setCustomName(Helper.capitalize(mob.replace('_', ' ')));
										mms.setInvulnerable(true);

//										net.minecraft.server.v1_13_R2.Entity nmsEntity = ((CraftEntity) mms).getHandle();
//										NBTTagCompound nbt = new NBTTagCompound();
//
//										NBTTagCompound spawnData = new NBTTagCompound();
//										spawnData.set("id", new NBTTagString("minecraft:" + mob));
//										nbt.set("SpawnData", spawnData);
//
//										NBTTagList spawnPotentials = new NBTTagList();
//										NBTTagCompound entityNBT = new NBTTagCompound();
//										entityNBT.set("Entity", spawnData);
//										entityNBT.set("Weight", new NBTTagInt(1));
//										spawnPotentials.add(entityNBT);
//										nbt.set("SpawnPotentials", spawnPotentials);
//
//										Bukkit.broadcastMessage(nbt.toString());
//										nmsEntity.f(nbt);
//										setEntityNBT(mms, nbt);

										NBTEntity nbtEntity = new NBTEntity(mms);

										NBTCompound spawnData = nbtEntity.getCompound("SpawnData");
										spawnData.setString("id", "minecraft:" + mob);

										JsonObject jsonObject = new JsonParser().parse("[{\"Entity\":{\"id\":\"minecraft:" + mob + "\"}, \"Weight\":1}]").getAsJsonObject();
										JsonArray data = jsonObject.getAsJsonArray();
										nbtEntity.setObject("SpawnPotentials", data);
										List<String> stored = mc.storage().data().getStringList("SpawnerMinecarts");
										stored.add(mms.getUniqueId().toString());
										mc.storage().data().set("SpawnerMinecarts", stored);
										mc.storage().saveData();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler
	public void onRide(VehicleMoveEvent e) {
		if (!mc.getConfig().getBoolean("SpawnerMinecarts.enabled")) return;
		if (e.getVehicle().getType() == EntityType.MINECART_MOB_SPAWNER) {
			Vehicle sm = e.getVehicle();
			if (e.getTo().getBlock().getType() == Material.ACTIVATOR_RAIL) {
				PoweredRail rail = (PoweredRail) e.getTo().getBlock().getState().getData();
				if (rail.isPowered()) {
					NBTEntity nbtEntity = new NBTEntity(sm);
					NBTCompound spawnData = nbtEntity.getCompound("SpawnData");
					String mob = spawnData.getString("id").replace("minecraft:", "").toUpperCase();

					Block block = sm.getLocation().getBlock().getRelative(BlockFace.UP);
					block.setType(Material.SPAWNER);
					BlockState blockState = block.getState();
					CreatureSpawner spawner = ((CreatureSpawner) blockState);
					spawner.setSpawnedType(Helper.getEntityByName(mob));
					blockState.update();

					List<String> stored = mc.storage().data().getStringList("SpawnerMinecarts");
					stored.remove(sm.getUniqueId().toString());
					mc.storage().data().set("SpawnerMinecarts", stored);
					mc.storage().saveData();

					Minecart mic = (Minecart) sm.getWorld().spawnEntity(sm.getLocation(), EntityType.MINECART);
					mic.setVelocity(sm.getVelocity());
					sm.remove();
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!mc.getConfig().getBoolean("SpawnerMinecarts.enabled")) return;
		if (e.getEntityType() == EntityType.MINECART_MOB_SPAWNER) {
			List<String> stored = mc.storage().data().getStringList("SpawnerMinecarts");
			if (stored.contains(e.getEntity().getUniqueId().toString())) {
				e.setCancelled(true);
			}
		}
	}
//	Check WIKI
}
