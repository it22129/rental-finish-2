package gr.hua.dit.ds.rental_management.Controllers;

import gr.hua.dit.ds.rental_management.Entities.Property;
import gr.hua.dit.ds.rental_management.Services.PropertyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/property-applications")
public class AdminPropertyController {

    private final PropertyService propertyService;

    public AdminPropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping //εμφανίζει όλες τις pending αιτήσεις στον διαχειριστή
    public String viewPendingProperties(Model model){
        List<Property> pendingProperties = propertyService.getPendingProperties();
        model.addAttribute("pendingProperties", pendingProperties);
        return "admin/property-applications";
    }

    //Εγκρίνει μια ιδιοκτησία αλλάζοντας την κατάστασή της σε "APPROVED"
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public String approveProperty(@PathVariable Long id){
        propertyService.updatePropertyStatus(id, Property.PropertyStatus.APPROVED);
        return "redirect:/admin/property-applications";
    }
 //Απορρίπτει μια ιδιοκτησία αλλάζοντας την κατάστασή της σε "REJECTED"
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/reject")
    public String rejectProperty(@PathVariable Long id){
        propertyService.updatePropertyStatus(id, Property.PropertyStatus.REJECTED);
        return "redirect:/admin/property-applications";
    }
}
