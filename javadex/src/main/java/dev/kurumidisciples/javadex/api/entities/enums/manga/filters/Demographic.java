package dev.kurumidisciples.javadex.api.entities.enums.manga.filters;


/**
 * <p>Demographic class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public enum Demographic {
  
  SHOUNEN("shounen"),
  SHOUJO("shoujo"),
  JOSEI("josei"),
  SEINEN("seinen"),
  NONE("none");

  private final String value;

  Demographic(String value) {
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
   * <p>getDemographic.</p>
   *
   * @param value a {@link java.lang.String} object
   * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic} object
   */
  public static Demographic getDemographic(String value) {
    for (Demographic demographic : Demographic.values()) {
      if (demographic.getValue().equals(value)) {
        return demographic;
      }
    }
    return Demographic.NONE;
}
}
