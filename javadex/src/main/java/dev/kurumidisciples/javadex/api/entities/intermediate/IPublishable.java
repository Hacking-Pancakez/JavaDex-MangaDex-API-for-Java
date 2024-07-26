package dev.kurumidisciples.javadex.api.entities.intermediate;

import java.time.OffsetDateTime;

/**
 * Provides an interface for an entity that has a publish date and a readableAt date.
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public interface IPublishable {

  /**
   * <p>getPublishAt.</p>
   *
   * @return a {@link java.time.OffsetDateTime} object
   */
  OffsetDateTime getPublishAt();
  /**
   * <p>getReadableAt.</p>
   *
   * @return a {@link java.time.OffsetDateTime} object
   */
  OffsetDateTime getReadableAt();
  
}
