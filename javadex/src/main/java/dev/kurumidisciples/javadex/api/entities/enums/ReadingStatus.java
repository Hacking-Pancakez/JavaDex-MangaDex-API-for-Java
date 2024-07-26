package dev.kurumidisciples.javadex.api.entities.enums;

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

    public String getStatus() {
        return status;
    }

    public static ReadingStatus fromString(String status) {
        for (ReadingStatus s : values()) {
            if (s.getStatus().equals(status)) {
                return s;
            }
        }
        return null;
    }
}
