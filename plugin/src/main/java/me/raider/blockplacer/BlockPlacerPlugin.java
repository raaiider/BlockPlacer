package me.raider.blockplacer;

import me.raider.blockplacer.command.PlacerCommand;
import me.raider.blockplacer.command.PlacerParameterType;
import me.raider.blockplacer.file.ConfigFile;
import me.raider.blockplacer.file.YmlFile;
import me.raider.blockplacer.listener.PlacerListener;
import me.raider.blockplacer.placer.Placer;
import me.raider.blockplacer.placer.PlacerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;


public class BlockPlacerPlugin extends JavaPlugin {

    private ConfigFile configFile;
    private PlacerManager placerManager;

    @Override
    public void onEnable() {
        this.configFile = new ConfigFile(this);
        YmlFile placersFile = new YmlFile(this, "placers");
        this.placerManager = new PlacerManager(placersFile, this);

        // command registration
        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this).parameterTypes(builder -> {
            builder.addParameterType(Placer.class, new PlacerParameterType(placerManager));
        }).build();
        lamp.register(new PlacerCommand(this, placerManager));

        placerManager.loadPlacers();
        placerManager.startTasks();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlacerListener(this, placerManager), this);
    }

    @Override
    public void onDisable() {
        placerManager.clearManager();
    }


    public ConfigFile getConfigFile() {
        return configFile;
    }

}
