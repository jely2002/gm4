package api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    // HELPER FUNCTIONS
    public static List<Entity> getNearbyEntities(Location l, int size) {
        List<Entity> entities = new ArrayList<Entity>();

        for(Entity e : l.getWorld().getEntities()) {
            if(l.distance(e.getLocation()) <= size) {
                entities.add(e);
            }
        }
        return entities;
    }
    public static List<EntityType> getNearbyEntityTypes(Location l, int size) {
        List<EntityType> entitytypes = new ArrayList<EntityType>();

        for(Entity e : l.getWorld().getEntities()) {
            if(l.distance(e.getLocation()) <= size) {
                entitytypes.add(e.getType());
            }
        }
        return entitytypes;
    }
    public static List<Player> getNearbyPlayers(Location l, int size) {
        List<Player> players = new ArrayList<Player>();

        for(Entity e : l.getWorld().getEntities()) {
            if(l.distance(e.getLocation()) <= size && e instanceof Player) {
                players.add((Player) e);
            }
        }
        return players;
    }
}
