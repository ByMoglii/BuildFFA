package de.moglii.buildffa.listener;

import de.moglii.buildffa.methods.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    @EventHandler
    public void handleFoodLevelChange(FoodLevelChangeEvent event) {
        if (Game.getConfig("foodLevelChange") == 0) {
            event.setCancelled(false);
        }
    }

}
