package me.cable.actionsapi;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Actions {

    private final List<String> actionStrings;

    private final Map<String, String> placeholders = new HashMap<>();
    private final Map<String, Function<Player, Object>> playerPlaceholders = new HashMap<>();

    public Actions(@NotNull List<String> actionStrings) {
        this.actionStrings = new ArrayList<>(actionStrings);
    }

    public Actions() {
        actionStrings = new ArrayList<>();
    }

    public void add(@NotNull String string) {
        actionStrings.add(string);
    }

    public @NotNull Actions placeholder(@Nullable String key, @Nullable Object val) {
        if (key != null && val != null) {
            placeholders.put(key, val.toString());
        }

        return this;
    }

    public @NotNull Actions placeholder(@Nullable String key, @NotNull Function<Player, Object> function) {
        if (key != null) {
            playerPlaceholders.put(key, function);
        }

        return this;
    }

    public @NotNull Actions placeholders(@NotNull Map<String, String> placeholders) {
        for (Entry<String, String> entry : placeholders.entrySet()) {
            placeholder(entry.getKey(), entry.getValue());
        }

        return this;
    }

    public void run(@Nullable CommandSender commandSender) {
        if (commandSender == null) {
            commandSender = Bukkit.getConsoleSender();
        }

        for (String string : actionStrings) {
            for (Entry<String, String> entry : placeholders.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();

                string = string.replace('{' + key + '}', val);
            }

            AapiMain.getInstance().getActionsAPI().run(string, commandSender);
        }
    }

    public void run() {
        run((CommandSender) null);
    }

    private boolean areConditionsMet(@NotNull String[] optionsParts) {
        if (optionsParts.length <= 1) {
            return true;
        }

        String rawExpression = "";

        // translate e.g. "cpt {countdown} <= 10" to "{countdown}<=10"
        for (int i = 1; i < optionsParts.length; i++) {
            rawExpression += optionsParts[i];
        }

        String[] andSeparated = rawExpression.split("and");

        for (String subExpression : andSeparated) {
            Matcher matcher = Pattern.compile("[<>=]+").matcher(subExpression);
            if (!matcher.find()) return false;

            String operation = matcher.group();
            String[] expressionParts = subExpression.split(operation);
            double a = Double.parseDouble(expressionParts[0]);
            double b = Double.parseDouble(expressionParts[1]);

            boolean v = switch (operation) {
                case "==" -> a == b;
                case ">" -> a > b;
                case ">=" -> a >= b;
                case "<" -> a < b;
                case "<=" -> a <= b;
                default -> false;
            };

            if (!v) {
                return false;
            }
        }

        return true;
    }

    private void runInternal(@Nullable Player target, @NotNull List<Player> players) {
        for (String string : actionStrings) {
            // apply placeholders
            for (Entry<String, String> entry : placeholders.entrySet()) {
                string = string.replace('{' + entry.getKey() + '}', entry.getValue());
            }

            String optionsString = string.substring(0, string.indexOf('[')); // part before action label (e.g. cpt {countdown} <= 10)
            String actionString = string.substring(string.indexOf('[')); // action label and everything after

            String[] optionsParts = optionsString.split(" ");
            String flags = optionsParts[0];

            if (flags.contains("c") && areConditionsMet(optionsParts)) { // console flag
                AapiMain.getInstance().getActionsAPI().run(actionString, Bukkit.getConsoleSender());
            }

            List<Player> targetPlayers = new ArrayList<>();

            if (flags.contains("p")) { // player flag
                targetPlayers.addAll(players);

                if (target != null) {
                    // remove target from target players
                    targetPlayers.removeIf(a -> a.equals(target));
                }
            }
            if (flags.contains("t") && target != null) { // target flag
                targetPlayers.add(target);
            }

            for (Player player : targetPlayers) {
                String playerOptions = optionsString;
                String playerAction = actionString;

                for (Entry<String, Function<Player, Object>> entry : playerPlaceholders.entrySet()) {
                    Object value = entry.getValue().apply(player);

                    if (value != null) {
                        playerAction = playerAction.replace('{' + entry.getKey() + '}', value.toString());
                        playerOptions = playerOptions.replace('{' + entry.getKey() + '}', value.toString());
                    }
                }

                optionsParts = optionsString.split(" ");

                if (areConditionsMet(optionsParts)) {
                    AapiMain.getInstance().getActionsAPI().run(actionString, player);
                }
            }
        }
    }

    /*
        Runs target actions for the target player, player actions for each player except the target player and console actions for the console.
     */
    public void run(@NotNull Player target, @NotNull List<Player> players) {
        runInternal(target, players);
    }

    /*
        Runs player actions for each player and console actions for the console.
     */
    public void run(@NotNull List<Player> players) {
        runInternal(null, players);
    }
}
