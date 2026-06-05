package pl.dawid.moviebase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(max = 120)
    @Column(nullable = false)
    private String title;
    @Size(max = 120)
    private String originalTitle;
    @NotBlank @Size(min = 10, max = 3000)
    @Column(length = 3000, nullable = false)
    private String description;
    @Min(1888) @Max(2100)
    private Integer releaseYear;
    @Min(1) @Max(1000)
    private Integer durationMinutes;
    private String posterPath;
    private String trailerUrl;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Set<Genre> getGenres() { return genres; }
    public void setGenres(Set<Genre> genres) { this.genres = genres; }
}
