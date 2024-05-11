package de.moglii.buildffa.methods;

import de.moglii.buildffa.listener.PlayerDeathListener;
import de.moglii.buildffa.main.Main;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Set;

public class Game {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    public static World activeMap;
    public static int map = 1;
    public static long removeBlockTime = getConfig("removeBlockTime");
    public static long replaceBlockTime = getConfig("replaceBlockTime");
    public static long playerRespawnTime = getConfig("playerRespawnTime");
    public static long playerFightTime = getConfig("playerFightTime");
    public static long mapSwitchingTime = getConfig("mapSwitchingTime");
    public static int killStreakDistance = getConfig("killStreakDistance");

    public static void playSound(CommandSender sender, int sound) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sound == 1) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
            } else if (sound == 2) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            } else if (sound == 3) {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f);
            } else {
                System.out.println(Main.PREFIX + "Ein Fehler ist aufgetreten. (playSound)");
            }
        }
    }

    private final ItemStack itemStack;

    public Game(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemStack toItemStack() {
        return itemStack;
    }

    public static void createKit(Player player) {
        File file = new File("plugins/BuildFFA/Server/Kit/", "Kit" + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        ItemStack[] inventoryContents = player.getInventory().getContents();
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        ConfigurationSection kitSection = cfg.createSection("kit");
        for (int i = 0; i < Math.min(inventoryContents.length, 36); i++) {
            if (inventoryContents[i] != null) {
                kitSection.set("item" + i, inventoryContents[i]);
            }
        }
        for (int i = 0; i < Math.min(armorContents.length, 4); i++) {
            if (armorContents[i] != null) {
                kitSection.set("armor" + i, armorContents[i]);
            }
        }
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getKit(Player player) {
        File file = new File("plugins/BuildFFA/Server/Kit/", "Kit.txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection kitSection = cfg.getConfigurationSection("kit");
        if (kitSection == null) {
            return;
        }
        ItemStack[] kitInventory = new ItemStack[36];
        ItemStack[] kitArmor = new ItemStack[4];
        Set<String> itemKeys = kitSection.getKeys(false);
        for (String itemKey : itemKeys) {
            if (itemKey.startsWith("item")) {
                int slot = Integer.parseInt(itemKey.substring(4));
                ItemStack item = kitSection.getItemStack(itemKey);
                kitInventory[slot] = item;
            } else if (itemKey.startsWith("armor")) {
                int slot = Integer.parseInt(itemKey.substring(5));
                ItemStack item = kitSection.getItemStack(itemKey);
                kitArmor[slot] = item;
            }
        }
        player.getInventory().clear();
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setContents(kitInventory);
        playerInventory.setArmorContents(kitArmor);
    }

    public static void setSpawn(Player player) {
        File file = new File("plugins/BuildFFA/Server/Spawns/" + player.getWorld().getName() + "/", "Spawn" + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("Location", player.getWorld().getName());
        cfg.set("Location X", player.getLocation().getX());
        cfg.set("Location Y", player.getLocation().getY());
        cfg.set("Location Z", player.getLocation().getZ());
        cfg.set("Location YAW", player.getLocation().getYaw());
        cfg.set("Location PITCH", player.getLocation().getPitch());
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getSpawn(Player player) {
        File file = new File("plugins/BuildFFA/Server/Spawns/" + player.getWorld().getName() + "/", "Spawn" + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            String worldName = cfg.getString("Location");
            double x = cfg.getDouble("Location X");
            double y = cfg.getDouble("Location Y");
            double z = cfg.getDouble("Location Z");
            float yaw = (float) cfg.getDouble("Location YAW");
            float pitch = (float) cfg.getDouble("Location PITCH");
            Location spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            player.teleport(spawnLocation);
        }
        Game.setScoreBoard(player);
    }

    public static void getWorld(Player player, String worldName) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        World world = worldCreator.createWorld();
        if (world != null) {
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            player.teleport(world.getSpawnLocation());
        }
        setScoreBoard(player);
    }

    public static void addWorldCount() {
        File file = new File("plugins/BuildFFA/Server/Config", "Config.txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        int value = cfg.getInt("worlds");
        value++;
        cfg.set("worlds", value);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setHeight(Player player, String maxOrMin, int value) {
        File file = new File("plugins/BuildFFA/Server/Config/" + player.getWorld().getName() + "/", "Config" + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (maxOrMin.equalsIgnoreCase("Max")) {
            cfg.set("HeightMax", value);
        } else if (maxOrMin.equalsIgnoreCase("Min")) {
            cfg.set("HeightMin", value);
        } else {
            System.out.println(Main.PREFIX + "Ein Fehler ist aufgetreten. (config" + player.getWorld().getName() + "Error)");
        }
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getHeight(Player player, String maxOrMin) {
        File file = new File("plugins/BuildFFA/Server/Config/" + player.getWorld().getName() + "/", "Config" + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            return cfg.getInt("Height" + maxOrMin);
        } else {
            return -999;
        }
    }

    public static void setBlock(Player player, Material material, boolean value) {
        File file = new File("plugins/BuildFFA/Server/Config/" + player.getWorld().getName() + "/", "Config" + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(material.name(), value);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean getBlock(Player player, Material material) {
        File file = new File("plugins/BuildFFA/Server/Config/" + player.getWorld().getName() + "/", "Config" + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            return cfg.getBoolean(material.name());
        } else {
            return false;
        }
    }

    public static void setWorldName(Player player, String name) {
        File file = new File("plugins/BuildFFA/Server/Config/" + player.getWorld().getName() + "/", "Config" + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("worldName", name);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getWorldName(Player player) {
        File file = new File("plugins/BuildFFA/Server/Config/" + player.getWorld().getName() + "/", "Config" + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            if (cfg.getString("worldName") != null) {
                return cfg.getString("worldName");
            } else {
                return "Namenslos";
            }
        } else {
            return player.getWorld().getName();
        }
    }

    public static void protectChunk(Player player) {
        org.bukkit.Chunk chunk = player.getLocation().getChunk();
        File file = new File("plugins/BuildFFA/Server/ProtectedChunks/" + player.getWorld().getName() + "/", chunk + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isInAProtectedChunk(Player player) {
        org.bukkit.Chunk chunk = player.getLocation().getChunk();
        File file = new File("plugins/BuildFFA/Server/ProtectedChunks/" + player.getWorld().getName() + "/", chunk + ".txt");
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static void addStats(Player player, String art) {
        File file = new File("plugins/BuildFFA/Player/Stats", player.getName() + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (art.equals("Kills")) {
            int value = cfg.getInt("Kills");
            value++;
            cfg.set("Kills", value);
        } else if (art.equals("Deaths")) {
            int value = cfg.getInt("Deaths");
            value++;
            cfg.set("Deaths", value);
        } else {
            System.out.println(Main.PREFIX + "Ein Fehler ist aufgetreten. (playerStats)");
        }
        int deaths = cfg.getInt("Deaths");
        if (deaths != 0) {
            int kills = cfg.getInt("Kills");
            double value = (double) kills / (double) deaths;
            cfg.set("K/D", value);
        } else {
            cfg.set("K/D", 0.00);
        }
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setScoreBoard(player);
    }

    public static int getStats(Player player, String art) {
        File file = new File("plugins/BuildFFA/Player/Stats", player.getName() + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            if (art.equals("K/D")) {
                return (int) cfg.getDouble("K/D");
            } else if (art.equals("Kills")) {
                return cfg.getInt("Kills");
            } else if (art.equals("Deaths")) {
                return cfg.getInt("Deaths");
            } else {
                System.out.println(Main.PREFIX + "Ein Fehler ist aufgetreten. (playerStats)");
                return 0;
            }
        } else {
            cfg.set("K/D", 0.00);
            cfg.set("Kills", 0);
            cfg.set("Deaths", 0);
            try {
                cfg.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 0;
        }
    }

    public static double getKD(Player player) {
        File file = new File("plugins/BuildFFA/Player/Stats", player.getName() + ".txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            String kdString = cfg.getString("K/D");
            if (kdString != null && !kdString.isEmpty()) {
                try {
                    double kd = Double.parseDouble(kdString);
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    return Double.parseDouble(decimalFormat.format(kd));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return 0.00;
                }
            } else {
                return 0.00;
            }
        } else {
            cfg.set("K/D", 0.00);
            cfg.set("Kills", 0);
            cfg.set("Deaths", 0);
            try {
                cfg.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 0.00;
        }
    }

    public static void setScoreBoard(Player player) {
        if (getConfig("scoreBoard") == 1) {
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = board.registerNewObjective("abcd", "abcd");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName("§e§lBuildFFA");
            objective.getScore(" ").setScore(14);
            objective.getScore("§fAktuelle Kills:").setScore(13);
            if (PlayerDeathListener.playerKills.get(player.getUniqueId()) != null) {
                objective.getScore("§7§l→§6 " + PlayerDeathListener.playerKills.get(player.getUniqueId())).setScore(12);
            } else {
                objective.getScore("§7§l→§6 0").setScore(12);
            }
            objective.getScore("  ").setScore(11);
            objective.getScore("§fAktuelle Map:").setScore(10);
            objective.getScore("§7§l→§e " + getWorldName(player)).setScore(9);
            objective.getScore("   ").setScore(8);
            objective.getScore("§fK/D:").setScore(7);
            objective.getScore("§7§l→§4 " + getKD(player)).setScore(6);
            objective.getScore("    ").setScore(5);
            objective.getScore("§fGesamte Kills:").setScore(4);
            objective.getScore("§7§l→§a " + getStats(player, "Kills")).setScore(3);
            objective.getScore("     ").setScore(2);
            objective.getScore("§fGesamte Tode:").setScore(1);
            objective.getScore("§7§l→§c " + getStats(player, "Deaths")).setScore(0);
            player.setScoreboard(board);
        }
    }

    public static int getConfig(String value) {
        File file = new File("plugins/BuildFFA/Server/Config", "Config.txt");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            if (value != null) {
                return cfg.getInt(value);
            } else {
                return 0;
            }
        } else {
            System.out.println(Main.PREFIX + "[PluginManager] - Config wurde geladen [0=false|1=true|otherNumbers=value]");
            cfg.set("removeBlockTime", 10 * 20);
            cfg.set("replaceBlockTime", 10 * 20);
            cfg.set("playerRespawnTime", 15L);
            cfg.set("playerFightTime", 20 * 20);
            cfg.set("mapSwitchingTime", 20 * 60 * 10);
            cfg.set("worlds", 0);
            cfg.set("joinAndQuitMessages", 1);
            cfg.set("teamsAllowed", 0);
            cfg.set("playerItemDrops", 0);
            cfg.set("itemUnbreakable", 1);
            cfg.set("blockDrops", 0);
            cfg.set("foodLevelChange", 0);
            cfg.set("scoreBoard", 1);
            cfg.set("playerPrefix", 0);
            cfg.set("killStreakDistance", 5);
            cfg.set("givePlayerItemAfterKill", 1);
            try {
                cfg.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 0;
        }
    }

}
