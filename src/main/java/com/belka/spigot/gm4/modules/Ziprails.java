package com.belka.spigot.gm4.modules;

import api.Setting;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.TripwireHook;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.HashMap;

public class Ziprails implements Module, Listener {

    private MainClass mc;
    private HashMap<Minecart, TripwireHook> suspendedCarts = new HashMap<>();

    public Ziprails(MainClass mc) {
        this.mc = mc;
    }

	@Override
	public Setting getSetting() { return new Setting("Ziprails", Material.STRING); }

    @EventHandler
    public void getOnLine(VehicleMoveEvent e) {
        if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        if(!isEnabled()) return;
        if(!(e.getVehicle() instanceof Minecart)) return;
        Minecart cart = (Minecart) e.getVehicle();
        if(cart.getLocation().add(0, 1, 0).getBlock().getType() == Material.TRIPWIRE_HOOK) {
            TripwireHook hook = (TripwireHook) cart.getLocation().add(0, 1, 0).getBlock().getBlockData();
            if(cart.getFacing().equals(hook.getFacing())) {
                if(hook.isAttached()) {
                    addCart(cart, hook);
                    if(!cart.getPassengers().isEmpty()) {
                        for(Entity passenger : cart.getPassengers()) {
                            if(passenger instanceof Player) {
                                Advancements.grantAdvancement("wheeeeeeeee", (Player) passenger);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void getOffLine(VehicleMoveEvent e) {
        if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        if(!isEnabled()) return;
        if(!(e.getVehicle() instanceof Minecart)) return;
        Minecart cart = (Minecart) e.getVehicle();
        if(suspendedCarts.containsKey(cart)) {
            if(e.getFrom().add(0, 1, 0).getBlock().getType() == Material.TRIPWIRE_HOOK) {
                TripwireHook hook = (TripwireHook) e.getFrom().add(0, 0, 0).getBlock().getBlockData();
                if(cart.getFacing().equals(hook.getFacing().getOppositeFace())) {
                    removeCart(cart);
                }
            }
        }
    }

    @EventHandler
    public void keepVelocity(VehicleMoveEvent e) {
        if(!isEnabled()) return;
        if(!(e.getVehicle() instanceof Minecart)) return;
        Minecart cart = (Minecart) e.getVehicle();
        if(suspendedCarts.containsKey(cart)) {
            if(!(e.getFrom().add(0, 1, 0).getBlock().getType() == Material.TRIPWIRE_HOOK)) {
                if(cart.getLocation().add(0,1,0).getBlock().getType() == Material.TRIPWIRE || cart.getLocation().add(0,1,0).getBlock().getType() == Material.TRIPWIRE_HOOK) {
                    cart.setVelocity(cart.getFacing().getDirection().multiply(mc.getStorage().config().getDouble("Ziprails.railSpeedFactor")));
                } else {
                    removeCart(cart);
                }
            }
        }
    }

    @EventHandler
    public void onDestroy(VehicleDestroyEvent e) {
        if(!isEnabled()) return;
        if(!(e.getVehicle() instanceof Minecart)) return;
        Minecart cart = (Minecart) e.getVehicle();
        suspendedCarts.remove(cart);
    }

    private void removeCart(Minecart cart) {
        cart.setGravity(true);
        suspendedCarts.remove(cart);
    }

    private void addCart(Minecart cart, TripwireHook hook) {
        if(!suspendedCarts.containsKey(cart)) {
            cart.setGravity(false);
            suspendedCarts.put(cart, hook);
        }
    }

    private boolean isEnabled() {
        return mc.getStorage().config().getBoolean("Ziprails.enabled");
    }

}
