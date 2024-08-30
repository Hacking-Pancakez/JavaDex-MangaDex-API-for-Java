package dev.kurumidisciples.javadex.api.proxies;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.exceptions.EntityNotFoundException;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.factories.GroupRelationFactory;


public class GroupProxy implements EntityProxy<ScanlationGroup> {

    private final static Logger logger = LogManager.getLogger(GroupProxy.class);

    private final UUID id;
    private final Type type = Type.SCANLATION_GROUP;

    public GroupProxy(UUID id) {
        this.id = id;
    }

    @Override
    public ScanlationGroup retrieve() throws RuntimeException{
        try {
            return GroupRelationFactory.getScanlationGroup(id.toString());
        } catch (HTTPRequestException e) {
            logger.error("An error occurred while retrieving the group", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<ScanlationGroup> retrieveAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return retrieve();
            } catch (RuntimeException e) {
                logger.error("An error occurred while retrieving the group", e);
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
