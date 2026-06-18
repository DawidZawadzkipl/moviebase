package pl.dawid.moviebase.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dawid.moviebase.model.Genre;
import pl.dawid.moviebase.model.Role;
import pl.dawid.moviebase.model.User;
import pl.dawid.moviebase.repository.GenreRepository;
import pl.dawid.moviebase.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            UserRepository users,
            GenreRepository genres,
            PasswordEncoder encoder) {
        return args -> {
            createUser(users, encoder, "admin", "admin@example.com", "admin123", Role.ADMIN);
            createUser(users, encoder, "user", "user@example.com", "user123", Role.USER);

            createGenre(genres, "Akcja", "Action");
            createGenre(genres, "Dramat", "Drama");
            createGenre(genres, "Sci-Fi", "Sci-Fi");
            createGenre(genres, "Thriller", "Thriller");
            createGenre(genres, "Przygodowy", "Adventure");
            createGenre(genres, "Fantasy", "Fantasy");
            createGenre(genres, "Animacja", "Animation");
            createGenre(genres, "Kryminal", "Crime");
            createGenre(genres, "Komedia", "Comedy");
            createGenre(genres, "Biograficzny", "Biography");
        };
    }

    private User createUser(
            UserRepository users,
            PasswordEncoder encoder,
            String username,
            String email,
            String password,
            Role role) {
        return users.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(encoder.encode(password));
            user.setRole(role);
            return users.save(user);
        });
    }

    private Genre createGenre(GenreRepository repo, String pl, String en) {
        return repo.findByNamePlIgnoreCase(pl)
                .or(() -> repo.findByNameEnIgnoreCase(en))
                .orElseGet(() -> {
                    Genre genre = new Genre();
                    genre.setNamePl(pl);
                    genre.setNameEn(en);
                    return repo.save(genre);
                });
    }
}
