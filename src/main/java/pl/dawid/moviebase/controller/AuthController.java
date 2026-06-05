package pl.dawid.moviebase.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.dawid.moviebase.dto.RegisterForm;
import pl.dawid.moviebase.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegisterForm registerForm,
            BindingResult result,
            Model model) {
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "validation.passwordsDontMatch", "Hasła nie są takie same");
        }
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.register(registerForm);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
        return "redirect:/login?registered=true";
    }
}
