package com.github.simkuenzi.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Http {
    private static final Logger log = Logger.getLogger(Http.class.getName());

    private final Endpoint<RegisteredUrl> endpoint;
    private final String remoteUser;

    public Http(Endpoint<RegisteredUrl> endpoint, String remoteUser) {
        this.endpoint = endpoint;
        this.remoteUser = remoteUser;
    }

    public <T> void send(String path, Object request, Class<T> responseType, Consumer<T> onResult, Runnable onError) {
        endpoint.url().tryLookup(
                (url) -> {
                    String serviceUrl = url + path;
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        StringWriter jsonWriter = new StringWriter();
                        objectMapper.writeValue(jsonWriter, request);

                        HttpClient httpClient = HttpClient.newHttpClient();
                        HttpRequest httpRequest = HttpRequest.newBuilder()
                                .POST(HttpRequest.BodyPublishers.ofString(jsonWriter.toString()))
                                .uri(new URI(serviceUrl))
                                .header("X-SK-Auth", remoteUser)
                                .build();

                        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                        if (httpResponse.statusCode() / 100 == 2) { // 2xx Success
                            if (httpResponse.statusCode() != 204 ) { // 204 No Content
                                onResult.accept(objectMapper.readValue(httpResponse.body(), responseType));
                            }
                        } else {
                            log.log(Level.SEVERE, String.format("Service call on %s ended with HTTP status %d", serviceUrl, httpResponse.statusCode()));
                            onError.run();
                        }
                    } catch (Exception e) {
                        log.log(Level.SEVERE, String.format("Unable to call service on %s", serviceUrl), e);
                        onError.run();
                    }
                },
                (name) -> {
                    log.log(Level.SEVERE, String.format("Service %s unavailable.", name));
                    onError.run();
                },
                (name, e) -> {
                    log.log(Level.SEVERE, String.format("Lookup of Service %s failed.", name), e);
                    onError.run();
                }
        );
    }
}
