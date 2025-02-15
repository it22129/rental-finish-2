package gr.hua.dit.ds.rental_management.Controllers;

import gr.hua.dit.ds.rental_management.Entities.Property;
import gr.hua.dit.ds.rental_management.Entities.RentalApplication;
import gr.hua.dit.ds.rental_management.Entities.Tenant;
import gr.hua.dit.ds.rental_management.Entities.User;
import gr.hua.dit.ds.rental_management.Repositories.PropertyRepository;
import gr.hua.dit.ds.rental_management.Repositories.RentalApplicationRepository;
import gr.hua.dit.ds.rental_management.Repositories.TenantRepository;
import gr.hua.dit.ds.rental_management.Services.PropertyService;
import gr.hua.dit.ds.rental_management.Services.RentalApplicationService;
import gr.hua.dit.ds.rental_management.Services.TenantService;
import gr.hua.dit.ds.rental_management.Services.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/rentals")
public class RentalApplicationController {
    private final RentalApplicationRepository rentalApplicationRepository;
    private final RentalApplicationService rentalApplicationService;
    private final PropertyService propertyService;
    private final TenantService tenantService;
    private final UserService userService;
    private final PropertyRepository propertyRepository;
    private final TenantRepository tenantRepository;


    public RentalApplicationController(RentalApplicationRepository rentalApplicationRepository, RentalApplicationService rentalApplicationService, PropertyService propertyService, TenantService tenantService, UserService userService, PropertyRepository propertyRepository, TenantRepository tenantRepository) {
        this.rentalApplicationRepository = rentalApplicationRepository;
        this.rentalApplicationService = rentalApplicationService;
        this.propertyService = propertyService;
        this.tenantService = tenantService;
        this.userService = userService;
        this.propertyRepository = propertyRepository;
        this.tenantRepository = tenantRepository;
    }

    @GetMapping
    public String showlistRentalApplic(Model model) { //Method that displays available rental applications
        List<RentalApplication> rentals = rentalApplicationService.getAllRentals();
        model.addAttribute("rentals", rentals);
        return "rental/rental_applications";
    }


    @GetMapping("/approved-rentals") // Shows list of approved rental applications
    public String listApprovedRentals(Model model) {
        List<RentalApplication> approvedRentals = rentalApplicationRepository.findByStatus("APPROVED");
        model.addAttribute("rentals", approvedRentals);
        return "rental/rentals";
    }

    @PostMapping("/applications/rental/approve/{id}") // Approve a rental applic with the id
    public String approveRentalApplication(@PathVariable Long id) {
        RentalApplication rental = rentalApplicationService.getRentalById(id);

        rental.setStatus("APPROVED");
        rentalApplicationService.saveRental(rental);

        RentalApplication updatedRental = rentalApplicationService.getRentalById(id);

        return "redirect:/rentals";
    }

    @PostMapping("/applications/rental/reject/{id}") //Reject a rental application by it's id
    public String rejectRentalApplication(@PathVariable Long id) {
        rentalApplicationService.deleteRental(id);
        return "redirect:/rentals";
    }


    @GetMapping("/new") //Show the form to create a new rental application form
    public String showNewRentalForm(Model model) {
        List<Property> availableProperties = propertyRepository.findAvailableProperties();// Fetch all properties
        List<Tenant> tenants = tenantRepository.findAll(); // Fetch all tenants

        model.addAttribute("properties", availableProperties);
        model.addAttribute("tenants", tenants);
        model.addAttribute("rentalApplication", new RentalApplication()); // Bind form to a new object

        return "rental/new_rental";
    }


    @PostMapping("/new") // Save the rental applic form and create it
    public String createRental(@ModelAttribute RentalApplication rental,
                               @RequestParam("property.id") Long propertyId,
                               @RequestParam("tenants") List<Integer> tenantIds,
                               @AuthenticationPrincipal User loggedInUser) {

        // Fetch Property
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // Fetch Tenants
        List<Tenant> tenants = tenantRepository.findAllById(tenantIds);
        if (tenants.isEmpty()) {
            throw new RuntimeException("No tenants selected");
        }

        // Set Missing Fields
        rental.setProperty(property);
        rental.setTenants(tenants);
        rental.setUser(loggedInUser);
        rental.setOwner(property.getOwner());
        rental.setStatus("PENDING");

        // Save Rental
        rentalApplicationService.saveRental(rental);

        return "redirect:/properties";
    }


}
