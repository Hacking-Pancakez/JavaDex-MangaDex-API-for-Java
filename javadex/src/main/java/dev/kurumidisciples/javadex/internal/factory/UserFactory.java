package dev.kurumidisciples.javadex.internal.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;

/**
 * <p>UserFactory class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class UserFactory {
    
    private static final String USER_API = "https://api.mangadex.org/user/";
    
    private static final Logger logger = LogManager.getLogger(UserFactory.class);

    /**
     * Builds a User object from the given Chapter object.
     *
     * @param chapter The Chapter object from which to build the User object.
     * @return The uploader of the chapter object.
     */
    public static User retrieveUploader(@NotNull Chapter chapter){
        RelationshipMap relationshipMap = chapter.getRelationshipMap();
        String uploaderId = relationshipMap.get(RelationshipType.USER).get(0).getId().toString();
        String jsonResponse = null;
        try {
            jsonResponse = HTTPRequest.get(USER_API + uploaderId);
        } catch (HTTPRequestException e) { 
            logger.error("Could not retrieve the requested user from the id: {}", uploaderId, e);
            return null; 
        }
        Gson gson = new Gson();
        return new User(gson.fromJson(jsonResponse, JsonObject.class));
    }
}
