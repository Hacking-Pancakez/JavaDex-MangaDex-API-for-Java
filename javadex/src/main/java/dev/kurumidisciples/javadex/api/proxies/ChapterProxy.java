package dev.kurumidisciples.javadex.api.proxies;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.internal.actions.retrieve.ChapterAction;

public class ChapterProxy implements EntityProxy<Chapter> {
    
    private final UUID id;
    private final Type type = Type.CHAPTER;

    public ChapterProxy(UUID id) {
        this.id = id;
    }

    @Override
    public Chapter retrieve() throws InterruptedException, ExecutionException{
        return ChapterAction.retrieveChapterById(id.toString()).get();
    }

    @Override
    public CompletableFuture<Chapter> retrieveAsync() {
        return ChapterAction.retrieveChapterById(id.toString());
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
