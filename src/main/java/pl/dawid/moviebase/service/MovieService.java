package pl.dawid.moviebase.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dawid.moviebase.model.Movie;
import pl.dawid.moviebase.repository.MovieRepository;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Page<Movie> search(String q, Pageable pageable) {
        return movieRepository.search(q, pageable);
    }

    public Page<Movie> search(String q, Long genreId, Integer yearFrom, Integer yearTo, Pageable pageable) {
        return movieRepository.search(normalize(q), genreId, yearFrom, yearTo, pageable);
    }

    public List<Movie> latest() {
        return movieRepository.findTop4ByOrderByCreatedAtDesc();
    }

    public Movie get(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public void delete(Long id) {
        movieRepository.deleteById(id);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }
}
