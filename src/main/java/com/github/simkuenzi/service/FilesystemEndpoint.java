package com.github.simkuenzi.service;

import java.nio.file.Files;
import java.nio.file.Path;

class FilesystemEndpoint implements Endpoint<RegisteredUrl> {
    private final Path file;

    FilesystemEndpoint(Path file) {
        this.file = file;
    }

    @Override
    public RegisteredUrl url() {
        return (onFound, onNotFound, onFailed) -> {
            try {
                if (Files.exists(file)) {
                    onFound.found(Files.newBufferedReader(file).readLine());
                } else {
                    onNotFound.notFound(file.getFileName().toString());
                }
            } catch (Exception e) {
                onFailed.failed(file.getFileName().toString(), e);
            }
        };
    }
}
