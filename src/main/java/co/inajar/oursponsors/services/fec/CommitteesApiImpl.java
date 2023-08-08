package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CommitteesApiImpl implements CommitteesApiManager {

    @Value("${fec.inajar.token.secret}")
    private String fecApiKey;

    @Override
    public List<FecCommitteeDonor> getFecCommitteeDonors(String committeeId, Integer twoYearTransactionPeriod) {
        // https://api.open.fec.gov/v1/schedules/schedule_a/
        // ?sort_hide_null=false&sort_nulls_last=false&data_type=processed&committee_id=C00701128
        // &two_year_transaction_period=2020&sort=-contribution_receipt_date&per_page=30
        // &sort=-contributor_aggregate_ytd

        var path = "/v1/schedules/schedule_a";

        var webClient = getClient().get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("sort_hide_null", false)
                        .queryParam("sort_nulls_last", false)
                        .queryParam("data_type", "processed")
                        .queryParam("committee_id", committeeId)
                        .queryParam("two_year_transaction_period", twoYearTransactionPeriod)
                        .queryParam("sort", "contribution_receipt_date")
                        .queryParam("per_page", 30)
                        .queryParam("sort", "contributor_aggregate_ytd")
                        .queryParam("api_key", fecApiKey)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .retry(3);
        var response = Optional.ofNullable(webClient.block());
        if (response.isPresent()) {
            return mapFecCommitteeDonorsToModel(webClient.block());
        }
        return Collections.emptyList();
    }

    private WebClient getClient() {
        return WebClient.builder()
                .clientConnector((connector()))
                .exchangeStrategies(ExchangeStrategies.builder().codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1000000)).build())
                .baseUrl("https://api.open.fec.gov")
                .build();
    }

    private List<FecCommitteeDonor> mapFecCommitteeDonorsToModel(String response) {
        var mappedDonors = new ArrayList<FecCommitteeDonor>();
        var objectMapper = new ObjectMapper();
        try {
            var tree = objectMapper.readTree(response);
            var donorsResponse = tree.get("results");
            for (JsonNode jsonNode : donorsResponse) {
                try {
                    var donor = objectMapper.treeToValue(jsonNode, FecCommitteeDonor.class);
                    mappedDonors.add(donor);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return mappedDonors;
    }

    private ClientHttpConnector connector() {
        return new ReactorClientHttpConnector(HttpClient.from(TcpClient.newConnection()));
    }
}
