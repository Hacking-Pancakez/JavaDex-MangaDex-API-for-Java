package dev.kurumidisciples.javadex.api.entities;

import dev.kurumidisciples.javadex.api.entities.intermediate.IRelationHolder;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;

import java.util.List;

/**
 * Represent a user on the MangaDex platform.
 * @author Hacking Pancakez
 */
public interface User extends ISnowflake, IRelationHolder{
    
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
        /**
         * Reserved for roles that are not yet implemented in the current version.
         */
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
          return UNKNOWN; // when the role is not yet implemented
        }
      
        @Override
        public String toString() {
          return id;
        }
    }

    /**
     * Returns the roles assigned to the user
     */
    List<Role> getRoles();
    /**
     * Returns the username of the user
     */
    String getUsername();
}
