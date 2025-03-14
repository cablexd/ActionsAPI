package me.cable.actionsapi;

import me.cable.actionsapi.provided.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AapiMain extends JavaPlugin {

    public static @NotNull AapiMain getInstance() {
        return JavaPlugin.getPlugin(AapiMain.class);
    }

    private ActionsAPI actionsAPI;

    @Override
    public void onEnable() {
        actionsAPI = new ActionsAPI();
        registerProvidedActions();
    }

    private void registerProvidedActions() {
        new ActionbarAction().register();
        new BroadcastAction().register();
        new CloseInventoryAction().register();
        new CommandAction().register();
        new ConsoleAction().register();
        new ExperienceAction().register();
        new MessageAction().register();
        new SoundAction().register();

        getLogger().info(getName() + " provided actions have been registered");
    }

    public ActionsAPI getActionsAPI() {
        return actionsAPI;
    }
}
