package pl.dawid.moviebase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dawid.moviebase.model.Movie;
import pl.dawid.moviebase.model.Review;
import pl.dawid.moviebase.model.User;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMovieAndApprovedTrueOrderByCreatedAtDesc(Movie movie);

    List<Review> findByUserOrderByCreatedAtDesc(User user);

    List<Review> findAllByOrderByCreatedAtDesc();

    Optional<Review> findByIdAndUser(Long id, User user);
}
