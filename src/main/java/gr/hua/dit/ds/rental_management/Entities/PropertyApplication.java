package gr.hua.dit.ds.rental_management.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/* Τον πίνακα property application , με βάση την λογική που υλοποιήσαμε , δηλαδή με το που εισάγει
* κάποιος owner ένα property να δημιουργείται αυτόματα σαν propertty application,με status pending
* δεν τον χρειαζόμαστε, δεν διαγράφουμε όμως το entity class , γιατί για την ώρα δημιουργεί θέματα
* σε άλλες κλάσεις.*/
@Entity
public class PropertyApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "application_status", nullable = false)
    private String status = "PENDING"; // Default τιμή

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="owner_id")
    private Owner owner;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="admin_id")
    private Admin admin;


    public PropertyApplication(Owner owner, String status, Admin admin) {
        this.owner = owner;
        this.status = status;
        this.admin = admin;
    }

    public PropertyApplication() {
    }


    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "PropertyApplication{" +
                "id=" + id +
                ", status=" + status +
                ", user=" + user +
                ", owner=" + owner +
                ", admin=" + admin +
                '}';
    }
}
