package dev.kurumidisciples.javadex.api.entities.enums; 

import dev.kurumidisciples.javadex.api.entities.enums.Locale; 
/**
 * @deprecated This class will be removed by 0.2.0. Use {@link Locale} instead.
 */
@Deprecated
@SuppressWarnings("unused")
public enum TranslatedLanguage {
  ENGLISH("en"),
  SPANISH("es"), 
  GERMAN("de"),
  GREEK("el"),
  FRENCH("fr"),
  JAPANESE("ja"),
  HINDI("hi"),
  ITALIAN("it"),
  KOREAN("ko"),
  CHINESE_SIMPLIFIED("zh-Hans"),
  CHINESE_TRADITIONAL("zh-Hant"),
  DUTCH("nl"),
  PORTUGUESE("pt"),
  RUSSIAN("ru"),
  SWEDISH("sv"),
  TURKISH("tr");

  private final String language;
  
  TranslatedLanguage(String language) {
    this.language = language;
  }
  
  public String getLanguage() {
    return language;
  }

  public static TranslatedLanguage getByLanguage(String language) {
    for (TranslatedLanguage translatedLanguage : TranslatedLanguage.values()) {
      if (translatedLanguage.getLanguage().equals(language)) {
        return translatedLanguage;
      }
    }
    return null;
  }
  
  @Override
  public String toString() {
    return language;
  }

}