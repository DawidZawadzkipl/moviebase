package pl.dawid.moviebase.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dawid.moviebase.model.Movie;
import pl.dawid.moviebase.repository.GenreRepository;
import pl.dawid.moviebase.repository.UserRepository;
import pl.dawid.moviebase.service.FileStorageService;
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

    public AdminController(
            MovieService movieService,
            GenreRepository genreRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService,
            ReviewService reviewService) {
        this.movieService = movieService;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("users", userRepository.count());
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
        model.addAttribute("movies", movieService.search(q, PageRequest.of(page, 10, Sort.by("title"))));
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
}
