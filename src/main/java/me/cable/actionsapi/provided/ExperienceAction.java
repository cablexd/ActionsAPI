package me.cable.actionsapi.provided;

import me.cable.actionsapi.Action;
import me.cable.actionsapi.utils.ExpUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ExperienceAction extends Action {

    // [experience] <add|remove|set> <amount with or without l for levels>

    public ExperienceAction() {
        super("experience");
    }

    @Override
    public void run(@NotNull Player player, @NotNull String[] args, @NotNull String raw) {
        if (args.length < 2) return;

        String amountArg = args[1];
        boolean levels = false;

        if (amountArg.toLowerCase(Locale.ROOT).endsWith("l")) {
            levels = true;
            amountArg = amountArg.substring(0, amountArg.length() - 1);
        }

        int amount;

        try {
            amount = Integer.parseInt(amountArg);
        } catch (NumberFormatException ex) {
            return;
        }

        switch (args[0]) {
            case "add": {
                if (levels) {
                    player.setLevel(player.getLevel() + amount);
                } else {
                    ExpUtils.addPoints(player, amount);
                }

                break;
            }
            case "remove": {
                if (levels) {
                    player.setLevel(player.getLevel() - amount);
                } else {
                    ExpUtils.removePoints(player, amount);
                }

                break;
            }
            case "set": {
                if (levels) {
                    player.setLevel(amount);
                } else {
                    ExpUtils.setPoints(player, amount);
                }

                break;
            }
        }
    }
}
