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

public class XeraVanish extends JavaPlugin {
    FileConfiguration configuration;
    File file;
    HashMap<Player, GameMode> gamemodelist = new HashMap<>();
    JavaPlugin plugin;
    ConsoleCommandSender console;
    HashMap<Player, Integer> taskidlist = new HashMap<>();

    public void onEnable() {
        plugin = this;
        console = getServer().getConsoleSender();
        String prefix = ChatColor.GREEN +  "[XeraVanish] ";
        
        console.sendMessage(prefix + "Loading config.");
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResource("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configuration = YamlConfiguration.loadConfiguration(file);

        console.sendMessage(prefix + "Registering listeners.");
        getServer().getPluginManager().registerEvents(new PlayerEvents(this),this);

        console.sendMessage(prefix + "Registering commands");
        PluginCommand vanish = getServer().getPluginCommand("vanish");

        if (vanish != null) {
            vanish.setExecutor(new VanishCommand(this));
            vanish.setTabCompleter(new VanishCommand(this));
        }

        console.sendMessage(prefix + "Checking for a newer version.");
        new UpdateChecker(this, 80763).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                console.sendMessage(prefix + "Your up to date!");
            } else {
                console.sendMessage(prefix + "There is a new update available. Download it at: https://www.spigotmc.org/resources/80763/updates (You may need to remove the old config to get a never one.)");
            }
        });

        console.sendMessage(prefix + "Loading metrics");
        new Metrics(this, 8622);

        console.sendMessage(prefix + "Finished starting");
    }

    public GameMode getGamemode(Player player) {
        return gamemodelist.get(player);
    }

    public void putGamemode(Player player, GameMode mode) {
        gamemodelist.put(player, mode);
    }

    public void removePlayer(Player player) {
        gamemodelist.remove(player, getGamemode(player));
    }

    public boolean isVanished(Player player) {
        return gamemodelist.containsKey(player);
    }

    public HashMap<Player, GameMode> getGamemodeList() {
        return gamemodelist;
    }

    public void putTaskID(Player player, Integer integer) {
        taskidlist.put(player, integer);
    }

    public Integer getTaskID(Player player) {
        return taskidlist.get(player);
    }

    public void removeScheduledPlayer(Player player) {
        taskidlist.remove(player, getGamemode(player));
    }

    public boolean isScheduled(Player player) {
        return taskidlist.containsKey(player);
    }

    public FileConfiguration getFileConfig() {
        return configuration;
    }
}
