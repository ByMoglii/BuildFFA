package de.moglii.buildffa.listener;

import de.moglii.buildffa.main.Main;
import de.moglii.buildffa.methods.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDeathListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    public static Map<UUID, Integer> playerKills = new HashMap<>();

    @EventHandler
    public void handlePlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.getDrops().clear();
        event.setDroppedExp(0);
        Player player = event.getPlayer();
        Player killer = event.getEntity().getKiller();
        Game.addStats(player, "Deaths");

        if (event.getEntity().getKiller() != null) {
            assert killer != null;
            UUID killerId = killer.getUniqueId();
            int kills = playerKills.getOrDefault(killerId, 0);
            kills++;
            playerKills.put(killerId, kills);
            if (kills % Game.killStreakDistance == 0) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.playSound(all.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                    all.sendMessage(Main.PREFIX + "§e" + killer.getName() + "§7 hat eine §e" + kills + "er Killstreak§7!");
                }
            }
            if (playerKills.get(player.getUniqueId()) != null && playerKills.get(player.getUniqueId()) % Game.killStreakDistance == 0) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1f, 1f);
                    all.sendMessage(Main.PREFIX + "§e" + killer.getName() + "§7 hat die§e Killstreak§7 von§e " + player.getName() + "§c beendet§7.");
                }
            }
            if (Game.getConfig("givePlayerItemAfterKill") == 1) {
                ItemStack item = new Game(Material.ENDER_PEARL).toItemStack();
                killer.getInventory().addItem(item);
            }
            Game.setScoreBoard(player);
            killer.setHealth(20);
            Game.addStats(killer, "Kills");
            Game.playSound(killer, 3);
            killer.sendMessage(Main.PREFIX + "Du hast§e " + player.getName() + "§c getötet§7.");
            player.sendMessage(Main.PREFIX + "Du wurdest von§e " + killer.getName() + "§c getötet§7.");
        } else {
            player.sendMessage(Main.PREFIX + "Du bist §cgestorben§7.");
        }

        if (playerKills.containsKey(player.getUniqueId())) {
            int kills = playerKills.get(player.getUniqueId());
            if (kills > 1) {
                player.sendMessage(Main.PREFIX + "Du hattest insgesamt§e " + kills + " Kills§7.");
            }
            playerKills.remove(player.getUniqueId());
        }

        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            player.spigot().respawn();
        }, Game.playerRespawnTime);
        player.playSound(player.getLocation(), Sound.ENTITY_BAT_DEATH, 1f, 2f);
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            Game.getKit(player);
            Game.getSpawn(player);
        }, 15L);
    }

}
