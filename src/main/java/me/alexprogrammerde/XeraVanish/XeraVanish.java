package me.alexprogrammerde.XeraVanish;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.logging.Logger;

public class XeraVanish extends JavaPlugin {
    private Logger console;
    public final HashMap<Player, GameMode> gamemodelist = new HashMap<>();
    public final HashMap<Player, Integer> taskidlist = new HashMap<>();
    public VanishPlayer vanishPlayer;

    public void onEnable() {
        console = getLogger();
        vanishPlayer = new VanishPlayer(this);

        console.info("Loading config.");
        saveDefaultConfig();

        console.info("Registering listeners.");
        getServer().getPluginManager().registerEvents(new PlayerEvents(this),this);

        console.info("Registering commands");
        PluginCommand vanish = getServer().getPluginCommand("vanish");

        if (vanish != null) {
            vanish.setExecutor(new VanishCommand(this));
            vanish.setTabCompleter(new VanishCommand(this));
        }

        console.info("Checking for a newer version.");
        new UpdateChecker(this, 80763).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                console.info("Your up to date!");
            } else {
                console.info("There is a new update available. Download it at: https://www.spigotmc.org/resources/80763/updates (You may need to remove the old config to get a never one.)");
            }
        });

        console.info("Loading metrics");
        new Metrics(this, 8622);

        console.info("Finished starting");
    }

    public boolean isVanished(Player player) {
        return gamemodelist.containsKey(player) && taskidlist.containsKey(player);
    }
}
