package me.cable.actionsapi.provided;

import me.cable.actionsapi.Action;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CloseInventoryAction extends Action {

    public CloseInventoryAction() {
        super("close_inventory");
    }

    @Override
    public void run(@NotNull Player player, @NotNull String[] arg, @NotNull String raw) {
        player.closeInventory();
    }
}
