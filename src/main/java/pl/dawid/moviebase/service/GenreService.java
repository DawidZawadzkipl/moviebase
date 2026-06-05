package pl.dawid.moviebase.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.dawid.moviebase.model.Genre;
import pl.dawid.moviebase.model.Movie;
import pl.dawid.moviebase.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> all() {
        return genreRepository.findAll().stream()
                .sorted(Comparator.comparing(Genre::getNamePl, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public Genre get(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found"));
    }

    public Genre save(Genre genre) {
        genre.setNamePl(normalize(genre.getNamePl()));
        genre.setNameEn(normalize(genre.getNameEn()));
        ensureUnique(genre);
        return genreRepository.save(genre);
    }

    @Transactional
    public void delete(Long id) {
        Genre genre = get(id);
        for (Movie movie : new ArrayList<>(genre.getMovies())) {
            movie.getGenres().remove(genre);
        }
        genreRepository.delete(genre);
    }

    private void ensureUnique(Genre genre) {
        genreRepository.findByNamePlIgnoreCase(genre.getNamePl())
                .filter(existing -> !existing.getId().equals(genre.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Gatunek PL juz istnieje");
                });

        genreRepository.findByNameEnIgnoreCase(genre.getNameEn())
                .filter(existing -> !existing.getId().equals(genre.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Gatunek EN juz istnieje");
                });
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
