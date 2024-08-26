package dev.kurumidisciples.javadex.api.entities.enums;

import dev.kurumidisciples.javadex.internal.actions.retrieve.FollowsAction;

/**
 * Represents the type of entity that is being followed.
 *
 * @see {@link FollowsAction}
 * @author Hacking Pancakez
 */
public enum FollowingEntityType {
    SELF_MANGA("/user/follows/manga", "manga"),
    SELF_USER("/user/follows/user", "user"),
    SELF_GROUP("/user/follows/group", "group"),
    SELF_LIST("/user/follows/list", "list");

    private final String endpoint;
    private final String type;

    FollowingEntityType(String endpoint, String type) {
        this.endpoint = endpoint;
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>endpoint</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getType() {
        return type;
    }

    /**
     * <p>fromEndpoint.</p>
     *
     * @param endpoint a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.FollowingEntityType} object
     */
    public static FollowingEntityType fromEndpoint(String endpoint) {
        for (FollowingEntityType type : values()) {
            if (type.getEndpoint().equals(endpoint)) {
                return type;
            }
        }
        return null;
    }

    /**
     * <p>fromType.</p>
     *
     * @param type a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.FollowingEntityType} object
     */
    public static FollowingEntityType fromType(String type) {
        for (FollowingEntityType entityType : values()) {
            if (entityType.getType().equals(type)) {
                return entityType;
            }
        }
        return null;
    }
}
