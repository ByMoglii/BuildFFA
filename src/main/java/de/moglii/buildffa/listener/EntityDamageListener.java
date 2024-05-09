package de.moglii.buildffa.listener;

import de.moglii.buildffa.main.Main;
import de.moglii.buildffa.methods.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityDamageListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    @EventHandler
    public void handleEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                if (Game.isInAProtectedChunk(player) && player.getLocation().getBlockY() - 2 >= Game.getHeight(player, "Max")) {
                    event.setCancelled(true);
                } else {
                    if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                        event.setCancelled(true);
                    } else {
                        if (player.getHealth() != 0) {
                            if (!Main.fight.contains(player)) {
                                Main.fight.add(player);
                            }
                            if (((Player) event.getEntity()).getKiller() != null) {
                                Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
                                    if (!player.isOnline()) {
                                        Game.addStats(player, "Deaths");
                                        if (((Player) event.getEntity()).getKiller() != null) {
                                            Game.addStats(((Player) event.getEntity()).getKiller(), "Kills");
                                        }
                                    }
                                    if (Main.fight.contains(player)) {
                                        Main.fight.remove(player);
                                    }
                                }, Game.playerFightTime);
                            }
                        }
                    }
                }
            }
        }
    }

}
