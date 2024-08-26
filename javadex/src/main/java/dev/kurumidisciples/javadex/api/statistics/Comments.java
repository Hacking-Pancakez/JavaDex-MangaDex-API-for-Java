package dev.kurumidisciples.javadex.api.statistics;

import org.apache.maven.api.annotations.Nonnull;

import com.google.gson.JsonObject;

public class Comments {

    private final Number threadId;
    private final Number repliesCount;

    protected Comments(@Nonnull JsonObject comments){
        this.threadId = comments.get("threadId").getAsNumber();
        this.repliesCount = comments.get("repliesCount").getAsNumber();
    }

    /**
     * Returns the unique identifier for the corresponding thread of this manga.
     */
    public Number getThreadId() {
        return threadId;
    }

    /**
     * Returns the number of replies to the thread.
     */
    public Number getRepliesCount() {
        return repliesCount;
    }
}
