package me.cable.actionsapi;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ActionsAPI {

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

    private @NotNull String formatLabel(@NotNull String string) {
        return '[' + string + ']';
    }

    private @Nullable Action getAction(@NotNull String formattedLabel) {
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
    public boolean run(@NotNull String string, @NotNull CommandSender commandSender) {
        // args

        String[] argsWithLabel = string.split("\\s+"); // split by groups of whitespace
        String label = argsWithLabel[0];

        // action

        Action action = getAction(label);
        if (action == null) return false;

        // run action

        string = string.replace("{sender}", commandSender.getName()); // sender placeholder

        String[] argsWithoutLabel = Arrays.copyOfRange(argsWithLabel, 1, argsWithLabel.length);
        String raw = string.substring(label.length()).stripLeading();

        action.run(commandSender, argsWithoutLabel, raw);
        return true;
    }
}
