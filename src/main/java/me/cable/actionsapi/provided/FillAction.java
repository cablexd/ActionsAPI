package me.cable.actionsapi.provided;

import me.cable.actionsapi.Action;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class FillAction extends Action {

    // [fill] [world] <x1> <y1> <z1> <x2> <y2> <z2> <material> [replace] [material]
    public FillAction() {
        super("fill");
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String[] args, @NotNull String raw) {
        if (args.length < 7) {
            return;
        }

        int worldExtraIndex;
        String worldName;

        try {
            Integer.parseInt(args[0]);

            // is integer, therefore no world
            worldExtraIndex = 0;
            worldName = "world";
        } catch (NumberFormatException e) {
            // not integer, therefore is world
            worldExtraIndex = 1;
            worldName = args[0];
        }

        String materialName = args[6 + worldExtraIndex];
        int x1, y1, z1, x2, y2, z2;

        try {
            x1 = Math.min(Integer.parseInt(args[worldExtraIndex]), Integer.parseInt(args[worldExtraIndex + 3]));
            y1 = Math.min(Integer.parseInt(args[worldExtraIndex + 1]), Integer.parseInt(args[worldExtraIndex + 4]));
            z1 = Math.min(Integer.parseInt(args[worldExtraIndex + 2]), Integer.parseInt(args[worldExtraIndex + 5]));
            x2 = Math.max(Integer.parseInt(args[worldExtraIndex]), Integer.parseInt(args[worldExtraIndex + 3]));
            y2 = Math.max(Integer.parseInt(args[worldExtraIndex + 1]), Integer.parseInt(args[worldExtraIndex + 4]));
            z2 = Math.max(Integer.parseInt(args[worldExtraIndex + 2]), Integer.parseInt(args[worldExtraIndex + 5]));
        } catch (NumberFormatException ex) {
            return;
        }

        World world = Bukkit.getWorld(worldName);
        Material material = Material.getMaterial(materialName);

        if (world == null || material == null) {
            return;
        }

        Material replaceMaterial = null;

        if (args.length >= 9 + worldExtraIndex && args[7 + worldExtraIndex].equals("replace")) {
            replaceMaterial = Material.getMaterial(args[8 + worldExtraIndex]);
            if (replaceMaterial == null) replaceMaterial = Material.AIR;
        }

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    Block block = world.getBlockAt(x, y, z);

                    if (replaceMaterial == null || block.getType() == replaceMaterial) {
                        block.setType(material);
                    }
                }
            }
        }
    }
}
