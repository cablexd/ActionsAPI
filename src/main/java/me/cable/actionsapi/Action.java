package me.cable.actionsapi;

import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Action {

    private final @NotNull String label;

    public Action(@NotNull String label) {
        if (!label.matches("^[a-z_]+$")) {
            // check has only lower case letters and underscores
            throw new IllegalArgumentException("Invalid label: " + label);
        }

        this.label = label;
    }

    public final void register() {
        ActionsAPI.registerAction(this);
    }

    public final @NotNull String label() {
        return label;
    }

    public void run(@NotNull CommandSender commandSender, @NotNull String[] args, @NotNull String raw) {
        if (commandSender instanceof ConsoleCommandSender consoleCommandSender) {
            run(consoleCommandSender, args, raw);
        } else if (commandSender instanceof Player player) {
            run(player, args, raw);
        }
    }

    public void run(@NotNull ConsoleCommandSender consoleCommandSender, @NotNull String[] arg, @NotNull String raw) {

    }

    public void run(@NotNull Player player, @NotNull String[] arg, @NotNull String raw) {

    }
}
