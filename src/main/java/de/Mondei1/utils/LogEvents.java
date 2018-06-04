package de.Mondei1.utils;

public enum LogEvents {
    BACKEND,    // Get used when something happen with the backend.
    CONNECTION, // Get used when something happen with the connection between client and the WebSocket.
    ORDER,      // Get used when the plugin get a command from the backend do to something.
    SYNC        // Get used when something happen while sync config.

}
