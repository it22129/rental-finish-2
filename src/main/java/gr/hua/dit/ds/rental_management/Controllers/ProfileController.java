package gr.hua.dit.ds.rental_management.Controllers;

import gr.hua.dit.ds.rental_management.Entities.User;
import gr.hua.dit.ds.rental_management.Repositories.UserRepository;
import gr.hua.dit.ds.rental_management.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ProfileController {

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile") //Μέθοδος για την προβολή του προφίλ του συνδεδεμένου χρήστη
    public String viewProfile(Model model, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        model.addAttribute("user", user.orElse(null));
        return "user/profile";
    }

    @PostMapping("/profile") //Μέθοδος για την ενημέρωση του προφίλ του χρήστη
    public String updateProfile(@ModelAttribute("user") User updatedUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login"; // Αν ο χρήστης δεν είναι authenticated, πήγαινέ τον στο login
        }

        String username = auth.getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return "redirect:/profile?error"; // Αν δεν βρεθεί χρήστης, redirect με error
        }

        User currentUser = optionalUser.get();
        currentUser.setFirst_name(updatedUser.getFirst_name());
        currentUser.setLast_name(updatedUser.getLast_name());
        currentUser.setUsername(updatedUser.getUsername());

        userRepository.save(currentUser);

        return "redirect:/profile?success";
    }
}
