package com.example.security1.oauth;

import com.example.security1.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로 부터 받은 userRuest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("useRequest:: {}", userRequest);
        log.info("getClientRegistration:: {}", userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
        log.info("getAccessToken :: {}", userRequest.getAccessToken().getTokenValue());
        // 구글 로그인버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴(OAuth Cleint 라이브러리) -> code를 통해 AcessToken요청
        // userRequest 정보 -> 회원 프로필 받아야함(super.loadUser) 함수 -> 구글로 부터 회원 프로필 받아줌
        log.info("getAttribute :: {}", super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        // attributes(User 정보) 를 가지고 UserEntity를 만든 후 강제로 회원가입 시킨다.


        return oAuth2User;
    }
}
