package me.raider.blockplacer.addon;

public abstract class AbstractAddon<T> implements Addon<T> {

    private final String uniqueName;

    public AbstractAddon(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AbstractAddon<?> other)) return false;
        return uniqueName.equals(other.uniqueName);
    }

    @Override
    public int hashCode() {
        return uniqueName.hashCode();
    }
}
