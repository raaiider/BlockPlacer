package me.raider.blockplacer.placer;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ProcessedPlaceSyncTask implements Runnable {

    private final PlacerManager placerManager;
    private final int maxPerTick = 100;

    public ProcessedPlaceSyncTask(PlacerManager placerManager) {
        this.placerManager = placerManager;
    }

    @Override
    public void run() {
        int placed = 0;
        ConcurrentLinkedQueue<ProcessedPlace> placedQueue = placerManager.getQueue();
        while (!placedQueue.isEmpty() && placed < maxPerTick) {
            ProcessedPlace place = placedQueue.poll(); // gets and removes the head
            if (place != null) {
                place.getLocation().getBlock().setType(place.getMaterial());
                placed++;
            }
        }
    }
}
