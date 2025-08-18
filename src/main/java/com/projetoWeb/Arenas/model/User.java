package com.projetoWeb.Arenas.model;

import com.projetoWeb.Arenas.model.converter.PermissaoEnumsConverter;
import com.projetoWeb.Arenas.model.converter.RolePlayerConverter;
import com.projetoWeb.Arenas.model.enums.PermissaoEnums;
import com.projetoWeb.Arenas.model.enums.RolePlayer;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false, length = 1000)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    @Convert(converter = PermissaoEnumsConverter.class)
    private PermissaoEnums role;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] profilePic;

    @Column(length = 5000)
    private String profileDescription;

    @Column(length = 11)
    private String phone;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column
    @Convert(converter = RolePlayerConverter.class)
    private RolePlayer rolePlayer;

    @ManyToMany(mappedBy = "user")
    private List<Match> matches;

    public boolean isLoginCorrect(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }
}
