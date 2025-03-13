package me.cable.actionsapi;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

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

    public void run(@NotNull CommandSender commandSender) {
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
        run(Bukkit.getConsoleSender());
    }

    // runs player actions for each player and console actions for the console
    public void run(@NotNull List<Player> players) {
        for (String string : actionStrings) {
            switch (string.charAt(0)) {
                case 'c' -> { // console action
                    for (Entry<String, String> entry : placeholders.entrySet()) {
                        string = string.replace('{' + entry.getKey() + '}', entry.getValue());
                    }

                    AapiMain.getInstance().getActionsAPI().run(string, Bukkit.getConsoleSender());
                }
                case 'p' -> { // player action
                    for (Player player : players) {
                        for (Entry<String, String> entry : placeholders.entrySet()) {
                            string = string.replace('{' + entry.getKey() + '}', entry.getValue());
                        }
                        for (Entry<String, Function<Player, Object>> entry : playerPlaceholders.entrySet()) {
                            Object value = entry.getValue().apply(player);

                            if (value != null) {
                                string = string.replace('{' + entry.getKey() + '}', value.toString());
                            }
                        }

                        AapiMain.getInstance().getActionsAPI().run(string, player);
                    }
                }
            }
        }
    }
}
