package de.moglii.buildffa.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    @EventHandler
    public void handlePlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        event.setRespawnLocation(player.getWorld().getSpawnLocation());
    }

}