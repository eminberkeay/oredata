package com.oredata.bookstoreapi.service;

import com.oredata.bookstoreapi.entity.Role;
import com.oredata.bookstoreapi.entity.User;
import com.oredata.bookstoreapi.entity.UserRole;
import com.oredata.bookstoreapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import com.oredata.bookstoreapi.exception.ExistingUserException;
import com.oredata.bookstoreapi.exception.UserNotFoundException;
import com.oredata.bookstoreapi.exception.WrongPasswordException;
import com.oredata.bookstoreapi.repository.RoleRepository;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
    }

    @Override
    public User signUp(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ExistingUserException("Bu e-posta adresi ile kayıtlı bir kullanıcı mevcut!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Ensure that user.getRoles() returns a collection of Role
        Set<Role> userRoles = user.getRoles().stream()
                .map(role -> findRoleByName(role.getName().name()))
                .collect(Collectors.toSet());

        // Set the roles back to the user
        user.setRoles(userRoles);

        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Kullanıcı bulunamadı!");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException("Parola hatalı. Lütfen tekrar deneyin.");
        }

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        long jwtExpirationMs = 26 * 60 * 60 * 1000;

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public User getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElse(null);
    }
}
