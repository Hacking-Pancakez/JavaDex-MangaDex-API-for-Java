package dev.kurumidisciples.javadex.api.entities.intermediate;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Provides an interface for an entity that has a unique identifier, creation date, and update date.
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public interface ISnowflake {

  /**
   * The unique identifier of the entity in raw form.
   *
   * @return a {@link java.lang.String} object
   */
  String getIdRaw();
  
  /**
   * The unique identifier of the entity.
   *
   * @return a {@link java.util.UUID} object
   */
  UUID getId();

  /**
   * The creation date of the entity.
   *
   * @return a {@link java.time.OffsetDateTime} object
   */
  OffsetDateTime getCreatedAt();

  /**
   * The last update date of the entity.
   *
   * @return a {@link java.time.OffsetDateTime} object
   */
  OffsetDateTime getUpdatedAt();
}
