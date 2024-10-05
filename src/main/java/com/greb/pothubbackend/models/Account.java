package com.greb.pothubbackend.models;

import com.greb.pothubbackend.constraints.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id @UuidGenerator
    private String id;

    @Column(unique=true, nullable=false)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String fullName;

    private Boolean isEnabled = false;

    private String otpCode;

    private LocalDateTime otpTime;

    private String refreshToken;

    @Column(nullable=false)
    private UserRole role;
}
