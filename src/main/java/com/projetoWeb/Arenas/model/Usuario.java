package com.projetoWeb.Arenas.model;

import com.projetoWeb.Arenas.model.converter.PermissaoEnumsConverter;
import com.projetoWeb.Arenas.model.enums.PermissaoEnums;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Entity
@Table(name = "users")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    @Convert(converter = PermissaoEnumsConverter.class)
    private PermissaoEnums role;

    public boolean isLoginCorrect(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }
}
