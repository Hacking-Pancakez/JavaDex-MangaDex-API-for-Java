package dev.kurumidisciples.javadex.api.entities.intermediate;

import java.time.OffsetDateTime;

/**
 * Provides an interface for an entity that has a publish date and a readableAt date.
 */
public interface IPublishable {

  OffsetDateTime getPublishAt();
  OffsetDateTime getReadableAt();
  
}
