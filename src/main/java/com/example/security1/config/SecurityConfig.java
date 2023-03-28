package com.example.security1.config;

import com.example.security1.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 1. 코드 받기(인증이 됨). 2. access token 받기(사용자 정보에 접근할 권한 생김).
// 3. 권한을 통해 사용자 프로필 정보를 가져오고
// 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 하고
// 4-2. (이메일, 전화번호, 이름 , 아이디) 쇼핑몰일 경우 집주소가 필요
// 추가적인 구성정보가 필요없다면 자동으로 회원가입, 필요하다면 정보를 더 받아옴

@EnableWebSecurity // spring security filter가 spring filter chain에 등록이 됨
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// secured 어노테이션 활성화. prePostEnabled : PreAuthorize, PostAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // spring security는 default로 header에 cache-control
        http.csrf().disable(); // CSRF disable
        http.authorizeRequests()
                // /user/** 이 주소로 들어오면 인증이 필요해!
                .antMatchers("/user/**") // login안하고 /user 로 접근하면 403 error 접근권한이 없다.
                .authenticated() // 인증만 되면 들어갈 수 있다.
                .antMatchers("/manager/**")
                // /manager/** 위 경로로 들어오면 인증 뿐만 아니라 ROLE_ADMIN이나 ROLE_MANAGER의 권한이 필요해!
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')").antMatchers("/admin/**")
                // /manager/** 위 경로로 들어오면 인증 뿐만 아니라 ROLE_ADMIN이나 ROLE_MANAGER의 권한이 필요해!
                .access("hasRole('ROLE_ADMIN')").anyRequest().permitAll() // 위 경로가 아닌 주소들은 전부 허용
                .and().formLogin().loginPage("/loginForm") // 권한이 없는 페이지에 요청이 들어갈 때 login페이지로 이동(redirect /login)
//                .usernameParameter("id")
                .loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아 채서 대신 로그인을 진행해줌. 따라서 controller에서 /login 을 안만들어도 됨
                .defaultSuccessUrl("/") // loginForm로와서 login을하면 /로 보내 줄텐데, 특정 페이지에서 로그인을 하면 그 주소로 바로 보내줌
                .and().oauth2Login() // oauth2 client 를 쓰면 로그인창이 /oauth2/authorization/google 고정인데, 404가 뜬다. 그래서 이처럼 해줘야함
//                .loginPage("/loginForm");
                .userInfoEndpoint()  // 구글 로그인이 완료된 뒤의 후처리가 필요함. TIP. 코드 x ( 엑세스 토큰 + 사용자 프로필정보를 한번에 받음 -> OAUTH Client가)
                .userService(principalOauth2UserService);
        ;
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        // password encoding
        return new BCryptPasswordEncoder();
    }
}
