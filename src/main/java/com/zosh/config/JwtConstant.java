package com.zosh.config;

public class JwtConstant {
    // Corrected to be at least 32 characters long to meet JWT security requirements.
    // Make sure to replace this with your own unique and secure string.
    public static final String SECRET_KEY = "your_strong_and_very_secure_secret_key_that_is_at_least_32_characters_long_and_unique_to_your_project";
    public static final String JWT_HEADER = "Authorization";
}