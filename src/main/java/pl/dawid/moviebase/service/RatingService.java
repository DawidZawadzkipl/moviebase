package pl.dawid.moviebase.service;

import org.springframework.stereotype.Service;
import pl.dawid.moviebase.model.Movie;
import pl.dawid.moviebase.model.Rating;
import pl.dawid.moviebase.model.User;
import pl.dawid.moviebase.repository.RatingRepository;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    public RatingService(RatingRepository ratingRepository) { this.ratingRepository = ratingRepository; }
    public void rate(User user, Movie movie, int value) {
        Rating rating = ratingRepository.findByUserAndMovie(user, movie).orElseGet(Rating::new);
        rating.setUser(user);
        rating.setMovie(movie);
        rating.setRatingValue(value);
        ratingRepository.save(rating);
    }
    public double average(Long movieId) { return ratingRepository.averageForMovie(movieId); }
}
