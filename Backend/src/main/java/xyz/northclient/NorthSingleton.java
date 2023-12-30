package xyz.northclient;

import lombok.Getter;

import java.io.IOException;

public enum NorthSingleton implements Runnable {
    INSTANCE;

    @Getter
    private Server server;

    @Override
    public void run() {
        this.server = new Server();
        server.start();
    }
}
