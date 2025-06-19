package me.raider.blockplacer.command;

import me.raider.blockplacer.placer.Placer;
import me.raider.blockplacer.placer.PlacerManager;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

public class PlacerParameterType implements ParameterType<BukkitCommandActor, Placer> {

    private final PlacerManager manager;

    public PlacerParameterType(final PlacerManager manager) {
        this.manager = manager;
    }

    @Override
    public Placer parse(@NotNull final MutableStringStream input, @NotNull final ExecutionContext<@NotNull BukkitCommandActor> executionContext) {
        Placer placer = manager.getPlacer(input.readString());
        if (placer == null) {
            throw new CommandErrorException("Placer not found");
        }
        return placer;
    }
}
