package pl.dawid.moviebase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "movie_id"}))
public class Rating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) private User user;
    @ManyToOne(optional = false) private Movie movie;
    @Min(1) @Max(10) private int ratingValue;
    private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }
    public int getRatingValue() { return ratingValue; }
    public void setRatingValue(int ratingValue) { this.ratingValue = ratingValue; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
