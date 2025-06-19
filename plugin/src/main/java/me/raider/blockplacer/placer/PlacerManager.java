package me.raider.blockplacer.placer;

import me.raider.blockplacer.BlockPlacerPlugin;
import me.raider.blockplacer.file.YmlFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PlacerManager {

    private final Map<Integer, List<ProcessedPlace>> tickBuffer = new ConcurrentHashMap<>();
    private final AtomicInteger asyncTick = new AtomicInteger(0);
    private int syncTick = 0;

    private final Map<String, Placer> placers = new HashMap<>();
    private final Set<PlacerInstance> instances = new HashSet<>();

    private final YmlFile placersFile;
    private final BlockPlacerPlugin plugin;

    private BukkitTask asyncTask;
    private BukkitTask syncTask;

    public PlacerManager(YmlFile placersFile, BlockPlacerPlugin plugin) {
        this.placersFile = placersFile;
        this.plugin = plugin;
    }

    public void loadPlacers() {
        ConfigurationSection section = placersFile.getConfigurationSection("placers");
        if (section == null) {
            plugin.getLogger().warning("No placers section found in " + placersFile.getName() + " please check the config.");
            return;
        }

        for (String key : section.getKeys(false)) {
            placers.put(key, new ConfigurablePlacer(this.placersFile, key));
        }
    }

    public void clearManager() {
        asyncTask.cancel();
        syncTask.cancel();
        asyncTick.set(0);
        tickBuffer.clear();
        placers.clear();
        instances.clear();
        this.syncTick = 0;
    }

    public void reloadPlacers() {
        clearManager();
        placersFile.reload();
        loadPlacers();
        startTasks();
    }

    public void addPlacerInstance(Location location, BlockFace face, String placerId, List<Boolean> airBlocks) {
        if (location == null || placerId == null) {
            plugin.getLogger().warning("Tried to add an invalid place");
            return;
        }
        Placer placer = placers.get(placerId);
        if (placer == null) {
            plugin.getLogger().warning("The placer with name " + placerId + " was not found, please check the config.");
            return;
        }
        this.instances.add(new PlacerInstance(placer, location, face, airBlocks));
    }

    public Set<PlacerInstance> getInstances() {
        return this.instances;
    }

    public AtomicInteger getAsyncTick() {
        return this.asyncTick;
    }

    public int incrementAsyncTick() {
        return asyncTick.getAndIncrement();
    }


    public Map<Integer, List<ProcessedPlace>> getTickBuffer() {
        return this.tickBuffer;
    }

    public int getSyncTick() {
        return this.syncTick;
    }

    public void incrementSyncTick() {
        this.syncTick++;
    }

    public Placer getPlacer(String placerId) {
        return this.placers.get(placerId);
    }

    public void startTasks() {
        this.asyncTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new PlacerLogicAsyncTask(this), 0L, 1L);
        this.syncTask = Bukkit.getScheduler().runTaskTimer(plugin, new ProcessedPlaceSyncTask(this), 0L, 1L);
    }


}
