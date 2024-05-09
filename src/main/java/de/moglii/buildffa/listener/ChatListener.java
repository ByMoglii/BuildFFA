package de.moglii.buildffa.listener;

import de.moglii.buildffa.methods.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    @EventHandler
    public void handlePlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (Game.getConfig("playerPrefix") == 1) {
            event.setCancelled(true);
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage("§eBuildFFA§7§l →§f " + player.getName() + "§7: " + event.getMessage());
            }
        }
    }

}
