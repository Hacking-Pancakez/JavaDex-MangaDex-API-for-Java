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
 * @since 0.0.1
 */
public class OffsetDateTimeTypeAdapter extends TypeAdapter<OffsetDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * Writes an OffsetDateTime object to a JSON string.
     * @param out The JsonWriter object to write to.
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
     * Reads an OffsetDateTime object from a JSON string.
     * @param in The JsonReader object to read from.
     * @return The OffsetDateTime object read from the JSON string.
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
