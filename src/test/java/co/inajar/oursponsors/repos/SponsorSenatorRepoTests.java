package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.SponsorSenator;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.SponsorSenatorsRepo;
import co.inajar.oursponsors.dbos.repos.fec.SponsorRepo;
import co.inajar.oursponsors.dbos.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.helpers.NameGenerator;
import co.inajar.oursponsors.helpers.SponsorGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SponsorSenatorRepoTests {


    @Autowired
    private SponsorSenatorsRepo sponsorSenatorsRepo;

    @Autowired
    private SenatorRepo senatorRepo;

    @Autowired
    private SponsorRepo sponsorRepo;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void SponsorSenatorRepo_FindSponsorsBySenatorId_ReturnsSenator() {

        // create member of senator
        String randomName = NameGenerator.generateRandomName();
        String[] name1 = randomName.split(" ");

        Senator senator = Senator.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .build();

        senatorRepo.save(senator);

        // create associated sponsors
        List<Sponsor> sponsors = SponsorGenerator.generateSenatorSponsors(senator);
        for (var sponsor : sponsors) {
            sponsorRepo.save(sponsor);
            SponsorSenator sponsorSenator = new SponsorSenator();
            sponsorSenator.setSenator(senator);
            sponsorSenator.setSponsor(sponsor);
            sponsorSenatorsRepo.save(sponsorSenator);
            sponsorSenatorsRepo.save(sponsorSenator);
        }

        // find our sponsors using sponsorSenator
        List<SponsorSenator> possibleSponsorSenator = sponsorSenatorsRepo.findSponsorsBySenatorId(senator.getId()).get();
        var sponsorSenator = possibleSponsorSenator.parallelStream()
                .map(SponsorSenator::getSponsor)
                .toList();
        List<Long> sponsorIds = sponsorSenator.parallelStream()
                .map(Sponsor::getId)
                .toList();
        List<Sponsor> detachedSponsors = sponsorIds.parallelStream()
                .map((s) -> sponsorRepo.getById(s))
                .toList();

        List<Sponsor> foundSponsors = new ArrayList<>();
        for (Sponsor detachedSponsor : detachedSponsors) {
            Sponsor mergedSponsor = entityManager.merge(detachedSponsor);
            foundSponsors.add(mergedSponsor);
        }

        // assert the sponsors are returned when we look up the ID
        Assertions.assertThat(foundSponsors.size()).isGreaterThan(0);
        Assertions.assertThat(foundSponsors).hasSameElementsAs(sponsors);

    }
}
