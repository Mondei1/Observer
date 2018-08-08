package de.Mondei1.utils.backend;

import de.Mondei1.utils.ConfigManager;
import de.Mondei1.utils.LogEvents;
import de.Mondei1.utils.LogUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.Callable;

public class Post implements Callable<HttpResponse> {

    private HttpClient client = null;
    private ConfigManager cfg;
    private ResponseInterface responseInterface;

    private String url;
    private String path;
    private String body;
    private String token;

    public Post(String url, String path, String body, String token, ResponseInterface responseInterface) {
        this.cfg = new ConfigManager();
        this.responseInterface = responseInterface;
        this.path = path;
        this.url = url;
        this.body = body;
        this.token = token;

        this.client = HttpClientBuilder.create().build();
    }

    @Override
    public HttpResponse call() throws ParseException, IOException {
        try {
            HttpPost req = new HttpPost(this.url + path);
            StringEntity params = new StringEntity(body);
            req.setEntity(params);
            if(token != null) req.addHeader("token", token);
            req.addHeader("content-type", "application/json");
            HttpResponse res =  client.execute(req);
            new LogUtil(this.url + path + "\t\t[" + body.replace("\n", "" ) + "]").event_success(LogEvents.POST);

            responseInterface.onResponse(res, null);
            return res;
        } catch (Exception e) {
            new LogUtil(this.url + path + "\t\t[" + body.replace("\n", "" ) + "]").event_error(LogEvents.POST);
            responseInterface.onResponse(null, e);
            return null;
        }
    }
}
