package com.projetoWeb.Arenas.model;

import com.projetoWeb.Arenas.model.converter.PermissaoEnumsConverter;
import com.projetoWeb.Arenas.model.converter.RolePlayerConverter;
import com.projetoWeb.Arenas.model.enums.PermissaoEnums;
import com.projetoWeb.Arenas.model.enums.RolePlayer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 1000)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    @Convert(converter = PermissaoEnumsConverter.class)
    private PermissaoEnums role;

    @Column(columnDefinition = "BYTEA")
    private byte[] profilePic;

    @Column(length = 5000)
    private String profileDescription;

    @Column(length = 11)
    private String phone;

    @Column()
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @Column
    private LocalDate birthDate;

    @Column
    @Convert(converter = RolePlayerConverter.class)
    private RolePlayer rolePlayer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
