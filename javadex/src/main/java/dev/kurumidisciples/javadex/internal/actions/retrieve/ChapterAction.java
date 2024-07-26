package dev.kurumidisciples.javadex.internal.actions.retrieve;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.IncludesType;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.actions.Action;
import dev.kurumidisciples.javadex.internal.annotations.Size;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;

/**
 * <p>ChapterAction is responsible for handling GET chapter endpoints on the MangaDex platform.</p> 
 * @apiNote See <a href="https://api.mangadex.org/docs/swagger.html#/Chapter/get-chapter">/chapter</a>
 */
public class ChapterAction extends Action<List<Chapter>> {
    
    private Integer limit;
    private Integer offset;
    private String uploaderId;
    private IncludesType[] includes;
    private String mangaId;
    private String title;
    private Short chapter;

    private List<String> groupIds;
    private List<ContentRating> contentRating;
    private List<Integer> volume;
    private List<String> chapterIds;
    private List<Locale> translatedLanguages;
    private List<Locale> originalLanguage;
    private List<Locale> excludedOriginalLanguages;
    private List<String> excludedGroupIds;

    private boolean includeFutureUpdates;
    private boolean includeEmptyPages;
    private boolean includeFuturePublishAt;
    private boolean includeExternalUrl;

    private OffsetDateTime createdAtSince;
    private OffsetDateTime updatedAtSince;
    private OffsetDateTime publishAtSince;

    private final static IncludesType[] AVAILABLE_INCLUDES  = new IncludesType[]{
            IncludesType.MANGA,
            IncludesType.SCANLATION_GROUP,
            IncludesType.USER
    };

    private final static String API_ENDPOINT = "https://api.mangadex.org/chapter";

    private final static Logger logger = LogManager.getLogger(ChapterAction.class);

    public ChapterAction() {
        this.limit = 10;
        this.offset = 0;
    }

    public ChapterAction addCreatedAtSince(@NotNull OffsetDateTime createdAtSince){
        this.createdAtSince = createdAtSince;
        return this;
    }

    public ChapterAction addUpdatedAtSince(@NotNull OffsetDateTime updatedAtSince){
        this.updatedAtSince = updatedAtSince;
        return this;
    }

    public ChapterAction addPublishAtSince(@NotNull OffsetDateTime publishAtSince){
        this.publishAtSince = publishAtSince;
        return this;
    }

    public ChapterAction includeFutureUpdates(boolean includeFutureUpdates){
        this.includeFutureUpdates = includeFutureUpdates;
        return this;
    }

    public ChapterAction setChapter(Short chapter){
        this.chapter = chapter;
        return this;
    }

    public ChapterAction includeEmptyPages(boolean includeEmptyPages){
        this.includeEmptyPages = includeEmptyPages;
        return this;
    }

    public ChapterAction includeFuturePublishAt(boolean includeFuturePublishAt){
        this.includeFuturePublishAt = includeFuturePublishAt;
        return this;
    }

    public ChapterAction includeExternalUrl(boolean includeExternalUrl){
        this.includeExternalUrl = includeExternalUrl;
        return this;
    }

    public ChapterAction addExcludedGroup(@NotNull UUID groupId){
        if (!this.excludedGroupIds.contains(groupId.toString())) this.excludedGroupIds.add(groupId.toString());
        return this;
    }

    public ChapterAction addExcludedGroup(@NotNull String groupId){
        if (!this.excludedGroupIds.contains(groupId)) this.excludedGroupIds.add(groupId);
        return this;
    }

    public ChapterAction addExcludedOriginalLanguage(@NotNull Locale language){
        if (!this.excludedOriginalLanguages.contains(language)) this.excludedOriginalLanguages.add(language);
        return this;
    }

    public ChapterAction addExcludedOriginalLanguages(@NotNull List<Locale> languages){
        languages.forEach(language -> {
            if (!this.excludedOriginalLanguages.contains(language)) this.excludedOriginalLanguages.add(language);
        });
        return this;
    }

    public ChapterAction addOriginalLanguage(@NotNull Locale language){
        if (!this.originalLanguage.contains(language)) this.originalLanguage.add(language);
        return this;
    }

    public ChapterAction addOriginalLanguages(@NotNull List<Locale> languages){
        languages.forEach(language -> {
            if (!this.originalLanguage.contains(language)) this.originalLanguage.add(language);
        });
        return this;
    }

    public ChapterAction addTranslatedLanguage(@NotNull Locale language){
        if (!this.translatedLanguages.contains(language)) this.translatedLanguages.add(language);
        return this;
    }

    public ChapterAction addTranslatedLanguages(@NotNull List<Locale> languages){
        languages.forEach(language -> {
            if (!this.translatedLanguages.contains(language)) this.translatedLanguages.add(language);
        });
        return this;
    }

    public ChapterAction addChapter(@NotNull UUID chapterId){
        if (!this.chapterIds.contains(chapterId.toString())) this.chapterIds.add(chapterId.toString());
        return this;
    }

    public ChapterAction addChapter(@NotNull String chapterId){
        if (!this.chapterIds.contains(chapterId)) this.chapterIds.add(chapterId);
        return this;
    }

    public ChapterAction addChapters(@NotNull List<String> chapterIds){
        chapterIds.forEach(chapterId -> {
            if (!this.chapterIds.contains(chapterId)) this.chapterIds.add(chapterId);
        });
        return this;
    }

    public ChapterAction addVolume(@NotNull Integer volume){
        if (!this.volume.contains(volume)) this.volume.add(volume);
        return this;
    }

    public ChapterAction addVolumes(@NotNull List<Integer> volumes){
        volumes.forEach(volume -> {
            if (!this.volume.contains(volume)) this.volume.add(volume);
        });
        return this;
    }

    public ChapterAction addContentRating(@NotNull ContentRating rating){
        if (!this.contentRating.contains(rating)) this.contentRating.add(rating);
        return this;
    }

    public ChapterAction addContentRatings(@NotNull List<ContentRating> ratings){
        ratings.forEach(rating -> {
            if (!this.contentRating.contains(rating)) this.contentRating.add(rating);
        });
        return this;
    }

    public ChapterAction addGroup(@NotNull UUID groupId){
        this.groupIds.add(groupId.toString());
        return this;
    }

    public ChapterAction addGroup(@NotNull String groupId){
        this.groupIds.add(groupId);
        return this;
    }

    public ChapterAction addGroup(@NotNull ScanlationGroup group){
        this.groupIds.add(group.getId().toString());
        return this;
    }

    public ChapterAction addGroups(@NotNull List<String> groupIds){
        this.groupIds.addAll(groupIds);
        return this;
    }

    public ChapterAction setMangaId(@NotNull UUID mangaId){
        this.mangaId = mangaId.toString();
        return this;
    }

    /**
     * Sets the manga id to search for.
     * @param manga the manga object thats contains the id.
     */
    public ChapterAction setMangaId(@NotNull Manga manga){
        this.mangaId = manga.getId().toString();
        return this;
    }
    /**
     * Sets the title of the chapter to search for.
     * @param title the title of the chapter.
     */
    public ChapterAction setTitle(@NotNull String title){
        this.title = title;
        return this;
    }

    @Override
    public ChapterAction setLimit(@Size(min=0, max=100) Integer limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public ChapterAction setOffset(@Size Integer offset) {
        if (offset < 0) throw new IllegalArgumentException("Offset must be greater than 0");
        this.offset = offset;
        return this;
    }

    @Override
    public ChapterAction setIncludes(IncludesType... includes) {
        for (IncludesType include : includes) {
            if (!isIncludeAvailable(include)) throw new IllegalArgumentException("Include \'" + include.getType() + "\' is not available for chapters");
        }
        this.includes = includes;
        return this;
    }

    private static boolean isIncludeAvailable(IncludesType include) {
        for (IncludesType availableInclude : AVAILABLE_INCLUDES) {
            if (availableInclude == include) return true;
        }
        return false;
    }

    public ChapterAction setUploader(@NotNull UUID uploader) {
        this.uploaderId = uploader.toString();
        return this;
    }

    public ChapterAction setUploader(@NotNull User uploader){
        this.uploaderId = uploader.getId().toString();
        return this;
    }

    public ChapterAction setUploader(@NotNull String uploader){
        this.uploaderId = uploader;
        return this;
    }

    @Override
    public List<Chapter> complete() throws HTTPRequestException{
        String response = HTTPRequest.get(API_ENDPOINT + toQuery());
        JsonObject chapterResponse = JsonParser.parseString(response).getAsJsonObject();
        List<Chapter> chapters = new ArrayList<>();
        chapterResponse.getAsJsonArray("data").forEach(chapter -> {
            chapters.add(new Chapter(chapter.getAsJsonObject()));
        });
        return chapters;
    }

    @Override
    public CompletableFuture<List<Chapter>> submit() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return complete();
            } catch (HTTPRequestException e) {
                logger.error("An error occurred while submitting the action: " + e.getMessage(), e);
                return null;
            }
        });
    }
    //TODO fix this
    public String toQuery(){
        StringBuilder query = new StringBuilder("?");
        query.append("limit=").append(limit).append("&");
        query.append("offset=").append(offset).append("&");
        if (uploaderId != null) query.append("uploader=").append(uploaderId).append("&");
        if (includes != null) query.append(arrayToQuery("includes[]", Arrays.asList(includes)));
        if (mangaId != null) query.append("manga=").append(mangaId).append("&");
        if (title != null) query.append("title=").append(title).append("&");
        if (chapter != null) query.append("chapter=").append(chapter).append("&");
        if (groupIds != null) query.append(arrayToQuery("groups[]", groupIds));
        if (contentRating != null) query.append(arrayToQuery("contentRating[]", contentRating));
        if (volume != null) query.append(arrayToQuery("volume[]", volume));
        if (chapterIds != null) query.append(arrayToQuery("chapter[]", chapterIds));
        if (translatedLanguages != null) query.append(arrayToQuery("translatedLanguage[]", translatedLanguages));
        if (originalLanguage != null) query.append(arrayToQuery("originalLanguage[]", originalLanguage));
        if (excludedOriginalLanguages != null) query.append(arrayToQuery("excludedOriginalLanguage[]", excludedOriginalLanguages));
        if (excludedGroupIds != null) query.append(arrayToQuery("excludedGroups[]", excludedGroupIds));
        query.append("includeFutureUpdates=").append(toInteger(includeFutureUpdates)).append("&");
        query.append("includeEmptyPages=").append(toInteger(includeEmptyPages)).append("&");
        query.append("includeFuturePublishAt=").append(toInteger(includeFuturePublishAt)).append("&");
        query.append("includeExternalUrl=").append(toInteger(includeExternalUrl)).append("&");
        if (createdAtSince != null) query.append("createdAtSince=").append(createdAtSince.toString()).append("&");
        if (updatedAtSince != null) query.append("updatedAtSince=").append(updatedAtSince.toString()).append("&");
        if (publishAtSince != null) query.append("publishAtSince=").append(publishAtSince.toString()).append("&");
        return query.toString();
    }

    private Integer toInteger(boolean bool){
        return bool ? 1 : 0;
    }

    private String arrayToQuery(String key, List<?> list){
        StringBuilder query = new StringBuilder();
        list.forEach(item -> {
            query.append(key).append("=").append(item.toString()).append("&");
        });
        return query.toString();
    }
}
