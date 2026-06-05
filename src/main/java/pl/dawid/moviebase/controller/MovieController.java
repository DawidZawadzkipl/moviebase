package pl.dawid.moviebase.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.dawid.moviebase.model.*;
import pl.dawid.moviebase.service.*;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final RatingService ratingService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final WatchlistService watchlistService;

    public MovieController(
            MovieService movieService,
            RatingService ratingService,
            ReviewService reviewService,
            UserService userService,
            WatchlistService watchlistService) {
        this.movieService = movieService;
        this.ratingService = ratingService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.watchlistService = watchlistService;
    }

    @GetMapping({"/", "/movies"})
    public String movies(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        model.addAttribute("movies", movieService.search(q, PageRequest.of(page, 8, Sort.by("title"))));
        model.addAttribute("q", q);
        return "movies/list";
    }

    @GetMapping("/movies/{id}")
    public String details(@PathVariable Long id, Model model) {
        Movie movie = movieService.get(id);
        model.addAttribute("movie", movie);
        model.addAttribute("average", ratingService.average(id));
        model.addAttribute("reviews", reviewService.forMovie(movie));
        model.addAttribute("statuses", WatchStatus.values());
        return "movies/details";
    }

    @PostMapping("/movies/{id}/ratings")
    public String rate(
            @PathVariable Long id,
            @RequestParam @Min(1) @Max(10) int value,
            Authentication auth) {
        User user = userService.getByEmail(auth.getName());
        ratingService.rate(user, movieService.get(id), value);
        return "redirect:/movies/" + id;
    }

    @PostMapping("/movies/{id}/reviews")
    public String review(
            @PathVariable Long id,
            @RequestParam @Size(min = 20, max = 2000) String content,
            Authentication auth) {
        User user = userService.getByEmail(auth.getName());
        reviewService.add(user, movieService.get(id), content);
        return "redirect:/movies/" + id;
    }

    @PostMapping("/movies/{id}/watchlist")
    public String watchlist(
            @PathVariable Long id,
            @RequestParam WatchStatus status,
            Authentication auth) {
        User user = userService.getByEmail(auth.getName());
        watchlistService.addOrUpdate(user, movieService.get(id), status);
        return "redirect:/movies/" + id;
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteOwnReview(@PathVariable Long id, Authentication auth) {
        User user = userService.getByEmail(auth.getName());
        Long movieId = reviewService.deleteOwn(id, user);
        return "redirect:/movies/" + movieId;
    }
}
