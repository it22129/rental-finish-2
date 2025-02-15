package gr.hua.dit.ds.rental_management.Repositories;

import gr.hua.dit.ds.rental_management.Entities.PropertyApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyApplicationRepository extends JpaRepository<PropertyApplication, Integer> {
    int countByUserUsername(String username);
    List<PropertyApplication> findByStatus(String status);
}
