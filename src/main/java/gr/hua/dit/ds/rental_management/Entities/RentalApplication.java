package gr.hua.dit.ds.rental_management.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "rental_applications")
public class RentalApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rental_application_status", nullable = false)
    private String status = "PENDING"; // Default τιμή

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="rentalAplic_tenantsId",
            joinColumns = @JoinColumn(name="rentalAplic_id"),
            inverseJoinColumns = @JoinColumn(name="tenant_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "rentalAplic_id"}) )
    private List<Tenant> tenants;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="owner_id")
    private Owner owner;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="property_id")
    private Property property;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;


    public RentalApplication(Owner owner, String status, List<Tenant> tenants, Property property) {
        this.owner = owner;
        this.status = status;
        this.tenants = tenants;
        this.property = property;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public RentalApplication() {
    }

    public RentalApplication(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }


    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public List<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




    @Override
    public String toString() {
        return "RentalApplication{" +
                "id=" + id +
                ", status=" + status +
                ", tenants=" + tenants +
                ", owner=" + owner +
                ", property=" + property +
                '}';
    }


}


