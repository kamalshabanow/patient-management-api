package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // AuthService.java daxilində

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        var userOptional = userService.findByEmail(loginRequestDTO.getEmail());

        if (userOptional.isEmpty()) {
            System.out.println("❌ LOGIN ERROR: User tapılmadı! Email: " + loginRequestDTO.getEmail());
            return Optional.empty();
        }

        var user = userOptional.get();

        // DB-dən gələn şifrəni və userin göndərdiyini yoxlayaq
        boolean isMatch = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());

        System.out.println("🔍 DEBUG INFO:");
        System.out.println("   Input Email: " + loginRequestDTO.getEmail());
        System.out.println("   DB Email:    " + user.getEmail());
        System.out.println("   DB Hash:     " + user.getPassword()); // Hash-in tam düşdüyünü yoxlamaq üçün
        System.out.println("   Match Result: " + isMatch);

        if (!isMatch) {
            System.out.println("❌ LOGIN ERROR: Şifrə uyğun gəlmir!");
            return Optional.empty();
        }

        System.out.println("✅ LOGIN SUCCESS: Token yaradılır...");
        return Optional.of(jwtUtil.generateToken(user.getEmail(), user.getRole()));
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "kamal2004";
        String encodedPassword = encoder.encode(password);

        System.out.println("New hash:   " +  encodedPassword);
    }
}
