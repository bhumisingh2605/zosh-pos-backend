package com.zosh.zosh.pos.system.config;

public class JwtConstant {
    // JWT_SECRET must be set as an environment variable. There is no
    // hardcoded fallback: a missing secret should fail loudly at startup
    // rather than silently signing tokens with a value visible in source.
    public static final String JWT_SECRET = requireEnv("JWT_SECRET");
    public static final String JWT_HEADER = "Authorization";

    private static String requireEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable " + name + " is not set. " +
                            "Set it before starting the application (e.g. a long random string for JWT_SECRET)."
            );
        }
        return value;
    }
}