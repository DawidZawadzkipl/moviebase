package pl.dawid.moviebase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Genre {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(max = 80) @Column(nullable = false, unique = true)
    private String namePl;
    @NotBlank @Size(max = 80) @Column(nullable = false, unique = true)
    private String nameEn;
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamePl() { return namePl; }
    public void setNamePl(String namePl) { this.namePl = namePl; }
    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }
    public Set<Movie> getMovies() { return movies; }
    public void setMovies(Set<Movie> movies) { this.movies = movies; }
}
