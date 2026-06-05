package pl.dawid.moviebase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dawid.moviebase.model.Movie;
import pl.dawid.moviebase.model.User;
import pl.dawid.moviebase.model.Watchlist;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    Optional<Watchlist> findByUserAndMovie(User user, Movie movie);

    Optional<Watchlist> findByIdAndUser(Long id, User user);

    List<Watchlist> findByUserOrderByCreatedAtDesc(User user);
}
