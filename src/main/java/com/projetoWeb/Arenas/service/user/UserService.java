package com.projetoWeb.Arenas.service.user;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.projetoWeb.Arenas.controller.user.dto.CreateUserDto;
import com.projetoWeb.Arenas.controller.user.dto.UpdateUserDto;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.model.enums.PermissaoEnums;
import com.projetoWeb.Arenas.repository.UserRepository;
import com.projetoWeb.Arenas.service.exception.AlreadyExistsEmailUserException;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Long createdUser(CreateUserDto createUserDto) {
        Optional<User> userFromDb = userRepository.findByEmail(createUserDto.email());
        if (userFromDb.isPresent()) {
            throw new AlreadyExistsEmailUserException("O email informado já está cadastrado no sistema.");
        }

        User user = User.builder()
                .email(createUserDto.email())
                .password(passwordEncoder.encode(createUserDto.password()))
                .role(PermissaoEnums.BASICO)
                .build();
        user = userRepository.save(user);

        return user.getId();
    }

    @Transactional
    public User createUserByGoogle(String email) {
        String passoword = UUID.randomUUID().toString();

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(passoword))
                .role(PermissaoEnums.BASICO)
                .build();
        user = userRepository.save(user);

        return user;
    }

    @Transactional
    public String getProfilePictureUrl(Long id) {
        User user = getUserById(id);
        byte[] profilePic = user.getProfilePic();
        if (profilePic != null && profilePic.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(profilePic);
            return "data:image/png;base64," + base64Image;
        } else {
            return null;
        }
    }

    @Transactional
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new EntityNotExistsException("O usuário com o id informado não existe na base de dados");
    }

    @Transactional
    public User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new EntityNotExistsException("O usuário com o email informado não existe na base de dados");
    }

    @Transactional
    public Long updateUser(Long userId, UpdateUserDto updateUserDto) {
        User user = getUserById(userId);
        user.setBirthDate(updateUserDto.birthDate());
        user.setPhone(updateUserDto.phone());
        user.setProfileDescription(updateUserDto.profileDescripton());
        user.setRolePlayer(updateUserDto.rolePlayer());
        user.setFirstName(updateUserDto.firstName());
        user.setLastName(updateUserDto.lastName());

        String base64Pic = updateUserDto.profilePic();

        if (base64Pic != null && !base64Pic.isEmpty()) {
            if (base64Pic.startsWith("data:image")) {
                base64Pic = base64Pic.split(",")[1];
            }
            byte[] imageBytes = Base64.getDecoder().decode(base64Pic);

            // Verifica tamanho da imagem (máximo 5MB)
            if (imageBytes.length > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("Imagem não pode exceder 5MB");
            }
            user.setProfilePic(imageBytes);
        }

        user = userRepository.save(user);

        return user.getId();
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
