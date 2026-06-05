package pl.dawid.moviebase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dawid.moviebase.model.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByNamePl(String namePl);

    Optional<Genre> findByNamePlIgnoreCase(String namePl);

    Optional<Genre> findByNameEnIgnoreCase(String nameEn);
}
