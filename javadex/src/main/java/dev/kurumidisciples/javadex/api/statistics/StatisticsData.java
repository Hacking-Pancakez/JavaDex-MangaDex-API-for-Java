package dev.kurumidisciples.javadex.api.statistics;

import org.apache.maven.api.annotations.Nonnull;

import com.google.gson.JsonObject;

// TODO Currently only usable for manga statistics, should be expanded to work with other entities.
public class StatisticsData {
    

    private final Rating rating;
    private final Comments comments;
    private final int follows;

    public StatisticsData(@Nonnull JsonObject stats){
        this.rating = new Rating(stats.getAsJsonObject("rating"));
        this.comments = new Comments(stats.getAsJsonObject("comments"));
        this.follows = stats.get("follows").getAsInt();
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
