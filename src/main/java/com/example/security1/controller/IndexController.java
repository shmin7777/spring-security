package com.example.security1.controller;

import com.example.security1.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class IndexController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    // @AuthenticationPrincipal 을 통해서 session정보에 접근을 할 수 있다. PrincipalDetails으로 다운 캐스팅
    // Authentication를 DI를 받아서 getUser을 할 수도 있고, @AuthenticationPrincipal을 통해서 UserDetails정보에 접근할 수 있다.
    // Session 내부에 Security Session 이 있고 security session 안엔 Authentication객체만 들어올 수 있다.
    // Authentication에는 두가지 타입을 가질 수 있다.
    // 1. UserDetails : 일반 로그인, @AuthenticationPrincipal UserDetails userDetails
    // 2. OAuth2User : OAuth 로그인, @AuthenticationPrincipal OAuth2User oAuth2User
    @GetMapping("/test/login")
    @ResponseBody
    public String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) { // DI
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.info("/test/login authentication ===================== {}", principal.getUser());
        log.info("userDetails:: {} ", userDetails.getUser());
        return "세션 정보 확인하기";
    }

    // DefaultOAuth2User cannot be cast to class com.example.security1.auth.PrincipalDetails
    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User) { // DI
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        log.info("/test/oauth/login authentication ===================== {}", principal.getAttributes());
        log.info("oAuth2User::: {} ", oAuth2User.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }


    // OAuth login을 해도 PrincipalDetails
    // 일반 로그인을 해도 PrincipalDetails
    @ResponseBody
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails ::: {} ", principalDetails.getUser().toString());
        return "user";
    }

    @ResponseBody
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @ResponseBody
    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    // spring security가 해당 주소를 낚아 채버림
    // 설정 수정해야함 -> SecurityConfig 파일 생성 후 여기 logic 작동
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        // 회원가입 페이지
        return "joinForm";
    }

    @PostMapping("/login")
    public String login() {
        // 회원가입 페이지
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        // 실제 회원가입
        log.info(user.toString());
        user.setRole("ROLE_USER");

        // 회원가입 잘됨. 비밀번호 : 1234 => security로 로그인을 할 수 없음. 이유는 password가 암호화가 안되었기 떄문
        // BCryptPasswordEncoder로 encoding을 시킨 password로 저장한다.
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @ResponseBody
    @Secured("ROLE_ADMIN") // secured를 자주 씀
    @GetMapping("/info")
    public String info() {
        return "개인정보";
    }

    //    @PostAuthorize() // 함수가 끝나고 난뒤.
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // data라는 메서드가 실행되기 진전에 실행
    @ResponseBody
    @GetMapping("/data")
    public String data() {
        return "데이터 정보";
    }
}
