package de.Mondei1.utils.backend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.Mondei1.bungee.Observer;
import de.Mondei1.utils.ConfigManager;
import de.Mondei1.utils.LogEvents;
import de.Mondei1.utils.LogUtil;
import de.Mondei1.utils.backend.events.broadcast;
import de.Mondei1.utils.backend.events.get_config;
import de.Mondei1.utils.backend.events.shutdown;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.md_5.bungee.api.ProxyServer;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URISyntaxException;
import java.util.concurrent.*;

public class BackendManager {

    private HttpClient client = null;

    private String url;
    private String path;
    private String token;
    private Socket socket;
    private boolean isConnected;
    private ConfigManager cfg;

    private int retrys;

    public BackendManager(String url) throws URISyntaxException {
        this.token = token;
        this.url = url;
        this.cfg = new ConfigManager();
        this.retrys = 0;
        this.isConnected = false;

        // Get token
        try {
            new LogUtil("Request authentication token ...").debug();
            this.login((res, err) -> {
                if(err != null) {
                    new LogUtil("Something went wrong while login: ").event_error(LogEvents.CONNECTION);
                    err.printStackTrace();
                }
                if(res.getStatusLine().getStatusCode() != 200) {
                    new LogUtil("Can't login! Are your user information right?").event_error(LogEvents.CONNECTION);
                    return;
                } else {
                    new LogUtil("Got token!").event_success(LogEvents.BACKEND);
                }
            }).get(5, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException | ExecutionException e) {
            new LogUtil("Woops... Never happend.").error();
            e.printStackTrace();
        } catch (TimeoutException e) {
            new LogUtil(new ConfigManager().getPrefix() + "Response from server takes too long!").event_error(LogEvents.CONNECTION);
        }

        // Setup socket after getting token.
        new LogUtil("Initialize WebSocket connection ...").debug();
        IO.Options options = new IO.Options();
        options.timeout=5000;
        options.reconnectionDelay=5000;
        options.query = "token=" + this.token;
        this.socket = IO.socket(url, options);

        socket.on(Socket.EVENT_CONNECT, objects -> {
            new LogUtil( "Connection to backend established!").event_success(LogEvents.CONNECTION);
            this.isConnected = true;

        }).on(Socket.EVENT_DISCONNECT, objects -> {
            new LogUtil("Connection to backend lost! The reason can be: Wrong token or your server is shut downed.").event_error(LogEvents.CONNECTION);
            this.isConnected = false;

        }).on(Socket.EVENT_RECONNECT_ATTEMPT, objects -> {
            new LogUtil("Retry to connect ...").event_error(LogEvents.CONNECTION);
            this.isConnected = false;

        }).on(Socket.EVENT_RECONNECT_FAILED, objects -> {
            retrys++;
            if (retrys > 4) {
                new LogUtil(retrys + ". retry failed! Giving up, disable ...").event_error(LogEvents.CONNECTION);
                ProxyServer.getInstance().getPluginManager().getPlugin("Observer").onDisable();
            }
            new LogUtil(retrys + ". retry failed!").event_error(LogEvents.CONNECTION);
        }).on(Socket.EVENT_RECONNECT, objects -> {
            new LogUtil("Connection established again!").event_success(LogEvents.CONNECTION);
            retrys = 0;
        }).on(Socket.EVENT_CONNECT_TIMEOUT, objects -> {
            new LogUtil("Timeout. That's not good.").event_error(LogEvents.CONNECTION);
        }).on(Socket.EVENT_CONNECT_ERROR, objects -> {
            new LogUtil("Can't connect! Maybe, your token is wrong? But this is impassable.").event_error(LogEvents.CONNECTION);
        // Events
        }).on("get config", get_config::new)
        .on("shutdown", shutdown::new)
        .on("broadcast", broadcast::new);
        new LogUtil("Try to get a connection ...").debug();
        socket.connect();   // Try to get a connection.

        this.client = HttpClientBuilder.create().build();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public static JSONObject toJSON(HttpResponse response) throws org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        String res = "";
        // Decode string to json.
        try {
            res = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // If response is an array, cast it, get element 0 and convert it into a JSONObject and return.
        try {
            try {
                if (parser.parse(res) instanceof JSONArray) return (JSONObject) ((JSONArray) parser.parse(res)).get(0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IndexOutOfBoundsException ex) {
            try {
                return (JSONObject) parser.parse(res);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // If everything is fine, convert it and return.
        try {
            return (JSONObject) parser.parse(res);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public boolean isConnected() {
        return isConnected;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Future<HttpResponse> login(ResponseInterface ri) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        return executor.submit(new Login(url, ri, this));
    }

    public Future<HttpResponse> post(String endpoint, String body, ResponseInterface ri) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        return executor.submit(new Post(this.url, endpoint, body, this.token, ri));
    }

    public Future<HttpResponse> get(String endpoint, ResponseInterface ri) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        return executor.submit(new Get(this.url, endpoint, this.token, ri));
    }

    public void emit(String event, JSONObject data) {
        data.put("token", this.token);
        this.socket.emit(event, data);
    }
}
