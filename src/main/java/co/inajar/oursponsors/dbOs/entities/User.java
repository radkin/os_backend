package co.inajar.oursponsors.dbOs.entities;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "inajar_api_key")
    private String apiKey;
    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Column(name = "email")
    private String email;
    public void setEmail(String theEmail) { email = theEmail; }
    public String getEmail() { return email; }

    @Column(name = "first_name")
    private String firstName;
    public void setFirstName(String theName) { firstName = theName; }
    public String getFirstName() { return firstName; }

    @Column(name = "last_name")
    private String lastName;
    public void setLastName(String theName) { lastName = theName; }
    public String getLastName() { return lastName; }

    @Column(name = "profile_img")
    private String profileImg;
    public void setProfileImg(String imageUrl) { profileImg = imageUrl; }
    public String getProfileImg() { return profileImg; }

    @Transient
    private String name;
    public String getName() {
        return firstName + " " + lastName;
    }

}

