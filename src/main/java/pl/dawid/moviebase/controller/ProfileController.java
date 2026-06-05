package pl.dawid.moviebase.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.dawid.moviebase.model.User;
import pl.dawid.moviebase.model.WatchStatus;
import pl.dawid.moviebase.service.ReviewService;
import pl.dawid.moviebase.service.UserService;
import pl.dawid.moviebase.service.WatchlistService;

@Controller
public class ProfileController {

    private final UserService userService;
    private final WatchlistService watchlistService;
    private final ReviewService reviewService;

    public ProfileController(
            UserService userService,
            WatchlistService watchlistService,
            ReviewService reviewService) {
        this.userService = userService;
        this.watchlistService = watchlistService;
        this.reviewService = reviewService;
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        User user = userService.getByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("watchlist", watchlistService.forUser(user));
        model.addAttribute("reviews", reviewService.forUser(user));
        model.addAttribute("statuses", WatchStatus.values());
        return "profile/index";
    }

    @PostMapping("/profile/watchlist/{id}/status")
    public String updateWatchlistStatus(
            @PathVariable Long id,
            @RequestParam WatchStatus status,
            Authentication auth) {
        User user = userService.getByEmail(auth.getName());
        watchlistService.updateStatus(id, user, status);
        return "redirect:/profile";
    }

    @PostMapping("/profile/watchlist/{id}/delete")
    public String deleteWatchlistItem(@PathVariable Long id, Authentication auth) {
        User user = userService.getByEmail(auth.getName());
        watchlistService.deleteOwn(id, user);
        return "redirect:/profile";
    }

    @PostMapping("/profile/reviews/{id}/delete")
    public String deleteOwnReviewFromProfile(@PathVariable Long id, Authentication auth) {
        User user = userService.getByEmail(auth.getName());
        reviewService.deleteOwn(id, user);
        return "redirect:/profile";
    }
}
