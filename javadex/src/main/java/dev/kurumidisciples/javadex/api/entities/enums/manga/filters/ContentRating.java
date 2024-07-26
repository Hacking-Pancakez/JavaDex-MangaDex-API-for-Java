package dev.kurumidisciples.javadex.api.entities.enums.manga.filters;

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
  
  public String getValue() {
    return value;
  }
  
  public static ContentRating getContentRating(String value) {
    for (ContentRating contentRating : ContentRating.values()) {
      if (contentRating.getValue().equals(value)) {
        return contentRating;
      }
    }
    return ContentRating.NONE;
  }

  @Override
  public String toString() {
    return value;
  }
}