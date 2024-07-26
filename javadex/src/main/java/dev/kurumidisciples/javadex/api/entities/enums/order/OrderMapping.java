package dev.kurumidisciples.javadex.api.entities.enums.order;

import java.util.HashMap;

public class OrderMapping extends HashMap<OrderMapping.Option, OrderMapping.Value> {

    enum Value {
  
        ASCENDING("asc"),
        DESCENDING("desc");
      
        private String value;
      
        Value(String value) {
          this.value = value;
        }
      
        public String getValue() {
          return value;
        }
        
    }

    enum Option {
      TITLE("title"),
      YEAR("year"),
      CREATED_AT("createdAt"),
      UPDATED_AT("updatedAt"),
      FOLLOWED_COUNT("followedCount"),
      RELEVANCE("relevance"),
      READABLE_AT("readableAt"),
      VOLUME("volume"),
      CHAPTER("chapter");

      private String value;

      Option(String value) {
        this.value = value;
      }

      public String getValue() {
        return value;
      }
      
    }

}
