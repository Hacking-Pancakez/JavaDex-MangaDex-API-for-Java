package dev.kurumidisciples.javadex.api.proxies;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import dev.kurumidisciples.javadex.api.entities.User;

//TODO Implement this class before BETA.2 release
public class UserProxy implements EntityProxy<User> {

    private final UUID id;
    private final Type type = Type.USER;

    public UserProxy(UUID id) {
        this.id = id;
    }

    @Override
    public User retrieve() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public CompletableFuture<User> retrieveAsync() {
        throw new UnsupportedOperationException("Not implemented yet");
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
