package de.Mondei1.utils.backend;

import de.Mondei1.utils.ConfigManager;
import de.Mondei1.utils.LogEvents;
import de.Mondei1.utils.LogUtil;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class Login implements Callable<HttpResponse> {

    private HttpClient client = null;
    private ConfigManager cfg;
    private ResponseInterface responseInterface;
    private BackendManager backendManager;
    private JSONObject serverData;
    private boolean spigot; // If server is Spigot true, else false.

    private String url;

    // For BungeeCord
    public Login(String url, boolean spigot, JSONObject serverData, ResponseInterface responseInterface, BackendManager backendManager) {
        this.cfg = new ConfigManager();
        this.responseInterface = responseInterface;
        this.backendManager = backendManager;
        this.url = url;
        this.serverData = serverData;
        this.spigot = spigot;

        this.client = HttpClientBuilder.create().build();
    }

    @Override
    public HttpResponse call() throws IOException, ParseException {
        try {
            // First, get token over normal http request.
            HttpPost req = new HttpPost(this.url + "/apikey");
            JSONObject obj = new JSONObject();
            obj.put("key", cfg.backend().get("apikey").toString());
            if(!spigot) obj.put("bungeecord", serverData);      // Send bungeecord information only If server is bungeecord.
            else obj.put("spigot", serverData);                 // Send spigot information only If server is spigot.
            StringEntity params = new StringEntity(obj.toString());
            req.setEntity(params);
            req.addHeader("content-type", "application/json");
            HttpResponse res =  client.execute(req);
            new LogUtil(this.url + "/apikey").event_success(LogEvents.POST);

            responseInterface.onResponse(res, null);
            return res;
        } catch (Exception e) {
            new LogUtil(this.url + "/apikey").event_error(LogEvents.POST);
            responseInterface.onResponse(null, e);
            e.printStackTrace();
            return null;
        }
    }
}
