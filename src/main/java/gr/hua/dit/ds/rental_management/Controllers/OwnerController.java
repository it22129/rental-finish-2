package gr.hua.dit.ds.rental_management.Controllers;

import gr.hua.dit.ds.rental_management.Entities.Owner;
import gr.hua.dit.ds.rental_management.Repositories.OwnerRepository;
import gr.hua.dit.ds.rental_management.Services.OwnerService;
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
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerRepository ownerRepository;
    private final OwnerService ownerService;

    public OwnerController(OwnerRepository ownerRepository, OwnerService ownerService) {
        this.ownerRepository = ownerRepository;
        this.ownerService = ownerService;
    }


    @GetMapping("/list")
    public String getOwnersList(Model model) {
        List<Owner> owners = ownerService.getOwners(); // Με το ownerService παίρνουμε την  owners list
        model.addAttribute("owners", owners); // Την προσθέτουμε στο model
        return "owner/owners"; // Επιστρέφουμε το αντίστοιχο template που παρουσιάζεται η owners list
    }

    @GetMapping("/new") // Μέθοδος για την εμφάνιση την φόρμας για να συμπληρώσει το προφίλ του ένας Owner
    public String addOwner(Model model){
        Owner owner = new Owner();
        model.addAttribute("owner", owner);
        return "owner/ownerForm";

    }


    @PostMapping("/new") // Μέθοδος για την αποθήκευση των στοιχείων της παραπάνω φόρμας
    public String saveOwner(@Valid @ModelAttribute("owner") Owner owner, BindingResult theBindingResult, Model model) {
        if (theBindingResult.hasErrors()) {
            System.out.println("error");
            return "owner/owners";
        } else {
            ownerService.saveOwner(owner);
            model.addAttribute("owner", ownerService.getOwners());
            model.addAttribute("successMessage", "Owner added successfully!");
            return "redirect:/properties";
        }

    }

}
