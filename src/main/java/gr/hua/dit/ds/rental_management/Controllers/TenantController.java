package gr.hua.dit.ds.rental_management.Controllers;


import gr.hua.dit.ds.rental_management.Entities.Owner;
import gr.hua.dit.ds.rental_management.Entities.Tenant;
import gr.hua.dit.ds.rental_management.Repositories.OwnerRepository;
import gr.hua.dit.ds.rental_management.Repositories.TenantRepository;
import gr.hua.dit.ds.rental_management.Services.OwnerService;
import gr.hua.dit.ds.rental_management.Services.TenantService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tenant")
public class TenantController {

    private final TenantRepository tenantRepository;
    private final TenantService tenantService;

    public TenantController(TenantRepository tenantRepository, TenantService tenantService) {
        this.tenantRepository = tenantRepository;
        this.tenantService = tenantService;

    }

    @GetMapping("/list") // Method to display all the available tenanta
    public String getTenantList(Model model) {
        List<Tenant> tenants = tenantService.getTenants(); //  tenantService gets the tenants list
        model.addAttribute("tenants", tenants); // Add the tenant list to the model
        return "tenant/tenants"; // Return the view name where the tenant's list is displayed
    }


    @GetMapping("/new") // Method to fulfill the tenant profile form
    public String addTenant(Model model){
        Tenant tenant = new Tenant();
        model.addAttribute("tenant", tenant);
        return "tenant/tenantForm";

    }


    @PostMapping("/new")//Method the saves the above form and creates the tenant profil
    public String saveTenant(@Valid @ModelAttribute("tenant") Tenant tenant, BindingResult theBindingResult, Model model) {
            if (theBindingResult.hasErrors()) {
                // If there are validation errors, return the form with error messages
                return "tenant/tenants";
            } else {
                // Save tenant information
                tenantService.saveTenant(tenant);

                // A success message to be displayed on the properties page
                model.addAttribute("successMessage", "Tenant added successfully!");

                // Redirect to the properties page (where the tenant can see available actions)
                return "redirect:/properties";
            }
        }

}



