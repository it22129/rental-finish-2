package gr.hua.dit.ds.rental_management.Controllers;

import gr.hua.dit.ds.rental_management.Entities.Roles;
import gr.hua.dit.ds.rental_management.Entities.User;
import gr.hua.dit.ds.rental_management.Repositories.RoleRepository;
import gr.hua.dit.ds.rental_management.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller

public class UserController {

    private final UserService userService;

    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserController(RoleRepository roleRepository, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }



    // Φόρμα για δημιουργία νέου χρήστη
    @GetMapping("/register")
    public String newUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "basic/register";// Επιστρέφει την φόρμα εγγραφής του χρήστη
    }

    @PostMapping("/saveNewUser") // Αποθηκεύει έναν νέο χρήστη στο σύστημα
    public String saveNewUser(@ModelAttribute User user,@RequestParam("role") String role, Model model){
        //Έλεγχος αν το username υπάρχει ήδη
        if (userService.userExists(user.getUsername())){
            model.addAttribute("error", "This user already exists!");
            return "basic/register"; //επιστροφή στην φόρμα εγγραφής
        }

        // Τύπωση των ρόλων debugging
        System.out.println("Roles: " + user.getRoles());

        //Βρες τον επιλεγμένο ρόλο από repository
        Roles selectedRole = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Role not found: " + role));

        //ανάθεση του ρόλου στον χρήστη
        user.setRoles(Set.of(selectedRole));

        // Encode τον κωδικό
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);  // Encode password
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);  // Φόρτωσε encoded password
        user.setPassword(encodedPassword);  // Θέσε τον encoded password

        // Αποθήκευσε τον  user and ανάκτησε το ID του
        Integer id = userService.saveUser(user);

        // Δείξε success message
        String message = "New user with '" + id + "' just saved successfully!";
        System.out.println("User Details:" + user);
        model.addAttribute("msg", message);

        // Ανακατεύθυνση στο index page
        return "basic/login";
    }

    @GetMapping("/users") //Προβάλλει τη λίστα όλων των χρηστών.
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "user/users";
    }

    @GetMapping("/user/{user_id}")//Προβάλλει τα στοιχεία ενός συγκεκριμένου χρήστη.
    public String showSpecificUser(@PathVariable Long user_id, Model model){
        model.addAttribute("user", userService.getUser(user_id));
        return "user/user";
    }

    @PostMapping("/user/{user_id}") //Ενημερώνει τα στοιχεία ενός υπάρχοντος χρήστη
    public String saveNewUser(@PathVariable Long user_id, @ModelAttribute("user") User user, Model model) {
        User new_user = (User) userService.getUser(user_id);
        new_user.setFirst_name(user.getFirst_name());
        new_user.setLast_name(user.getLast_name());
        new_user.setUsername(user.getUsername());
        userService.updateUser(new_user);
        model.addAttribute("users", userService.getUsers());
        return "user/users"; }


    @GetMapping("/user/role/delete/{user_id}/{role_id}")// Διαγράφει έναν ρόλο από έναν χρήστη
    public String deleteRolefromUser(@PathVariable Long user_id, @PathVariable Integer role_id, Model model){
        User user = (User) userService.getUser(user_id);
        Roles role = roleRepository.findById(role_id).get();
        user.getRoles().remove(role);
        System.out.println("Roles: "+user.getRoles());
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "users/users";

    }
    @GetMapping("/user/role/add/{user_id}/{role_id}")//Προσθέτει έναν νέο ρόλο σε έναν χρήστη
    public String addNewRole(@PathVariable Long user_id, @PathVariable Integer role_id, Model model){
        User user = (User) userService.getUser(user_id);
        Roles role = roleRepository.findById(role_id).get();
        user.getRoles().add(role);
        System.out.println("Roles: "+user.getRoles());
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "users/users";

    }

    //Διαγραφή χρήστη
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model){
        userService.deleteUser(id);
        model.addAttribute("users", userService.getUsers());
        return "user/users";
    }


}
