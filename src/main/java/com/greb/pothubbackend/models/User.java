package com.greb.pothubbackend.models;

import com.greb.pothubbackend.constraints.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
public class User {
    @Id @UuidGenerator
    private String id;

    @Column(unique=true, nullable=false)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String fullName;

    private String avatarUrl;

    private Boolean isEnabled = false;

    private String otpCode;

    private LocalDateTime otpExpiryDate;

    @Column(nullable=false)
    private UserRole role;
}
