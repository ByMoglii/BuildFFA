package de.moglii.buildffa.listener;

import de.moglii.buildffa.methods.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class ItemDamageListener implements Listener {

    /*
     * Copyright © 2024 Emilio Tropeano
     * All rights reserved
     */

    @EventHandler
    public void handleItemDamage(PlayerItemDamageEvent event) {
        if (Game.getConfig("itemUnbreakable") == 1) {
            event.getItem().setDurability((short) 0);
            event.setCancelled(true);
        }
    }

}
