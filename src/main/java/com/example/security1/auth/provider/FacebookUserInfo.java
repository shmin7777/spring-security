package com.example.security1.auth.provider;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class FacebookUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return getAttribute(attributes, "id");
    }

    @Override
    public String getProvider() {
        return "facebook";
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
