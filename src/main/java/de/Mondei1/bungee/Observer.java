package de.Mondei1.bungee;

import de.Mondei1.bungee.commands.Sync;
import de.Mondei1.bungee.listeners.PlayerListener;
import de.Mondei1.utils.ConfigManager;
import de.Mondei1.utils.LogUtil;
import de.Mondei1.utils.Network;
import de.Mondei1.utils.backend.BackendManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            JSONObject obj = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            // Convert servers map to array.
            for(String serverName : ProxyServer.getInstance().getServers().keySet()) {
                JSONObject serverJSON = new JSONObject();
                ServerInfo server = ProxyServer.getInstance().getServerInfo(serverName);
                serverJSON.put("name", server.getName());
                serverJSON.put("port", server.getAddress().getPort());
                jsonArray.add(serverJSON);
            }
            try {
                obj.put("mac", Network.getMacAddress());
                obj.put("servers", jsonArray);
            } catch (UnknownHostException | SocketException e) {
                e.printStackTrace();
            }
            System.out.println("Set obj: " + obj.toString());
            this.backendManager = new BackendManager("http://" + cfg.backend().get("host") + ":" + cfg.backend().get("port"), false, obj);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
        for(String server : servers.keySet()) {
            System.out.println("Port of server " + server + " is " + servers.get(server).getAddress().getPort());
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
