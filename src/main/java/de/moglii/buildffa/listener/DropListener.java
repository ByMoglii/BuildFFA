package de.moglii.buildffa.listener;

import de.moglii.buildffa.main.Main;
import de.moglii.buildffa.methods.Game;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    @EventHandler
    public void handlePlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            if (Game.getConfig("playerItemDrops") == 0) {
                if (Game.isInAProtectedChunk(player) && player.getLocation().getBlockY() >= Game.getHeight(player, "Max")) {
                    event.setCancelled(true);
                    Game.playSound(player, 2);
                    player.sendMessage(Main.PREFIX + "§cDu darfst am Spawn nicht interagieren.");
                } else {
                    event.setCancelled(true);
                }
            } else {
                if (Game.isInAProtectedChunk(player) && player.getLocation().getBlockY() >= Game.getHeight(player, "Max")) {
                    event.setCancelled(true);
                    Game.playSound(player, 2);
                    player.sendMessage(Main.PREFIX + "§cDu kannst am Spawn nicht interagieren.");
                }
            }
        }
    }

}
