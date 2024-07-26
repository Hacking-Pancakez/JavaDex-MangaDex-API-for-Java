package dev.kurumidisciples.javadex.internal.http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Type adapter for OffsetDateTime.
 * <p> This class is used to serialize and deserialize OffsetDateTime objects to and from JSON.
 * <p> This class is used internally by the JavaDex API.
 *
 * @since 0.0.1
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class OffsetDateTimeTypeAdapter extends TypeAdapter<OffsetDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * {@inheritDoc}
     *
     * Writes an OffsetDateTime object to a JSON string.
     */
    @Override
    public void write(JsonWriter out, OffsetDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(formatter));
        }
    }

    /**
     * {@inheritDoc}
     *
     * Reads an OffsetDateTime object from a JSON string.
     */
    @Override
    public OffsetDateTime read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            return OffsetDateTime.parse(in.nextString(), formatter);
        }
    }
}
