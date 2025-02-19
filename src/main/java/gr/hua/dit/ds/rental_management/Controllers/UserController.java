package gr.hua.dit.ds.rental_management.Controllers;

import gr.hua.dit.ds.rental_management.Entities.*;
import gr.hua.dit.ds.rental_management.Repositories.*;
import gr.hua.dit.ds.rental_management.Services.OwnerService;
import gr.hua.dit.ds.rental_management.Services.TenantService;
import gr.hua.dit.ds.rental_management.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller

public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final TenantService tenantService;
    private final OwnerService ownerService;
    private final OwnerRepository ownerRepository;
    private final TenantRepository tenantRepository;


    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserController(RoleRepository roleRepository, UserService userService, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, TenantService tenantService, OwnerService ownerService, OwnerRepository ownerRepository, TenantRepository tenantRepository) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tenantService = tenantService;
        this.ownerService = ownerService;
        this.ownerRepository = ownerRepository;
        this.tenantRepository = tenantRepository;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users") //Προβάλλει τη λίστα όλων των χρηστών.
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "user/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{user_id}")//Προβάλλει τα στοιχεία ενός συγκεκριμένου χρήστη.
    public String showSpecificUser(@PathVariable Long user_id, Model model){
        model.addAttribute("user", userService.getUser(user_id));
        return "user/user";
    }




    @PreAuthorize("hasRole('ADMIN')") //Εμφανίζει τισ λεπτομέρειες για έναν χρήστη, όνομα , επίθετο και ρόλο
    @GetMapping("/{id}")
    public String viewUserDetails(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Το αντικείμενο user με το συγκεκριμένο id περνιέται στο model
        model.addAttribute("user", user);
        return "user/user_details"; // Επέστρεψε το template user_detail
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/edit/{id}") // Μέθοδος για την εμφάνιση της φόρμας edit του χρήστη
    public String showEditUserForm(@PathVariable Long id, Model model) {
        // Ανάκτηση του εκάστοτε χρήστη από την βάση με το ID
        User user = (User) userService.getUser(id); // Επιστρέφεται ένα User object

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Προσθέτουμε τους ρόλους και τον user στον model για να μπορούν να φανούν στην φόρμα
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("isEditMode", true);  //  editing mode είναι ενεργό, θέτωντας το isEdit σε true
        return "user/edit_user"; // Επέστρεψε το edit_form
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, Model model) {
        try {
            // Ανάκτηση user με το ID
            User existingUser = (User) userService.getUser(id);

            // Ενημέρωση των στοιχείων του χρήστη με τα δεδομένα που ήρθαν από τη φόρμα
            existingUser.setFirst_name(user.getFirst_name());
            existingUser.setLast_name(user.getLast_name());
            existingUser.setUsername(user.getUsername());
            existingUser.setRoles(user.getRoles());

            // Αν το πεδίο του κωδικού έχει τιμή, επικαιροποιούμε τον κωδικό με το νέο κωδικό (αν υπάρχει).
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));  // Κάνε Update τον password
            }

            // Κάνε save τον updated user
            userService.updateUser(existingUser);

            // Αν όλα πήγαν καλά δείξε μήνυμα επιτυχίασ
            model.addAttribute("msg", "User updated successfully!");

        } catch (Exception e) {
            // Αν ο user δεν βρέθηκε ή προέκυψε κάποιο λάθος , δείξε το αντίστοιχο μήνυμα
            model.addAttribute("error", "Error updating user: " + e.getMessage());
        }

        // Επέστρεψε το  user list
        return "redirect:/users";
    }




   /* @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user/{user_id}") //Ενημερώνει τα στοιχεία ενός υπάρχοντος χρήστη
    public String saveNewUser(@PathVariable Long user_id, @ModelAttribute("user") User user, Model model) {
        User new_user = (User) userService.getUser(user_id);
        new_user.setFirst_name(user.getFirst_name());
        new_user.setLast_name(user.getLast_name());
        new_user.setUsername(user.getUsername());
        userService.updateUser(new_user);
        model.addAttribute("users", userService.getUsers());
        return "user/users"; } */




    @GetMapping("/user/role/delete/{user_id}/{role_id}") // Διαγράφει έναν ρόλο από τον χρήστη
    public String deleteRolefromUser(@PathVariable Long user_id, @PathVariable Integer role_id, Model model) {
        // Ανάκτηση του εκάστοτε χρήστη με βάση το ID
        User user = (User) userService.getUser(user_id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Fetch the role by ID
        Roles role = roleRepository.findById(role_id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Αφαίρεση του ρόλου του
        user.getRoles().remove(role);
        userService.updateUser(user);

        //Έλενξε εάν ο χρήστης ήταν owner ή Tenant και αναλόγως διέγραψέ και το προφίλ του (δεν γίνεται δηλαδή να αφαιρέσουμε έναν ρόλο tenant από τον χρήστη και να συνεχίσει να έχει tenant_id στην βάση)
        if ("OWNER".equals(role.getName())) {
            deleteOwnerIfExists(user.getUsername()); // Καλέι την deleteOwnerIfExists που ορίζεται παρακάτω
        } else if ("TENANT".equals(role.getName())) {
            deleteTenantIfExists(user.getUsername()); // Καλέι την deleteTenantIfExists που ορίζεται παρακάτω
        }

        // Ενημέρωση των  model attributes
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());

        return "user/users"; // Επέστρεψε στην  user list page
    }



 @GetMapping("/user/role/add/{user_id}/{role_id}") // Προσθέτει έναν ρόλο στον χρήστη
 public String addNewRole(@PathVariable Long user_id, @PathVariable Integer role_id) {
     // Ανακτά τον user με το συκγεκριμένο ID
     User user = (User) userService.getUser(user_id);
     if (user == null) {
         throw new RuntimeException("User not found");
     }

     // Ανακτά τον ρόλο του user
     Roles role = roleRepository.findById(role_id)
             .orElseThrow(() -> new RuntimeException("Role not found"));

     // Φέρνει τους διαθέσιμους ρόλους και τους προσθέτει στον χρήστη
     user.getRoles().add(role);

     // Ο χρήστης με τον νέο ρόλο αποθηκεύεται
     userService.updateUser(user);

     //Επιστροφή στην λίστα με τους users
     return "redirect:/users";
 }


    @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/user/delete/{id}") //Διαγραφή ενός χρήστη από τον admin
  public String deleteUser(@PathVariable Long id, Model model) {
      // Ανάκτηση του χρήστη πριν την διαγραφή
      User user = (User) userService.getUser(id);

      if (user == null) {
          throw new RuntimeException("User not found");
      }

      String username = user.getUsername(); // Κάνει extract το username

      //Με βάση το username, εάν ο χρήστης που πρόκειται να διαγραφθεί είναι owner ή tenant (που θα είναι προφανώς) διέγραψε και το αντίστοιχο προφίλ του , είτε tenant είται owner
      deleteTenantIfExists(username);
      deleteOwnerIfExists(username);

      //Διέγραψε τον και από τον πίνακα users
      userService.deleteUser(id);

      // Ανανέωσε τους users χωρίς τον user Που μόλις διαγράφθηκε
      model.addAttribute("users", userService.getUsers());

      return "redirect:/users"; // επέστρεψε στην λίστα με τους users
  }


   // Μέθοδος που ελέγχει εάν ο χρήστης με το αντίστοιχο username έχει προφίλ tenant,έτσι ώστε αν έχει να το διαγράψει και αυτό
    private void deleteTenantIfExists(String username) {
        // Ελέγχει εάν υπάρχει tenant με το συκγεκριμένο username
        Tenant tenant = tenantRepository.findByUsername(username);

        if (tenant != null) { // Αν υπάρχει διαγράφει το tenant προφιλ
            tenantService.deleteTenantByUsername(username);
        }
    }

    // Μέθοδος που ελέγχει εάν ο χρήστης με το αντίστοιχο username έχει προφίλ owner,έτσι ώστε αν έχει να το διαγράψει και αυτό
    private void deleteOwnerIfExists(String username) {
        // Ελέγχει εάν υπάρχει owner με το συκγεκριμένο username
        Owner owner = ownerRepository.findByUsername(username);

        if (owner != null) { // Αν υπάρχει διαγράφει το owner προφιλ
            ownerService.deleteOwnerByUsername(username);
        }
    }
    }






