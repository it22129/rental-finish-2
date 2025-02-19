package gr.hua.dit.ds.rental_management.Repositories;

import gr.hua.dit.ds.rental_management.Entities.Property;
import gr.hua.dit.ds.rental_management.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByStatus(Property.PropertyStatus status);
    // Fetch only properties that are available (not assigned to a tenant)
    @Query("SELECT p FROM Property p WHERE p.tenant IS NULL")
    List<Property> findAvailableProperties();

    @Query("SELECT p FROM Property p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "(p.parking = true AND LOWER(:keyword) LIKE '%parking%')")
    List<Property> searchProperties(@Param("keyword") String keyword);



}
