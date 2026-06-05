package pl.dawid.moviebase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.dawid.moviebase.repository.GenreRepository;
import pl.dawid.moviebase.repository.MovieRepository;
import pl.dawid.moviebase.repository.ReviewRepository;
import pl.dawid.moviebase.service.MovieService;

@Controller
public class HomeController {

    private final MovieService movieService;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;

    public HomeController(
            MovieService movieService,
            MovieRepository movieRepository,
            GenreRepository genreRepository,
            ReviewRepository reviewRepository) {
        this.movieService = movieService;
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featuredMovies", movieService.latest());
        model.addAttribute("movieCount", movieRepository.count());
        model.addAttribute("genreCount", genreRepository.count());
        model.addAttribute("reviewCount", reviewRepository.count());
        return "home";
    }
}
