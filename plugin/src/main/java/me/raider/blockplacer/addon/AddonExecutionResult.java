package me.raider.blockplacer.addon;

import java.util.ArrayList;
import java.util.List;

public class AddonExecutionResult {

    private final List<Boolean> booleanResults;
    private final List<Addon<NullExecution>> voidReturn;

    public AddonExecutionResult() {
        this.booleanResults = new ArrayList<>();
        this.voidReturn = new ArrayList<>();
    }

    public List<Boolean> getBooleanResults() {
        return booleanResults;
    }

    public List<Addon<NullExecution>> getVoidReturn() {
        return voidReturn;
    }
}
