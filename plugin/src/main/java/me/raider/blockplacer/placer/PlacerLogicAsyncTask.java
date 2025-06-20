package me.raider.blockplacer.placer;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class PlacerLogicAsyncTask implements Runnable {

    private final PlacerManager placerManager;

    public PlacerLogicAsyncTask(PlacerManager placerManager) {
        this.placerManager = placerManager;
    }

    @Override
    public void run() {
        List<ProcessedPlace> placings = new ArrayList<>();
        List<PlacerInstance> toRemove = new ArrayList<>();
        for (PlacerInstance instance : placerManager.getInstances()) {
            Location location = instance.render();
            if (location != null) {
                ProcessedPlace place = new ProcessedPlace(location, instance.getMaterial());
                placings.add(place);
            }
            if (instance.isDone()) {
                toRemove.add(instance);
            }
        }

        for (PlacerInstance instance : toRemove) {
            placerManager.getInstances().remove(instance);
        }

        for (ProcessedPlace place : placings) {
            placerManager.getQueue().add(place);
        }
    }
}
