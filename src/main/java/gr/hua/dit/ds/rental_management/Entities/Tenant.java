package gr.hua.dit.ds.rental_management.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private int id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 20)
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Size(max = 20)
    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "tenant", cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Property> properties;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="rentalAplic_tenantsId",
            joinColumns = @JoinColumn(name="tenant_id"),
            inverseJoinColumns = @JoinColumn(name="rentalAplic_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"rentalAplic_id", "tenant_id"})
    )
    private List<RentalApplication> rentalAplics;

    public Tenant(String firstName, String lastName, List<Property> properties,List<RentalApplication> rentalAplics) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.properties = properties;
        this.rentalAplics = rentalAplics;

    }

    public Tenant() {
    }

    public List<RentalApplication> getRentalAplics() {
        return rentalAplics;
    }

    public void setRentalAplics(List<RentalApplication> rentalAplics) {
        this.rentalAplics = rentalAplics;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "firstName='" + firstName + '\'' +
                ", id=" + id +
                ", lastName='" + lastName + '\'' +
                ", properties=" + properties +
                ", rentalAplics=" + rentalAplics +
                '}';
    }


}
