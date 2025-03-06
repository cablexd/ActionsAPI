package me.cable.actionsapi.provided;

import me.cable.actionsapi.Action;
import me.cable.actionsapi.utils.Utils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends Action {

    public MessageAction() {
        super("message");
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String[] args, @NotNull String raw) {
        String message = Utils.format(raw);
        commandSender.sendMessage(message);
    }
}
