package api;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static List<Entity> getNearbyEntities(Location l, int size) {
        List<Entity> entities = new ArrayList<>();

        for(Entity e : l.getWorld().getEntities()) {
            if(l.distance(e.getLocation()) <= size) {
                entities.add(e);
            }
        }
        return entities;
    }
    public static List<EntityType> getNearbyEntityTypes(Location l, int size) {
        List<EntityType> entityTypes = new ArrayList<>();

        for(Entity e : l.getWorld().getEntities()) {
            if(l.distance(e.getLocation()) <= size) {
				entityTypes.add(e.getType());
            }
        }
        return entityTypes;
    }
    public static List<Player> getNearbyPlayers(Location l, int size) {
        List<Player> players = new ArrayList<>();

        for(Entity e : l.getWorld().getEntities()) {
            if(l.distance(e.getLocation()) <= size && e instanceof Player) {
                players.add((Player) e);
            }
        }
        return players;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

	public static float radToDeg(float radians) {
		return (float) ((radians * 180) / Math.PI);
	}
	public static float degToRad(float degrees) {
		return (float) ((degrees * Math.PI)/180);
	}
}
