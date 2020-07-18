package com.github.simkuenzi.service;

import java.io.IOException;
import java.nio.file.Path;

public interface Registry {
    Registry local = new FilesystemRegistry(Path.of(System.getProperty("user.home")).resolve("simkuenzi-endpoint-registry"));

    void register(String name, Endpoint<String> endpoint) throws IOException;
    Endpoint<RegisteredUrl> lookup(String name);
}
