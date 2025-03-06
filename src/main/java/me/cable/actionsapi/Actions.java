package me.cable.actionsapi;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

public class Actions {

    private final List<String> strings;

    private final Map<String, String> placeholders = new HashMap<>();
    private final Map<String, Function<Player, Object>> playerPlaceholders = new HashMap<>();

    public Actions(@NotNull List<String> strings) {
        this.strings = new ArrayList<>(strings);
    }

    public Actions() {
        strings = new ArrayList<>();
    }

    public void add(@NotNull String string) {
        strings.add(string);
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

    public boolean run(@Nullable CommandSender commandSender) {
        boolean success = true;

        for (String string : strings) {
            for (Entry<String, String> entry : placeholders.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();

                string = string.replace('{' + key + '}', val);
            }

            success &= ActionsAPI.run(string, commandSender);
        }

        return success;
    }

    // runs the actions for each player while only running global actions once
    public void run(@NotNull List<Player> players) {
        List<String> playerActions = new ArrayList<>();
        List<String> globalActions = new ArrayList<>();

        // get global and player actions
        for (String string : strings) {
            if (string.startsWith("g")) {
                globalActions.add(string.substring(1).stripLeading());
            } else {
                playerActions.add(string);
            }
        }

        for (Player player : players) {
            for (String string : playerActions) {
                for (Entry<String, String> entry : placeholders.entrySet()) {
                    string = string.replace('{' + entry.getKey() + '}', entry.getValue());
                }
                for (Entry<String, Function<Player, Object>> entry : playerPlaceholders.entrySet()) {
                    Object value = entry.getValue().apply(player);

                    if (value != null) {
                        string = string.replace('{' + entry.getKey() + '}', value.toString());
                    }
                }

                ActionsAPI.run(string, player);
            }
        }

        for (String string : globalActions) {
            for (Entry<String, String> entry : placeholders.entrySet()) {
                string = string.replace('{' + entry.getKey() + '}', entry.getValue());
            }

            ActionsAPI.run(string, null);
        }
    }
}
