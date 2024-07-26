package dev.kurumidisciples.javadex.api.entities.enums;

/**
 * Enum representing various locales using ISO 639 language codes.
 * This enum is used to specify the language of content within the application.
 * It includes a method to retrieve a {@code Locale} instance by its language code.
 * 
 * <p>Each enum constant represents a specific locale with its associated ISO 639 language code.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * Locale locale = Locale.getByLanguage("en");
 * System.out.println(locale); // Output: en
 * }</pre>
 * 
 * <p>Author: Hacking Pancakez</p>
 * <p>Version: $Id: $Id</p>
 */
public enum Locale {
    /** Simplified Chinese */
    SIMPLIFIED_CHINESE("zh"), // Simplified Chinese

    /** Traditional Chinese (Hong Kong) */
    TRADITIONAL_CHINESE("zh-hk"), // Traditional Chinese (Hong Kong)

    /** Brazilian Portuguese */
    BRAZILIAN_PORTUGESE("pt-br"), // Brazilian Portuguese

    /** Castilian Spanish (Spain) */
    CASTILIAN_SPANISH("es"), // Castilian Spanish (Spain)

    /** Latin American Spanish */
    LATIN_AMERICAN_SPANISH("es-la"), // Latin American Spanish

    /** Romanized Japanese */
    ROMANIZED_JAPANESE("ja-ro"), // Romanized Japanese

    /** Romanized Korean */
    ROMANIZED_KOREAN("ko-ro"), // Romanized Korean

    /** Romanized Chinese */
    ROMANIZED_CHINESE("zh-ro"), // Romanized Chinese

    /** English */
    ENGLISH("en"), // English

    /** Japanese */
    JAPANESE("ja"), // Japanese

    /** Korean */
    KOREAN("ko"), // Korean

    /** Spanish */
    SPANISH("es"), // Spanish

    /** French */
    FRENCH("fr"), // French

    /** German */
    GERMAN("de"), // German

    /** Italian */
    ITALIAN("it"), // Italian

    /** Russian */
    RUSSIAN("ru"), // Russian

    /** Hindi */
    HINDI("hi"), // Hindi

    /** Swedish */
    SWEDISH("sv"), // Swedish

    /** Dutch */
    DUTCH("nl"), // Dutch

    /** Portuguese */
    PORTUGUESE("pt"), // Portuguese

    /** Turkish */
    TURKISH("tr"), // Turkish

    /** Greek */
    GREEK("el"), // Greek

    /** Fallback for unspecified locales */
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
     * If the language code does not match any locale, {@code UNKNOWN} is returned.
     *
     * @param language The ISO 639 language code to search for.
     * @return The {@code Locale} instance matching the language code, or {@code UNKNOWN} if not found.
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
     * {@inheritDoc}
     *
     * Returns the ISO 639 language code of the locale.
     */
    @Override
    public String toString() {
        return language;
    }
}