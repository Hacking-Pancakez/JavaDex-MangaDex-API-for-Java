package dev.kurumidisciples.javadex.api.entities.enums;

import dev.kurumidisciples.javadex.api.entities.content.Manga;

/**
 * @deprecated This class will be moved into {@link Manga} by 0.2.0.
 */
public enum State {
  DRAFT("draft"),
  PUBLISHED("published");

  private final String value;

  State(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static State getByValue(String value) {
    for (State state : values()) {
      if (state.getValue().equals(value)) {
        return state;
      }
    }
    return null;
  }
}