package dev.kurumidisciples.javadex.api.entities.enums;

/**
 * <p>OriginalLanguage class.</p>
 *
 * @deprecated This class will be removed by 0.2.0. Use {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} instead.
 * @author Hacking Pancakez
 * @version $Id: $Id
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

  /**
   * <p>Getter for the field <code>value</code>.</p>
   *
   * @return a {@link java.lang.String} object
   */
  public String getValue() {
    return value;
  }

  /**
   * <p>getLanguage.</p>
   *
   * @param value a {@link java.lang.String} object
   * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.OriginalLanguage} object
   */
  public static OriginalLanguage getLanguage(String value) {
    for (OriginalLanguage language : OriginalLanguage.values()) {
      if (language.getValue().equals(value)) {
        return language;
      }
    }
    return null;
  }


  /** {@inheritDoc} */
  @Override
  public String toString() {
    return value;
  }
  
}
