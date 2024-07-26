package dev.kurumidisciples.javadex.api.entities.content;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.entities.enums.*;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status;
import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.api.proxies.CoverProxy;
import dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown;
import dev.kurumidisciples.javadex.internal.annotations.NotLessThanOne;
import dev.kurumidisciples.javadex.internal.annotations.Size;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;
import dev.kurumidisciples.javadex.internal.parsers.MangaParsers;

/**
 * Represents a Manga entity in the MangaDex API.
 * <p>Could represent a <b>draft version</b> of a manga, these are not yet published and are not visible to the public. They will not have any retrievable chapters and/or pages. Use the method {@code getState()} to check if it is a draft.</p>
 * @apiNote Can represent any readable content in MangaDex, encapsulating manga, manhwa, manhua, and doujinshi.
 * @see <a href="https://api.mangadex.org/docs/03-manga/creation/#creation">Creation</a>
 * @see <a href="https://api.mangadex.org/docs/03-manga/search/">Search</a>
 */
public class Manga extends Entity implements ISnowflake {

    public enum Tag {

        ONESHOT("Oneshot", UUID.fromString("0234a31e-a729-4e28-9d6a-3f87c4966b9e")),
        THRILLER("Thriller", UUID.fromString("07251805-a27e-4d59-b488-f0bfbec15168")),
        AWARD_WINNING("Award Winning", UUID.fromString("0a39b5a1-b235-4886-a747-1d05d216532d")),
        REINCARNATION("Reincarnation", UUID.fromString("0bc90acb-ccc1-44ca-a34a-b9f3a73259d0")),
        SCI_FI("Sci-Fi", UUID.fromString("256c8bd9-4904-4360-bf4f-508a76d67183")),
        TIME_TRAVEL("Time Travel", UUID.fromString("292e862b-2d17-4062-90a2-0356caa4ae27")),
        GENDERSWAP("Genderswap", UUID.fromString("2bd2e8d0-f146-434a-9b51-fc9ff2c5fe6a")),
        TRADITIONAL_GAMES("Traditional Games", UUID.fromString("31932a7e-5b8e-49a6-9f12-2afa39dc544c")),
        OFFICIAL_COLORED("Official Colored", UUID.fromString("320831a8-4026-470b-94f6-8353740e6f04")),
        HISTORICAL("Historical", UUID.fromString("33771934-028e-4cb3-8744-691e866a923e")),
        MONSTERS("Monsters", UUID.fromString("36fd93ea-e8b8-445e-b836-358f02b3d33d")),
        ACTION("Action", UUID.fromString("391b0423-d847-456f-aff0-8b0cfc03066b")),
        DEMONS("Demons", UUID.fromString("39730448-9a5f-48a2-85b0-a70db87b1233")),
        PSYCHOLOGICAL("Psychological", UUID.fromString("3b60b75c-a2d7-4860-ab56-05f391bb889c")),
        GHOSTS("Ghosts", UUID.fromString("3bb26d85-09d5-4d2e-880c-c34b974339e9")),
        ANIMALS("Animals", UUID.fromString("3de8c75d-8ee3-48ff-98ee-e20a65c86451")),
        LONG_STRIP("Long Strip", UUID.fromString("3e2b8dae-350e-4ab8-a8ce-016e844b9f0d")),
        ROMANCE("Romance", UUID.fromString("423e2eae-a7a2-4a8b-ac03-a8351462d71d")),
        NINJA("Ninja", UUID.fromString("489dd859-9b61-4c37-af75-5b18e88daafc")),
        COMEDY("Comedy", UUID.fromString("4d32cc48-9f00-4cca-9b5a-a839f0764984")),
        MECHA("Mecha", UUID.fromString("50880a9d-5440-4732-9afb-8f457127e836")),
        ANTHOLOGY("Anthology", UUID.fromString("51d83883-4103-437c-b4b1-731cb73d786c")),
        BOYS_LOVE("Boys' Love", UUID.fromString("5920b825-4181-4a17-beeb-9918b0ff7a30")),
        INCEST("Incest", UUID.fromString("5bd0e105-4481-44ca-b6e7-7544da56b1a3")),
        CRIME("Crime", UUID.fromString("5ca48985-9a9d-4bd8-be29-80dc0303db72")),
        SURVIVAL("Survival", UUID.fromString("5fff9cde-849c-4d78-aab0-0d52b2ee1d25")),
        ZOMBIES("Zombies", UUID.fromString("631ef465-9aba-4afb-b0fc-ea10efe274a8")),
        REVERSE_HAREM("Reverse Harem", UUID.fromString("65761a2a-415e-47f3-bef2-a9dababba7a6")),
        SPORTS("Sports", UUID.fromString("69964a64-2f90-4d33-beeb-f3ed2875eb4c")),
        SUPERHERO("Superhero", UUID.fromString("7064a261-a137-4d3a-8848-2d385de3a99c")),
        MARTIAL_ARTS("Martial Arts", UUID.fromString("799c202e-7daa-44eb-9cf7-8a3c0441531e")),
        FAN_COLORED("Fan Colored", UUID.fromString("7b2ce280-79ef-4c09-9b58-12b7c23a9b78")),
        SAMURAI("Samurai", UUID.fromString("81183756-1453-4c81-aa9e-f6e1b63be016")),
        MAGICAL_GIRLS("Magical Girls", UUID.fromString("81c836c9-914a-4eca-981a-560dad663e73")),
        MAFIA("Mafia", UUID.fromString("85daba54-a71c-4554-8a28-9901a8b0afad")),
        ADVENTURE("Adventure", UUID.fromString("87cc87cd-a395-47af-b27a-93258283bbc6")),
        GIRLS_LOVE("Girls' Love", UUID.fromString("a3c67850-4684-404e-9b7f-c69850ee5da6")),
        GORE("Gore", UUID.fromString("b29d6a3d-1569-4e7a-8caf-7557bc92cd5d")),
        MEDICAL("Medical", UUID.fromString("c8cbe35b-1b2b-4a3f-9c37-db84c4514856")),
        SCHOOL_LIFE("School Life", UUID.fromString("caaa44eb-cd40-4177-b930-79d3ef2afe87")),
        HORROR("Horror", UUID.fromString("cdad7e68-1419-41dd-bdce-27753074a640")),
        FANTASY("Fantasy", UUID.fromString("cdc58593-87dd-415e-bbc0-2ec27bf404cc")),
        VILLAINESS("Villainess", UUID.fromString("d14322ac-4d6f-4e9b-afd9-629d5f4d8a41")),
        VAMPIRES("Vampires", UUID.fromString("d7d1730f-6eb0-4ba6-9437-602cac38664c")),
        DELINQUENTS("Delinquents", UUID.fromString("da2d50ca-3018-4cc0-ac7a-6b7d472a29ea")),
        MONSTER_GIRLS("Monster Girls", UUID.fromString("dd1f77c5-dea9-4e2b-97ae-224af09caf99")),
        POLICE("Police", UUID.fromString("df33b754-73a3-4c54-80e6-1a74a8058539")),
        WEB_COMIC("Web Comic", UUID.fromString("e197df38-d0e7-43b5-9b09-2842d0c326dd")),
        SLICE_OF_LIFE("Slice of Life", UUID.fromString("e5301a23-ebd9-49dd-a0cb-2add944c7fe9")),
        ALIENS("Aliens", UUID.fromString("e64f6742-c834-471d-8d72-dd51fc02b835")),
        COOKING("Cooking", UUID.fromString("ea2bc92d-1c26-4930-9b7c-d5c0dc1b6869")),
        SUPERNATURAL("Supernatural", UUID.fromString("eabc5b4c-6aff-42f3-b657-3e90cbd00b75")),
        MYSTERY("Mystery", UUID.fromString("ee968100-4191-4968-93d3-f82d72be7e46")),
        ADAPTATION("Adaptation", UUID.fromString("f4122d1c-3b44-44d0-9936-ff7502c39ad3")),
        MUSIC("Music", UUID.fromString("f42fbf9e-188a-447b-9fdc-f19dc1e4d685")),
        FULL_COLOR("Full Color", UUID.fromString("f5ba408b-0e7a-484d-8d49-4e9125ac96de")),
        TRAGEDY("Tragedy", UUID.fromString("f8f62932-27da-4fe4-8ee1-6779a8c5edba")),
        GYARU("Gyaru", UUID.fromString("fad12b5e-68ba-460e-b933-9ae8318f5b65")),
        ISEKAI("Isekai", UUID.fromString("ace04997-f6bd-436e-b261-779182193d3d")),
        MILITARY("Military", UUID.fromString("ac72833b-c4e9-4878-b9db-6c8a4a99444a")),
        PHILOSOPHICAL("Philosophical", UUID.fromString("b1e97889-25b4-4258-b28b-cd7f4d28ea9b")),
        VIRTUAL_REALITY("Virtual Reality", UUID.fromString("8c86611e-fab7-4986-9dec-d1a2f44acdd5")),
        OFFICE_WORKERS("Office Workers", UUID.fromString("92d6d951-ca5e-429c-ac78-451071cbf064")),
        VIDEO_GAMES("Video Games", UUID.fromString("9438db5a-7e2a-4ac0-b39e-e0d95a34b8a8")),
        POST_APOCALYPTIC("Post-Apocalyptic", UUID.fromString("9467335a-1b83-4497-9231-765337a00b96")),
        SEXUAL_VIOLENCE("Sexual Violence", UUID.fromString("97893a4c-12af-4dac-b6be-0dffb353568e")),
        CROSSDRESSING("Crossdressing", UUID.fromString("9ab53f92-3eed-4e9b-903a-917c86035ee3")),
        MAGIC("Magic", UUID.fromString("a1f53773-c69a-4ce5-8cab-fffcd90b1565")),
        SELF_PUBLISHED("Self-Published", UUID.fromString("891cf039-b895-47f0-9229-bef4c96eccd4")),
        WUXIA("Wuxia", UUID.fromString("acc803a4-c95a-4c22-86fc-eb6b582d82a2")),
        DOUJINSHI("Doujinshi", UUID.fromString("b13b2a48-c720-44a9-9c77-39c9979373fb"));
      
        private final String name;
        private final UUID id;
      
        Tag(String name, UUID id) {
          this.name = name;
          this.id = id;
        }
      
        public String getName() {
          return name;
        }
      
        public UUID getId() {
          return id;
        }
      
        public static Tag getByName(String name) {
          for (Tag tag : Tag.values()) {
            if (tag.getName().equalsIgnoreCase(name)) {
              return tag;
            }
          }
          return null;
        }
      
        public static Tag getById(UUID id) {
          for (Tag tag : Tag.values()) {
            if (tag.getId().equals(id)) {
              return tag;
            }
          }
          return null;
        }
    }

    private static final Logger logger = LogManager.getLogger(Manga.class);
    private static final Gson gson = new Gson();

    private final UUID id;
    private final List<Tag> tags;
    private final String title;
    private final UUID author;
    private final Map<Locale, String> description;
    private final Map<Locale, List<String>> altTitles;
    private final boolean isLocked;
    private final Locale originalLanguage;
    private final Number lastVolume;
    private final Number lastChapter;
    private final Demographic publicationDemographic;
    private final Status status;
    private final Long year;
    private final ContentRating contentRating;
    private final State state;
    private final boolean chapterNumbersResetOnNewVolume;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final long version;
    private final List<String> availableTranslatedLanguages;
    private final String latestUploadedChapterId;
    private final RelationshipMap relationshipMap;

    public Manga(@NotNull JsonObject mangaJson) {
        JsonObject attributes = mangaJson.getAsJsonObject("attributes");
        this.id = UUID.fromString(mangaJson.get("id").getAsString());
        this.title = attributes.getAsJsonObject("title").has("en") ? attributes.getAsJsonObject("title").get("en").getAsString() : "No title";

        this.description = MangaParsers.parseDescription(attributes.getAsJsonObject("description"));
        this.altTitles = MangaParsers.parseAltTitles(attributes.getAsJsonArray("altTitles"));
        this.isLocked = attributes.get("isLocked").getAsBoolean();
        this.originalLanguage = Locale.getByLanguage(attributes.get("originalLanguage").getAsString());
        this.lastVolume = attributes.has("lastVolume") && !attributes.get("lastVolume").isJsonNull() ? attributes.get("lastVolume").getAsNumber() : null;
        this.lastChapter = attributes.has("lastChapter") && !attributes.get("lastChapter").isJsonNull() ? attributes.get("lastChapter").getAsNumber() : null;
        this.publicationDemographic = Demographic.getDemographic(attributes.has("publicationDemographic") && !attributes.get("publicationDemographic").isJsonNull() ? attributes.get("publicationDemographic").getAsString() : "Unknown");
        this.status = Status.getStatus(attributes.get("status").getAsString());
        this.year = attributes.has("year") && !attributes.get("year").isJsonNull() ? attributes.get("year").getAsLong() : null;
        this.contentRating = ContentRating.getContentRating(attributes.get("contentRating").getAsString());
        this.state = State.getByValue(attributes.get("state").getAsString());
        this.chapterNumbersResetOnNewVolume = attributes.get("chapterNumbersResetOnNewVolume").getAsBoolean();
        this.createdAt = OffsetDateTime.parse(attributes.get("createdAt").getAsString());
        this.updatedAt = OffsetDateTime.parse(attributes.get("updatedAt").getAsString());
        this.version = attributes.get("version").getAsLong();
        this.latestUploadedChapterId = attributes.has("latestUploadedChapter") && !attributes.get("latestUploadedChapter").isJsonNull() ? attributes.get("latestUploadedChapter").getAsString() : null;
        this.tags = MangaParsers.parseTags(attributes.getAsJsonArray("tags"));
        this.availableTranslatedLanguages = MangaParsers.parseAvailableTranslatedLanguages(attributes.getAsJsonArray("availableTranslatedLanguages"));
        this.relationshipMap = new RelationshipMap(mangaJson.getAsJsonArray("relationships"));
        this.author = relationshipMap.get(RelationshipType.AUTHOR).get(0).getId();
    }

    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    public boolean doChapterNumbersResetOnNewVolume() {
        return chapterNumbersResetOnNewVolume;
    }

    public State getState() {
        return state;
    }

    public CompletableFuture<List<CoverProxy>> retrieveCovers(@NotNull @MustNotBeUnknown Locale lang, @Size(min=0, max=100) int limit,@Size(min=0) int offset) {
        if (state == State.DRAFT) throw new UnsupportedOperationException("Cannot retrieve covers for a draft manga.");
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/cover?limit=" + limit + "&offset=" + offset + "&manga[]=" + getId() + "&locales[]=" + lang.getLanguage()), JsonObject.class);
                List<CoverProxy> covers = new ArrayList<>();
                JsonArray coverArray = response.getAsJsonArray("data");
                for (JsonElement coverElement : coverArray) {
                    covers.add(new CoverProxy(coverElement.getAsJsonObject()));
                }
                return covers;
            } catch (HTTPRequestException e) {
                logger.error("Could not retrieve all covers for manga " + id, e);
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<List<CoverProxy>> retrieveCovers(Locale lang) {
        return retrieveCovers(lang, 100, 0);
    }

    public CompletableFuture<List<CoverProxy>> retrieveCovers(Locale lang, int limit) {
        return retrieveCovers(lang, limit, 0);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getIdRaw() {
        return id.toString();
    }

    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getDefaultTitle() {
        return title;
    }

    public UUID getAuthor() {
        return author;
    }

    public String getDescription(Locale lang) {
        return description.get(lang);
    }

    public Map<Locale, String> getDescriptions() {
        return description;
    }

    public Map<Locale, List<String>> getAltTitles() {
        return altTitles;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public Locale getOriginalLanguage() {
        return originalLanguage;
    }

    public Number getLastVolume() {
        return lastVolume;
    }

    public Number getLastChapter() {
        return lastChapter;
    }

    public ContentRating getContentRating() {
        return contentRating;
    }

    public Demographic getPublicationDemographic() {
        return publicationDemographic;
    }

    public Status getStatus() {
        return status;
    }

    public Long getYear() {
        return year;
    }

    public UUID getLatestUploadedChapterId() {
        return UUID.fromString(latestUploadedChapterId);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public CompletableFuture<List<Chapter>> retrieveFeed() {
        if (state == State.DRAFT) throw new UnsupportedOperationException("Cannot retrieve feed for a draft manga.");
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/manga/" + getId() + "/feed"), JsonObject.class);
                if (isError(response)) {
                    throw new RuntimeException("Error retrieving manga feed: " + response.getAsJsonArray("errors").get(0).getAsJsonObject().get("detail").getAsString());
                }
                JsonArray chapters = response.getAsJsonArray("data");
                List<Chapter> chaptersList = new ArrayList<>();
                for (JsonElement chapter : chapters) {
                    chaptersList.add(new Chapter(chapter.getAsJsonObject()));
                }
                return chaptersList;
            } catch (HTTPRequestException e) {
                logger.error("Error retrieving feed", e);
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Integer> retrieveChapterCount(Locale lang) {
        if (state == State.DRAFT) throw new UnsupportedOperationException("Cannot retrieve chapter count for a draft manga.");
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/manga/" + getId() + "/aggregate?translatedLanguage[]=" + lang.getLanguage()), JsonObject.class);
                List<UUID> chaptersList = parseIds(response);
                return chaptersList.size();
            } catch (HTTPRequestException e) {
                logger.error("Error retrieving chapter count", e);
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<List<UUID>> retrieveChaptersIds(Locale lang) {
        if (state == State.DRAFT) throw new UnsupportedOperationException("Cannot retrieve chapters for a draft manga.");
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/manga/" + getId() + "/aggregate?translatedLanguage[]=" + lang.getLanguage()), JsonObject.class);
                List<UUID> chaptersList = parseIds(response);
                Collections.reverse(chaptersList);
                return chaptersList;
            } catch (HTTPRequestException e) {
                logger.error("Error retrieving chapter IDs", e);
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<List<Chapter>> retrieveChaptersOrdered(@MustNotBeUnknown @NotNull Locale language) {
        if (state == State.DRAFT) throw new UnsupportedOperationException("Cannot retrieve chapters for a draft manga.");
        return retrieveChaptersIds(language).thenComposeAsync(ids -> {
            if (ids == null) return CompletableFuture.completedFuture(null);

            List<CompletableFuture<Chapter>> chapterFutures = ids.stream().map(id ->
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/chapter/" + id), JsonObject.class);
                            return new Chapter(response.getAsJsonObject("data"));
                        } catch (HTTPRequestException e) {
                            logger.error("An error occurred while attempting to retrieve chapter " + id, e);
                            throw new CompletionException(e);
                        }
                    }, Executors.newCachedThreadPool())
            ).collect(Collectors.toList());

            return CompletableFuture.allOf(chapterFutures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> chapterFutures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()));
        });
    }

    public CompletableFuture<List<Chapter>> retrieveChapterByNumber(@NotNull Locale lang, @NotLessThanOne int number) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Chapter> chapters = new ArrayList<>();
                String apiCall = "https://api.mangadex.org/chapter?limit=100&offset=0&manga=" + id + "&translatedLanguage[]=" + lang.getLanguage() + "&chapter=" + number;
                JsonObject response = gson.fromJson(HTTPRequest.get(apiCall), JsonObject.class);
                if (isError(response)) return null;
                JsonArray chapterArray = response.getAsJsonArray("data");
                if (chapterArray.size() == 0) logger.warn("No chapters found for manga " + id + " with number " + number + " in language " + lang.getLanguage() + ".");
                for (JsonElement chapterElement : chapterArray) {
                    chapters.add(new Chapter(chapterElement.getAsJsonObject()));
                }
                return chapters;
            } catch (HTTPRequestException e) {
                logger.error("Could not retrieve chapter " + number + " for manga " + id, e);
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<CoverProxy> retrieveCurrentCover() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String coverId = relationshipMap.get(RelationshipType.COVER_ART).get(0).getId().toString();
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/cover/" + coverId), JsonObject.class);
                return new CoverProxy(response.getAsJsonObject("data"));
            } catch (HTTPRequestException e) {
                logger.error("Could not retrieve all covers for manga " + id, e);
                throw new CompletionException(e);
            }
        });
    }

    private static boolean isError(JsonObject response) {
        return response.get("result").getAsString().equals("error");
    }

    private static List<UUID> parseIds(JsonObject jsonObject) {
        List<UUID> uuids = new ArrayList<>();
        JsonObject volumes = jsonObject.getAsJsonObject("volumes");

        for (Map.Entry<String, JsonElement> volumeEntry : volumes.entrySet()) {
            JsonObject chapters = volumeEntry.getValue().getAsJsonObject().getAsJsonObject("chapters");
            for (Map.Entry<String, JsonElement> chapterEntry : chapters.entrySet()) {
                String id = chapterEntry.getValue().getAsJsonObject().get("id").getAsString();
                uuids.add(UUID.fromString(id));
            }
        }
        return uuids;
    }

    @Override
    public String toString() {
        return "Manga{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", altTitles=" + altTitles +
                ", isLocked=" + isLocked +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", lastVolume='" + lastVolume + '\'' +
                ", lastChapter='" + lastChapter + '\'' +
                ", publicationDemographic=" + publicationDemographic +
                ", status=" + status +
                ", year=" + year +
                ", contentRating=" + contentRating +
                ", state='" + state + '\'' +
                ", chapterNumbersResetOnNewVolume=" + chapterNumbersResetOnNewVolume +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", version=" + version +
                ", availableTranslatedLanguages=" + availableTranslatedLanguages +
                ", latestUploadedChapterId='" + latestUploadedChapterId + '\'' +
                ", relationshipMap=" + relationshipMap +
                '}';
    }
}
