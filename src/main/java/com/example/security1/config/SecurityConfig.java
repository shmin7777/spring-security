package com.example.security1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity // spring security filter가 spring filter chain에 등록이 됨
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // CSRF disable
        http.authorizeRequests()
                // /user/** 이 주소로 들어오면 인증이 필요해!
                .antMatchers("/user/**") // login안하고 /user 로 접근하면 403 error 접근권한이 없다.
                .authenticated()
                .antMatchers("/manager/**")
                // /manager/** 위 경로로 들어오면 인증 뿐만 아니라 ROLE_ADMIN이나 ROLE_MANAGER의 권한이 필요해!
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**")
                // /manager/** 위 경로로 들어오면 인증 뿐만 아니라 ROLE_ADMIN이나 ROLE_MANAGER의 권한이 필요해!
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 위 경로가 아닌 주소들은 전부 허용
                .and()
                .formLogin()
                .loginPage("/login") // 권한이 없는 페이지에 요청이 들어갈 때 login페이지로 이동(redirect /login)
                ;
    }
}
