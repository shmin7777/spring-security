package com.example.security1.auth.provider;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return getAttribute(attributes, "sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return getAttribute(attributes, "email");
    }

    @Override
    public String getName() {
        return getAttribute(attributes, "name");
    }
}
