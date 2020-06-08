package api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	public static List<Block> getBlocksBetween(Location loc1, Location loc2) {
		List<Block> blocks = new ArrayList<>();
		if (loc1.getWorld() != loc2.getWorld()) return null;

		int maxBlockX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		int bominBlockX = Math.min(loc1.getBlockX(), loc2.getBlockX());

		int maxBlockY = Math.max(loc1.getBlockY(), loc2.getBlockY());
		int bominBlockY = Math.min(loc1.getBlockY(), loc2.getBlockY());

		int maxBlockZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		int bominBlockZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

		for (int x = bominBlockX; x <= maxBlockX; x++)
			for(int z = bominBlockZ; z <= maxBlockZ; z++)
				for(int y = bominBlockY; y <= maxBlockY; y++)
					blocks.add(loc1.getWorld().getBlockAt(x, y, z));

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

	public static int toInteger(String number) {
		int a = 0;
		try {
			a = Integer.parseInt(number);
		} catch(NumberFormatException e) {
			return a;
		}
		return a;
	}
	public static boolean isInteger(String number) {
		try {
			Integer.parseInt(number);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static List<String> filterTab(String arg, String... items) {
		return Stream.of(items).filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).collect(Collectors.toList());
	}
	public static List<String> filterTab(String arg, Stream<String> items) {
		return items.filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).collect(Collectors.toList());
	}
	public static List<String> filterTab(String arg, List<String> items) {
		return items.stream().filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).collect(Collectors.toList());
	}

	public static List<String> getLookingCoords(Player p, int i, int size) {
    	Block b = p.getTargetBlockExact(5);
    	List<String> items = new ArrayList<>();
    	if (b != null) {
    		if (i == 0) {
    			if (size >= 3) items.add(b.getX() + " " + b.getY() + " " + b.getZ());
				if (size >= 2) items.add(b.getX() + " " + b.getY());
				if (size >= 1) items.add(b.getX() + "");
			}
			else if (i == 1) {
				if (size >= 2) items.add(b.getY() + " " + b.getZ());
				if (size >= 1) items.add(b.getY() + "");
			}
			else if (i == 2) items.add(b.getZ() + "");
		}
    	else {
			if (i == 0) {
				if (size >= 3) items.add("~ ~ ~");
				if (size >= 2) items.add("~ ~");
				if (size >= 1) items.add("~");
			}
			else if (i == 1) {
				if (size >= 2) items.add("~ ~");
				if (size >= 1) items.add("~");
			}
			else if (i == 2) items.add("~");
		}
    	return items;
	}

	public static String getItemName(ItemStack item) {
		String name = "";
		if (item.getItemMeta() != null) name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
		if (name.equalsIgnoreCase("")) name = Helper.capitalize(item.getType().name().replace("_", " "));
		return name;
	}
}
