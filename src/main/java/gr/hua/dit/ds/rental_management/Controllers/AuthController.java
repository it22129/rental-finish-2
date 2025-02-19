package gr.hua.dit.ds.rental_management.Controllers;

import gr.hua.dit.ds.rental_management.Entities.Roles;
import gr.hua.dit.ds.rental_management.Repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    RoleRepository roleRepository;

    public AuthController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct //Με το που αρχικοποιηθεί η εφαρμογή δημιουργούνται αυτοί οι ρόλοι στην βάση
    public void setup() {
        Roles role_tenant = new Roles("TENANT");
        Roles role_admin = new Roles("ADMIN");
        Roles role_owner = new Roles("OWNER");



        roleRepository.updateOrInsert(role_tenant);
        roleRepository.updateOrInsert(role_admin);
        roleRepository.updateOrInsert(role_owner);
    }

    @GetMapping("/login")
    public String login() {
        return "basic/login";
    }
}

