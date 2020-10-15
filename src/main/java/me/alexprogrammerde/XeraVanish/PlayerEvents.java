package me.alexprogrammerde.XeraVanish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {
    XeraVanish xeravanish;

    public PlayerEvents(XeraVanish xeravanish) {
        this.xeravanish = xeravanish;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for (Player vanishedPlayer : xeravanish.gamemodelist.keySet()) {
            player.hidePlayer(xeravanish, vanishedPlayer);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (xeravanish.isVanished(player)) {
            event.setQuitMessage(null);

            xeravanish.vanishPlayer.unvanishPlayer(player);
        }
    }
}
