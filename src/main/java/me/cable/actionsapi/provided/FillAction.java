package me.cable.actionsapi.provided;

import me.cable.actionsapi.Action;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class FillAction extends Action {

    // [fill] [world] <x1> <y1> <z1> <x2> <y2> <z2> <material>
    public FillAction() {
        super("fill");
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String[] args, @NotNull String raw) {
        if (args.length < 7) {
            return;
        }

        int startCoordIndex;
        String worldName;
        String materialName;

        if (args.length == 7) {
            startCoordIndex = 0;
            worldName = "world";
            materialName = args[6];
        } else {
            startCoordIndex = 1;
            worldName = args[0];
            materialName = args[7];
        }

        int x1, y1, z1, x2, y2, z2;

        try {
            x1 = Math.min(Integer.parseInt(args[startCoordIndex]), Integer.parseInt(args[startCoordIndex + 3]));
            y1 = Math.min(Integer.parseInt(args[startCoordIndex + 1]), Integer.parseInt(args[startCoordIndex + 4]));
            z1 = Math.min(Integer.parseInt(args[startCoordIndex + 2]), Integer.parseInt(args[startCoordIndex + 5]));
            x2 = Math.max(Integer.parseInt(args[startCoordIndex]), Integer.parseInt(args[startCoordIndex + 3]));
            y2 = Math.max(Integer.parseInt(args[startCoordIndex + 1]), Integer.parseInt(args[startCoordIndex + 4]));
            z2 = Math.max(Integer.parseInt(args[startCoordIndex + 2]), Integer.parseInt(args[startCoordIndex + 5]));
        } catch (NumberFormatException ex) {
            return;
        }

        World world = Bukkit.getWorld(worldName);
        Material material = Material.getMaterial(materialName);

        if (world == null || material == null) {
            return;
        }

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }
}
