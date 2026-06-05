package pl.dawid.moviebase.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dawid.moviebase.dto.RegisterForm;
import pl.dawid.moviebase.model.Role;
import pl.dawid.moviebase.model.User;
import pl.dawid.moviebase.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User register(RegisterForm form) {
        if (userRepository.existsByEmail(form.getEmail())) throw new IllegalArgumentException("Email already exists");
        if (userRepository.existsByUsername(form.getUsername())) throw new IllegalArgumentException("Username already exists");
        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
