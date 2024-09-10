package dev.kurumidisciples.javadex.api.statistics;

import java.util.UUID;

import org.apache.maven.api.annotations.Nonnull;
import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

// TODO Currently only usable for manga statistics, should be expanded to work with other entities.
public class StatisticsData {
    

    private final Rating rating;
    private final Comments comments;
    private final int follows;
    private final UUID id;

    public StatisticsData(@NotNull JsonObject stats){
        String uuidString = stats.keySet().iterator().next();
        this.id = UUID.fromString(uuidString);

        JsonObject entityStats = stats.getAsJsonObject(uuidString);
        this.comments = new Comments(entityStats.getAsJsonObject("comments"));
        this.rating = new Rating(entityStats.getAsJsonObject("rating"));
        this.follows = entityStats.get("follows").getAsInt();
    }

    /**
     * Returns the rating statistics of the Manga.
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * Returns the comments statistics of the Manga.
     */
    public Comments getComments() {
        return comments;
    }

    /**
     * Returns the number of users following the Manga.
     */
    public int getFollows() {
        return follows;
    }
}
