package co.inajar.oursponsors.helpers;

import co.inajar.oursponsors.dbos.entities.candidates.Contributor;

import java.util.ArrayList;
import java.util.List;

public class ContributorGenerator {

    public static void main(String[] args) {
        List<Contributor> generatedContributors = generateContributors();
        for (Contributor contributor : generatedContributors) {
            System.out.println("Contributor Org: " + contributor.getOrgName() + ", Total: " + contributor.getTotal());
        }
    }

    public static List<Contributor> generateContributors() {
        List<Contributor> contributors = new ArrayList<>();
        String contributorOrg = "Meta";
        String cid = "N00001234";

        for (int i = 1; i <= 11; i++) {
            int total = 1000 - (i * 100); // Vary the total in each iteration
            Contributor contributor = Contributor.builder()
                    .orgName(contributorOrg)
                    .total(total)
                    .cid(cid)
                    .build();
            contributors.add(contributor);
        }

        return contributors;
    }
}

