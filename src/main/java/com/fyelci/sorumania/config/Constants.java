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

    public class ContentTypes {
        public static final long CATEGORY  = 400L;
        public static final long QUESTION  = 401L;
        public static final long LESSON    = 402L;
        public static final long COMMENT   = 403L;
    }

    public class ContentPreferences {
        public static final long HIDE  = 800L;
        public static final long SHOW  = 801L;
        public static final long WATCH = 802L;
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

    public class ScoreTypes {
        public static final long ASK_QUESTION   = 600L;
        public static final long COMMENT        = 601L;
    }

    public class ReportedContentLimits {
        public static final long QUESTION  = 2L;
        public static final long COMMENT   = 2L;
    }

    public class UserRelationTypes {
        public static final long PENDING  = 300L;
        public static final long APPROVED = 301L;
        public static final long REJECTED = 302L;
        public static final long BLOCKED  = 303L;
        public static final long DELETED  = 304L;
    }

    public class LeaderboardListTypes {
        public static final int DAILY  = 1;
        public static final int WEEKLY = 2;
        public static final int MONTHLY= 3;
    }

}
