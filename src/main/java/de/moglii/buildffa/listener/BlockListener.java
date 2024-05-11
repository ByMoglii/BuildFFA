package de.moglii.buildffa.listener;

import de.moglii.buildffa.main.Main;
import de.moglii.buildffa.methods.Game;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class BlockListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    private HashMap<Integer, Material> hashMap = new HashMap<>();

    @EventHandler
    public void handlePlayerPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location blockLocation = event.getBlock().getLocation();

        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            if (Game.isInAProtectedChunk(player) && blockLocation.getBlockY() >= Game.getHeight(player, "Max")) {
                event.setCancelled(true);
                Game.playSound(player, 2);
                player.sendMessage(Main.PREFIX + "§cDu darfst am Spawn nicht interagieren.");
            } else if (blockLocation.getBlockY() >= Game.getHeight(player, "Max")) {
                event.setCancelled(true);
                Game.playSound(player, 2);
                player.sendMessage(Main.PREFIX + "§cDu hast die Maximale Bauhöhe erreicht.");
            } else {
                Material blockType = event.getBlock().getType();
                int blockID = blockLocation.getBlock().hashCode();

                hashMap.put(blockID, blockType);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (hashMap.containsKey(blockID) && hashMap.get(blockID) == blockType) {
                            blockLocation.getBlock().setType(Material.AIR);
                            hashMap.remove(blockID);
                        }
                    }
                }.runTaskLater(Main.getPlugin(), Game.removeBlockTime);
            }
        }
    }

    @EventHandler
    public void handlePlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location blockLocation = event.getBlock().getLocation();

        if (Game.getConfig("blockDrops") == 0) {
            event.setDropItems(false);
        }
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            if (Game.isInAProtectedChunk(player) && blockLocation.getBlockY() >= Game.getHeight(player, "Max")) {
                event.setCancelled(true);
                Game.playSound(player, 2);
                player.sendMessage(Main.PREFIX + "§cDu darfst am Spawn nicht interagieren.");
            } else {
                Material blockType = event.getBlock().getType();
                int blockID = blockLocation.getBlock().hashCode();

                if (Game.getBlock(player, blockType)) {
                    event.setCancelled(true);
                    Game.playSound(player, 2);
                    player.sendMessage(Main.PREFIX + "§cDu darfst mit diesem Block nicht interagieren.");
                } else {
                    hashMap.put(blockID, blockType);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (hashMap.containsKey(blockID)) {
                                blockLocation.getBlock().setType(hashMap.get(blockID));
                                hashMap.remove(blockID);
                            }
                        }
                    }.runTaskLater(Main.getPlugin(), Game.replaceBlockTime);
                }
            }
        }
    }

}
