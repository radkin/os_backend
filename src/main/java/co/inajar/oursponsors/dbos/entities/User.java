package co.inajar.oursponsors.dbos.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inajar_api_key")
    private String apiKey;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "password")
    private String password;

    @Column(name = "state")
    private String state;

    @Column(name = "gender")
    private String gender;

    @Column(name = "party")
    private String party;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Transient
    private String name;

    public String getName() {
        return firstName + " " + lastName;
    }

}
