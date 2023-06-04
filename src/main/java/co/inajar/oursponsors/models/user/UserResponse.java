package co.inajar.oursponsors.models.user;

import co.inajar.oursponsors.dbOs.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResponse {

    public UserResponse() {}

    public UserResponse(User user) {
        id = user.getId();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        profileImage = user.getProfileImg();
        name = user.getName();
        state = user.getState();
        gender = user.getGender();
        party = user.getParty();
        isEnabled = user.getIsEnabled();
    }

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "profile_image")
    private String profileImage;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "state")
    private String state;

    @JsonProperty(value = "gender")
    private String gender;

    @JsonProperty(value = "party")
    private String party;

    @JsonProperty(value = "is_enabled")
    private Boolean isEnabled;
}
