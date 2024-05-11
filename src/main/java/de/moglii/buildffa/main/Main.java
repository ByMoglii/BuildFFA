package de.moglii.buildffa.main;

import de.moglii.buildffa.commands.BuildFFACommand;
import de.moglii.buildffa.listener.*;
import de.moglii.buildffa.methods.Game;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    public static Main plugin;
    public static final String PREFIX = "§7[§eBuildFFA§7]§l → §7";

    public static List<Player> fight = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;
        Game.getConfig(null);
        loadWorld();
        mapSwitching();
        for (Player all : Bukkit.getOnlinePlayers()) {
            Game.setScoreBoard(all);
        }
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new BlockListener(), this);
        pluginManager.registerEvents(new DropListener(), this);
        pluginManager.registerEvents(new FoodLevelChangeListener(), this);
        pluginManager.registerEvents(new EntityDamageListener(), this);
        pluginManager.registerEvents(new PlayerDeathListener(), this);
        pluginManager.registerEvents(new MoveListener(), this);
        pluginManager.registerEvents(new PlayerRespawnListener(), this);
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new InteractListener(), this);
        pluginManager.registerEvents(new ItemDamageListener(), this);

        getCommand("BuildFFA").setExecutor(new BuildFFACommand());
        getCommand("BuildFFA").setTabCompleter(new BuildFFACommand());

        getLogger().info(Main.PREFIX + "[PluginName] - BuildFFA");
        getLogger().info(Main.PREFIX + "[PluginVersion] - v1.0.0");
        getLogger().info(Main.PREFIX + "[PluginAuthor] - By_Moglii");
        getLogger().info(Main.PREFIX + "[PluginManager] - Plugin wurde erkannt");
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static void loadWorld() {
        if (Game.getConfig("worlds") != 0) {
            for (int i = 1; i <= Game.getConfig("worlds"); i++) {
                WorldCreator cv = new WorldCreator("BuildFFAWorld_MAP_" + i).type(WorldType.FLAT);
                World world = cv.createWorld();
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            }
        }
        Game.activeMap = Bukkit.getWorld("BuildFFAWorld_MAP_1");
    }

    public static void mapSwitching() {
        if (Game.getConfig("worlds") > 1) {
            Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {
                Game.map++;
                if (Bukkit.getWorld("BuildFFAWorld_MAP_" + Game.map) != null) {
                    Game.activeMap = Bukkit.getWorld("BuildFFAWorld_MAP_" + Game.map);
                } else {
                    Game.map = 1;
                    Game.activeMap = Bukkit.getWorld("BuildFFAWorld_MAP_" + Game.map);
                }
                for (Player all : Bukkit.getOnlinePlayers()) {
                    Game.getSpawn(all);
                    Game.playSound(all, 1);
                    all.sendMessage(PREFIX + "Die §eMap§7 wurde zu §e" + Game.getWorldName(all) + "§e gewechselt§7.");
                }

            }, Game.mapSwitchingTime, Game.mapSwitchingTime);
        } else {
            System.out.println(PREFIX + "Map wurde nicht gewechselt, da nicht genügend existieren. (Dies ist kein Fehler)");
        }
    }

}
