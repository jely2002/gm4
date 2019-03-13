package api;

import org.bukkit.Location;
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
}
