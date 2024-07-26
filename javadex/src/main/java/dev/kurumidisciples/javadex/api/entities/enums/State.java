package dev.kurumidisciples.javadex.api.entities.enums;

import dev.kurumidisciples.javadex.api.entities.content.Manga;

/**
 * <p>State class.</p>
 *
 * @deprecated This class will be moved into {@link dev.kurumidisciples.javadex.api.entities.content.Manga} by 0.2.0.
 * @author Hacking Pancakez
 * @version $Id: $Id
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
