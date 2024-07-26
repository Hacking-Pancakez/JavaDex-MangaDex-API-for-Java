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

public class MangaParsers {
    
    public static List<String> parseAvailableTranslatedLanguages(JsonArray availableTranslatedLanguagesArray) {
        List<String> languages = new ArrayList<>();
        for (JsonElement langElement : availableTranslatedLanguagesArray) {
            languages.add(langElement.getAsString());
        }
        return languages;
    }

    public static List<Tag> parseTags(JsonArray tagsArray) {
        List<Tag> tags = new ArrayList<>();
        for (JsonElement tagElement : tagsArray) {
            JsonObject tagObject = tagElement.getAsJsonObject();
            String tagName = tagObject.get("id").getAsString();
            tags.add(Tag.getById(UUID.fromString(tagName)));
        }
        return tags;
    }

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

    public static Map<Locale, String> parseDescription(JsonObject descriptionObject) {
        Map<Locale, String> descriptions = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : descriptionObject.entrySet()) {
            descriptions.put(Locale.getByLanguage(entry.getKey()), entry.getValue().getAsString());
        }
        return descriptions;
    }
}
