package gr.hua.dit.ds.rental_management.Services;

import gr.hua.dit.ds.rental_management.Entities.Owner;
import gr.hua.dit.ds.rental_management.Entities.Tenant;
import gr.hua.dit.ds.rental_management.Repositories.OwnerRepository;
import gr.hua.dit.ds.rental_management.Repositories.TenantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    private TenantRepository tenantRepository;


    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public List<Tenant> getTenants(){
        return tenantRepository.findAll();
    }

    @Transactional
    public Tenant getTenant(Integer Id) {
        return tenantRepository.findById(Id).get();
    }

    @Transactional
    public void saveTenant(Tenant tenant) {
        tenantRepository.save(tenant);
    }

}
