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
import dev.kurumidisciples.javadex.api.entities.enums.IncludesType;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Tag;
import dev.kurumidisciples.javadex.internal.actions.Action;
import dev.kurumidisciples.javadex.api.entities.enums.State;
import dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown;

/**
 * <p>MangaCreation class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
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

    /**
     * <p>Constructor for MangaCreation.</p>
     *
     * @param locale a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param title a {@link java.lang.String} object
     */
    public MangaCreation(@MustNotBeUnknown Locale locale, String title) {
        this.titles.put(locale, List.of(title));
        //TODO: Implement Token
    }


    /**
     * <p>Constructor for MangaCreation.</p>
     *
     * @param titles a {@link java.util.Map} object
     */
    public MangaCreation(Map<Locale, List<String>> titles) {
        this.titles = titles;
        this.authorization = Token.getInstance();
    }

    
    /**
     * <p>addAltTitle.</p>
     *
     * @param locale a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param title a {@link java.lang.String} object
     * @return MangaCreation
     */
    public MangaCreation addAltTitle(@MustNotBeUnknown Locale locale, String title) {
        if (this.altTitles.containsKey(locale)) {
            this.altTitles.get(locale).add(title);
        } else {
            this.altTitles.put(locale, List.of(title));
        }
        return this;
    }

    
    /**
     * <p>Setter for the field <code>altTitles</code>.</p>
     *
     * @param altTitles a {@link java.util.Map} object
     * @return MangaCreation
     */
    public MangaCreation setAltTitles(Map<@MustNotBeUnknown Locale, List<String>> altTitles) {
        this.altTitles = altTitles;
        return this;
    }

    
    /**
     * <p>addDescription.</p>
     *
     * @param locale a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param description a {@link java.lang.String} object
     * @return MangaCreation
     */
    public MangaCreation addDescription(@MustNotBeUnknown Locale locale, String description) {
        this.descriptions.put(locale, description);
        return this;
    }

    
    /**
     * <p>Setter for the field <code>descriptions</code>.</p>
     *
     * @param descriptions a {@link java.util.Map} object
     * @return MangaCreation
     */
    public MangaCreation setDescriptions(Map<@MustNotBeUnknown Locale, String> descriptions) {
        this.descriptions = descriptions;
        return this;
    }

    /**
     * <p>addAuthor.</p>
     *
     * @param author a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addAuthor(String author) {
        this.authors.add(author);
        return this;
    }

    /**
     * <p>Setter for the field <code>authors</code>.</p>
     *
     * @param authors a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    /**
     * <p>addAuthor.</p>
     *
     * @param authorId a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addAuthor(UUID authorId) {
       return addAuthor(authorId.toString());
    }

    /**
     * <p>addAuthor.</p>
     *
     * @param author a {@link dev.kurumidisciples.javadex.api.entities.Author} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addAuthor(Author author) {
        return addAuthor(author.getId());
    }


    /**
     * <p>addArtist.</p>
     *
     * @param artist a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addArtist(String artist) {
        this.artists.add(artist);
        return this;
    }

    /**
     * <p>Setter for the field <code>artists</code>.</p>
     *
     * @param artists a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setArtists(List<String> artists) {
        this.artists = artists;
        return this;
    }

    /**
     * <p>addArtist.</p>
     *
     * @param artistId a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addArtist(UUID artistId) {
        return addArtist(artistId.toString());
    }

    /**
     * <p>addArtist.</p>
     *
     * @param artist a {@link dev.kurumidisciples.javadex.api.entities.Author} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addArtist(Author artist) {
        return addArtist(artist.getId());
    }

    /**
     * <p>addLink.</p>
     *
     * @param type a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType} object
     * @param link a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addLink(@MustNotBeUnknown LinkType type, @NotNull String link) {
        this.links.put(type, link);
        return this;
    }

    /**
     * <p>Setter for the field <code>links</code>.</p>
     *
     * @param links a {@link java.util.Map} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setLinks(Map<@MustNotBeUnknown LinkType, String> links) {
        this.links = links;
        return this;
    }

    /**
     * <p>addTag.</p>
     *
     * @param tag a {@link dev.kurumidisciples.javadex.api.entities.content.Manga.Tag} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    /**
     * <p>Setter for the field <code>tags</code>.</p>
     *
     * @param tags a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * <p>Setter for the field <code>year</code>.</p>
     *
     * @param year a int
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setYear(int year) {
        this.year = year;
        return this;
    }

    /**
     * <p>Setter for the field <code>originalLanguage</code>.</p>
     *
     * @param originalLanguage a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setOriginalLanguage(@MustNotBeUnknown Locale originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    /**
     * <p>Setter for the field <code>contentRating</code>.</p>
     *
     * @param contentRating a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setContentRating(ContentRating contentRating) {
        this.contentRating = contentRating;
        return this;
    }

    /**
     * <p>Setter for the field <code>lastVolume</code>.</p>
     *
     * @param lastVolume a {@link java.lang.Number} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setLastVolume(Number lastVolume) {
        this.lastVolume = lastVolume;
        return this;
    }

    /**
     * <p>Setter for the field <code>lastChapter</code>.</p>
     *
     * @param lastChapter a {@link java.lang.Number} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setLastChapter(Number lastChapter) {
        this.lastChapter = lastChapter;
        return this;
    }

    /**
     * <p>Setter for the field <code>demographic</code>.</p>
     *
     * @param demographic a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setDemographic(Demographic demographic) {
        this.demographic = demographic;
        return this;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setStatus(Status status) {
        this.status = status;
        return this;
    }


    

    /** {@inheritDoc} 
     * 
     * @return will return a manga object in the {@link State.DRAFT} state if successful.
    */
    @Override
    public CompletableFuture<Manga> submit(){
        CompletableFuture<Manga> future = new CompletableFuture<>();
        future.completeExceptionally(new UnsupportedOperationException("This action is not yet supported."));
        return future;
    }

    /** {@inheritDoc} 
     * 
     * @return will return a manga object in the {@link State.DRAFT} state if successful.
    */
    @Override
    public Manga complete() {
        throw new UnsupportedOperationException("This action is not yet supported.");
    }










    /** {@inheritDoc} */
    @Override
    public MangaCreation setLimit(Integer limit) {
        throw new UnsupportedOperationException("This action does not support setting a limit.");
    }

    /** {@inheritDoc} */
    @Override
    public MangaCreation setOffset(Integer offset) {
        throw new UnsupportedOperationException("This action does not support setting an offset.");
    }

    /** {@inheritDoc} */
    @Override
    public MangaCreation setIncludes(IncludesType... includes) {
        throw new UnsupportedOperationException("This action does not support setting includes.");
    }



}
