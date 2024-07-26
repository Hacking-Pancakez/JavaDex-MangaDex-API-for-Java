package dev.kurumidisciples.javadex.api.entities.enums.manga.filters;

public enum Status {
  ONGOING("ongoing"),
  COMPLETED("completed"),
  HIATUS("hiatus"),
  CANCELLED("cancelled");

  private final String value;
  
  private Status(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Status getStatus(String value) {
    for (Status status : Status.values()) {
      if (status.getValue().equals(value)) {
        return status;
      }
    }
    return null;
  }
}