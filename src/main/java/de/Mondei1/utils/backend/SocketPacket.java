package de.Mondei1.utils.backend;

import de.Mondei1.utils.ConfigManager;
import io.socket.emitter.Emitter;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

import java.util.concurrent.Callable;

public class SocketPacket implements Callable<Emitter> {

    private HttpClient client = null;
    private ConfigManager cfg;
    private ResponseInterface responseInterface;
    private BackendManager backendManager;

    private String emit;
    private JSONObject data;

    public SocketPacket(String emit, JSONObject data, ResponseInterface responseInterface, BackendManager backendManager) {
        this.cfg = new ConfigManager();
        this.responseInterface = responseInterface;
        this.backendManager = backendManager;
        this.emit = emit;

        this.client = HttpClientBuilder.create().build();
        this.data = data;
    }

    @Override
    public Emitter call() throws Exception {
        backendManager.getSocket().emit(emit, data);
        return null;
    }
}
