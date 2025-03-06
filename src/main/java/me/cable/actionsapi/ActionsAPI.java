package me.cable.actionsapi;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ActionsAPI {

    private static final Set<Action> actions = new HashSet<>();

    public static void registerAction(@NotNull Action action) {
        String label = action.label();

        for (Action a : actions) {
            if (a.label().equals(label)) {
                throw new IllegalStateException("Action label already being used: " + label);
            }
        }

        actions.add(action);
        AapiMain.getInstance().getLogger().info("Action \"" + label + "\" has been registered");
    }

    private static @NotNull String formatLabel(@NotNull String string) {
        return '[' + string + ']';
    }

    private static @Nullable Action getAction(@NotNull String formattedLabel) {
        for (Action action : actions) {
            String label = action.label();

            if (formatLabel(label).equals(formattedLabel)) {
                return action;
            }
        }

        return null;
    }

    /**
     * @param string        the action to run
     * @param commandSender the sender executing
     * @return if the action was found or not
     */
    public static boolean run(@NotNull String string, @Nullable CommandSender commandSender) {
        // args

        String[] argsWithLabel = string.split("\\s+"); // split by groups of whitespace
        String label = argsWithLabel[0];

        // action

        Action action = getAction(label);
        if (action == null) return false;

        // run action

        if (commandSender != null) {
            string = string.replace("{sender}", commandSender.getName()); // sender placeholder
        }

        String[] argsWithoutLabel = Arrays.copyOfRange(argsWithLabel, 1, argsWithLabel.length);
        String raw = string.substring(label.length()).stripLeading();

        if (commandSender == null) {
            action.run(argsWithoutLabel, raw);
        } else {
            action.run(commandSender, argsWithLabel, raw);
        }

        return true;
    }
}
