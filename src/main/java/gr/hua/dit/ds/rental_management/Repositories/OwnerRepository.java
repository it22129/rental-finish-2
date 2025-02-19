package gr.hua.dit.ds.rental_management.Repositories;

import gr.hua.dit.ds.rental_management.Entities.Owner;
import gr.hua.dit.ds.rental_management.Entities.Tenant;
import gr.hua.dit.ds.rental_management.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner,Integer> {
    Optional<Owner> findByLastName(String last_name);
    Boolean existsByLastName(String last_name);

    Owner findByUsername(String username);


}
