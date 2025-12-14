package knx.planner.project.controller;


import knx.planner.project.auth.AuthRequest;
import knx.planner.project.auth.AuthResponse;
import knx.planner.project.auth.RegisterRequest;
import knx.planner.project.auth.RegisterResponse;
import knx.planner.project.entity.User;
import knx.planner.project.security.JwtService;
import knx.planner.project.service.AuthService;
import knx.planner.project.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthService authService;

    @GetMapping("/test")
    public ResponseEntity<?> isAppRunning() {
        return ResponseEntity.ok("Authentication service is running");
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        Authentication auth = new UsernamePasswordAuthenticationToken(req.username(), req.password());
        authManager.authenticate(auth);
        User user = userDetailsService.loadUserByUsername(req.username());
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req) {
        RegisterResponse resp = authService.register(req);
        return ResponseEntity.ok(resp);
    }
}