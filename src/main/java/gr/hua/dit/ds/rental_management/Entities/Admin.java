package gr.hua.dit.ds.rental_management.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Admin {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer admin_Id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 20)
    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "admin", cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<PropertyApplication> propertyApplications;

    public Admin() {
    }

    public Admin(String firstName, String lastName, List<PropertyApplication> propertyApplications) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.propertyApplications = propertyApplications;
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

    public Integer getAdmin_Id() {
        return admin_Id;
    }

    public void setAdmin_Id(Integer owner_Id) {
        this.admin_Id = owner_Id;
    }

    public List<PropertyApplication> getPropertyApplications() {
        return propertyApplications;
    }

    public void setPropertyApplications(List<PropertyApplication> propertyApplications) {
        this.propertyApplications = propertyApplications;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "firstName='" + firstName + '\'' +
                ", owner_Id=" + admin_Id +
                ", lastName='" + lastName + '\'' +
                ", propertyApplications=" + propertyApplications +
                '}';
    }
}



