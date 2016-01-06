package com.fyelci.sorumania.config;

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";

    public static final String SYSTEM_ACCOUNT = "system";

    public static String MANAGABLE_LOV_TYPES = "MANAGABLE_TYPES";

    private Constants() {
    }

    public class QuestionStatus {
        public static final long ACTIVE     = 200L;
        public static final long DELETED    = 201L;
        public static final long REPORTED   = 202L;
    }

    public class CommentStatus {
        public static final long ACTIVE     = 700L;
        public static final long DELETED    = 701L;
    }


}
