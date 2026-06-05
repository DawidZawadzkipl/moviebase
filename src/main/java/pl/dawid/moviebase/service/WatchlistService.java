package pl.dawid.moviebase.service;

import org.springframework.stereotype.Service;
import pl.dawid.moviebase.model.*;
import pl.dawid.moviebase.repository.WatchlistRepository;

import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public void addOrUpdate(User user, Movie movie, WatchStatus status) {
        Watchlist item = watchlistRepository.findByUserAndMovie(user, movie).orElseGet(Watchlist::new);
        item.setUser(user);
        item.setMovie(movie);
        item.setStatus(status);
        watchlistRepository.save(item);
    }

    public void updateStatus(Long id, User user, WatchStatus status) {
        Watchlist item = watchlistRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Watchlist item not found"));
        item.setStatus(status);
        watchlistRepository.save(item);
    }

    public void deleteOwn(Long id, User user) {
        Watchlist item = watchlistRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Watchlist item not found"));
        watchlistRepository.delete(item);
    }

    public List<Watchlist> forUser(User user) {
        return watchlistRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
