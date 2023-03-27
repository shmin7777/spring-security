package com.example.security1.controller;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @ResponseBody
    @GetMapping("/user")
    public String user() {
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
}
