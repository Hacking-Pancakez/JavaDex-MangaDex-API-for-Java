package dev.kurumidisciples.javadex.internal.actions.create;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import com.google.errorprone.annotations.DoNotCall;

import dev.kurumidisciples.javadex.api.core.authentication.Token;
import dev.kurumidisciples.javadex.api.entities.Author;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.content.Manga.Tag;
import dev.kurumidisciples.javadex.api.entities.enums.IncludesType;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status;
import dev.kurumidisciples.javadex.internal.actions.Action;
import dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown;

public class MangaCreation extends Action<Manga> {

    private Map<Locale, List<String>> titles;
    private Map<Locale, List<String>> altTitles;
    private Map<Locale, String> descriptions;
    private List<String> authors;
    private List<String> artists;
    private Map<LinkType, String> links;
    private List<Tag> tags;
    private int year;
    private Locale originalLanguage;
    private ContentRating contentRating;
    private Number lastVolume;
    private Number lastChapter;
    private Demographic demographic;
    private Status status;
    private final int version = 1;

    private Token authorization;

    public MangaCreation(@MustNotBeUnknown Locale locale, String title) {
        this.titles.put(locale, List.of(title));
        //TODO: Implement Token
    }


    public MangaCreation(Map<Locale, List<String>> titles) {
        this.titles = titles;
        this.authorization = Token.getInstance();
    }






    public MangaCreation addAltTitle(@MustNotBeUnknown Locale locale, String title) {
        if (this.altTitles.containsKey(locale)) {
            this.altTitles.get(locale).add(title);
        } else {
            this.altTitles.put(locale, List.of(title));
        }
        return this;
    }

    public MangaCreation setAltTitles(Map<@MustNotBeUnknown Locale, List<String>> altTitles) {
        this.altTitles = altTitles;
        return this;
    }

    public MangaCreation addDescription(@MustNotBeUnknown Locale locale, String description) {
        this.descriptions.put(locale, description);
        return this;
    }

    public MangaCreation setDescriptions(Map<@MustNotBeUnknown Locale, String> descriptions) {
        this.descriptions = descriptions;
        return this;
    }

    public MangaCreation addAuthor(String author) {
        this.authors.add(author);
        return this;
    }

    public MangaCreation setAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    public MangaCreation addAuthor(UUID authorId) {
       return addAuthor(authorId.toString());
    }

    public MangaCreation addAuthor(Author author) {
        return addAuthor(author.getId());
    }


    public MangaCreation addArtist(String artist) {
        this.artists.add(artist);
        return this;
    }

    public MangaCreation setArtists(List<String> artists) {
        this.artists = artists;
        return this;
    }

    public MangaCreation addArtist(UUID artistId) {
        return addArtist(artistId.toString());
    }

    public MangaCreation addArtist(Author artist) {
        return addArtist(artist.getId());
    }

    public MangaCreation addLink(@MustNotBeUnknown LinkType type, @NotNull String link) {
        this.links.put(type, link);
        return this;
    }

    public MangaCreation setLinks(Map<@MustNotBeUnknown LinkType, String> links) {
        this.links = links;
        return this;
    }

    public MangaCreation addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public MangaCreation setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public MangaCreation setYear(int year) {
        this.year = year;
        return this;
    }

    public MangaCreation setOriginalLanguage(@MustNotBeUnknown Locale originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    public MangaCreation setContentRating(ContentRating contentRating) {
        this.contentRating = contentRating;
        return this;
    }

    public MangaCreation setLastVolume(Number lastVolume) {
        this.lastVolume = lastVolume;
        return this;
    }

    public MangaCreation setLastChapter(Number lastChapter) {
        this.lastChapter = lastChapter;
        return this;
    }

    public MangaCreation setDemographic(Demographic demographic) {
        this.demographic = demographic;
        return this;
    }

    public MangaCreation setStatus(Status status) {
        this.status = status;
        return this;
    }


    

    @Override
    public CompletableFuture<Manga> submit(){
        CompletableFuture<Manga> future = new CompletableFuture<>();
        future.completeExceptionally(new UnsupportedOperationException("This action is not yet supported."));
        return future;
    }

    @Override
    public Manga complete() {
        throw new UnsupportedOperationException("This action is not yet supported.");
    }










    @DoNotCall
    @Override
    public MangaCreation setLimit(Integer limit) {
        throw new UnsupportedOperationException("This action does not support setting a limit.");
    }

    @DoNotCall
    @Override
    public MangaCreation setOffset(Integer offset) {
        throw new UnsupportedOperationException("This action does not support setting an offset.");
    }

    @DoNotCall
    @Override
    public MangaCreation setIncludes(IncludesType... includes) {
        throw new UnsupportedOperationException("This action does not support setting includes.");
    }



}
