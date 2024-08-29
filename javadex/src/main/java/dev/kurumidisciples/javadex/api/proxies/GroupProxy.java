package dev.kurumidisciples.javadex.api.proxies;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;

//TODO Implement this class before BETA.2 release
public class GroupProxy implements EntityProxy<ScanlationGroup> {

    private final UUID id;
    private final Type type = Type.SCANLATION_GROUP;

    public GroupProxy(UUID id) {
        this.id = id;
    }

    @Override
    public ScanlationGroup retrieve() throws InterruptedException, ExecutionException{
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public CompletableFuture<ScanlationGroup> retrieveAsync() {
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
