package de.moglii.buildffa.listener;

import de.moglii.buildffa.methods.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (Game.isInAProtectedChunk(player) && player.getLocation().getBlockY() >= Game.getHeight(player, "Max") && (player.getItemInHand().getType().equals(Material.ENDER_PEARL) || player.getItemInHand().getType().equals(Material.FISHING_ROD) || player.getItemInHand().getType().equals(Material.BOW))) {
            event.setCancelled(true);
        }
    }

}
