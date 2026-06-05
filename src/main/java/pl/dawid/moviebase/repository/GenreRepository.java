package pl.dawid.moviebase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dawid.moviebase.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByNamePl(String namePl);
}
