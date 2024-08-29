package dev.kurumidisciples.javadex.api.proxies;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import dev.kurumidisciples.javadex.api.entities.Manga;
import dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction;

public class MangaProxy implements EntityProxy<Manga> {

    private final UUID id;
    private final Type type = Type.MANGA;

    public MangaProxy(UUID id) {
        this.id = id;
    }

    @Override
    public Manga retrieve() throws InterruptedException, ExecutionException{
        return MangaAction.retrieveMangaById(id.toString()).get();
    }

    @Override
    public CompletableFuture<Manga> retrieveAsync() {
        return MangaAction.retrieveMangaById(id.toString());
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
