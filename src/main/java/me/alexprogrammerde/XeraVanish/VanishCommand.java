package me.alexprogrammerde.XeraVanish;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor, TabExecutor {
    FileConfiguration config;
    VanishPlayer vanishplayer;
    XeraVanish xeravanish;

    public VanishCommand(XeraVanish xeravanish) {
        this.xeravanish = xeravanish;
        this.vanishplayer = new VanishPlayer(xeravanish);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        config = xeravanish.getFileConfig();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (XeraVanish.getPlugin(XeraVanish.class).isVanished(player)) {
                if (player.hasPermission("playervanishplus.vanish")) {
                    vanishplayer.unvanishPlayer(player);

                    player.setGameMode(xeravanish.getGamemode(player));
                    xeravanish.removePlayer(player);

                    Bukkit.broadcastMessage(config.getString("joinmessage").replaceAll("%playername%", player.getDisplayName()));

                    Bukkit.getScheduler().cancelTask(xeravanish.getTaskID(player));

                    player.sendMessage("You are now unvanished!");
                } else {
                    sender.sendMessage("You have no permission to do that!");
                }
            } else {
                if (player.hasPermission("playervanishplus.vanish")) {
                    GameMode mode = player.getGameMode();

                    xeravanish.putGamemode(player, mode);

                    vanishplayer.vanishPlayer(player);
                    player.setGameMode(GameMode.CREATIVE);

                    xeravanish.getLogger().info(xeravanish.getGamemodeList().toString());

                    Bukkit.broadcastMessage(config.getString("quitmessage").replaceAll("%playername%", player.getDisplayName()));

                    int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(xeravanish, new Runnable() {
                        @Override
                        public void run() {
                            TextComponent actionbar = new TextComponent("You are vanished at the moment!");
                            actionbar.setColor(ChatColor.GOLD);

                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
                        }
                    }, 20, 20);

                    xeravanish.putTaskID(player, taskID);

                    player.sendMessage("You are now vanished!");
                } else {
                    sender.sendMessage("You have no permission to do that!");
                }
            }
        } else {
            sender.sendMessage("You need to be online to do that!");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
