package gr.hua.dit.ds.rental_management.Repositories;


import gr.hua.dit.ds.rental_management.Entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {

    Optional<Roles> findByName(String roleName);

    default Roles updateOrInsert(Roles role) {
        Roles existing_role = findByName(role.getName()).orElse(null);
        if (existing_role != null) {
            return existing_role;
        }
        else {
            return save(role);
        }
    }
}
