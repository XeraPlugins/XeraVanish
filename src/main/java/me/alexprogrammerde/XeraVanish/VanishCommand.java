package me.alexprogrammerde.XeraVanish;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor, TabExecutor {
    XeraVanish xeraVanish;

    public VanishCommand(XeraVanish xeraVanish) {
        this.xeraVanish = xeraVanish;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (xeraVanish.isVanished(player)) {
                if (player.hasPermission("xeravanish.vanish")) {
                    xeraVanish.vanishPlayer.unvanishPlayer(player);

                    player.sendMessage("You are now unvanished!");
                } else {
                    sender.sendMessage("You have no permission to do that!");
                }
            } else {
                if (player.hasPermission("xeravanish.vanish")) {
                    xeraVanish.vanishPlayer.vanishPlayer(player);

                    player.sendMessage("You are now vanished!");
                } else {
                    sender.sendMessage("You have no permission to do that!");
                }
            }
        } else {
            sender.sendMessage("You need to be a player to do that!");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
