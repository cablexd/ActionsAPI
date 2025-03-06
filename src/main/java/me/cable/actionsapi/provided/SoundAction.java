package me.cable.actionsapi.provided;

import me.cable.actionsapi.Action;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundAction extends Action {

    public SoundAction() {
        super("sound");
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String[] args, @NotNull String raw) {
        if (args.length == 0) {
            return;
        }

        /* Sound */

        String soundName = args[0];
        Sound sound = null;

        for (Sound a : Sound.values()) {
            if (a.toString().endsWith(soundName)) {
                if (sound != null) return; // ambiguous: don't play sound
                sound = a;
            }
        }

        if (sound == null) {
            return;
        }

        /* Volume */

        float volume = 1;

        if (args.length >= 2) {
            try {
                volume = Float.parseFloat(args[1]);
            } catch (NumberFormatException ex) {
                // ignored
            }
        }

        /* Pitch */

        float pitch = 1;

        if (args.length >= 3) {
            try {
                pitch = Float.parseFloat(args[2]);
            } catch (NumberFormatException ex) {
                // ignored
            }
        }

        /* Play */

        if (args.length >= 4 && args[3].equals("true")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), sound, volume, pitch);
            }
        } else if (commandSender instanceof Player player) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}
