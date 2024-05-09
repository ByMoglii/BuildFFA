package de.moglii.buildffa.listener;

import de.moglii.buildffa.main.Main;
import de.moglii.buildffa.methods.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        if (Game.getConfig("joinAndQuitMessages") == 1) {
            event.setQuitMessage(Main.PREFIX + "Der Spieler§e " + player.getName() + "§7 hat§e BuildFFA§c verlassen§7.");
        } else {
            event.setQuitMessage(null);
        }

        if (Main.fight.contains(player)) {
            Main.fight.remove(player);
            Game.addStats(player, "Deaths");
        } else {
            int blocks = 0;
            for (int i = 1; i <= 150; i++) {
                if (player.getLocation().subtract(0, i, 0).getBlock().getType().equals(Material.AIR)) {
                    blocks++;
                } else {
                    break;
                }
            }
            if (blocks == 150) {
                Game.addStats(player, "Deaths");
            }
        }
    }

}
