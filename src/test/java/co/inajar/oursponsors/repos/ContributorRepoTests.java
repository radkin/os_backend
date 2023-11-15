package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import co.inajar.oursponsors.dbos.repos.opensecrets.ContributorRepo;
import co.inajar.oursponsors.helpers.ContributorGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ContributorRepoTests {

    @Autowired
    private ContributorRepo contributorRepo;

    @Test
    public void ContributorRepo_FindTop10ContributorsByCidOrderByTotalDesc_ReturnsListOfContributors() {

        List<Contributor> contributors = ContributorGenerator.generateContributors();
        var firstContributor = contributors.stream().findFirst();

        contributorRepo.saveAll(contributors);

        List<Contributor> foundContributos = contributorRepo.findTop10ContributorsByCidOrderByTotalDesc(firstContributor.get().getCid()).get();

        // make sure the smallest contributor is not in our list of the top 10
        Contributor smallestContributor = findContributorWithSmallestTotal(contributors);

        Assertions.assertThat(foundContributos.size()).isGreaterThan(0);
        Assertions.assertThat(foundContributos).doesNotContain(smallestContributor);
    }

    private Contributor findContributorWithSmallestTotal(List<Contributor> contributors) {
        if (contributors.isEmpty()) {
            return null;
        }

        Contributor smallestContributor = contributors.get(0);
        for (Contributor contributor : contributors) {
            if (contributor.getTotal() < smallestContributor.getTotal()) {
                smallestContributor = contributor;
            }
        }

        return smallestContributor;
    }

}