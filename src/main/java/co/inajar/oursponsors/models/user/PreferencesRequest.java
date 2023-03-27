package co.inajar.oursponsors.models.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class PreferencesRequest {

    @JsonAlias(value = "id")
    private Long id;
}
