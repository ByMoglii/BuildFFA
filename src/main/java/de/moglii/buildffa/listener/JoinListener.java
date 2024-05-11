package de.moglii.buildffa.listener;

import de.moglii.buildffa.main.Main;
import de.moglii.buildffa.methods.Game;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Game.getConfig("playerPrefix") == 1) {
            player.setPlayerListName("§eBuildFFA§7§l →§f " + player.getName());
        }
        if (Game.activeMap != null) {
            Game.playSound(player, 1);
            sendActionBar(player);
            if (Game.getConfig("joinAndQuitMessages") == 1) {
                event.setJoinMessage(Main.PREFIX + "Der Spieler§e " + player.getName() + "§7 hat§e BuildFFA§a betreten§7.");
            } else {
                event.setJoinMessage(null);
            }
            Game.getWorld(player, Game.activeMap.getName());
            Game.getSpawn(player);
            Game.getKit(player);
            Game.setScoreBoard(player);
            player.setHealth(player.getMaxHealth());
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            System.out.println(Main.PREFIX + "Ein Fehler ist aufgetreten. (worldTeleport)");
        }
    }

    private void sendActionBar(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    if (Game.getConfig("teamsAllowed") == 0) {
                        player.sendActionBar("§cTeams verboten");
                    } else {
                        player.sendActionBar("§aTeams erlaubt");
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 20L);
    }

}
