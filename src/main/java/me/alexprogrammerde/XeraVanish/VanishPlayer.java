package me.alexprogrammerde.XeraVanish;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class VanishPlayer {
    XeraVanish xeraVanish;

    public VanishPlayer(XeraVanish xeraVanish) {
        this.xeraVanish = xeraVanish;
    }

    public void vanishPlayer(Player vanishplayer) {
        xeraVanish.gamemodelist.put(vanishplayer, vanishplayer.getGameMode());
        vanishplayer.setGameMode(GameMode.CREATIVE);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', xeraVanish.getConfig().getString("quitmessage").replaceAll("%playername%", vanishplayer.getDisplayName())));

        xeraVanish.taskidlist.put(vanishplayer, Bukkit.getScheduler().scheduleSyncRepeatingTask(xeraVanish, () -> {
            TextComponent actionbar = new TextComponent("You are vanished at the moment!");
            actionbar.setColor(ChatColor.GOLD);

            vanishplayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
        }, 0, 20));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != vanishplayer) {
                player.hidePlayer(xeraVanish, vanishplayer);
            }
        }
    }

    public void unvanishPlayer(Player unvanishplayer) {
        unvanishplayer.setGameMode(xeraVanish.gamemodelist.get(unvanishplayer));
        xeraVanish.gamemodelist.remove(unvanishplayer);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', xeraVanish.getConfig().getString("joinmessage").replaceAll("%playername%", unvanishplayer.getDisplayName())));

        Bukkit.getScheduler().cancelTask(xeraVanish.taskidlist.get(unvanishplayer));
        xeraVanish.taskidlist.remove(unvanishplayer);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != unvanishplayer) {
                player.showPlayer(xeraVanish, unvanishplayer);
            }
        }
    }
}