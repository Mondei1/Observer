package de.Mondei1.spigot;

import de.Mondei1.utils.LogEvents;
import de.Mondei1.utils.Network;
import de.Mondei1.utils.backend.BackendManager;
import de.Mondei1.utils.ConfigManager;
import de.Mondei1.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Observer extends JavaPlugin {

    public static Observer instance;
    private BackendManager backendManager;
    private int afkScheduler;
    private HashMap<Player, Integer> afkTimer = new HashMap<>();    // <Player, Seconds where player don't move >   => 600sek (10min) means AFK
    private HashMap<Player, Location> lastLoc = new HashMap<>();    // <Player, Last location>
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
        try {
            instance = this;
            this.cfg = new ConfigManager();

            try {
                cfg.createConfig(); // Create config if not exist.
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("mac", Network.getMacAddress());
                    obj.put("port", Bukkit.getPort());
                } catch (UnknownHostException | SocketException e) {
                    e.printStackTrace();
                }
                this.backendManager = new BackendManager("http://" + cfg.backend().get("host") + ":" + cfg.backend().get("port"), true, obj);    // Setup connection to backend.
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        /*if(!this.backendManager.isConnected()) {
            new LogUtil("Noticed that there is no active connection with the backend, that isn't good. Disable before something nasty happens...").event_error(LogEvents.WEBSOCKET);
        }*/

            new LogUtil("Setting UncaughtExceptionHandler ...").debug();
            // Set exception handler
            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
                new LogUtil("An unhandled error from type \"" + throwable.getCause().toString() + "\" occurred:").error();
                throwable.printStackTrace();
            });

            // AFK scheduler
            new LogUtil("Starting AFK scheduler ...").debug();
            afkScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, (Runnable) () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    int afkTime = 0;
                    try { afkTime = afkTimer.get(p); } catch (NullPointerException e) {  }
                    new LogUtil("Run for " + p.getName() + "\tLoc: " + lastLoc.get(p) + "\tTimer: " + afkTime).debug();
                    if(lastLoc.containsKey(p)) {
                        new LogUtil("Moved: " + (lastLoc.get(p).equals(p.getLocation()))).debug();
                        if(afkTime >= 600) {
                            JSONObject obj = new JSONObject();
                            obj.put("user", p.getUniqueId().toString());
                            this.backendManager.emit("player afk", obj);
                        }
                        // Add one second to timer.
                        if(lastLoc.get(p).equals(p.getLocation())) {
                            afkTimer.put(p, afkTime+1);
                            new LogUtil(p.getName() + " moved not!").debug();
                        }
                        // Remove player from timer because he moved.
                        else {
                            afkTimer.remove(p);
                            lastLoc.put(p, p.getLocation());
                        }
                        new LogUtil("Player " + p.getName() + " is afk since " + afkTime + " sec").debug();
                    } else {
                        new LogUtil("Add player to lastLoc...").debug();;
                        lastLoc.put(p, p.getLocation());
                    }
                }
            }, 0, 20);

            // Register event
            Bukkit.getPluginManager().registerEvents(new de.Mondei1.spigot.Listeners.Player(), this);

            new LogUtil("Wow! Observer is started.").success();
        } catch (Exception e) {}

    }

    @Override
    public void onDisable() {
        try {
            JSONObject logout = new JSONObject();
            logout.put("id", backendManager.getSessionID());
            backendManager.emit("server logout", logout);
            if(backendManager.getSocket() != null) backendManager.getSocket().disconnect();
        } catch (Exception e) {}
        new LogUtil("Goodbye (͡° ͜ʖ ͡°)").success();
    }

    public static Observer getInstance() {
        return instance;
    }

    public BackendManager getBackendManager() {
        return backendManager;
    }
}
