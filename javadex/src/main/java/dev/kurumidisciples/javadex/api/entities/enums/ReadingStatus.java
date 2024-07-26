package dev.kurumidisciples.javadex.api.entities.enums;

/**
 * <p>ReadingStatus class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public enum ReadingStatus {
    READING("reading"),
    ON_HOLD("on_hold"),
    PLAN_TO_READ("plan_to_read"),
    DROPPED("dropped"),
    RE_READING("re_reading"),
    COMPLETED("completed");

    private final String status;

    ReadingStatus(String status) {
        this.status = status;
    }

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getStatus() {
        return status;
    }

    /**
     * <p>fromString.</p>
     *
     * @param status a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.ReadingStatus} object
     */
    public static ReadingStatus fromString(String status) {
        for (ReadingStatus s : values()) {
            if (s.getStatus().equals(status)) {
                return s;
            }
        }
        return null;
    }
}
