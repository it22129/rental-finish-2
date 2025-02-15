package gr.hua.dit.ds.rental_management.Repositories;

import gr.hua.dit.ds.rental_management.Entities.RentalApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalApplicationRepository extends JpaRepository<RentalApplication, Long> {
   List<RentalApplication> findByPropertyId (int id);
   int countByUserUsername(String username);
   List<RentalApplication> findByStatus(String status);
}
