package com.example.security1.repository;

import com.example.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// CRUD 함수를 JpaRepository가 들고 있음.
// @Repository라는 어노테이션이 없어도 IOC됨.
// 이유는 JpaRepository를 상속했기 떄문에
public interface UserRepository extends JpaRepository<User, Integer> {
    // select * from user where username = 1?
    Optional<User> findByUsername(String username);
}
