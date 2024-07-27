package dev.kurumidisciples.javadex.internal.http.ratelimit;

public enum EndpointLimits {
    AUTH_LOGIN("/auth/login", 30, 60, Method.POST),
    AUTH_REFRESH("/auth/refresh", 60, 60, Method.POST),

    AT_HOME("/at-home/server/{id}", 40, 1, Method.GET),

    AUTHOR_POST("/author", 10, 60, Method.POST),
    AUTHOR_PUT("/author", 10, 1, Method.PUT),
    AUTHOR_DELETE("/author/{id}", 10, 10, Method.DELETE),

    CAPTCHA_POST("/captcha/solve", 10, 10, Method.POST),

    COVER_POST("/cover", 100, 10, Method.POST),
    COVER_PUT("/cover/{id}", 100, 10, Method.PUT),
    COVER_DELETE("/cover/{id}", 10, 10, Method.DELETE),

    CHAPTER_POST("/chapter/{id}/read", 300, 10, Method.POST),
    CHAPTER_PUT("/chapter/{id}", 10, 1, Method.PUT),
    CHAPTER_DELETE("/chapter/{id}", 10, 1, Method.DELETE),

    FORUMS_POST("/forums/thread", 10, 1, Method.POST),

    MANGA_POST("/manga", 10, 60, Method.POST),
    MANGA_PUT("/manga/{id}", 10, 60, Method.PUT),
    MANGA_DELETE("/manga/{id}", 10, 10, Method.DELETE),
    MANGA_DRAFT_COMMIT("/manga/draft/{id}/commit", 10, 60, Method.POST),
    MANGA_RANDOM("/manga/random", 60, 1, Method.GET),

    REPORT_POST("/report", 10, 1, Method.POST),
    REPORT_GET("/report", 10, 1, Method.GET),

    SCANLATION_GROUP_POST("/group", 10, 60, Method.POST),
    SCANLATION_GROUP_PUT("/group/{id}", 10, 1, Method.PUT),
    SCANLATION_GROUP_DELETE("/group/{id}", 10, 10, Method.DELETE),

    UPLOAD_SESSIONS_GET("/upload", 30, 1, Method.GET),
    UPLOAD_SESSIONS_POST("/upload/begin", 20, 1, Method.POST),
    UPLOAD_SESSIONS_POST_ID("/upload/begin/{id}", 20, 1, Method.POST),
    UPLOAD_SESSIONS_COMMIT("/upload/{id}/commit", 10, 1, Method.POST),
    UPLOAD_SESSIONS_DELETE("/upload/{id}", 30, 1, Method.DELETE),
    UPLOAD_FILES_POST("/upload/{id}", 250, 1, Method.POST),
    UPLOAD_FILES_DELETE_ID("/upload/{id}/{id}", 250, 1, Method.DELETE),
    UPLOAD_FILES_BATCH_DELETE("/upload/{id}/batch", 250, 1, Method.DELETE),

    DEFAULT("", 100, 1, Method.GET);

    private final String endpoint;
    private final int requestsPerTimePeriod;
    private final int timePeriodInMinutes;
    private final Method method;

    EndpointLimits(String endpoint, int requestsPerTimePeriod, int timePeriodInMinutes, Method method) {
        this.endpoint = endpoint;
        this.requestsPerTimePeriod = requestsPerTimePeriod;
        this.timePeriodInMinutes = timePeriodInMinutes;
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public int getRequestsPerTimePeriod() {
        return requestsPerTimePeriod;
    }

    public int getTimePeriodInMinutes() {
        return timePeriodInMinutes;
    }

    public Method getMethod() {
        return method;
    }

    public enum Method {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        private final String method;

        Method(String method) {
            this.method = method;
        }

        public String getMethod() {
            return method;
        }

        public static Method fromString(String method) {
            for (Method m : values()) {
                if (m.method.equalsIgnoreCase(method)) {
                    return m;
                }
            }
            return null;
        }
    }
}
