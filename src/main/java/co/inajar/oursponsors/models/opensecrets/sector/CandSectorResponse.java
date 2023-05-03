package co.inajar.oursponsors.models.opensecrets.sector;

import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandSectorResponse {

    // we need to extract cycle from the @attributes object. I think I did this kind of thing with workpress
    @JsonProperty(value = "@attributes")
    private JsonNode attributes;

    @JsonProperty(value = "sector")
    private List<Sector> sectors;
}
