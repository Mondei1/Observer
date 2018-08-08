package de.Mondei1.utils.backend;

import de.Mondei1.utils.ConfigManager;
import de.Mondei1.utils.LogEvents;
import de.Mondei1.utils.LogUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.Callable;

public class Get implements Callable<HttpResponse> {

    private HttpClient client = null;
    private ConfigManager cfg;
    private ResponseInterface responseInterface;

    private String url;
    private String path;
    private String token;

    public Get(String url, String path, String token, ResponseInterface responseInterface) {
        this.cfg = new ConfigManager();
        this.responseInterface = responseInterface;
        this.path = path;
        this.url = url;
        this.token = token;

        this.client = HttpClientBuilder.create().build();
    }

    @Override
    public HttpResponse call() throws ParseException, IOException {
        HttpResponse res = null;
        try {
            HttpGet req = new HttpGet(this.url + path);
            if(token != null) req.addHeader("token", token);
            req.addHeader("content-type", "application/json");
            res =  client.execute(req);
            new LogUtil(this.url + path).event_success(LogEvents.GET);

            responseInterface.onResponse(res, null);
            return res;
        } catch (Exception e) {
            new LogUtil(this.url + path).event_error(LogEvents.GET);
            responseInterface.onResponse(res, e);
            return null;
        }
    }
}
