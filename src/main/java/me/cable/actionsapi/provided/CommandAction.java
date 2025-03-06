package me.cable.actionsapi.provided;

import me.cable.actionsapi.Action;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

// command run by command sender
public class CommandAction extends Action {

    public CommandAction() {
        super("command");
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String[] args, @NotNull String raw) {
        Bukkit.dispatchCommand(commandSender, raw);
    }
}
