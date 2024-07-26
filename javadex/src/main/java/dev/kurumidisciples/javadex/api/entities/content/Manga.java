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
import dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType;
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
 *
 * @see Can represent any readable content in MangaDex, encapsulating manga, manhwa, manhua, and doujinshi.
 * @see <a href="https://api.mangadex.org/docs/03-manga/creation/#creation">Creation</a>
 * @see <a href="https://api.mangadex.org/docs/03-manga/search/">Search</a>
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class Manga extends Entity implements ISnowflake {

    /**
     * Enum representing various tags used for categorizing manga.
     */
    public enum Tag {

        /**
         * Tag for one-shot manga.
         */
        ONESHOT("Oneshot", UUID.fromString("0234a31e-a729-4e28-9d6a-3f87c4966b9e")),

        /**
         * Tag for thriller genre manga.
         */
        THRILLER("Thriller", UUID.fromString("07251805-a27e-4d59-b488-f0bfbec15168")),

        /**
         * Tag for award-winning manga.
         */
        AWARD_WINNING("Award Winning", UUID.fromString("0a39b5a1-b235-4886-a747-1d05d216532d")),

        /**
         * Tag for reincarnation-themed manga.
         */
        REINCARNATION("Reincarnation", UUID.fromString("0bc90acb-ccc1-44ca-a34a-b9f3a73259d0")),

        /**
         * Tag for science fiction genre manga.
         */
        SCI_FI("Sci-Fi", UUID.fromString("256c8bd9-4904-4360-bf4f-508a76d67183")),

        /**
         * Tag for time travel-themed manga.
         */
        TIME_TRAVEL("Time Travel", UUID.fromString("292e862b-2d17-4062-90a2-0356caa4ae27")),

        /**
         * Tag for gender-swap-themed manga.
         */
        GENDERSWAP("Genderswap", UUID.fromString("2bd2e8d0-f146-434a-9b51-fc9ff2c5fe6a")),

        /**
         * Tag for manga about traditional games.
         */
        TRADITIONAL_GAMES("Traditional Games", UUID.fromString("31932a7e-5b8e-49a6-9f12-2afa39dc544c")),

        /**
         * Tag for officially colored manga.
         */
        OFFICIAL_COLORED("Official Colored", UUID.fromString("320831a8-4026-470b-94f6-8353740e6f04")),

        /**
         * Tag for historical genre manga.
         */
        HISTORICAL("Historical", UUID.fromString("33771934-028e-4cb3-8744-691e866a923e")),

        /**
         * Tag for manga featuring monsters.
         */
        MONSTERS("Monsters", UUID.fromString("36fd93ea-e8b8-445e-b836-358f02b3d33d")),

        /**
         * Tag for action genre manga.
         */
        ACTION("Action", UUID.fromString("391b0423-d847-456f-aff0-8b0cfc03066b")),

        /**
         * Tag for manga featuring demons.
         */
        DEMONS("Demons", UUID.fromString("39730448-9a5f-48a2-85b0-a70db87b1233")),

        /**
         * Tag for psychological genre manga.
         */
        PSYCHOLOGICAL("Psychological", UUID.fromString("3b60b75c-a2d7-4860-ab56-05f391bb889c")),

        /**
         * Tag for manga featuring ghosts.
         */
        GHOSTS("Ghosts", UUID.fromString("3bb26d85-09d5-4d2e-880c-c34b974339e9")),

        /**
         * Tag for manga featuring animals.
         */
        ANIMALS("Animals", UUID.fromString("3de8c75d-8ee3-48ff-98ee-e20a65c86451")),

        /**
         * Tag for long strip format manga.
         */
        LONG_STRIP("Long Strip", UUID.fromString("3e2b8dae-350e-4ab8-a8ce-016e844b9f0d")),

        /**
         * Tag for romance genre manga.
         */
        ROMANCE("Romance", UUID.fromString("423e2eae-a7a2-4a8b-ac03-a8351462d71d")),

        /**
         * Tag for manga featuring ninjas.
         */
        NINJA("Ninja", UUID.fromString("489dd859-9b61-4c37-af75-5b18e88daafc")),

        /**
         * Tag for comedy genre manga.
         */
        COMEDY("Comedy", UUID.fromString("4d32cc48-9f00-4cca-9b5a-a839f0764984")),

        /**
         * Tag for mecha genre manga.
         */
        MECHA("Mecha", UUID.fromString("50880a9d-5440-4732-9afb-8f457127e836")),

        /**
         * Tag for anthology format manga.
         */
        ANTHOLOGY("Anthology", UUID.fromString("51d83883-4103-437c-b4b1-731cb73d786c")),

        /**
         * Tag for boys' love genre manga.
         */
        BOYS_LOVE("Boys' Love", UUID.fromString("5920b825-4181-4a17-beeb-9918b0ff7a30")),

        /**
         * Tag for manga featuring incest themes.
         */
        INCEST("Incest", UUID.fromString("5bd0e105-4481-44ca-b6e7-7544da56b1a3")),

        /**
         * Tag for crime genre manga.
         */
        CRIME("Crime", UUID.fromString("5ca48985-9a9d-4bd8-be29-80dc0303db72")),

        /**
         * Tag for survival genre manga.
         */
        SURVIVAL("Survival", UUID.fromString("5fff9cde-849c-4d78-aab0-0d52b2ee1d25")),

        /**
         * Tag for manga featuring zombies.
         */
        ZOMBIES("Zombies", UUID.fromString("631ef465-9aba-4afb-b0fc-ea10efe274a8")),

        /**
         * Tag for reverse harem genre manga.
         */
        REVERSE_HAREM("Reverse Harem", UUID.fromString("65761a2a-415e-47f3-bef2-a9dababba7a6")),

        /**
         * Tag for sports genre manga.
         */
        SPORTS("Sports", UUID.fromString("69964a64-2f90-4d33-beeb-f3ed2875eb4c")),

        /**
         * Tag for superhero genre manga.
         */
        SUPERHERO("Superhero", UUID.fromString("7064a261-a137-4d3a-8848-2d385de3a99c")),

        /**
         * Tag for martial arts genre manga.
         */
        MARTIAL_ARTS("Martial Arts", UUID.fromString("799c202e-7daa-44eb-9cf7-8a3c0441531e")),

        /**
         * Tag for fan-colored manga.
         */
        FAN_COLORED("Fan Colored", UUID.fromString("7b2ce280-79ef-4c09-9b58-12b7c23a9b78")),

        /**
         * Tag for manga featuring samurais.
         */
        SAMURAI("Samurai", UUID.fromString("81183756-1453-4c81-aa9e-f6e1b63be016")),

        /**
         * Tag for magical girls genre manga.
         */
        MAGICAL_GIRLS("Magical Girls", UUID.fromString("81c836c9-914a-4eca-981a-560dad663e73")),

        /**
         * Tag for mafia genre manga.
         */
        MAFIA("Mafia", UUID.fromString("85daba54-a71c-4554-8a28-9901a8b0afad")),

        /**
         * Tag for adventure genre manga.
         */
        ADVENTURE("Adventure", UUID.fromString("87cc87cd-a395-47af-b27a-93258283bbc6")),

        /**
         * Tag for girls' love genre manga.
         */
        GIRLS_LOVE("Girls' Love", UUID.fromString("a3c67850-4684-404e-9b7f-c69850ee5da6")),

        /**
         * Tag for gore genre manga.
         */
        GORE("Gore", UUID.fromString("b29d6a3d-1569-4e7a-8caf-7557bc92cd5d")),

        /**
         * Tag for medical genre manga.
         */
        MEDICAL("Medical", UUID.fromString("c8cbe35b-1b2b-4a3f-9c37-db84c4514856")),

        /**
         * Tag for school life genre manga.
         */
        SCHOOL_LIFE("School Life", UUID.fromString("caaa44eb-cd40-4177-b930-79d3ef2afe87")),

        /**
         * Tag for horror genre manga.
         */
        HORROR("Horror", UUID.fromString("cdad7e68-1419-41dd-bdce-27753074a640")),

        /**
         * Tag for fantasy genre manga.
         */
        FANTASY("Fantasy", UUID.fromString("cdc58593-87dd-415e-bbc0-2ec27bf404cc")),

        /**
         * Tag for manga featuring villainesses.
         */
        VILLAINESS("Villainess", UUID.fromString("d14322ac-4d6f-4e9b-afd9-629d5f4d8a41")),

        /**
         * Tag for manga featuring vampires.
         */
        VAMPIRES("Vampires", UUID.fromString("d7d1730f-6eb0-4ba6-9437-602cac38664c")),

        /**
         * Tag for manga featuring delinquents.
         */
        DELINQUENTS("Delinquents", UUID.fromString("da2d50ca-3018-4cc0-ac7a-6b7d472a29ea")),

        /**
         * Tag for manga featuring monster girls.
         */
        MONSTER_GIRLS("Monster Girls", UUID.fromString("dd1f77c5-dea9-4e2b-97ae-224af09caf99")),

        /**
         * Tag for police genre manga.
         */
        POLICE("Police", UUID.fromString("df33b754-73a3-4c54-80e6-1a74a8058539")),

        /**
         * Tag for web comic format manga.
         */
        WEB_COMIC("Web Comic", UUID.fromString("e197df38-d0e7-43b5-9b09-2842d0c326dd")),

        /**
         * Tag for slice of life genre manga.
         */
        SLICE_OF_LIFE("Slice of Life", UUID.fromString("e5301a23-ebd9-49dd-a0cb-2add944c7fe9")),

        /**
         * Tag for manga featuring aliens.
         */
        ALIENS("Aliens", UUID.fromString("e64f6742-c834-471d-8d72-dd51fc02b835")),

        /**
         * Tag for cooking genre manga.
         */
        COOKING("Cooking", UUID.fromString("ea2bc92d-1c26-4930-9b7c-d5c0dc1b6869")),

        /**
         * Tag for supernatural genre manga.
         */
        SUPERNATURAL("Supernatural", UUID.fromString("eabc5b4c-6aff-42f3-b657-3e90cbd00b75")),

        /**
         * Tag for mystery genre manga.
         */
        MYSTERY("Mystery", UUID.fromString("ee968100-4191-4968-93d3-f82d72be7e46")),

        /**
         * Tag for manga adaptations.
         */
        ADAPTATION("Adaptation", UUID.fromString("f4122d1c-3b44-44d0-9936-ff7502c39ad3")),

        /**
         * Tag for music genre manga.
         */
        MUSIC("Music", UUID.fromString("f42fbf9e-188a-447b-9fdc-f19dc1e4d685")),

        /**
         * Tag for full-color manga.
         */
        FULL_COLOR("Full Color", UUID.fromString("f5ba408b-0e7a-484d-8d49-4e9125ac96de")),

        /**
         * Tag for tragedy genre manga.
         */
        TRAGEDY("Tragedy", UUID.fromString("f8f62932-27da-4fe4-8ee1-6779a8c5edba")),

        /**
         * Tag for gyaru genre manga.
         */
        GYARU("Gyaru", UUID.fromString("fad12b5e-68ba-460e-b933-9ae8318f5b65")),

        /**
         * Tag for isekai genre manga.
         */
        ISEKAI("Isekai", UUID.fromString("ace04997-f6bd-436e-b261-779182193d3d")),

        /**
         * Tag for military genre manga.
         */
        MILITARY("Military", UUID.fromString("ac72833b-c4e9-4878-b9db-6c8a4a99444a")),

        /**
         * Tag for philosophical genre manga.
         */
        PHILOSOPHICAL("Philosophical", UUID.fromString("b1e97889-25b4-4258-b28b-cd7f4d28ea9b")),

        /**
         * Tag for virtual reality-themed manga.
         */
        VIRTUAL_REALITY("Virtual Reality", UUID.fromString("8c86611e-fab7-4986-9dec-d1a2f44acdd5")),

        /**
         * Tag for manga featuring office workers.
         */
        OFFICE_WORKERS("Office Workers", UUID.fromString("92d6d951-ca5e-429c-ac78-451071cbf064")),

        /**
         * Tag for video games genre manga.
         */
        VIDEO_GAMES("Video Games", UUID.fromString("9438db5a-7e2a-4ac0-b39e-e0d95a34b8a8")),

        /**
         * Tag for post-apocalyptic genre manga.
         */
        POST_APOCALYPTIC("Post-Apocalyptic", UUID.fromString("9467335a-1b83-4497-9231-765337a00b96")),

        /**
         * Tag for manga featuring sexual violence.
         */
        SEXUAL_VIOLENCE("Sexual Violence", UUID.fromString("97893a4c-12af-4dac-b6be-0dffb353568e")),

        /**
         * Tag for cross-dressing-themed manga.
         */
        CROSSDRESSING("Crossdressing", UUID.fromString("9ab53f92-3eed-4e9b-903a-917c86035ee3")),

        /**
         * Tag for magic genre manga.
         */
        MAGIC("Magic", UUID.fromString("a1f53773-c69a-4ce5-8cab-fffcd90b1565")),

        /**
         * Tag for self-published manga.
         */
        SELF_PUBLISHED("Self-Published", UUID.fromString("891cf039-b895-47f0-9229-bef4c96eccd4")),

        /**
         * Tag for wuxia genre manga.
         */
        WUXIA("Wuxia", UUID.fromString("acc803a4-c95a-4c22-86fc-eb6b582d82a2")),

        /**
         * Tag for doujinshi format manga.
         */
        DOUJINSHI("Doujinshi", UUID.fromString("b13b2a48-c720-44a9-9c77-39c9979373fb"));
    
        private final String name;
        private final UUID id;
    
        Tag(String name, UUID id) {
        this.name = name;
        this.id = id;
        }
    
        /**
         * Gets the name of the tag.
         *
         * @return the name of the tag
         */
        public String getName() {
        return name;
        }
    
        /**
         * Gets the UUID of the tag.
         *
         * @return the UUID of the tag
         */
        public UUID getId() {
        return id;
        }
    
        /**
         * Gets the Tag enum by its name.
         *
         * @param name the name of the tag
         * @return the Tag enum, or null if not found
         */
        public static Tag getByName(String name) {
        for (Tag tag : Tag.values()) {
            if (tag.getName().equalsIgnoreCase(name)) {
            return tag;
            }
        }
        return null;
        }
    
        /**
         * Gets the Tag enum by its UUID.
         *
         * @param id the UUID of the tag
         * @return the Tag enum, or null if not found
         */
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

    private final Map<LinkType, String> links;
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

    /**
     * <p>Constructor for Manga.</p>
     *
     * @param mangaJson a {@link com.google.gson.JsonObject} object
     */
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
        this.links = MangaParsers.parseLinks(attributes.getAsJsonObject("links"));
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

    /**
     * <p>Getter for the field <code>relationshipMap</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap} object
     */
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    /**
     * <p>doChapterNumbersResetOnNewVolume.</p>
     *
     * @return a boolean
     */
    public boolean doChapterNumbersResetOnNewVolume() {
        return chapterNumbersResetOnNewVolume;
    }

    /**
     * <p>Getter for the field <code>state</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.State} object
     */
    public State getState() {
        return state;
    }

    /**
     * <p>retrieveCovers.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param limit a int
     * @param offset a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /**
     * <p>retrieveCovers.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<CoverProxy>> retrieveCovers(Locale lang) {
        return retrieveCovers(lang, 100, 0);
    }

    /**
     * <p>retrieveCovers.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param limit a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<CoverProxy>> retrieveCovers(Locale lang, int limit) {
        return retrieveCovers(lang, limit, 0);
    }

    /** {@inheritDoc} */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * <p>getIdRaw.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIdRaw() {
        return id.toString();
    }

    /** {@inheritDoc} */
    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /** {@inheritDoc} */
    @Override
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * The title of the manga found in the <code>title</code> property of the api response. 
     * <p><prev><code>
     * "title": {
     *           "en": "One Piece"
     *      },</code></prev></p>
     * <p>This is typically in the English language or in Japanese romanization.</p>
     *
     * @return a {@link String} the default title of the manga
     */
    public String getDefaultTitle() {
        return title;
    }

    /**
     * <p>Links to outside resources connected with the manga.</p>
     * <p>These could be links to the official website, the author's website, or the manga's page on other websites.</p>
     * @return
     * @warn Not all links have the full URL, some are just the IDs or SLUGs of the resource.
     */
    public Map<LinkType, String> getLinks() {
        return links;
    }

    /**
     * <p>Getter for the field <code>author</code>.</p>
     *
     * @return a {@link java.util.UUID} object
     */
    public UUID getAuthor() {
        return author;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link java.lang.String} object
     */
    public String getDescription(Locale lang) {
        return description.get(lang);
    }

    /**
     * <p>getDescriptions.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<Locale, String> getDescriptions() {
        return description;
    }

    /**
     * <p>All other possible titles for the manga.</p>
     * <p>These could be in different languages and have different translations of the title.</p>
     *
     * @return a {@link Map} object
     */
    public Map<Locale, List<String>> getAltTitles() {
        return altTitles;
    }

    /**
     * <p>Defines whether the manga can be uploaded to by anyone.</p>
     * <p>Manga are typically locked because it has been requested by the official publisher.</p>
     *
     * @return true if the manga is locked, false otherwise
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * <p>Getter for the field <code>originalLanguage</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     */
    public Locale getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * <p>Getter for the field <code>lastVolume</code>.</p>
     *
     * @return a {@link java.lang.Number} object
     */
    public Number getLastVolume() {
        return lastVolume;
    }

    /**
     * <p>Getter for the field <code>lastChapter</code>.</p>
     *
     * @return a {@link java.lang.Number} object
     */
    public Number getLastChapter() {
        return lastChapter;
    }

    /**
     * <p>Getter for the field <code>contentRating</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating} object
     */
    public ContentRating getContentRating() {
        return contentRating;
    }

    /**
     * <p>Getter for the field <code>publicationDemographic</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic} object
     */
    public Demographic getPublicationDemographic() {
        return publicationDemographic;
    }

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status} object
     */
    public Status getStatus() {
        return status;
    }

    /**
     * <p>Getter for the field <code>year</code>.</p>
     *
     * @return a {@link java.lang.Long} object
     */
    public Long getYear() {
        return year;
    }

    /**
     * <p>Getter for the field <code>latestUploadedChapterId</code>.</p>
     *
     * @return a {@link java.util.UUID} object
     */
    public UUID getLatestUploadedChapterId() {
        return UUID.fromString(latestUploadedChapterId);
    }

    /**
     * <p>Getter for the field <code>tags</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * <p>retrieveFeed.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /**
     * <p>retrieveChapterCount.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /**
     * <p>retrieveChaptersIds.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /**
     * <p>retrieveChaptersOrdered.</p>
     *
     * @param language a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /**
     * <p>retrieveChapterByNumber.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param number a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /**
     * <p>retrieveCurrentCover.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /** {@inheritDoc} */
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
