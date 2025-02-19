package gr.hua.dit.ds.rental_management.Config;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

    @Service
    public class SecurityService {
        public String getCurrentUsername() {
            return SecurityContextHolder.getContext().getAuthentication().getName();  // Επιστρέφει το όνομα χρήστη του τρέχοντος συνδεδεμένου χρήστη
        }
    }


