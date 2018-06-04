package de.Mondei1.spigot;

import de.Mondei1.utils.backend.BackendManager;
import de.Mondei1.utils.ConfigManager;
import de.Mondei1.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Observer extends JavaPlugin {

    public static Observer instance;
    private BackendManager backendManager;
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
            cfg.createConfig(); // Create config if not exist.
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.backendManager = new BackendManager("http://" + cfg.backend().get("host") + ":" + cfg.backend().get("port"));    // Setup connection to backend.
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        new LogUtil("Setting UncaughtExceptionHandler ...").debug();
        // Set exception handler
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            new LogUtil("An unhandled error from type \"" + throwable.getCause().toString() + "\" occurred:").error();
            throwable.printStackTrace();
        });
        try {
            backendManager.login((res, err) -> {
                if(err != null) {
                    new LogUtil("Something went wrong while login: ").error();
                    err.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("Observer"));
                }
                if(res.getStatusLine().getStatusCode() != 200) {
                    new LogUtil("Can't login! Are your user information right?").error();
                    Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("Observer"));
                }
            }).get(5, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException | ExecutionException e) {
            new LogUtil("Woops... Never happend.").error();
            e.printStackTrace();
        } catch (TimeoutException e) {
            new LogUtil(new ConfigManager().getPrefix() + "Response from server takes too long! Canceling ...").error();
            Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("Observer"));
        }

        new LogUtil("Wow! Observer is started.").success();
    }

    @Override
    public void onDisable() {
        if(backendManager.getSocket() != null) backendManager.getSocket().disconnect();
        new LogUtil("Goodbye!").success();
    }

    public static Observer getInstance() {
        return instance;
    }

    public BackendManager getBackendManager() {
        return backendManager;
    }
}
