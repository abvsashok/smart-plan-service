package knx.planner.project.service;

import knx.planner.project.entity.User;
import knx.planner.project.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with " + email + ", please register first."));
        return user;// new User(user.getEmail(), user.getPassword(), user.getRole());
    }
}