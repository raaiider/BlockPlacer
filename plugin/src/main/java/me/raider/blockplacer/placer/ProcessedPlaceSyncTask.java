package me.raider.blockplacer.placer;

import java.util.List;

public class ProcessedPlaceSyncTask implements Runnable {

    private final PlacerManager placerManager;

    public ProcessedPlaceSyncTask(PlacerManager placerManager) {
        this.placerManager = placerManager;
    }

    @Override
    public void run() {
        int syncTick = placerManager.getSyncTick();
        int asyncTick = placerManager.getAsyncTick().get();
        if (syncTick <= asyncTick) {
            List<ProcessedPlace> placings = placerManager.getTickBuffer().remove(syncTick);
            if (placings != null) {
                for (ProcessedPlace placing : placings) {
                    placing.getLocation().getBlock().setType(placing.getMaterial());
                }
            }
            placerManager.incrementSyncTick();
        }
    }
}
