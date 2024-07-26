package dev.kurumidisciples.javadex.api.entities.enums;

/**
 * Enum representing various locales using ISO 639 language codes.
 * This enum is used to specify the language of content within the application.
 * It includes a method to retrieve a {@code Locale} instance by its language code.
 */
public enum Locale {
    SIMPLIFIED_CHINESE("zh"), // Simplified Chinese
    TRADITIONAL_CHINESE("zh-hk"), // Traditional Chinese (Hong Kong)
    BRAZILIAN_PORTUGESE("pt-br"), // Brazilian Portuguese
    CASTILIAN_SPANISH("es"), // Castilian Spanish (Spain)
    LATIN_AMERICAN_SPANISH("es-la"), // Latin American Spanish
    ROMANIZED_JAPANESE("ja-ro"), // Romanized Japanese
    ROMANIZED_KOREAN("ko-ro"), // Romanized Korean
    ROMANIZED_CHINESE("zh-ro"), // Romanized Chinese
    ENGLISH("en"), // English
    JAPANESE("ja"), // Japanese
    KOREAN("ko"), // Korean
    SPANISH("es"), // Spanish
    FRENCH("fr"), // French
    GERMAN("de"), // German
    ITALIAN("it"), // Italian
    RUSSIAN("ru"), // Russian
    HINDI("hi"), // Hindi
    SWEDISH("sv"), // Swedish
    DUTCH("nl"), // Dutch
    PORTUGUESE("pt"), // Portuguese
    TURKISH("tr"), // Turkish
    GREEK("el"), // Greek
    UNKNOWN("unknown"); // Fallback for unspecified locales

    private final String language; // ISO 639 language code

    /**
     * Constructor for the enum constants.
     *
     * @param language The ISO 639 language code associated with the locale.
     */
    Locale(String language) {
        this.language = language;
    }

    /**
     * Retrieves the ISO 639 language code of the locale.
     *
     * @return The language code of the locale.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Retrieves a {@code Locale} instance by its ISO 639 language code.
     * If the language code does not match any locale, {@code NOT_SPECIFIED} is returned.
     *
     * @param language The ISO 639 language code to search for.
     * @return The {@code Locale} instance matching the language code, or {@code NOT_SPECIFIED} if not found.
     */
    public static Locale getByLanguage(String language) {
        for (Locale locale : Locale.values()) {
            if (locale.getLanguage().equals(language)) {
                return locale;
            }
        }
        return UNKNOWN;
    }

    /**
     * Returns the ISO 639 language code of the locale.
     *
     * @return The language code as a string.
     */
    @Override
    public String toString() {
        return language;
    }
}