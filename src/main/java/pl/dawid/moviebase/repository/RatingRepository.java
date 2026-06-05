package pl.dawid.moviebase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dawid.moviebase.model.Movie;
import pl.dawid.moviebase.model.Rating;
import pl.dawid.moviebase.model.User;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserAndMovie(User user, Movie movie);
    @Query("select coalesce(avg(r.ratingValue), 0) from Rating r where r.movie.id = :movieId")
    double averageForMovie(@Param("movieId") Long movieId);
}
