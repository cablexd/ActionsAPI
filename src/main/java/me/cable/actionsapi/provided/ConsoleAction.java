package me.cable.actionsapi.provided;

import me.cable.actionsapi.Action;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

// command run by console
public class ConsoleAction extends Action {

    public ConsoleAction() {
        super("console");
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String[] args, @NotNull String raw) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), raw);
    }
}
