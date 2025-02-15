package gr.hua.dit.ds.rental_management.Controllers;

import gr.hua.dit.ds.rental_management.Config.SecurityService;
import gr.hua.dit.ds.rental_management.Entities.*;
import gr.hua.dit.ds.rental_management.Repositories.*;
import gr.hua.dit.ds.rental_management.Services.PropertyApplicationService;
import gr.hua.dit.ds.rental_management.Services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final OwnerRepository ownerRepository;
    private final PropertyApplicationRepository propertyApplicationRepository;
    private final PropertyApplicationService propertyApplicationService;
    private final SecurityService securityService;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public PropertyController(PropertyService propertyService, OwnerRepository ownerRepository, PropertyApplicationRepository propertyApplicationRepository, PropertyApplicationService propertyApplicationService,SecurityService securityService, TenantRepository tenantRepository, UserRepository userRepository, AdminRepository adminRepository) {
        this.propertyService = propertyService;
        this.ownerRepository = ownerRepository;
        this.propertyApplicationRepository = propertyApplicationRepository;
        this.propertyApplicationService = propertyApplicationService;
        this.securityService = securityService;
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }


   @GetMapping
   public String showPropertiesPage(Model model) { // Method for checking on a new user
       String username = securityService.getCurrentUsername(); // Get logged-in username

       // Check if the logged-in user is an owner or tenant
       Owner owner = ownerRepository.findByUsername(username); // Check if owner profile exists
       Tenant tenant = tenantRepository.findByUsername(username); // Check if tenant profile exists

       // If the owner's profile is incomplete , redirect to the owner form
       if (owner != null && (owner.getUsername() == null || owner.getUsername().isEmpty())) {
           return "redirect:/owner/ownerForm"; // Redirect to the owner's form to fulfill profile
       }

       // If owner profile is complete, show properties for the owner
       if (owner != null && owner.getUsername() != null && !owner.getUsername().isEmpty()) {
           model.addAttribute("owner", owner);
           return "property/properties"; // Return properties page for owner
       }

       // If tenant profile exists and is complete, show properties for tenant
       if (tenant != null && tenant.getUsername() != null && !tenant.getUsername().isEmpty()) {
           model.addAttribute("tenant", tenant);
           return "property/properties"; // Return properties page for tenant
       }

       // If neither profile is complete, redirect to the tenant profile creation page
       if (tenant != null && (tenant.getUsername() == null || tenant.getUsername().isEmpty())){
           return "redirect:/tenant/tenantForm";
       }
        return "property/properties";// Redirect to tenant profile creation if tenant profile is incomplete
   }


    @GetMapping("/list")//Method for displaying the list of all the available properties
    public String showlistProperties(Model model) {
        List<Property> properties = propertyService.getApprovedProperties();
        model.addAttribute("properties", properties);
        return "property/property_list";
    }


    @PostMapping("/applications/property/approve/{id}") //Method for approving a property applic
    public String approvePropertyApplication(@PathVariable Integer id) {
        PropertyApplication propertyApplication = propertyApplicationService.getPropertyApplicById(id);
        propertyApplication.setStatus("APPROVED");
        propertyApplicationService.savePropertyApplic(propertyApplication);
        return "redirect:/properties";
    }

    @PostMapping("/applications/property/reject/{id}") //Method for rejecting a property applic
    public String rejectPropertyApplication(@PathVariable Integer id) {
        propertyApplicationService.deletePropertyApplic(id);
        return "redirect:/properties";
    }


    @GetMapping("/new")
    public String showNewPropertyForm(Model model) {
        model.addAttribute("property", new Property());
        //φόρτωση όλων των owners από την βάση
        model.addAttribute("owners", ownerRepository.findAll());

        return "property/new_property";
    }


    @PostMapping
    public String saveProperty(@ModelAttribute Property property, @RequestParam Integer owner) {
        //βρίσκουμε τον ιδιοκτήτη από τη βάση
        Owner existingOwner = ownerRepository.findById(owner)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid owner ID:" + owner));

        //ορίζουμε τον ιδιοκτήτη στο property
        property.setOwner(existingOwner);

        property.setStatus(Property.PropertyStatus.PENDING); //ορίζουμε το status ως pending

        //αποθηκεύουμε το property
        propertyService.saveProperty(property);

        return "redirect:/properties";
    }


    /* GET request for the property application form */
    @GetMapping("/application")
    public String showPropertyApplicationForm(Model model) {
        // Add a new PropertyApplication object to the model to be bound to the form
        model.addAttribute("propertyApplication", new PropertyApplication());

        // Add the available owners, users, and admins to the model
        model.addAttribute("owners", ownerRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("admins", adminRepository.findAll());

        // Return the name of the  template for the property application form
        return "property/propery_applic"; // Template to render the form

    }

    /* POST request to save a property application */
    @PostMapping("/application")
    public String savePropertyApplication(@ModelAttribute PropertyApplication propertyApplication,
                                          @RequestParam Integer ownerId,
                                          @RequestParam Long userId,
                                          @RequestParam Integer adminId) {
        // Fetch Owner, User, and Admin from the database using the provided IDs
        Owner existingOwner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid owner ID: " + ownerId));

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin ID: " + adminId));

        // Set the fetched entities into the PropertyApplication
        propertyApplication.setOwner(existingOwner);
        propertyApplication.setUser(existingUser);
        propertyApplication.setAdmin(existingAdmin);
        propertyApplication.setStatus("PENDING"); // Default status for the application

        // Save the PropertyApplication object to the repository
        propertyApplicationRepository.save(propertyApplication);

        // Redirect to the properties page after saving
        return "redirect:/properties"; //  redirect to any page you want after saving
    }


    @GetMapping("/detail/{propertyId}") //Method for displaying the details of a property
    public String showPropertyDetail(@PathVariable Long propertyId, Model model) {
        Property property = propertyService.getPropertyById(propertyId);
        model.addAttribute("property", property);
        return "property/property_detail";
    }

    @GetMapping("/edit/{id}")//Method for editing a property
    public String showEditPropertyForm(@PathVariable Long id, Model model) {
        Property property = propertyService.getPropertyById(id);
        model.addAttribute("property", property);
        return "property/edit_property";
    }

    @PostMapping("/{id}") //Method for saving the changes made to the property
    public String updateProperty(@PathVariable Long id, @ModelAttribute Property property) {
        propertyService.updateProperty(id, property);
        return "redirect:/properties";
    }

    @GetMapping("/delete/{id}") //Method for deleting a property
    public String deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return "redirect:/properties";
    }
}

