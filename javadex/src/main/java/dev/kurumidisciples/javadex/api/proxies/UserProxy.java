package dev.kurumidisciples.javadex.api.proxies;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.internal.factories.UploaderFactory;

//TODO Implement this class before BETA.2 release
public class UserProxy implements EntityProxy<User> {

    private final UUID id;
    private final Type type = Type.USER;

    public UserProxy(UUID id) {
        this.id = id;
    }

    @Override
    public User retrieve() throws RuntimeException {
        return UploaderFactory.retrieveUser(id.toString());
    }

    @Override
    public CompletableFuture<User> retrieveAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return retrieve();
            } catch (RuntimeException e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public UUID getId() {
        return id;
    }

}
