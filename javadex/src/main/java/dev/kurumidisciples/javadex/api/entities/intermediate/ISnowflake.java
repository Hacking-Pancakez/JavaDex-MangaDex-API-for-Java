package dev.kurumidisciples.javadex.api.entities.intermediate;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Provides an interface for an entity that has a unique identifier, creation date, and update date.
 *
 * @author Hacking Pancakez
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
  default OffsetDateTime getCreatedAt(){
    throw new UnsupportedOperationException("Not supported for this entity.");
  }

  /**
   * The last update date of the entity.
   *
   * @return a {@link java.time.OffsetDateTime} object
   */
  default OffsetDateTime getUpdatedAt(){
    throw new UnsupportedOperationException("Not supported for this entity.");
  }

  /**
   * The version of the entity. Indicates how many times the entity has been modified.
   */
  Integer getVersion();
}
