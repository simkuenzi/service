package com.github.simkuenzi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesystemRegistry implements Registry {
    private final Path directory;

    public FilesystemRegistry(Path directory) {
        this.directory = directory;
    }

    @Override
    public void register(String name, Endpoint<String> endpoint) throws IOException {
        Files.createDirectories(directory);
        Files.writeString(directory.resolve(name), endpoint.url());
    }

    @Override
    public Endpoint<RegisteredUrl> lookup(String name) {
        return new FilesystemEndpoint(directory.resolve(Path.of(name)));
    }
}
