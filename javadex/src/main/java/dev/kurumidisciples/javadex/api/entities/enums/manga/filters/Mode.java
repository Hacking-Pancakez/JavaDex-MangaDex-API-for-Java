package dev.kurumidisciples.javadex.api.entities.enums.manga.filters;



/**
 * <p>Mode class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public enum Mode {

  AND("AND"),
  OR("OR");

  private String value;

  Mode(String value) {
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
   * <p>fromValue.</p>
   *
   * @param value a {@link java.lang.String} object
   * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Mode} object
   */
  public static Mode fromValue(String value) {
    if (value == null) {
      return null;
    }
    for (Mode e : Mode.values()) {
      if (e.getValue().equals(value)) {
        return e;
      }
    }
    return null;
  }
  
}
