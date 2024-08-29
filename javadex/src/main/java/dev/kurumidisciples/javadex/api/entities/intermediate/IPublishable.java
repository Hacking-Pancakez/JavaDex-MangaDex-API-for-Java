package dev.kurumidisciples.javadex.api.entities.intermediate;

import java.time.OffsetDateTime;

/**
 * Provides an interface for an entity that has a publish date and a readableAt date.
 *
 * @author Hacking Pancakez
 */
public interface IPublishable {

  /**
   * Returns the publish date of the entity.
   *
   * @return a {@link java.time.OffsetDateTime} object
   */
  OffsetDateTime getPublishAt();
  /**
   * Returns the date that the entity was viewable to the public.
   *
   * @return a {@link java.time.OffsetDateTime} object
   */
  OffsetDateTime getReadableAt();
  
}
