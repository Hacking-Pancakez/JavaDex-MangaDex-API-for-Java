package dev.kurumidisciples.javadex.api.entities.enums;

import dev.kurumidisciples.javadex.internal.actions.retrieve.FollowsAction;

/**
 * Represents the type of entity that is being followed.
 * @See {@link FollowsAction}
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

    public String getEndpoint() {
        return endpoint;
    }

    public String getType() {
        return type;
    }

    public static FollowingEntityType fromEndpoint(String endpoint) {
        for (FollowingEntityType type : values()) {
            if (type.getEndpoint().equals(endpoint)) {
                return type;
            }
        }
        return null;
    }

    public static FollowingEntityType fromType(String type) {
        for (FollowingEntityType entityType : values()) {
            if (entityType.getType().equals(type)) {
                return entityType;
            }
        }
        return null;
    }
}
