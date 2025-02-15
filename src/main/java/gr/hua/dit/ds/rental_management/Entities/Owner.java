package gr.hua.dit.ds.rental_management.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Owner {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Integer owner_Id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 20)
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Size(max = 5)
    @Column(name = "appartment_number")
    private String aprtm_number;


    @NotBlank
    @Size(max = 20)
    @Column(name = "username")
    private String username;


    @OneToMany(mappedBy = "owner", cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List <Property>  properties;

    @OneToMany(mappedBy = "owner", cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List <RentalApplication>  rental_applications;

    @OneToMany(mappedBy = "owner", cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List <PropertyApplication>  property_applications;


    public Owner(String aprtm_number, String firstName, String lastName, List<Property> properties,List<RentalApplication> rental_applications,List<PropertyApplication> property_applications) {
        this.aprtm_number = aprtm_number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.properties = properties;
        this.rental_applications = rental_applications;
        this.property_applications = property_applications;
    }

    public Owner() {
    }

    public List<RentalApplication> getRental_applications() {
        return rental_applications;
    }

    public void setRental_applications(List<RentalApplication> rental_applications) {
        this.rental_applications = rental_applications;
    }

    public List<PropertyApplication> getProperty_applications() {
        return property_applications;
    }

    public void setProperty_applications(List<PropertyApplication> property_applications) {
        this.property_applications = property_applications;
    }

    public Integer getOwner_Id() {
        return owner_Id;
    }

    public void setOwner_Id(Integer owner_Id) {
        this.owner_Id = owner_Id;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getAprtm_number() {
        return aprtm_number;
    }

    public void setAprtm_number(String aprtm_number) {
        this.aprtm_number = aprtm_number;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
        return "Owner{" +
                "aprtm_number='" + aprtm_number + '\'' +
                ", owner_Id=" + owner_Id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", properties=" + properties +
                ", rental_applications=" + rental_applications +
                ", property_applications=" + property_applications +
                '}';
    }


}


