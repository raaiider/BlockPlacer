package me.raider.blockplacer.placer;

import me.raider.blockplacer.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.List;
import java.util.UUID;

public class PlacerInstance {

    private final String id;
    private final Placer attachedPlacer;
    private final Material material;
    private final BlockFace face;
    private final List<Boolean> airBlocks;

    private int firstNonAir;
    private Location actualLoc;
    private boolean done;
    private int actualTick;
    private int blocksPlaced;

    public PlacerInstance(Placer attachedPlacer, Location initialLoc, BlockFace face, List<Boolean> airBlocks) {
        this.id = UUID.randomUUID().toString();
        this.attachedPlacer = attachedPlacer;
        this.material = attachedPlacer.getBlockToPlace();
        // location of the block clicked
        this.airBlocks = airBlocks;
        this.actualLoc = initialLoc;
        this.face = face;
        this.actualTick = 0;
        this.blocksPlaced = 0;
        this.firstNonAir = -1;
        this.done = false;
    }

    public Location render() {
        if (this.blocksPlaced >= this.attachedPlacer.getMaxPlaced()) {
            this.done = true;
            return null;
        }
        if (attachedPlacer.isBeforeBlocks() && this.firstNonAir != -1) {
            this.done = true;
            return null;
        }
        this.tick();
        if (this.shouldRender()) {
            Location result;
            if (this.attachedPlacer.getForcedFace() != BlockFace.SELF) {
                if (this.blocksPlaced == 0) {
                    result = Utils.processNextLocationForcedAsync(this.actualLoc, this.face);
                } else {
                    result = Utils.processNextLocationForcedAsync(attachedPlacer, this.actualLoc);
                }
            } else {
                result = Utils.processNextLocationAsync(attachedPlacer, this.actualLoc, face);
            }
            Boolean air = airBlocks.get(this.blocksPlaced);
            // System.out.println("to be placed " + this.blocksPlaced);
            if (air != null) {
                if (air) {
                    // System.out.println("placed in air");
                    this.blocksPlaced++;
                    this.actualTick = 0;
                    this.actualLoc = result;
                    return result;
                } else {
                    this.actualTick = 0;
                    if (firstNonAir == -1) {
                       /*  System.out.println("found first non-air");
                        for (Boolean b : airBlocks) {
                            System.out.println("check state: " + b.toString());
                        }*/
                        firstNonAir = this.blocksPlaced;
                    }

                    if (!attachedPlacer.isOnlyInAir()) {
                        //System.out.println("placed and replaced not air");
                        this.blocksPlaced++;
                        this.actualLoc = result;
                        return result;
                    }
                }
            }
            //System.out.println("not placed");
            this.blocksPlaced++;
            this.actualTick = 0;
            this.actualLoc = result;
            return null;
        }
        return null;
    }

    public Material getMaterial() {
        return this.material;
    }

    public boolean isDone() {
        return this.done;
    }

    private void tick() {
        this.actualTick++;
    }

    private boolean shouldRender() {
        return actualTick >= this.attachedPlacer.getWaitTicks();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PlacerInstance other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}
