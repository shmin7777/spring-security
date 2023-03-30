package com.example.security1.auth.provider;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();

    default <T> T getAttribute(Map<String, Object> attributes, String attribute) {
        return (T) attributes.get(attribute);
    }
}
