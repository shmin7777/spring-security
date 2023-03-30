package com.example.security1.oauth;

import com.example.security1.auth.PrincipalDetails;
import com.example.security1.auth.provider.FacebookUserInfo;
import com.example.security1.auth.provider.GoogleUserInfo;
import com.example.security1.auth.provider.NaverUserInfo;
import com.example.security1.auth.provider.OAuth2UserInfo;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import com.example.security1.utils.EncodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRuest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("useRequest:: {}", userRequest);
        log.info("getClientRegistration:: {}", userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
        log.info("getAccessToken :: {}", userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글 로그인버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴(OAuth Cleint 라이브러리) -> code를 통해 AcessToken요청
        // userRequest 정보 -> 회원 프로필 받아야함(super.loadUser) 함수 -> 구글로 부터 회원 프로필 받아줌
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("getAttribute :: {}", attributes);

        // attributes(User 정보) 를 가지고 UserEntity를 만든 후 강제로 회원가입 시킨다.
        OAuth2UserInfo oAuth2UserInfo = null;
        if ("google".equals(userRequest.getClientRegistration().getRegistrationId())) {
            log.info("google 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(attributes);
        } else if ("facebook".equals(userRequest.getClientRegistration().getRegistrationId())) {
            log.info("facebook 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(attributes);
        } else if ("naver".equals(userRequest.getClientRegistration().getRegistrationId())) {
            log.info("naver 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map) attributes.get("response"));
        } else {
            log.info("우리는 구글과 페이스북과 네이버만 지원해요");
        }
        // provider를 사용
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();  // google login : sub, facebook login : id
        String username = provider + "_" + providerId;
        String email = oAuth2User.getAttribute("email");
        String password = EncodeUtils.bcryptEncode("홍쓰");// 크게 의미 없는 정보
        String role = "ROLE_USER";
//        String provider = userRequest.getClientRegistration().getRegistrationId(); // google
//        String providerId = oAuth2User.getAttribute("sub"); // google login : sub, facebook login : id

        Optional<User> optUser = userRepository.findByUsername(username);
        User user = optUser.orElseGet(() -> {
            log.info("oauth 로그인이 최초입니다.");
            User tmpUser = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(tmpUser);
            return tmpUser;
        });


        return new PrincipalDetails(user, attributes);
    }
}
