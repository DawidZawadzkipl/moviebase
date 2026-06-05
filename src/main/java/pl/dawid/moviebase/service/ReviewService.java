package pl.dawid.moviebase.service;

import org.springframework.stereotype.Service;
import pl.dawid.moviebase.model.Movie;
import pl.dawid.moviebase.model.Review;
import pl.dawid.moviebase.model.User;
import pl.dawid.moviebase.repository.ReviewRepository;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> forMovie(Movie movie) {
        return reviewRepository.findByMovieAndApprovedTrueOrderByCreatedAtDesc(movie);
    }

    public List<Review> forUser(User user) {
        return reviewRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Review> all() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    public void add(User user, Movie movie, String content) {
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setContent(content);
        reviewRepository.save(review);
    }

    public Long deleteOwn(Long id, User user) {
        Review review = reviewRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        Long movieId = review.getMovie().getId();
        reviewRepository.delete(review);
        return movieId;
    }

    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}
