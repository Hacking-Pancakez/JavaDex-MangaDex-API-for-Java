package dev.kurumidisciples.javadex.internal.actions.retrieve;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.kurumidisciples.javadex.api.core.authentication.Token;
import dev.kurumidisciples.javadex.api.entities.MDList;
import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.FollowingEntityType;
import dev.kurumidisciples.javadex.api.entities.enums.IncludesType;
import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;
import dev.kurumidisciples.javadex.api.entities.intermediate.middlemen.EntityMiddleman;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.actions.Action;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;

/**
 * <p>FollowsAction is responsible for handling the follow actions for different entities
 * such as {@link dev.kurumidisciples.javadex.api.entities.content.Manga}, {@link dev.kurumidisciples.javadex.api.entities.ScanlationGroup}, {@link dev.kurumidisciples.javadex.api.entities.User}, and Lists on the MangaDex platform.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
@SuppressWarnings("unused")
public class FollowsAction extends Action<List<EntityMiddleman>> {

    private int limit;
    private int offset;
    private final FollowingEntityType followingType;
    private IncludesType[] includes = null;
    private Token authorization;

    private static final String BASE_URL = "https://api.mangadex.org";

    private static final IncludesType[] AVAILABLE_INCLUDES_TYPES_MANGA = new IncludesType[] {
        IncludesType.MANGA,
        IncludesType.COVER_ART,
        IncludesType.AUTHOR,
        IncludesType.ARTIST,
        IncludesType.TAG,
        IncludesType.CREATOR
    };

    private static final IncludesType[] AVAILABLE_INCLUDES_TYPES_GROUP = new IncludesType[] {
        IncludesType.LEADER,
        IncludesType.MEMBER
    };

    private static final Logger logger = LogManager.getLogger(FollowsAction.class);

    /**
     * Constructs a new FollowsAction with the provided entity and authorization.
     *
     * @param entity the entity to follow.
     * @param authorization the authorization token.
     */
    public FollowsAction(FollowingEntityType entity, Token authorization) {
        this.followingType = entity;
        this.authorization = authorization;
        this.limit = 10;
        this.offset = 0;
    }

    /**
     * {@inheritDoc}
     *
     * Sets the limit for the number of entities to fetch.
     */
    @Override
    public FollowsAction setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Sets the offset for fetching entities.
     */
    @Override
    public FollowsAction setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Sets the includes types for the follow request.
     * <p>See the <a href="https://api.mangadex.org/docs/swagger.html#/Follows">MangaDex API documentation</a> for more information on available includes.</p>
     */
    @Override
    public FollowsAction setIncludes(@NotNull IncludesType... includes) {
        for (IncludesType include : includes) {
            if (!isValidInclude(include)) {
                throw new IllegalArgumentException("Invalid IncludeType for FollowingType{" + followingType.getType() + "}: " + include.getType());
            }
        }
        this.includes = includes;
        return this;
    }

    /**
     * Validates if the provided include type is valid for the current following type.
     * @param include the include type to validate.
     * @return true if the include type is valid, false otherwise.
     */
    private boolean isValidInclude(@NotNull IncludesType include) {
        switch (followingType) {
            case SELF_MANGA:
                for (IncludesType availableInclude : AVAILABLE_INCLUDES_TYPES_MANGA) {
                    if (availableInclude == include) {
                        return true;
                    }
                }
                break;
            case SELF_GROUP:
                for (IncludesType availableInclude : AVAILABLE_INCLUDES_TYPES_GROUP) {
                    if (availableInclude == include) {
                        return true;
                    }
                }
                break;
            default:
                return false;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public List<EntityMiddleman> complete() throws HTTPRequestException {
        String query = toString();
        String response = HTTPRequest.get(query, Optional.of(authorization.getAccessToken()));
        switch (followingType) {
            case SELF_MANGA:
                JsonObject mangaResponse = JsonParser.parseString(response).getAsJsonObject();
                JsonArray mangaData = mangaResponse.getAsJsonArray("data");
                List<EntityMiddleman> mangaList = new ArrayList<>();
                for (JsonElement manga : mangaData) {
                    mangaList.add(new EntityMiddleman(new Manga(manga.getAsJsonObject())));
                }
                return mangaList;
            case SELF_GROUP:
                JsonObject groupResponse = JsonParser.parseString(response).getAsJsonObject();
                JsonArray groupData = groupResponse.getAsJsonArray("data");
                List<EntityMiddleman> groupList = new ArrayList<>();
                for (JsonElement group : groupData) {
                    groupList.add(new EntityMiddleman(new ScanlationGroup(group.getAsJsonObject())));
                }
                return groupList;
            case SELF_USER:
                JsonObject userResponse = JsonParser.parseString(response).getAsJsonObject();
                JsonArray userData = userResponse.getAsJsonArray("data");
                List<EntityMiddleman> userList = new ArrayList<>();
                for (JsonElement user : userData) {
                    userList.add(new EntityMiddleman(new User(user.getAsJsonObject())));
                }
                return userList;
            case SELF_LIST:
                JsonObject listResponse = JsonParser.parseString(response).getAsJsonObject();
                JsonArray listData = listResponse.getAsJsonArray("data");
                List<EntityMiddleman> listList = new ArrayList<>();
                for (JsonElement list : listData) {
                    listList.add(new EntityMiddleman(new MDList(list.getAsJsonObject())));
                }
                return listList;
            default:
                return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * Submits the follow action asynchronously.
     */
    @Override
    public CompletableFuture<List<EntityMiddleman>> submit() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return complete();
            } catch (HTTPRequestException e) {
                logger.error("An error occurred while queuing the action: " + e.getMessage(), e);
                return null;
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * Converts the follow action parameters to a URL query string.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(BASE_URL).append(followingType.getEndpoint());
        builder.append("?limit=").append(limit);
        builder.append("&offset=").append(offset);
        if (includes != null) {
            builder.append("&includes[]=");
            for (IncludesType include : includes) {
                builder.append(include.getType()).append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    /**
     * Encodes a value to be used in a URL query string.
     * @param value The value to encode.
     * @return The encoded value.
     */
    private static String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
