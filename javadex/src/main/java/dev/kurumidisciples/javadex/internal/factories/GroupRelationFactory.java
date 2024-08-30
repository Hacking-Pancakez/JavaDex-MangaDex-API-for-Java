package dev.kurumidisciples.javadex.internal.factories;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipData;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPNotFoundException;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.factories.entities.GroupFactory;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;

/**
 * <p>GroupFactory class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class GroupRelationFactory {

    private static final String GROUP_API = "https://api.mangadex.org/group/";

    private static final Logger logger = LogManager.getLogger(GroupRelationFactory.class);

    /**
     * Builds a group from relationship data
     *
     * @return ScanlationGroup object
     * @param data a {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipData} object
     * @throws java.io.IOException if any.
     * @throws java.lang.InterruptedException if any.
     */
    public static ScanlationGroup getScanlationGroup(RelationshipData data) throws IOException, InterruptedException{
            String groupId = data.getId().toString();
            String jsonResponse;
            try {
                jsonResponse = HTTPRequest.get(GROUP_API + groupId);
            } catch (HTTPRequestException e) {
                // Handle the exception here
                logger.error("Request was unable to be completed", e);
                return null; // Or handle the exception in a different way
            }
            Gson gson = new Gson();
            return GroupFactory.createEntity(gson.fromJson(jsonResponse, JsonObject.class).getAsJsonObject("data"));
    }

    public static ScanlationGroup getScanlationGroup(String groupId) throws HTTPRequestException{
        String jsonResponse = new String();
        try {
            jsonResponse = HTTPRequest.get(GROUP_API + groupId);
        } catch (HTTPNotFoundException e) {
            throw new HTTPNotFoundException("Group not found", e);
        }
        Gson gson = new Gson();
        return GroupFactory.createEntity(gson.fromJson(jsonResponse, JsonObject.class).getAsJsonObject("data"));
    }

}
