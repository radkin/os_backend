package co.inajar.oursponsors.models.propublica.senator;

import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.models.fec.SponsorRequest;
import co.inajar.oursponsors.models.fec.SponsorResponse;
import co.inajar.oursponsors.services.fec.CommitteesManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class MiniSenatorResponse {

    private static final String BASE_URL = "https://theunitedstates.io/images/congress/original";
    private CommitteesManager committeesManager;
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

    public MiniSenatorResponse(Senator senator, CommitteesManager committeesManager) {
        id = senator.getId();
        title = senator.getTitle();
        firstName = senator.getFirstName();
        lastName = senator.getLastName();
        party = senator.getParty();
        state = senator.getState();
        imageUrl = BASE_URL + "/" + senator.getProPublicaId() + ".jpg";
        this.committeesManager = committeesManager;

        sponsors = new HashSet<>();

        SponsorRequest data = new SponsorRequest();
        data.setChamber("senator");
        data.setOsId(senator.getId());

        var possibleSponsors = Optional.ofNullable(committeesManager.getSponsors(data));
        if (possibleSponsors.isPresent()) {
            var set = possibleSponsors.get().parallelStream()
                    .map(SponsorResponse::new)
                    .collect(Collectors.toSet());
            sponsors.addAll(set);
        }
    }
}
