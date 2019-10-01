package api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static List<Entity> getNearbyEntities(Location l, double size) {
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

    public static EntityType getEntityByName(String name) {
		for (EntityType type : EntityType.values()) {
			if(type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}

	public static float radToDeg(float radians) {
		return (float) ((radians * 180) / Math.PI);
	}
	public static float degToRad(float degrees) {
		return (float) ((degrees * Math.PI)/180);
	}

	public static String capitalize(String str) {
        char[] ch = str.toCharArray();
		for (int i = 0; i < str.length(); i++) {
			if (i == 0 && ch[i] != ' ' ||
					ch[i] != ' ' && ch[i - 1] == ' ') {
				if (ch[i] >= 'a' && ch[i] <= 'z') {
					ch[i] = (char)(ch[i] - 'a' + 'A');
				}
			}
			else if (ch[i] >= 'A' && ch[i] <= 'Z')
				ch[i] = (char)(ch[i] + 'a' - 'A');
		}
		return new String(ch);
	}

	public static Location locFromConfig(String line) {
		String[] a = line.split(" ");
		int x = Integer.parseInt(a[0].split(":")[1]);
		int y = Integer.parseInt(a[1].split(":")[1]);
		int z = Integer.parseInt(a[2].split(":")[1]);
		World world = Bukkit.getWorld(a[3].split(":")[1]);
		return new Location(world, x, y, z);
	}

	public static boolean allEqual(Object... objs) {
		if(objs.length < 2) return true; // 0 or 1 objects are all equal
		Object key = objs[0]; // pick one
		for(Object o : objs) if(!o.equals(key)) return false;
		return true;
	}
}
