package com.github.simkuenzi.service;

public interface RegisteredUrl {
    void tryLookup(Found onFound, NotFound onNotFound, Failed onFailed);

    interface Found {
        void found(String url);
    }

    interface NotFound {
        void notFound(String serviceName);
    }

    interface Failed {
        void failed(String serviceName, Throwable e);
    }
}
