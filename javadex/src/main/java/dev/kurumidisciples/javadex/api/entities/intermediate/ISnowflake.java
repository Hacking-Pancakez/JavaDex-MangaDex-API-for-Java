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
   * <p>getIdRaw.</p>
   *
   * @return a {@link java.lang.String} object
   */
  String getIdRaw();
  
  /**
   * <p>getId.</p>
   *
   * @return a {@link java.util.UUID} object
   */
  UUID getId();

  /**
   * <p>getCreatedAt.</p>
   *
   * @return a {@link java.time.OffsetDateTime} object
   */
  OffsetDateTime getCreatedAt();

  /**
   * <p>getUpdatedAt.</p>
   *
   * @return a {@link java.time.OffsetDateTime} object
   */
  OffsetDateTime getUpdatedAt();
}
