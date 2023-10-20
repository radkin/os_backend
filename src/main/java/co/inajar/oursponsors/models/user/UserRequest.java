package co.inajar.oursponsors.models.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class UserRequest {

    @JsonAlias(value = "id")
    private Long id;

    @JsonAlias(value = "email")
    private String email;

    @JsonAlias(value = "first_name")
    private String firstName;

    @JsonAlias(value = "last_name")
    private String lastName;

    @JsonAlias(value = "profile_image")
    private String profileImage;

    @JsonAlias(value = "state")
    private String state;

    @JsonAlias(value = "gender")
    private String gender;

    @JsonAlias(value = "party")
    private String party;

    @JsonAlias(value = "is_enabled")
    private Boolean isEnabled;

    @JsonAlias(value = "is_logged_in")
    private Boolean isLoggedIn;

    @JsonAlias(value = "name")
    private String name;

    @JsonAlias(value = "google_uid")
    private String googleUid;
}
