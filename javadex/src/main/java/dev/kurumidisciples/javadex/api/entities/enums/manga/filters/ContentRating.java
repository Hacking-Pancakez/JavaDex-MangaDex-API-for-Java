package dev.kurumidisciples.javadex.api.entities.enums.manga.filters;

/**
 * <p>ContentRating class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public enum ContentRating {

  NONE("none"),
  SAFE("safe"),
  SUGGESTIVE("suggestive"),
  EROTICA("erotica"),
  PORNOGRAPHIC("pornographic");

  private final String value;

  ContentRating(String value) {
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
   * <p>getContentRating.</p>
   *
   * @param value a {@link java.lang.String} object
   * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating} object
   */
  public static ContentRating getContentRating(String value) {
    for (ContentRating contentRating : ContentRating.values()) {
      if (contentRating.getValue().equals(value)) {
        return contentRating;
      }
    }
    return ContentRating.NONE;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return value;
  }
}
