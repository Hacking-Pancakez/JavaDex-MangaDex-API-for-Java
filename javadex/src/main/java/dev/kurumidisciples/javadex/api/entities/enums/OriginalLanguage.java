package dev.kurumidisciples.javadex.api.entities.enums;

/**
 * @deprecated This class will be removed by 0.2.0. Use {@link Locale} instead.
 */
@Deprecated
public enum OriginalLanguage {

  /* fill in the rest of the enum */

  JAPANESE("ja"),
  CHINESE("zh"),
  FRENCH("fr"),
  GERMAN("de"),
  KOREAN("ko"),
  SPANISH("es"),
  ITALIAN("it"),
  ENGLISH("en"),
  HINDI("hi"),
  RUSSIAN("ru"),
  SWEDISH("sv"),
  DUTCH("nl"),
  PORTUGUESE("pt"),
  TURKISH("tr"),
  GREEK("el");


  private String value;

  private OriginalLanguage(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static OriginalLanguage getLanguage(String value) {
    for (OriginalLanguage language : OriginalLanguage.values()) {
      if (language.getValue().equals(value)) {
        return language;
      }
    }
    return null;
  }


  @Override
  public String toString() {
    return value;
  }
  
}