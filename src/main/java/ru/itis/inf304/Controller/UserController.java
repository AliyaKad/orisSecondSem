package ru.itis.inf304.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.inf304.dto.CreateUserDto;
import ru.itis.inf304.entity.User;
import ru.itis.inf304.Service.AuthService;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthService authService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new CreateUserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute CreateUserDto userDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            String baseUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            authService.registerUser(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
            redirectAttributes.addFlashAttribute("message", "Check your email to confirm registration");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam String code, Model model) {
        try {
            authService.confirmEmailByCode(code);
            model.addAttribute("message", "Email confirmed successfully!");
            return "confirmation-success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "confirmation-error";
        }
    }


    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        return authService.getAllUsers();
    }
}