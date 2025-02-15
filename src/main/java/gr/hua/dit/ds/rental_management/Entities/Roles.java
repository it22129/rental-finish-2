package gr.hua.dit.ds.rental_management.Entities;

import jakarta.persistence.*;


@Entity
@Table(name = "roles")
public class Roles {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(length = 20)
        private String name;

        public Roles() {

        }

        public Roles(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

