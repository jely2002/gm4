package api;

import com.belka.spigot.gm4.MainClass;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationHelper {

    public static MainClass mc;

    public LocationHelper(MainClass mc) {
        this.mc = mc;
    }

    public static void writeLocation(Location loc, FileConfiguration config, String path) {
        if(loc == null) throw new NullArgumentException("Location cannot be null");
        config.set(path + ".x", loc.getX());
        config.set(path + ".y", loc.getY());
        config.set(path + ".z", loc.getZ());
        config.set(path + ".world", loc.getWorld().getName());
        mc.storage.saveAll();
    }

    public static void writeLocations(ArrayList<Location> locations, FileConfiguration config, String path) {
        for(Location loc : locations) {
            int id;
            if(config.getConfigurationSection(path).getKeys(false).isEmpty()) {
                id = 0;
            } else {
                Set<String> keysString = config.getConfigurationSection(path).getKeys(false);
                Set<Integer> keys = keysString.stream()
                        .map(s -> Integer.parseInt(s))
                        .collect(Collectors.toSet());
                id = Collections.max(keys) + 1;
            }
            config.set(path + "." + id +".x", loc.getX());
            config.set(path + "." + id + ".y", loc.getY());
            config.set(path + "." + id + ".z", loc.getZ());
            config.set(path + "." + id + ".world", loc.getWorld().getName());
            mc.storage.saveAll();
        }
    }

    public static ArrayList<Location> readLocations(FileConfiguration config, String containerPath) {
        ArrayList<Location> locations = new ArrayList<>();
        if(!config.contains(containerPath)) throw new IllegalArgumentException("No locations were found at this path");
        for(String key : config.getConfigurationSection(containerPath).getKeys(false)) {
            int x = config.getInt(containerPath + "." + key + ".x");
            int y = config.getInt(containerPath + "." + key + ".y");
            int z = config.getInt(containerPath + "." + key + ".z");
            String worldName = config.getString(containerPath + "." + key + ".world");
            World world = Bukkit.getWorld(worldName);
            Location loc = new Location(world, x, y, z);
            locations.add(loc);
        }
        return locations;
    }

    public static Location readLocation(FileConfiguration config, String path) {
        if(!config.contains(path)) throw new IllegalArgumentException("No location was found at this path");
        int x = config.getInt(path + ".x");
        int y = config.getInt(path + ".y");
        int z = config.getInt(path + ".z");
        String worldName = config.getString(path + ".world");
        if(worldName == null) throw new IllegalArgumentException("No location was found at this path (invalid world)");
        World world = Bukkit.getWorld(worldName);
        return new Location(world, x, y, z);
    }

}
