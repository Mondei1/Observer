package de.Mondei1.utils.backend;

import de.Mondei1.utils.ConfigManager;
import de.Mondei1.utils.LogUtil;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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

    private String url;

    public Login(String url, ResponseInterface responseInterface, BackendManager backendManager) {
        this.cfg = new ConfigManager();
        this.responseInterface = responseInterface;
        this.backendManager = backendManager;
        this.url = url;

        this.client = HttpClientBuilder.create().build();
    }

    @Override
    public HttpResponse call() throws IOException, ParseException {
        try {
            // First, get token over normal http request.
            JSONObject body = new JSONObject();
            body.put("username", cfg.backend().get("username"));
            body.put("password", cfg.backend().get("password"));
            HttpPost req = new HttpPost(this.url + "/auth");
            StringEntity params = new StringEntity(body.toString());
            req.setEntity(params);
            req.addHeader("content-type", "application/json");
            HttpResponse res =  client.execute(req);
            backendManager.setToken(BackendManager.toJSON(res).get("token").toString());

            if(backendManager.getToken() == null) {
                responseInterface.onResponse(res, null);
                return res;
            }
            responseInterface.onResponse(res, null);
            return res;
        } catch (Exception e) {
            responseInterface.onResponse(null, e);
            return null;
        }
    }
}
