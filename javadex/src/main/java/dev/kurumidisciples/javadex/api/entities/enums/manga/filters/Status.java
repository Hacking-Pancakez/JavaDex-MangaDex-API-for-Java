package dev.kurumidisciples.javadex.api.entities.enums.manga.filters;

/**
 * <p>Status class.</p>
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public enum Status {
   /**
     * The project is currently ongoing.
     */
    ONGOING("ongoing"),

    /**
     * The project has been completed.
     */
    COMPLETED("completed"),

    /**
     * The project is currently on hiatus.
     */
    HIATUS("hiatus"),

    /**
     * The project has been cancelled.
     */
    CANCELLED("cancelled");

  private final String value;
  
  private Status(String value) {
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
   * <p>getStatus.</p>
   *
   * @param value a {@link java.lang.String} object
   * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status} object
   */
  public static Status getStatus(String value) {
    for (Status status : Status.values()) {
      if (status.getValue().equals(value)) {
        return status;
      }
    }
    return null;
  }
}
