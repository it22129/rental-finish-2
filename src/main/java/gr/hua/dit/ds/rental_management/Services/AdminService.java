package gr.hua.dit.ds.rental_management.Services;

import gr.hua.dit.ds.rental_management.Entities.Admin;
import gr.hua.dit.ds.rental_management.Entities.Owner;
import gr.hua.dit.ds.rental_management.Repositories.AdminRepository;
import gr.hua.dit.ds.rental_management.Repositories.OwnerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional
    public List<Admin> getAdmin(){
        return adminRepository.findAll();
    }

    @Transactional
    public Admin getAdmin(Integer Id) {
        return adminRepository.findById(Id).get();
    }

    @Transactional
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

}


