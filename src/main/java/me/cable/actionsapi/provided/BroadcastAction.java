package me.cable.actionsapi.provided;

import me.cable.actionsapi.Action;
import me.cable.actionsapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastAction extends Action {

    public BroadcastAction() {
        super("broadcast");
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String[] args, @NotNull String raw) {
        String message = Utils.format(raw);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }
}
