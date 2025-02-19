package gr.hua.dit.ds.rental_management.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotBlank
    @Size(min = 0, max = 100, message = "Location must be between 0 and 100 characters.")
    @Column(name = "location")
    private String location;


    @Column(name = "price")
    private double price;


    @Column(name = "parking")
    private boolean parking;


    @Enumerated(EnumType.STRING)
    private PropertyStatus status = PropertyStatus.PENDING; // default value

    @Column(name = "square meters")
    private double sm;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="owner_id")
    private Owner owner;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="tenant_id")
    private Tenant tenant;

    @OneToMany(mappedBy = "property", cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<RentalApplication> rentalAplic;

    @OneToOne
    @JoinColumn(name = "property_application_id")
    private PropertyApplication propertyApplication;

    public Property(String description, String location, Owner owner, double price, String title, Tenant tenant,List<RentalApplication> rentalAplic, boolean parking, double sm, PropertyApplication propertyApplication) {
        this.description = description;
        this.location = location;
        this.owner = owner;
        this.price = price;
        this.title = title;
        this.tenant = tenant;
        this.rentalAplic = rentalAplic;
        this.parking = parking;
        this.sm = sm;
        this.propertyApplication = propertyApplication;

    }

    public PropertyApplication getPropertyApplication() {
        return propertyApplication;
    }

    public void setPropertyApplication(PropertyApplication propertyApplication) {
        this.propertyApplication = propertyApplication;
    }

    public Property() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RentalApplication> getRentalAplic() {
        return rentalAplic;
    }

    public void setRentalAplic(List<RentalApplication> rentalAplic) {
        this.rentalAplic = rentalAplic;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Tenant getTenant() {
        return tenant;
    }
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public double getSm() {
        return sm;
    }

    public void setSm(double sm) {
        this.sm = sm;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public enum PropertyStatus {
        PENDING, APPROVED, REJECTED
    }

    @Override
    public String toString() {
        return "Property{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", parking=" + parking +
                ", sm=" + sm +
                ", owner=" + owner +
                ", tenant=" + tenant +
                ", rentalAplic=" + rentalAplic +
                '}';
    }
}


