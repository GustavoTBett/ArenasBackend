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

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
