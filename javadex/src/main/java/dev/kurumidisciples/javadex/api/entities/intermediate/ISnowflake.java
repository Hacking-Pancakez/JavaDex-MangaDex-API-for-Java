package dev.kurumidisciples.javadex.api.entities.intermediate;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Provides an interface for an entity that has a unique identifier, creation date, and update date.
 */
public interface ISnowflake {

  String getIdRaw();
  
  UUID getId();

  OffsetDateTime getCreatedAt();

  OffsetDateTime getUpdatedAt();
}