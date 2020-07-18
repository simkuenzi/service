package com.github.simkuenzi.service;

public class LocalEndpoint implements Endpoint<String> {
    private final int port;
    private final String context;

    public LocalEndpoint(int port, String context) {
        this.port = port;
        this.context = context;
    }

    @Override
    public String url() {
        return String.format("http://localhost:%d%s", port, context);
    }
}
