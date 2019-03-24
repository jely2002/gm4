package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.material.PoweredRail;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
                        Bukkit.broadcastMessage("spawner " + sp.getLocation().subtract(0,1,0).getBlock().getType().toString());
                        for (Entity entity : Helper.getNearbyEntities(sp.getLocation().subtract(0, 1, 0), 1)) {
                            if (entity instanceof Minecart) {
                                Minecart m = (Minecart) entity;
                                if (m.getPassengers().size() == 0) {
                                    if (m.getDisplayBlock().getItemType() == Material.AIR || m.getDisplayBlock().getItemType() == Material.LEGACY_AIR) {
										Bukkit.broadcastMessage("Mob " + spawner.getSpawnedType().toString());
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

										JSONParser parser = new JSONParser();
										String s = "[{\"Entity\":{\"id\":\"minecraft:" + mob + "\"}, \"Weight\":1}]";
										try {
											Object obj = parser.parse(s);
											JSONArray data = (JSONArray)obj;
											nbtEntity.setObject("SpawnPotentials", data);
										} catch(ParseException pe) {
											System.out.println("Position: " + pe.getPosition());
											System.out.println(pe);
										}
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


					Minecart mic = (Minecart) sm.getWorld().spawnEntity(sm.getLocation(), EntityType.MINECART);
					mic.setVelocity(sm.getVelocity());
					sm.remove();
				}
			}
		}
	}

}