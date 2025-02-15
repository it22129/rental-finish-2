package gr.hua.dit.ds.rental_management.Controllers;

import gr.hua.dit.ds.rental_management.Entities.Property;
import gr.hua.dit.ds.rental_management.Services.PropertyApplicationService;
import gr.hua.dit.ds.rental_management.Services.PropertyService;
import gr.hua.dit.ds.rental_management.Services.RentalApplicationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final RentalApplicationService rentalApplicationService;
    private final PropertyService propertyService;
    private final PropertyApplicationService propertyApplicationService;

    public HomeController(RentalApplicationService rentalApplicationService, PropertyService propertyService, PropertyApplicationService propertyApplicationService) {
        this.rentalApplicationService = rentalApplicationService;
        this.propertyService = propertyService;
        this.propertyApplicationService = propertyApplicationService;
    }

    @GetMapping //Επιστρέφεται η αρχική σελίδα της εφαρμογής και προσθέτονται πληροφορίες για τον συνδεδεμένο χρήστη
    public String home(Model model, @AuthenticationPrincipal UserDetails user) {
        if (user != null) { // Only add attributes if user is logged in
            String username = user.getUsername();


            boolean hasRentalApplications = rentalApplicationService.userHasApplications(username);
            boolean hasPropertyApplications = propertyApplicationService.userHasPropertyApplications(username);

            model.addAttribute("hasRentalApplications", hasRentalApplications);
            model.addAttribute("hasPropertyApplications", hasPropertyApplications);

            model.addAttribute("properties", propertyService.getApprovedProperties());
        }

        return "basic/home";
    }

    @GetMapping("/search") // Μέθοδος για την αναζήτηση property
    public String searchProperties(@RequestParam("query") String query, Model model) {
        List<Property> searchResults = propertyService.searchProperties(query);
        model.addAttribute("properties", searchResults);
        return "property/property_list"; // Επιστρέφουμε στη property_list με φιλτραρισμένα αποτελέσματα
    }


}