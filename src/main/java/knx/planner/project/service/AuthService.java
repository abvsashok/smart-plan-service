package knx.planner.project.service;

import jakarta.transaction.Transactional;
import knx.planner.project.auth.AuthResponse;
import knx.planner.project.auth.RegisterRequest;
import knx.planner.project.auth.RegisterResponse;
import knx.planner.project.entity.User;
import knx.planner.project.repository.UserRepository;
import knx.planner.project.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public RegisterResponse register(RegisterRequest req) {
        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setEmail(req.email());
        user.setFirstName(req.name());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole("USER"); // adapt if you have an enum or roles table

        userRepository.save(user);

//        var userDetails = new User(user.getEmail(), user.getPassword(), user.getRole());
//        String token = jwtService.generateToken(userDetails);

        return new RegisterResponse("User registered successfully");
    }
}