package com.example.security1.auth;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login  요청이  오면 자동으로 UserDetailsService 타입으로 IOC 되어있는
// loadUserByUsername함수가 실행
@Slf4j
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // security session(내부 Authentication(내부 UserDetails))로 들어감
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // input tag의 name과 동일해야한다!!!!
        // 동일하지 않다면 usernameParameter 를 써주어야함
        log.info("username ::  {}", username);

        Optional<User> optUserEntity = userRepository.findByUsername(username);

        if(optUserEntity.isPresent())
            return new PrincipalDetails(optUserEntity.get());

        return null;
    }
}
