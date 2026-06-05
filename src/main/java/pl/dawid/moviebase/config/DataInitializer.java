package pl.dawid.moviebase.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dawid.moviebase.model.*;
import pl.dawid.moviebase.repository.*;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            UserRepository users,
            GenreRepository genres,
            MovieRepository movies,
            PasswordEncoder encoder) {
        return args -> {
            if (!users.existsByEmail("admin@example.com")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                users.save(admin);
            }
            if (!users.existsByEmail("user@example.com")) {
                User user = new User();
                user.setUsername("user");
                user.setEmail("user@example.com");
                user.setPassword(encoder.encode("user123"));
                user.setRole(Role.USER);
                users.save(user);
            }
            createGenre(genres, "Akcja", "Action");
            createGenre(genres, "Dramat", "Drama");
            createGenre(genres, "Sci-Fi", "Sci-Fi");
            if (movies.count() == 0) {
                Movie movie = new Movie();
                movie.setTitle("Interstellar");
                movie.setOriginalTitle("Interstellar");
                movie.setDescription(
                        "Grupa badaczy wyrusza przez tunel czasoprzestrzenny, aby znaleźć nowy dom dla ludzkości.");
                movie.setReleaseYear(2014);
                movie.setDurationMinutes(169);
                movie.getGenres().addAll(genres.findAll());
                movies.save(movie);
            }
        };
    }

    private void createGenre(GenreRepository repo, String pl, String en) {
        if (!repo.existsByNamePl(pl)) {
            Genre genre = new Genre();
            genre.setNamePl(pl);
            genre.setNameEn(en);
            repo.save(genre);
        }
    }
}
