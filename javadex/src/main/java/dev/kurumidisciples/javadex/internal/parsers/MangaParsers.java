package dev.kurumidisciples.javadex.internal.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.content.Manga.Tag;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType;

/**
 * <p>MangaParsers class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class MangaParsers {
    
    
    /**
     * <p>parseAvailableTranslatedLanguages.</p>
     *
     * @param availableTranslatedLanguagesArray a {@link com.google.gson.JsonArray} object
     * @return List of available translated languages.
     */
    public static List<String> parseAvailableTranslatedLanguages(JsonArray availableTranslatedLanguagesArray) {
        List<String> languages = new ArrayList<>();
        for (JsonElement langElement : availableTranslatedLanguagesArray) {
            if (!langElement.isJsonNull()) {
                languages.add(langElement.getAsString());
            }
        }
        return languages;
    }
    /**
     * <p>parseTags.</p>
     *
     * @param tagsArray a {@link com.google.gson.JsonArray} object
     * @return List of tags.
     */
    public static List<Tag> parseTags(JsonArray tagsArray) {
        List<Tag> tags = new ArrayList<>();
        for (JsonElement tagElement : tagsArray) {
            JsonObject tagObject = tagElement.getAsJsonObject();
            String tagName = tagObject.get("id").getAsString();
            tags.add(Tag.getById(UUID.fromString(tagName)));
        }
        return tags;
    }

    /**
     * <p>parseAltTitles.</p>
     *
     * @param altTitlesArray a {@link com.google.gson.JsonArray} object
     * @return a {@link java.util.Map} object
     */
    public static Map<Locale, List<String>> parseAltTitles(JsonArray altTitlesArray) {
        Map<Locale, List<String>> altTitles = new HashMap<>();
        for (JsonElement altTitleElement : altTitlesArray) {
            JsonObject altTitleObject = altTitleElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : altTitleObject.entrySet()) {
                Locale locale = Locale.getByLanguage(entry.getKey());
                altTitles.computeIfAbsent(locale, k -> new ArrayList<>()).add(entry.getValue().getAsString());
            }
        }
        return altTitles;
    }

    /**
     * <p>parseDescription.</p>
     *
     * @param descriptionObject a {@link com.google.gson.JsonObject} object
     * @return a {@link java.util.Map} object
     */
    public static Map<Locale, String> parseDescription(JsonObject descriptionObject) {
        Map<Locale, String> descriptions = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : descriptionObject.entrySet()) {
            descriptions.put(Locale.getByLanguage(entry.getKey()), entry.getValue().getAsString());
        }
        return descriptions;
    }

    /**
     * Pareses links into a map of {@link LinkType} and {@link String}.
     * @param linksObject jsonobject of links.
     * @return Map of links.
     */
    public static Map<LinkType, String> parseLinks(JsonObject linksObject) {
        Map<LinkType, String> links = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : linksObject.entrySet()) {
            links.put(LinkType.getByType(entry.getKey()), entry.getValue().getAsString());
        }
        return links;
    }
}
