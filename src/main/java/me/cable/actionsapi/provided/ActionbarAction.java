package me.cable.actionsapi.provided;

import me.cable.actionsapi.Action;
import me.cable.actionsapi.utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionbarAction extends Action {

    // [actionbar] text
    public ActionbarAction() {
        super("actionbar");
    }

    @Override
    public void run(@NotNull Player player, @NotNull String[] args, @NotNull String raw) {
        String message = Utils.format(raw);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(message));
    }
}
