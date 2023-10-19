package co.inajar.oursponsors.helpers;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SponsorGenerator {

//    public static void main(String[] args) {
//        List<Sponsor> generatedSponsors = generateSponsors();
//        for (Sponsor sponsor : generatedSponsors) {
//            System.out.println("Sponsor Org: " + sponsor.getContributorOccupation() + ", Total: " + sponsor.getContributorAggregateYtd());
//        }
//    }

    public static List<Sponsor> generateCongressSponsors(Congress congress) {
        List<Sponsor> sponsors = new ArrayList<>();
        String sponsorOccupation = "Fashion Magnate";

        for (int i = 1; i <= 11; i++) {
            BigDecimal total = BigDecimal.valueOf(1000 - (i * 100)); // Vary the total in each iteration
            Sponsor sponsor = Sponsor.builder()
                    .contributorOccupation(sponsorOccupation)
                    .contributorAggregateYtd(total)
                    .congress(congress)
                    .build();
            sponsors.add(sponsor);
        }

        return sponsors;
    }

    public static List<Sponsor> generateSenatorSponsors(Senator senator) {
        List<Sponsor> sponsors = new ArrayList<>();
        String sponsorOccupation = "Fashion Magnate";

        for (int i = 1; i <= 11; i++) {
            BigDecimal total = BigDecimal.valueOf(1000 - (i * 100)); // Vary the total in each iteration
            Sponsor sponsor = Sponsor.builder()
                    .contributorOccupation(sponsorOccupation)
                    .contributorAggregateYtd(total)
                    .senator(senator)
                    .build();
            sponsors.add(sponsor);
        }

        return sponsors;
    }
}

