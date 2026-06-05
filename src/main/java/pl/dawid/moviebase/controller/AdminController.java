package pl.dawid.moviebase.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dawid.moviebase.model.Genre;
import pl.dawid.moviebase.model.Movie;
import pl.dawid.moviebase.repository.GenreRepository;
import pl.dawid.moviebase.repository.UserRepository;
import pl.dawid.moviebase.service.FileStorageService;
import pl.dawid.moviebase.service.GenreService;
import pl.dawid.moviebase.service.MovieService;
import pl.dawid.moviebase.service.ReviewService;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MovieService movieService;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final ReviewService reviewService;
    private final GenreService genreService;

    public AdminController(
            MovieService movieService,
            GenreRepository genreRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService,
            ReviewService reviewService,
            GenreService genreService) {
        this.movieService = movieService;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.reviewService = reviewService;
        this.genreService = genreService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("users", userRepository.count());
        model.addAttribute("genres", genreRepository.count());
        return "admin/dashboard";
    }

    @GetMapping("/reviews")
    public String reviews(Model model) {
        model.addAttribute("reviews", reviewService.all());
        return "admin/reviews";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return "redirect:/admin/reviews";
    }

    @GetMapping("/movies")
    public String movies(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        model.addAttribute("movies", movieService.search(q, PageRequest.of(Math.max(page, 0), 10, Sort.by("title"))));
        model.addAttribute("q", q);
        return "admin/movies";
    }

    @GetMapping("/movies/new")
    public String newMovie(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("genres", genreRepository.findAll());
        return "admin/movie-form";
    }

    @PostMapping("/movies")
    public String save(
            @Valid @ModelAttribute Movie movie,
            BindingResult result,
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam(required = false) MultipartFile poster,
            Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("genres", genreRepository.findAll());
            return "admin/movie-form";
        }
        movie.setGenres(new HashSet<>(genreRepository.findAllById(genreIds == null ? List.of() : genreIds)));
        String path = fileStorageService.save(poster);
        if (path != null) {
            movie.setPosterPath(path);
        }
        movieService.save(movie);
        return "redirect:/admin/movies";
    }

    @GetMapping("/movies/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.get(id));
        model.addAttribute("genres", genreRepository.findAll());
        return "admin/movie-form";
    }

    @PostMapping("/movies/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute Movie movie,
            BindingResult result,
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam(required = false) MultipartFile poster,
            Model model) throws IOException {
        Movie existing = movieService.get(id);
        if (result.hasErrors()) {
            movie.setId(id);
            model.addAttribute("genres", genreRepository.findAll());
            return "admin/movie-form";
        }
        movie.setId(id);
        movie.setCreatedAt(existing.getCreatedAt());
        movie.setPosterPath(existing.getPosterPath());
        movie.setGenres(new HashSet<>(genreRepository.findAllById(genreIds == null ? List.of() : genreIds)));
        String path = fileStorageService.save(poster);
        if (path != null) {
            movie.setPosterPath(path);
        }
        movieService.save(movie);
        return "redirect:/admin/movies";
    }

    @PostMapping("/movies/{id}/delete")
    public String delete(@PathVariable Long id) {
        movieService.delete(id);
        return "redirect:/admin/movies";
    }

    @GetMapping("/genres")
    public String genres(Model model) {
        model.addAttribute("genres", genreService.all());
        return "admin/genres";
    }

    @GetMapping("/genres/new")
    public String newGenre(Model model) {
        model.addAttribute("genre", new Genre());
        return "admin/genre-form";
    }

    @PostMapping("/genres")
    public String saveGenre(
            @Valid @ModelAttribute Genre genre,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "admin/genre-form";
        }
        try {
            genreService.save(genre);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/genre-form";
        }
        return "redirect:/admin/genres";
    }

    @GetMapping("/genres/{id}/edit")
    public String editGenre(@PathVariable Long id, Model model) {
        model.addAttribute("genre", genreService.get(id));
        return "admin/genre-form";
    }

    @PostMapping("/genres/{id}")
    public String updateGenre(
            @PathVariable Long id,
            @Valid @ModelAttribute Genre genre,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            genre.setId(id);
            return "admin/genre-form";
        }
        try {
            genre.setId(id);
            genreService.save(genre);
        } catch (IllegalArgumentException e) {
            genre.setId(id);
            model.addAttribute("error", e.getMessage());
            return "admin/genre-form";
        }
        return "redirect:/admin/genres";
    }

    @PostMapping("/genres/{id}/delete")
    public String deleteGenre(@PathVariable Long id) {
        genreService.delete(id);
        return "redirect:/admin/genres";
    }
}
