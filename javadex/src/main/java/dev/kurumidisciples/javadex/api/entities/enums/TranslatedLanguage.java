package dev.kurumidisciples.javadex.api.entities.enums; 

import dev.kurumidisciples.javadex.api.entities.enums.Locale; 
/**
 * <p>TranslatedLanguage class.</p>
 *
 * @deprecated This class will be removed by 0.2.0. Use {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} instead.
 * @author Hacking Pancakez
 * @version $Id: $Id
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
  
  /**
   * <p>Getter for the field <code>language</code>.</p>
   *
   * @return a {@link java.lang.String} object
   */
  public String getLanguage() {
    return language;
  }

  /**
   * <p>getByLanguage.</p>
   *
   * @param language a {@link java.lang.String} object
   * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.TranslatedLanguage} object
   */
  public static TranslatedLanguage getByLanguage(String language) {
    for (TranslatedLanguage translatedLanguage : TranslatedLanguage.values()) {
      if (translatedLanguage.getLanguage().equals(language)) {
        return translatedLanguage;
      }
    }
    return null;
  }
  
  /** {@inheritDoc} */
  @Override
  public String toString() {
    return language;
  }

}
