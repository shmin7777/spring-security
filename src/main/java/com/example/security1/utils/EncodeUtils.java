package com.example.security1.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public final class EncodeUtils {
    public static <T> T bcryptEncode(String object) {
        BCryptPasswordEncoder bCryptPasswordEncoder = ApplicationContextProvider.getBean("encodePwd");
        return (T) bCryptPasswordEncoder.encode(object);
    }
}
