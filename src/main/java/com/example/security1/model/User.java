package com.example.security1.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
//@Getter
//@Builder
//@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString
public class User {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; // ROLE_USER, ROLE_ADMIN
    @CreationTimestamp // insert 시점에 자동으로 현재 시간을 값으로 채워서 쿼리 생성
    private Timestamp createDate;

}
