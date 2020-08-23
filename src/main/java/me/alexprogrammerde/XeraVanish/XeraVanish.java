package me.alexprogrammerde.XeraVanish;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
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

        String text = configuration.getString("text");

        console.sendMessage(prefix + "Registering listeners.");
        getServer().getPluginManager().registerEvents(new PlayerEvents(this),this);

        console.sendMessage(prefix + "Registering commands");
        XeraVanish.getPlugin(XeraVanish.class).getServer().getPluginCommand("vanish").setExecutor(new VanishCommand(this));

        console.sendMessage(prefix + "Checking for a newer version.");
        new UpdateChecker(this, 80763).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                console.sendMessage(prefix + "Your up to date!");
            } else {
                console.sendMessage(prefix + "There is a new update available. Download it at: https://www.spigotmc.org/resources/playervanishplus.80763/updates (You may need to remove the old config to get a never one.)");
            }
        });

        console.sendMessage(prefix + "Loading metrics");
        int pluginId = 8622;
        Metrics metrics = new Metrics(this, pluginId);

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

    public void reloadConfiguration() {
        configuration = YamlConfiguration.loadConfiguration(file);
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

    public HashMap<Player, Integer> getTaskIDlist() {
        return taskidlist;
    }

    public FileConfiguration getFileConfig() {
        return configuration;
    }

    public ConsoleCommandSender getConsole() {
        return console;
    }
}
