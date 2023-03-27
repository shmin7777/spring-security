package com.example.security1.auth;

// 시큐리티가 /login 주소 요청이 오면 을 낚아 채서 로그인을 진행시킨다.
// 로그인을 진행 완료가 되면 시큐리티 session을 만들어줌. (Security ContextHolder)라는 key값에 session정보를 저장
// key값이 되는 오브젝트 => Authentication 타입의 객체
// Authentication 안에 User 정보가 있어야함
// User Object의 타입은 => UserDetails 타입 객체어야한다.

import com.example.security1.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


// Security Session => Authenication => UserDeatails(PrincipalDetails)
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final User user;

    // 해당 User의 권한을 return하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
//        collect.add(new GrantedAuthority(){
//
//            @Override
//            public String getAuthority() {
//                return user.getRole();
//            }
//        });
        collect.add(user::getRole);
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 이 계정 만료했니?
    @Override
    public boolean isAccountNonExpired() {
        return true; // 아니요
    }

    // 이 계정 잠겼니?
    @Override
    public boolean isAccountNonLocked() {
        return true; // 아니요
    }

    // 비밀번호가 너무 오래사용한건 아니니?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 아니요
    }

    // 계정이 활성화 되어있니?
    @Override
    public boolean isEnabled() {
        // 우리 사이트!!
        // 1년동안 회원이 로그인을 안하면 휴먼 계정으로 하기로 함.

        return true; // 활성화 되어있음
    }
}
