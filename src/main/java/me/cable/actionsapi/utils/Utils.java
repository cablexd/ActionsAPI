package me.cable.actionsapi.utils;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class Utils {

    @Contract("!null -> !null")
    public static @Nullable String format(@Nullable String string) {
        return (string == null) ? null : ChatColor.translateAlternateColorCodes('&', string);
    }
}
