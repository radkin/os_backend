package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.SponsorCongress;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.repos.SponsorCongressRepo;
import co.inajar.oursponsors.dbos.repos.fec.SponsorsRepo;
import co.inajar.oursponsors.dbos.repos.propublica.CongressRepo;
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
public class SponsorCongressRepoTests {

    @Autowired
    private SponsorCongressRepo sponsorCongressRepo;

    @Autowired
    private CongressRepo congressRepo;

    @Autowired
    private SponsorsRepo sponsorsRepo;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void SponsorCongressRepo_FindSponsorsByCongressId_ReturnsCongress() {

        // create member of congress
        String randomName = NameGenerator.generateRandomName();
        String[] name1 = randomName.split(" ");

        Congress congress = Congress.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .build();

        congressRepo.save(congress);

        // create associated sponsors
        List<Sponsor> sponsors = SponsorGenerator.generateCongressSponsors(congress);
        for (var sponsor : sponsors) {
            sponsorsRepo.save(sponsor);
            SponsorCongress sponsorCongress = new SponsorCongress();
            sponsorCongress.setCongress(congress);
            sponsorCongress.setSponsor(sponsor);
            sponsorCongressRepo.save(sponsorCongress);
            sponsorCongressRepo.save(sponsorCongress);
        }

        // find our sponsors using sponsorCongress
        List<SponsorCongress> possibleSponsorCongress = sponsorCongressRepo.findSponsorsByCongressId(congress.getId()).get();
        var sponsorCongress = possibleSponsorCongress.parallelStream()
                .map(SponsorCongress::getSponsor)
                .toList();
        List<Long> sponsorIds = sponsorCongress.parallelStream()
                .map(Sponsor::getId)
                .toList();
        List<Sponsor> detachedSponsors = sponsorIds.parallelStream()
                .map((s) -> sponsorsRepo.getById(s))
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
