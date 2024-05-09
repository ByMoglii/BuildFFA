package de.moglii.buildffa.listener;

import de.moglii.buildffa.methods.Game;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    @EventHandler
    public void handlePlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            if (player.getLocation().getBlockY() <= Game.getHeight(player, "Min")) {
                player.setHealth(0);
            }
        }
    }

}
