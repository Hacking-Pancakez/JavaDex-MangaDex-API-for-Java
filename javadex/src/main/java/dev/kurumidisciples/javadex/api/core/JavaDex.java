package dev.kurumidisciples.javadex.api.core;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.kurumidisciples.javadex.api.core.authentication.Token;
import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.FollowingEntityType;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;
import dev.kurumidisciples.javadex.api.exceptions.AuthorizationException;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.actions.retrieve.ChapterAction;
import dev.kurumidisciples.javadex.internal.actions.retrieve.FollowsAction;
import dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction;
import dev.kurumidisciples.javadex.internal.annotations.Size;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;
import dev.kurumidisciples.javadex.internal.utils.ErrorResponseChecker;
import dev.kurumidisciples.javadex.internal.annotations.Authenticated;
import okhttp3.Response;

/**
 * Main class for interacting with the MangaDex API.
 *
 * @author Hacking Pancakez
 */
public class JavaDex implements AutoCloseable{

    /**
     * Represents the mode of access for the MangaDex API.
     * 
     * @since 0.1.4
     */
    public enum Mode {
        /**
         * Guest access mode.
         * <p>
         * In this mode, methods that require authorization will throw {@link AuthorizationException}.
         * Methods that require authorization will have the {@link Authenticated} annotation.
         * </p>
         */
        GUEST,
        /**
         * Authorized access mode.
         * <p>
         * In this mode, all methods are available. Methods annotated with {@link Authenticated} will not throw an {@link AuthorizationException}.
         * </p>
         */
        AUTHORIZED
    }

    private static final Logger logger = LogManager.getLogger(JavaDex.class);
    private static final Duration DEFAULT_REFRESH_RATE = Duration.ofMinutes(15);
    private static final Gson GSON = new Gson();

    private final Token token;
    private final Duration refreshRate;
    private final String clientId;
    private final String clientSecret;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Mode mode;

    /**
     * Constructs a new JavaDex instance.
     *
     * @param tokens The authentication tokens.
     * @param refreshRate The rate at which tokens should be refreshed.
     * @param clientId The client ID.
     * @param clientSecret The client secret.
     */
    protected JavaDex(String[] tokens, Duration refreshRate, String clientId, String clientSecret) {
        this.token = new Token(tokens[0], tokens[1]);
        this.refreshRate = (refreshRate != null) ? refreshRate : DEFAULT_REFRESH_RATE;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        logger.debug("JavaDex instance created with access token: {}", token.getAccessToken());
        logger.info("JavaDex Object Built Successfully");
        scheduleTokenRefresh();
        this.mode = Mode.AUTHORIZED;
    }

    /**
     * <p>Constructor for JavaDex.</p>
     */
    protected JavaDex(){
        this.token = null;
        this.refreshRate = DEFAULT_REFRESH_RATE;
        this.clientId = null;
        this.clientSecret = null;
        logger.info("JavaDex Guest Object Built Successfully");
        this.mode = Mode.GUEST;
    }

    /**
     * Returns the authorization mode of the JavaDex object.
     *
     * @see Authenticated
     * @return a {@link Mode} object
     */
    public Mode getMode(){
        return mode;
    }

    /**
     * Initiates a search action with the provided query.
     *
     * This method creates a new SearchAction object with the provided query. The SearchAction object
     * can be used to perform a search on the MangaDex API.
     *
     * @param query The search query. This should be a non-null and non-empty string.
     * @return A new SearchAction object with the provided query.
     * @throws IllegalArgumentException If the provided query is null or empty.
     */
    public MangaAction search(@NotNull String query) {
      return new MangaAction(query);
    }

    /**
     * Initiates a search action.
     *
     * @return A SearchAction object.
     */
    public MangaAction search(){
      return new MangaAction();
    }

    /**
     * Initiates a search action.
     *
     * @param query The search query.
     * @param limit The number of results to return.
     * @return A SearchAction object.
     * @param offset a int
     */
    public MangaAction search(@NotNull String query, @Size(min=0, max=100) int limit, @Size(min=0) int offset){
      return new MangaAction(query, limit, offset);
    }

    /**
     * <p><b>Retrieves a Manga object for the specified ID.</b></p>
     *
     * This method sends a GET request to the MangaDex API to retrieve the details of the manga with the specified ID.
     * The ID is sent as a parameter in the URL of the request.
     *
     * @param id The ID of the manga to retrieve. This should be a non-null and non-empty string.
     * @return A CompletableFuture that will be completed with the Manga object when the API response is received and parsed.
     */
    public CompletableFuture<Manga> getMangaById(@NotNull String id) {
        return MangaAction.getMangaById(id);
    }

    /**
     * <b>{@link dev.kurumidisciples.javadex.internal.annotations.Authenticated} Method</b>
     * <b>Returns the access token.</b>
     *
     * @return a {@link java.lang.String} object
     */
    @Authenticated
    public synchronized String getAccessToken() {
        return token.getAccessToken();
    }
    /**
     * <b>{@link dev.kurumidisciples.javadex.internal.annotations.Authenticated} Method</b>
     * <b>Returns the refresh token.</b>
     *
     * @return a {@link java.lang.String} object
     */
    @Authenticated
    public synchronized String getRefreshToken() {
        return token.getRefreshToken();
    }

    /**
     * Schedules the token refresh at fixed intervals.
     */
    private void scheduleTokenRefresh() {
        logger.debug("Scheduling token refresh every {} milliseconds", refreshRate.toMillis());
        scheduler.scheduleAtFixedRate(() -> {
            try {
                refreshAccessToken();
            } catch (HTTPRequestException e) {
                logger.error("Failed to refresh access token", e);
            }
        }, refreshRate.toMillis(), refreshRate.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Follows a specific manga.
     *
     * @param manga - Manga object to follow.
     * @return true if the manga was successfully followed, false if error.
     */
    @Authenticated
    public CompletableFuture<Boolean> followManga(@NotNull Manga manga) {
        return followManga(manga.getIdRaw());
    }
    /**
     * Follows a specific manga.
     *
     * @param mangaId - UUID of the manga to follow.
     * @return true if the manga was successfully followed, false if error.
     */
    @Authenticated
    public CompletableFuture<Boolean> followManga(@NotNull UUID mangaId) {
        return followManga(mangaId.toString());
    }

    /**
     * Follows a specific manga.
     * 
     *
     * @param mangaId - String of the manga to follow.
     * @return CompletableFuture - true if the manga was successfully followed, false if error.
     */
    @Authenticated
    public CompletableFuture<Boolean> followManga(@NotNull String mangaId) {
        final String url = String.format("https://api.mangadex.org/manga/%s/follow", mangaId);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                String response = HTTPRequest.post(url, Optional.of(token.getAccessToken()));
                boolean success = response.contains("ok");
                if (success) {
                    logger.debug("Successfully followed manga {}", mangaId);
                } else {
                    logger.error("Failed to follow manga {}", mangaId);
                }
                return success;
            } catch (HTTPRequestException e) {
                logger.error("Unable to follow the given Manga object", e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Retrieves a list of UUIDs representing the chapters read for a specific manga.
     *
     * <p>This method sends a GET request to the MangaDex API to retrieve the list of read chapters for the manga with the specified ID.
     * The ID is appended to the URL of the request.</p>
     *
     * This method is asynchronous and returns a CompletableFuture that will be completed with the list of UUIDs when the
     * API response is received and parsed.
     *
     * @param mangaId The ID of the manga to retrieve the read chapters for. This should be a non-null and non-empty string.
     * @return A CompletableFuture that will be completed with the list of UUIDs representing the read chapters when the API response is received and parsed.
     */
    @Authenticated
    public CompletableFuture<List<UUID>> retrieveReadChapters(@NotNull String mangaId) {
        final String url = "https://api.mangadex.org/manga/" + mangaId + "/read";
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                String jsonResponse = HTTPRequest.get(url, Optional.of(token.getAccessToken()));
                JsonArray chapters = JsonParser.parseString(jsonResponse)
                                                .getAsJsonObject()
                                                .getAsJsonArray("data");
                List<UUID> chaptersList = new ArrayList<>();
                for (JsonElement chapter : chapters) {
                    chaptersList.add(UUID.fromString(chapter.getAsString()));
                }
                return chaptersList;
            } catch (HTTPRequestException e) {
                logger.error("Error retrieving read chapters", e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Marks a specific chapter as read.
     *
     * <p>This method sends a POST request to the MangaDex API to mark the specified chapter as read.
     * The chapter ID is sent in the request body as a JSON object.</p>
     *
     * This method is asynchronous and returns a CompletableFuture that will be completed when the
     * API response is received and parsed.
     *
     * @param chapter A {@link NotNull} Chapter object to mark as read.
     * @return A CompletableFuture that will be completed when the chapter is marked as read.
     */
    @Authenticated
    public CompletableFuture<Void> markChapterAsRead(@NotNull Chapter chapter) {
        return CompletableFuture.supplyAsync(() -> {
            JsonObject requestBody = new JsonObject();
            JsonArray read = new JsonArray();
            read.add(chapter.getIdRaw());
            requestBody.add("chapterIdsRead", read);
            requestBody.add("chapterIdsUnread", new JsonArray());
            try {
                String response = HTTPRequest.post("https://api.mangadex.org/manga/" + chapter.getRelationshipMap().get(RelationshipType.MANGA) + "/read", requestBody.toString(), Optional.of(token.getAccessToken()));
                logger.debug("Chapter {} marked as read with response {}", chapter.getId(), response);
            } catch (HTTPRequestException e) {
                logger.error("An error occured when attempting to mark the chapter as read", e);
                throw new CompletionException(e);
            }
            return null;
        });
    }

    /**
     * Initiates a chapter search action.
     *
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.ChapterAction} object
     */
    public ChapterAction retrieveChapters(){
        return new ChapterAction();
    }

    /**
     * Initiates a chapter search action with the specified title.
     *
     * @param title The title of the chapter.
     * @return A ChapterAction object.
     */
    public ChapterAction retrieveChapters(@NotNull String title){
        return new ChapterAction().setTitle(title);
    }

    /**
     * Initiates a chapter search action with the specified title and limit.
     *
     * @param title The title of the chapter.
     * @param limit The number of results to return.
     * @return A ChapterAction object.
     */
    public ChapterAction retrieveChapters(@NotNull String title, @Size(min=1, max=100) int limit){
        return new ChapterAction().setTitle(title).setLimit(limit);
    }

    /**
     * Retrieves the list of Scanlation Groups that the user is following.
     *
     * @return A CompletableFuture that will be completed with the list of ScanlationGroup objects
     *         when the API response is received and parsed.
     * @deprecated Use {@link #retrieveFollowingGroups()} instead.
     */
    @Deprecated
    @Authenticated
    public CompletableFuture<List<ScanlationGroup>> retrieveFollowingGroupsOLD(){
        if (token == null) {
            throw new AuthorizationException("Cannot retrieve following groups without authorization.");
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = GSON.fromJson(HTTPRequest.get("https://api.mangadex.org/user/follows/group", Optional.of(token.getAccessToken())), JsonObject.class);
                JsonArray groups = response.getAsJsonArray("data");

                // Use Stream API to transform JsonArray to List<ScanlationGroup>
                List<ScanlationGroup> groupList = StreamSupport.stream(groups.spliterator(), false)
                        .map(JsonElement::getAsJsonObject)
                        .map(ScanlationGroup::new)
                        .collect(Collectors.toList());

                return groupList;
            } catch (HTTPRequestException e) {
                logger.error("Could not retrieve following groups", e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Retrieves the list of Scanlation Groups that the user is following.
     *
     * @return FollowsAction object that you can use to complete the action.
     */
    @Authenticated
    public FollowsAction retrieveFollowingGroups(){
        return new FollowsAction(FollowingEntityType.SELF_GROUP, token);
    }
    
    /**
     * Checks if self is following a specific group.
     *
     * @return A CompletableFuture that will be completed with the list of ScanlationGroup objects
     *         when the API response is received and parsed.
     * @param group The ScanlationGroup object to check if self is following.
     */
    @Authenticated
    public CompletableFuture<Boolean> checkIfFollowingGroup(@NotNull ScanlationGroup group){
      return CompletableFuture.supplyAsync(() -> {
          try {
              String url = String.format("https://api.mangadex.org/user/follows/group/%s", group.getId());
              Response response = HTTPRequest.getResponse(url, Optional.of(token.getAccessToken()));
              int responseCode = response.code();
  
              switch (responseCode) {
                  case 200:
                      return true;
                  case 404:
                      return false;
                  default:
                      logger.error("Unexpected response code: {}", responseCode);
                      throw ErrorResponseChecker.retrieveCorrectHTTPException(response);
              }
          } catch (HTTPRequestException e) {
              logger.error("An HTTP exception occured while checking if self user was following given Scanlation Group", e);
              throw new CompletionException(e);
          }
      });
    }

    /**
     * Creates a new FollowsAction object to retrieve the manga that self is following.
     * @return a {@link FollowsAction} object
     */
    @Authenticated
    public FollowsAction retrieveFollowingManga(){
        return new FollowsAction(FollowingEntityType.SELF_MANGA, token);
    }

    /**
     * <Retrieves the current user's information from the MangaDex API.
     *
     * <p>This method sends a GET request to the "/user/me" endpoint of the MangaDex API.
     * The response is parsed into a {@link User} object and returned as a {@link CompletableFuture}.
     *
     * @return a CompletableFuture that will complete with the User object for the current user
     */
    @Authenticated
    public CompletableFuture<User> retrieveSelf(){
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = GSON.fromJson(HTTPRequest.get("https://api.mangadex.org/user/me", Optional.of(token.getAccessToken())), JsonObject.class);
                return new User(response);
            } catch (HTTPRequestException e) {
                logger.error("Unable to retrieve self user.", e);
                throw new CompletionException(e);
            }
        });
    }

   /**
   * Refreshes the access token using the refresh token.
   *
   * <p>This method sends a POST request to the MangaDex authentication API to refresh the access token.
   * The refresh token, client ID, and client secret are sent as form data in the request.</p>
   *
   * <p>If the refresh is successful, the new access token and refresh token are stored in the {@link Token} object.</p>
   *
   * <p><b>This method is synchronous and will block until the API response is received and parsed.</b></p>
   *
   * @throws CompletionException If an InterruptedException is thrown when waiting for the API response.
   *         The CompletionException will contain the original InterruptedException as its cause.
   */
  private synchronized void refreshAccessToken() {
    logger.debug("Refreshing access token using refresh token: {}", getRefreshToken());
    String refreshUrl = "https://auth.mangadex.org/realms/mangadex/protocol/openid-connect/token";
    Map<String, String> formData = Map.of(
        "grant_type", "refresh_token",
        "refresh_token", getRefreshToken(),
        "client_id", clientId,
        "client_secret", clientSecret
    );

    try {
        String jsonResponse = HTTPRequest.postForm(refreshUrl, formData);
        RefreshResponse refreshResponse = new Gson().fromJson(jsonResponse, RefreshResponse.class);
        token.setAccessToken(refreshResponse.access_token);
        token.setRefreshToken(refreshResponse.refresh_token);
        logger.debug("Access token refreshed successfully. New access token: {}", token.getAccessToken());
    } catch (HTTPRequestException e) {
        logger.fatal("Access token was not able to be refreshed. Check your credentials and/or Mangadex API status.", e);
        throw new CompletionException(e);
    }
  }


    /**
     * Inner class representing the response from the MangaDex API during token refresh.
     */
    private class RefreshResponse {
        private String access_token;
        private String refresh_token;
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        scheduler.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS))
                    logger.error("Scheduler did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            scheduler.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
