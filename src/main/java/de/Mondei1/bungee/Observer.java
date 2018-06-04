package de.Mondei1.bungee;

import de.Mondei1.bungee.commands.Sync;
import de.Mondei1.bungee.listeners.PlayerListener;
import de.Mondei1.utils.ConfigManager;
import de.Mondei1.utils.LogUtil;
import de.Mondei1.utils.backend.BackendManager;
import net.md_5.bungee.api.plugin.Plugin;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
public class Observer extends Plugin {

    public static Observer instance;
    private BackendManager backendManager;
    private String token;

    public static String jarPath;
    public static String dataPath;

    static {
        try {
            jarPath = Observer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replace("\\", "/");
            dataPath = new File(Observer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent().replace("\\", "/") + "/Observer";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private ConfigManager cfg;

    @Override
    public void onEnable() {
        instance = this;
        this.cfg = new ConfigManager();

        try {
            cfg.createConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.backendManager = new BackendManager("http://" + cfg.backend().get("host") + ":" + cfg.backend().get("port"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        new LogUtil("Setting UncaughtExceptionHandler (get's executed when a unexpected error occur) ...").debug();
        // Set exception handler
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            new LogUtil("An unhandled error from type \"" + throwable.getCause().toString() + "\" occurred:").error();
            throwable.printStackTrace();
        });
        if((boolean) cfg.client().get("sync")) backendManager.emit("get config", new JSONObject());

        // Register commands
        getProxy().getPluginManager().registerCommand(this, new Sync());

        // Register commands
        getProxy().getPluginManager().registerListener(this, new PlayerListener());

        new LogUtil("Wow! Observer is started.").success();
    }

    public BackendManager getBackendManager() {
        return backendManager;
    }

    public static Observer getInstance() {
        return instance;
    }
}
