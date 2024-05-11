package de.moglii.buildffa.commands;

import de.moglii.buildffa.main.Main;
import de.moglii.buildffa.methods.Game;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildFFACommand extends ChunkGenerator implements CommandExecutor, TabCompleter {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("own.buildFFA")) {
            List<String> list = new ArrayList<>();
            if (args.length == 1) {
                list.add("Setup");
                list.add("Player");
                list.add("Help");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("Setup")) {
                    list.add("Spawn");
                    list.add("ChunkProtect");
                    list.add("World");
                    list.add("Border");
                    list.add("Height");
                    list.add("Kit");
                    list.add("Block");
                } else if (args[0].equalsIgnoreCase("Player")) {
                    list.add("Stats");
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("Setup")) {
                    if (args[1].equalsIgnoreCase("Spawn")) {
                        list.add("Set");
                    } else if (args[1].equalsIgnoreCase("World")) {
                        list.add("Create");
                        list.add("Name");
                        list.add("Warp");
                    } else if (args[1].equalsIgnoreCase("Border")) {
                        list.add("Set");
                        list.add("Delete");
                    } else if (args[1].equalsIgnoreCase("ChunkProtect")) {
                        list.add("Create");
                        list.add("Delete");
                    } else if (args[1].equalsIgnoreCase("Height")) {
                        list.add("Max");
                        list.add("Min");
                    } else if (args[1].equalsIgnoreCase("Kit")) {
                        list.add("Create");
                    } else if (args[1].equalsIgnoreCase("Block")) {
                        list.add("List");
                    }
                } else if (args[0].equalsIgnoreCase("Player")) {
                    if (args[1].equalsIgnoreCase("Stats")) {
                        list.add("User");
                    }
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("Setup")) {
                    if (args[1].equalsIgnoreCase("Spawn") && args[2].equalsIgnoreCase("Set")) {
                        list.add("Spawn");
                    } else if (args[1].equalsIgnoreCase("Border") && args[2].equalsIgnoreCase("Set")) {
                        list.add("[WERT]");
                    } else if (args[1].equalsIgnoreCase("Height") && (args[2].equalsIgnoreCase("Max") || args[2].equalsIgnoreCase("Min"))) {
                        list.add("Set");
                    } else if (args[1].equalsIgnoreCase("World") && args[2].equalsIgnoreCase("Name")) {
                        list.add("Set");
                    } else if (args[1].equalsIgnoreCase("World") && args[2].equalsIgnoreCase("Warp")) {
                        list.add("To");
                    } else if (args[1].equalsIgnoreCase("Block") && args[2].equalsIgnoreCase("List")) {
                        list.add("Add");
                        list.add("Remove");
                    }
                } else if (args[0].equalsIgnoreCase("Player")) {
                    if (args[1].equalsIgnoreCase("Stats") && args[2].equalsIgnoreCase("User")) {
                        File file = new File("plugins/BuildFFA/Player/Stats/");
                        for (File folders : file.listFiles()) {
                            String folderName = folders.getName();
                            if (folderName.contains(".txt")) {
                                folderName = folderName.replace(".txt", "");
                            }
                            list.add(folderName);
                        }
                    }
                }
            } else if (args.length == 5) {
                if (args[0].equalsIgnoreCase("Setup")) {
                    if (args[1].equalsIgnoreCase("Height") && (args[2].equalsIgnoreCase("Max") || args[2].equalsIgnoreCase("Min")) && args[3].equalsIgnoreCase("Set")) {
                        list.add("[WERT]");
                        list.add("Position");
                    } else if (args[1].equalsIgnoreCase("World") && args[2].equalsIgnoreCase("Name") && args[3].equalsIgnoreCase("Set")) {
                        list.add("[WERT]");
                    } else if (args[1].equalsIgnoreCase("World") && args[2].equalsIgnoreCase("Warp") && args[3].equalsIgnoreCase("To")) {
                        if (Game.getConfig("worlds") != 0) {
                            for (int i = 1; i <= Game.getConfig("worlds"); i++) {
                                list.add("BuildFFAWorld_MAP_" + i);
                            }
                        }
                    } else if (args[1].equalsIgnoreCase("Block") && args[2].equalsIgnoreCase("List") && (args[3].equalsIgnoreCase("Add") || args[3].equalsIgnoreCase("Remove"))) {
                        list.add("Block");
                    }
                } else if (args[0].equalsIgnoreCase("Player")) {
                    if (args[1].equalsIgnoreCase("Stats") && args[2].equalsIgnoreCase("User") && args[3] != null) {
                        list.add("Delete");
                    }
                }
            }
            return list;
        } else {
            return null;
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("own.buildFFA")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("Help")) {
                        Game.playSound(player, 1);
                        player.sendMessage(Main.PREFIX + "§e§l--------------------------------");
                        player.sendMessage("§a§l/BuildFFA Player§7 ...");
                        player.sendMessage("§eStats User <Spieler> Delete:");
                        player.sendMessage("§7§l→§7   Setze Stats von einem Spieler zurück. (Mapübergreifend)");
                        player.sendMessage("");
                        player.sendMessage("§a§l/BuildFFA Setup§7 ...");
                        player.sendMessage("§eWorld Create:");
                        player.sendMessage("§7§l→§7   Erstelle eine neue Map.");
                        player.sendMessage("§eWorld Name Set <Name>:");
                        player.sendMessage("§7§l→§7   Setze den Mapnamen (Nicht Mapübergreifend).");
                        player.sendMessage("§eWorld Warp <Weltname>:");
                        player.sendMessage("§7§l→§7   Teleportiere dich zu deinen Maps.");
                        player.sendMessage("§eSpawn Set Spawn:");
                        player.sendMessage("§7§l→§7   Setze die Anfangsposition (Nicht Mapübergreifend).");
                        player.sendMessage("§eKit Create:");
                        player.sendMessage("§7§l→§7   Erstelle das Kit (Speichert dein jetziges Inventar | Mapübergreifend).");
                        player.sendMessage("§eHeight <Max, Min> Set <Y-Koordinate>:");
                        player.sendMessage("§7§l→§7   Setze die Maximale Bauhöhe und die Todeshöhe (Nicht Mapübergreifend).");
                        player.sendMessage("§eChunkProtect <Create, Delete>:");
                        player.sendMessage("§7§l→§7   Mach Chunks unzerstörbar (Nicht Mapübergreifend | MaxHeight muss überschritten werden)");
                        player.sendMessage("§eBorder Set <Radius> | Border Delete:");
                        player.sendMessage("§7§l→§7   Setze eine Border um deine Map (Radius beginnt vom Spawn | Nicht Mapübergreifend)");
                        player.sendMessage("§eBlock List <Add, Remove> Block:");
                        player.sendMessage("§7§l→§7   Mach Blöcke unzerstörbar (Nicht Mapübergreifend)");
                        player.sendMessage(Main.PREFIX + "§e§l--------------------------------");
                    } else {
                        Game.playSound(player, 2);
                        player.sendMessage(Main.PREFIX + "Bitte nutze§e /BuildFFA Help§7.");
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("Setup")) {
                        if (args[1].equalsIgnoreCase("World") && args[2].equalsIgnoreCase("Create")) {
                            int value = Game.getConfig("worlds");
                            value++;
                            WorldCreator cv = new WorldCreator("BuildFFAWorld_MAP_" + value).type(WorldType.FLAT);
                            World world = cv.createWorld();
                            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                            if (Bukkit.getWorld("BuildFFAWorld_MAP_" + value) != null) {
                                world.setSpawnLocation((int) world.getSpawnLocation().getX(), 300 + 1, (int) world.getSpawnLocation().getZ());
                                world.getSpawnLocation().subtract(0, 1, 0).getBlock().setType(Material.SANDSTONE);
                                player.teleport(world.getSpawnLocation());
                            }
                            Game.addWorldCount();
                            if (Game.getConfig("worlds") == 1){
                                Main.mapSwitching();
                            }
                            player.setGameMode(GameMode.CREATIVE);
                            Game.setScoreBoard(player);
                            Game.playSound(player, 1);
                            player.sendMessage(Main.PREFIX + "Du hat die Welt§e BuildFFAWorld_MAP_" + value + "§a erstellt§7.");
                        } else if (args[1].equalsIgnoreCase("Border") && args[2].equalsIgnoreCase("Delete")) {
                            World world = player.getWorld();
                            if (world.getWorldBorder().getSize() < 0) {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "§cDiese Welt hat §ckeine§e Begrenzung§c.");
                            } else {
                                world.getWorldBorder().reset();
                                Game.playSound(player, 1);
                                player.sendMessage(Main.PREFIX + "Du hast die§e Begrenzung§c entfernt§7.");
                            }
                        } else if (args[1].equalsIgnoreCase("ChunkProtect") && args[2].equalsIgnoreCase("Create")) {
                            if (!Game.isInAProtectedChunk(player)) {
                                Game.protectChunk(player);
                                Game.playSound(player, 1);
                                player.sendMessage(Main.PREFIX + "Dieser§e Chunk§7 ist nun§a geschützt§7.");
                            } else {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "§cDieser Chunk ist bereits geschützt.");
                            }
                        } else if (args[1].equalsIgnoreCase("ChunkProtect") && args[2].equalsIgnoreCase("Delete")) {
                            if (Game.isInAProtectedChunk(player)) {
                                File file = new File("plugins/BuildFFA/Server/ProtectedChunks" + player.getWorld().getName(), player.getChunk() + ".txt");
                                file.delete();
                                Game.playSound(player, 1);
                                player.sendMessage(Main.PREFIX + "Dieser§e Chunk§7 ist nun §cnicht§7 mehr geschützt.");
                            } else {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "§cDieser Chunk ist nicht geschützt.");
                            }
                        } else if (args[1].equalsIgnoreCase("Kit") && args[2].equalsIgnoreCase("Create")) {
                            Game.createKit(player);
                            Game.playSound(player, 1);
                            player.sendMessage(Main.PREFIX + "Du hast das§e Kit§a gesetzt§7.");
                        } else {
                            Game.playSound(player, 2);
                            player.sendMessage(Main.PREFIX + "Bitte nutze§e /BuildFFA Help§7.");
                        }
                    } else {
                        Game.playSound(player, 2);
                        player.sendMessage(Main.PREFIX + "Bitte nutze§e /BuildFFA Help§7.");
                    }
                } else if (args.length == 4) {
                    if (args[0].equalsIgnoreCase("Setup")) {
                        if (args[1].equalsIgnoreCase("Spawn") && args[2].equalsIgnoreCase("Set") && args[3].equalsIgnoreCase("Spawn")) {
                            player.getWorld().setSpawnLocation(player.getLocation());
                            Game.setSpawn(player);
                            Game.playSound(player, 1);
                            player.sendMessage(Main.PREFIX + "Du hat die§e Position§7 für den§e Spawn§a gesetzt§7.");
                        } else if (args[1].equalsIgnoreCase("Border") && args[2].equalsIgnoreCase("Set") && args[3].matches("\\d+")) {
                            int value;
                            try {
                                value = Integer.parseInt(args[3]);
                            } catch (NumberFormatException event) {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "§cVersuche es, mit einem kleineren Wert.");
                                System.out.println(Main.PREFIX + "Ein Fehler ist aufgetreten. (borderToBig)");
                                return true;
                            }
                            File file = new File("plugins/BuildFFA/Server/Spawns/" + player.getWorld().getName() + "/", "Spawn" + ".txt");
                            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                            double x = cfg.getDouble("Location X");
                            double z = cfg.getDouble("Location Z");
                            World world = player.getWorld();
                            if (!(world.getWorldBorder().getSize() == value)) {
                                world.getWorldBorder().setCenter(x, z);
                                world.getWorldBorder().setSize(value);
                                Game.playSound(player, 1);
                                player.sendMessage(Main.PREFIX + "Du hast die§e Begrenzung§7 auf§e " + value + " Blöcke§a gesetzt§7.");
                            } else {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "§cDu hast die§e Begrenzung§c bereits auf§e " + value + " Blöcke§c gesetzt.");
                            }
                        } else {
                            Game.playSound(player, 2);
                            player.sendMessage(Main.PREFIX + "Bitte nutze§e /BuildFFA Help§7.");
                        }
                    } else {
                        Game.playSound(player, 2);
                        player.sendMessage(Main.PREFIX + "Bitte nutze§e /BuildFFA Help§7.");
                    }
                } else if (args.length == 5) {
                    if (args[0].equalsIgnoreCase("Setup")) {
                        if (args[1].equalsIgnoreCase("World") && args[2].equalsIgnoreCase("Name") && args[3].equalsIgnoreCase("Set")) {
                            if (!Game.getWorldName(player).equals(args[4])) {
                                Game.setWorldName(player, args[4]);
                                Game.setScoreBoard(player);
                                Game.playSound(player, 1);
                                player.sendMessage(Main.PREFIX + "Du hast den §eNamen§7 dieser §eWelt§7 auf§e " + args[4] + "§a gesetzt§7.");
                            } else {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "§cDieser Name wurde für diese Welt bereits gesetzt.");
                            }
                        } else if (args[1].equalsIgnoreCase("World") && args[2].equalsIgnoreCase("Warp") && args[3].equalsIgnoreCase("To")) {
                            if (Bukkit.getWorld(args[4]) != null) {
                                if (!Bukkit.getWorld(args[4]).getName().equals(player.getWorld().getName())) {
                                    player.setGameMode(GameMode.CREATIVE);
                                    Game.getWorld(player, args[4]);
                                    Game.playSound(player, 1);
                                    player.sendMessage(Main.PREFIX + "Du hast dich zur §e" + args[4] + "§7 (§e" + Game.getWorldName(player) + "§7)§e Welt§a teleportiert§7.");
                                } else {
                                    Game.playSound(player, 2);
                                    player.sendMessage(Main.PREFIX + "§cDu befindest dich bereits in dieser Welt.");
                                }
                            } else {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "§cDiese Welt existiert nicht.");
                            }
                        } else if (args[1].equalsIgnoreCase("Height") && args[3].equalsIgnoreCase("Set") && (args[4].matches("\\d+") || args[4].equalsIgnoreCase("Position"))) {
                            int value;
                            try {
                                if (args[4].equalsIgnoreCase("Position")) {
                                    value = player.getLocation().getBlockY();
                                } else {
                                    value = Integer.parseInt(args[4]);
                                }
                            } catch (NumberFormatException event) {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "§cVersuche es, mit einem kleineren Wert.");
                                System.out.println(Main.PREFIX + "Ein Fehler ist aufgetreten. (heightToLarge)");
                                return true;
                            }
                            if (args[2].equalsIgnoreCase("Max")) {
                                if (!(Game.getHeight(player, "Max") == value)) {
                                    Game.setHeight(player, "Max", value);
                                    Game.playSound(player, 1);
                                    player.sendMessage(Main.PREFIX + "Du hast die§e Maximale-Bauhöhe§7 auf§e " + value + " Z§a gesetzt§7.");
                                } else {
                                    Game.playSound(player, 2);
                                    player.sendMessage(Main.PREFIX + "§cDu hast die§e Maximale-Bauhöhe§c §cbereits§c auf§e " + value + " Z§c gesetzt.");
                                }
                            } else if (args[2].equalsIgnoreCase("Min")) {
                                if (!(Game.getHeight(player, "Min") == value)) {
                                    Game.setHeight(player, "Min", value);
                                    Game.playSound(player, 1);
                                    player.sendMessage(Main.PREFIX + "Du hast die§e Todes-Bauhöhe§7 auf§e " + value + " Z§a gesetzt§7.");
                                } else {
                                    Game.playSound(player, 2);
                                    player.sendMessage(Main.PREFIX + "§cDu hast die§e Todes-Bauhöhe§c §cbereits§c auf§e " + value + " Z§c gesetzt.");
                                }
                            } else {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "Bitte nutze§e /BuildFFA Help§7.");
                            }
                        } else if (args[1].equalsIgnoreCase("Block") && args[2].equalsIgnoreCase("List") && (args[3].equalsIgnoreCase("Add") || args[3].equalsIgnoreCase("Remove")) && args[4].equalsIgnoreCase("Block")) {
                            if (!player.getItemInHand().getType().equals(Material.AIR)) {
                                if (args[3].equalsIgnoreCase("Add")) {
                                    if (!Game.getBlock(player, player.getItemInHand().getType())) {
                                        Game.setBlock(player, player.getItemInHand().getType(), true);
                                        Game.playSound(player, 1);
                                        player.sendMessage(Main.PREFIX + "Der Block§e " + player.getItemInHand().getType().name() + "§7 ist nun§a eingetragen§7.");
                                    } else {
                                        Game.playSound(player, 2);
                                        player.sendMessage(Main.PREFIX + "§cDieser Block ist bereits eingetragen.");
                                    }
                                } else {
                                    if (Game.getBlock(player, player.getItemInHand().getType())) {
                                        Game.setBlock(player, player.getItemInHand().getType(), false);
                                        Game.playSound(player, 1);
                                        player.sendMessage(Main.PREFIX + "Der Block§e " + player.getItemInHand().getType().name() + "§7 ist nun§c nicht§7 mehr eingetragen.");
                                    } else {
                                        Game.playSound(player, 2);
                                        player.sendMessage(Main.PREFIX + "§cDieser Block ist nicht eingetragen.");
                                    }
                                }
                            } else {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "§cDu hast keinen Block in der Hand.");
                            }
                        } else {
                            Game.playSound(player, 2);
                            player.sendMessage(Main.PREFIX + "Bitte nutze§e /BuildFFA Help§7.");
                        }
                    } else if (args[0].equalsIgnoreCase("Player")) {
                        if (args[1].equalsIgnoreCase("Stats") && args[2].equalsIgnoreCase("User") && args[3] != null && args[4].equalsIgnoreCase("Delete")) {
                            File file = new File("plugins/BuildFFA/Player/Stats", args[3] + ".txt");
                            if (file.exists()) {
                                FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                                if (cfg.getDouble("K/D") != 0.00 || cfg.getInt("Kills") != 0 || cfg.getInt("Deaths") != 0) {
                                    cfg.set("K/D", 0.00);
                                    cfg.set("Kills", 0);
                                    cfg.set("Deaths", 0);
                                    try {
                                        cfg.save(file);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Player target = Bukkit.getPlayer(args[3]);
                                    if (target != null && target.isOnline()) {
                                        Game.setScoreBoard(target);
                                    }
                                    Game.playSound(player, 1);
                                    player.sendMessage(Main.PREFIX + "Du hast die§e Stats§7 von§e " + args[3] + "§c gelöscht§7.");
                                } else {
                                    Game.playSound(player, 2);
                                    player.sendMessage(Main.PREFIX + "§cDiese Stats wurden bereits gelöscht.");
                                }
                            } else {
                                Game.playSound(player, 2);
                                player.sendMessage(Main.PREFIX + "§cDieser Spieler besitzt keine Stats.");
                            }
                        } else {
                            Game.playSound(player, 2);
                            player.sendMessage(Main.PREFIX + "Bitte nutze§e /BuildFFA Help§7.");
                        }
                    } else {
                        Game.playSound(player, 2);
                        player.sendMessage(Main.PREFIX + "Bitte nutze§e /BuildFFA Help§7.");
                    }
                } else {
                    Game.playSound(player, 2);
                    player.sendMessage(Main.PREFIX + "Bitte nutze§e /BuildFFA Help§7.");
                }
            } else {
                Game.playSound(player, 2);
                player.sendMessage(Main.PREFIX + "Dazu hast du keine Rechte.");
            }
        } else {
            sender.sendMessage(Main.PREFIX + "Ein Fehler ist aufgetreten. (onlyPlayerAllowed)");
        }
        return false;
    }

}

