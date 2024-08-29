package dev.kurumidisciples.javadex.internal.actions.retrieve;

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
import dev.kurumidisciples.javadex.api.entities.Manga;
import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.api.entities.enums.FollowingEntityType;
import dev.kurumidisciples.javadex.api.entities.enums.IncludesType;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.actions.Action;
import dev.kurumidisciples.javadex.internal.factories.entities.GroupFactory;
import dev.kurumidisciples.javadex.internal.factories.entities.MDListFactory;
import dev.kurumidisciples.javadex.internal.factories.entities.MangaFactory;
import dev.kurumidisciples.javadex.internal.factories.entities.UserFactory;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;

/**
 * <p>FollowsAction is responsible for handling the follow actions for different entities
 * such as {@link dev.kurumidisciples.javadex.api.entities.Manga}, {@link dev.kurumidisciples.javadex.api.entities.ScanlationGroup}, {@link dev.kurumidisciples.javadex.api.entities.User}, and Lists on the MangaDex platform.</p>
 */
@SuppressWarnings("unused")
public class FollowsAction extends Action<List<?>> {

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
            case SELF_MANGA -> {
                for (IncludesType availableInclude : AVAILABLE_INCLUDES_TYPES_MANGA) {
                    if (availableInclude == include) {
                        return true;
                    }
                }
            }
            case SELF_GROUP -> {
                for (IncludesType availableInclude : AVAILABLE_INCLUDES_TYPES_GROUP) {
                    if (availableInclude == include) {
                        return true;
                    }
                }
            }
            default -> {
                return false;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public List<?> complete() throws HTTPRequestException {
        String query = toString();
        String response = HTTPRequest.get(query, Optional.of(authorization.getAccessToken()));
        switch (followingType) {
            case SELF_MANGA -> {
                return parseMangaResponse(response);
            }
            case SELF_GROUP -> {
                return parseGroupResponse(response);
            }
            case SELF_USER -> {
                return parseUserResponse(response);
            }
            case SELF_LIST -> {
                return parseListResponse(response);
            }
            default -> {
                return null;
            }
        }
    }

    private List<Manga> parseMangaResponse(String response) {
        JsonObject mangaResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray mangaData = mangaResponse.getAsJsonArray("data");
        List<Manga> mangaList = new ArrayList<>();
        for (JsonElement manga : mangaData) {
            Manga mangaEntity = MangaFactory.createEntity(manga.getAsJsonObject());
            mangaList.add(mangaEntity);
        }
        return mangaList;
    }

    private List<ScanlationGroup> parseGroupResponse(String response) {
        JsonObject groupResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray groupData = groupResponse.getAsJsonArray("data");
        List<ScanlationGroup> groupList = new ArrayList<>();
        for (JsonElement group : groupData) {
            groupList.add(GroupFactory.createEntity(group.getAsJsonObject()));
        }
        return groupList;
    }

    private List<User> parseUserResponse(String response) {
        JsonObject userResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray userData = userResponse.getAsJsonArray("data");
        List<User> userList = new ArrayList<>();
        for (JsonElement user : userData) {
            userList.add(UserFactory.createEntity(user.getAsJsonObject()));
        }
        return userList;
    }

    private List<MDList> parseListResponse(String response) {
        JsonObject listResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray listData = listResponse.getAsJsonArray("data");
        List<MDList> listList = new ArrayList<>();
        for (JsonElement list : listData) {
            listList.add(MDListFactory.createEntity(list.getAsJsonObject()));
        }
        return listList;
    }

    /**
     * {@inheritDoc}
     *
     * Submits the follow action asynchronously.
     */
    @Override
    public CompletableFuture<List<?>> submit() {
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
    @Deprecated
    private static String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
