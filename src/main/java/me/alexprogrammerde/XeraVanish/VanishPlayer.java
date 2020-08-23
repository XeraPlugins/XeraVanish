package me.alexprogrammerde.XeraVanish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanishPlayer {
    XeraVanish xeraVanish;

    public VanishPlayer(XeraVanish xeraVanish) {
        this.xeraVanish = xeraVanish;
    }

    public void vanishPlayer(Player vanishplayer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != vanishplayer) {
                player.hidePlayer(xeraVanish, vanishplayer);
            }
        }
    }

    public void unvanishPlayer(Player unvanishplayer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != unvanishplayer) {
                player.showPlayer(xeraVanish, unvanishplayer);
            }
        }
    }
}