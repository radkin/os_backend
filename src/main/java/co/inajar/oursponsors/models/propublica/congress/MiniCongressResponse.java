package co.inajar.oursponsors.models.propublica.congress;

import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.models.fec.SponsorRequest;
import co.inajar.oursponsors.models.fec.SponsorResponse;
import co.inajar.oursponsors.services.fec.SponsorsManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class MiniCongressResponse {

    private static final String REPTYPE = "congress";

    private static final String BASE_URL = "https://theunitedstates.io/images/congress/original";
    @JsonProperty(value = "rep_type")
    private String repType;
    private SponsorsManager sponsorsManager;
    @JsonProperty(value = "id")
    private Long id;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    @JsonProperty(value = "party")
    private String party;
    @JsonProperty(value = "state")
    private String state;
    @JsonProperty(value = "image_url")
    private String imageUrl;
    @JsonProperty(value = "sponsors")
    private Set<SponsorResponse> sponsors;

    public MiniCongressResponse(Congress congress, SponsorsManager sponsorsManager) {
        repType = REPTYPE;
        id = congress.getId();
        title = congress.getTitle();
        firstName = congress.getFirstName();
        lastName = congress.getLastName();
        party = congress.getParty();
        state = congress.getState();
        imageUrl = BASE_URL + "/" + congress.getProPublicaId() + ".jpg";
        this.sponsorsManager = sponsorsManager;

        sponsors = new HashSet<>();

        SponsorRequest data = new SponsorRequest();
        data.setChamber("congress");
        data.setOsId(congress.getId());

        var possibleSponsors = Optional.ofNullable(sponsorsManager.getSponsors(data));
        if (possibleSponsors.isPresent()) {
            var set = possibleSponsors.get().parallelStream()
                    .map(SponsorResponse::new)
                    .limit(2)
                    .collect(Collectors.toSet());
            sponsors.addAll(set);
        }
    }

}
