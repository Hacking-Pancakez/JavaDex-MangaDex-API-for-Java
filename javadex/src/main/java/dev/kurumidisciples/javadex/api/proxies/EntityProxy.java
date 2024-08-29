package dev.kurumidisciples.javadex.api.proxies;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface EntityProxy<T> {

    enum Type {
        MANGA, CHAPTER, SCANLATION_GROUP, USER, AUTHOR
    }

    T retrieve() throws Exception;
    CompletableFuture<T> retrieveAsync();
    Type getType();
    UUID getId();
}
