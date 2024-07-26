package dev.kurumidisciples.javadex.api.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;

/**
 * Represents a user in the MangaDex API.
 *
 * @since 0.0.1
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class User extends Entity{

    /**
     * All possible roles a user can have within the MangaDex API.
     */
    public enum Role {
        GROUP_MEMBER("ROLE_GROUP_MEMBER"),
        GROUP_OWNER("ROLE_GROUP_LEADER"),
        MEMBER("ROLE_MEMBER"),
        MD_AT_HOME("ROLE_MD_AT_HOME"),
        GLOBAL_MODERATOR("ROLE_GLOBAL_MODERATOR"),
        POWER_UPLOADER("ROLE_POWER_UPLOADER"),
        PUBLIC_RELATIONS("ROLE_PUBLIC_RELATIONS"),
        DEVELOPER("ROLE_DEVELOPER"),
        DESIGNER("ROLE_DESIGNER"),
        ADMIN("ROLE_ADMIN"),
        BANNED("ROLE_BANNED"),
        GUEST("ROLE_GUEST"),
        VIP("ROLE_VIP"),
        UNVERIFIED("ROLE_UNVERIFIED"),
        STAFF("ROLE_STAFF"),
        FORUM_MODERATOR("ROLE_FORUM_MODERATOR"),
        UNKNOWN("unknown");
      
      
        private final String id;
        
        Role(String id) {
          this.id = id;
        }
      
        public String getId() {
          return id;
        }
      
        public static Role getRole(String id) {
          for (Role role : Role.values()) {
            if (role.getId().equals(id)) {
              return role;
            }
          }
          return UNKNOWN;
        }
      
        @Override
        public String toString() {
          return id;
        }
    }

    private final List<Role> roles;
    private UUID id;
    private final String username;
    private final RelationshipMap relationshipMap;

    /**
     * <p>Constructor for User.</p>
     *
     * @param user a {@link com.google.gson.JsonObject} object
     */
    public User(JsonObject user) {
        JsonObject original = user;
        try {
            user = user.getAsJsonObject("data");
            this.id = UUID.fromString(user.get("id").getAsString());
        } catch (Exception e) {
            user = original;
            this.id = UUID.fromString(user.get("id").getAsString());
        }

        JsonObject attributes = user.getAsJsonObject("attributes");

        this.username = attributes.get("username").getAsString();
        this.roles = getRoles(attributes.getAsJsonArray("roles"));
        this.relationshipMap = new RelationshipMap(user.getAsJsonArray("relationships"));
    }

    private static List<Role> getRoles(JsonArray jsonArray) {
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            roles.add(Role.getRole(jsonArray.get(i).getAsString()));
        }
        return roles;
    }

    /**
     * <p>Getter for the field <code>roles</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.util.UUID} object
     */
    public UUID getId() {
        return id;
    }

    /**
     * <p>Getter for the field <code>username</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getUsername() {
        return username;
    }

    /**
     * <p>Getter for the field <code>relationshipMap</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap} object
     */
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }
}
