package dev.kurumidisciples.javadex.api.entities.enums;

import dev.kurumidisciples.javadex.api.entities.Manga;

/**
 * <p>Defines the state of a {@link Manga}.</p>
 * <p>State is a property that indicates whether the manga object is visible on the MangaDex website.
 *  Drafts are incomplete manga objects that are not visible to the public and are only visible to the creator.
 * </p>
 *
 * @warn This class will be moved into {@link Manga} by 0.2.0.
 * @author Hacking Pancakez
 */
public enum State {
  DRAFT("draft"),
  PUBLISHED("published");

  private final String value;

  State(String value) {
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
   * <p>getByValue.</p>
   *
   * @param value a {@link java.lang.String} object
   * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.State} object
   */
  public static State getByValue(String value) {
    for (State state : values()) {
      if (state.getValue().equals(value)) {
        return state;
      }
    }
    return null;
  }
}
