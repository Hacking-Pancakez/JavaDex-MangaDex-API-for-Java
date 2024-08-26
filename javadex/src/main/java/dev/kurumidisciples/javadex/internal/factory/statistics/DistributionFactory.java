package dev.kurumidisciples.javadex.internal.factory.statistics;

import org.apache.maven.api.annotations.Nonnull;

import com.google.gson.JsonObject;

public class DistributionFactory {
    
    private static int[][] distribution = {
        {1, 0},
        {2, 0},
        {3, 0},
        {4, 0},
        {5, 0},
        {6, 0},
        {7, 0},
        {8, 0},
        {9, 0},
        {10, 0}
    };

    public static int[][] createDistribution(@Nonnull JsonObject distribution){
        int[][] newDistribution = new int[10][2];
        for(int i = 0; i < 10; i++){
            newDistribution[i][0] = i + 1;
            newDistribution[i][1] = distribution.get(String.valueOf(i + 1)).getAsInt();
        }
        return newDistribution;
    }
}
