package dev.kurumidisciples.javadex.api.statistics;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.internal.factories.statistics.DistributionFactory;

public class Rating {
    
    private final Number average;
    private final Number bayesian;
    private final int[][] distribution;

    protected Rating(JsonObject rating){
        this.average = rating.get("average").getAsNumber();
        this.bayesian = rating.get("bayesian").getAsNumber();
        this.distribution = DistributionFactory.createDistribution(rating.getAsJsonObject("distribution"));
    }

    /**
     * Average rating of the Manga.
     */
    public Number getAverage() {
        return average;
    }

    /**
     * <p><b>From MangaDex API Documentation:</b></p>
     * <p><blockquote>Bayesian scores start with a set bias. This is a method to avoid little-known Manga to be greatly affected by a few people's opinion. The Bayesian Average ultimately converges to the mean, as the reviews popoulate.</blockquote></p>
     */
    public Number getBayesian() {
        return bayesian;
    }

    /**
     * <p>Getter for the field <code>distribution</code>.</p>
     * The 2d array follows this format:
     * <pre>
     *  "1": 0,
     * "2": 0,
     * "3": 0,
     * "4": 0,
     * "5": 0,
     * "6": 0,
     * "7": 0,
     * "8": 0,
     * "9": 0,
     * "10": 0
     * </pre>
     */
    public int[][] getDistribution() {
        return distribution;
    }

}
