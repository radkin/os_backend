package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.repos.propublica.CongressRepo;
import co.inajar.oursponsors.helpers.FiftyCharacterGenerator;
import co.inajar.oursponsors.helpers.NameGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CongressRepoTests {

    @Autowired
    private CongressRepo congressRepo;

    @Test
    public void CongressRepo_findCongressById_ReturnsCongress() {

        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");

        Congress congress = Congress.builder()
                .firstName(name[0])
                .lastName(name[1])
                .build();

        congressRepo.save(congress);

        Congress foundCongress = congressRepo.findCongressById(congress.getId()).get();

        Assertions.assertThat(foundCongress.getId()).isEqualTo(congress.getId());

    }

    @Test
    public void CongressRepo_findFirstCongressByProPublicaId_ReturnsOneCongress() {

        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");
        String proPublicaId = FiftyCharacterGenerator.generateToken();

        Congress congress = Congress.builder()
                .firstName(name[0])
                .lastName(name[1])
                .proPublicaId(proPublicaId)
                .build();

        congressRepo.save(congress);

        Congress foundCongress = congressRepo.findFirstCongressByProPublicaId(congress.getProPublicaId()).get();

        Assertions.assertThat(foundCongress.getProPublicaId()).isEqualTo(congress.getProPublicaId());

    }

    @Test
    public void CongressRepo_findCongressesByState_ReturnsCongress() {

        String randomName1 = NameGenerator.generateRandomName();
        String[] name1 = randomName1.split(" ");


        String randomName2 = NameGenerator.generateRandomName();
        String[] name2 = randomName2.split(" ");


        Congress congress1 = Congress.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .state("CA")
                .build();

        Congress congress2 = Congress.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .state("CA")
                .build();

        congressRepo.save(congress1);
        congressRepo.save(congress2);

        List<Congress> foundCongress = congressRepo.findCongressesByState("CA").get();

        Assertions.assertThat(foundCongress.size()).isGreaterThan(0);

    }

    @Test
    public void CongressRepo_findCongressesByParty_ReturnsCongresss() {

        String randomName1 = NameGenerator.generateRandomName();
        String[] name1 = randomName1.split(" ");


        String randomName2 = NameGenerator.generateRandomName();
        String[] name2 = randomName2.split(" ");


        Congress congress1 = Congress.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .party("R")
                .build();

        Congress congress2 = Congress.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .party("R")
                .build();

        congressRepo.save(congress1);
        congressRepo.save(congress2);

        List<Congress> foundCongresss = congressRepo.findCongressesByParty("R").get();

        Assertions.assertThat(foundCongresss.size()).isGreaterThan(0);

    }


    @Test
    public void CongressRepo_findCongressesByStateAndParty_ReturnsCongress() {

        String randomName1 = NameGenerator.generateRandomName();
        String[] name1 = randomName1.split(" ");


        String randomName2 = NameGenerator.generateRandomName();
        String[] name2 = randomName2.split(" ");


        Congress congress1 = Congress.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .state("OH")
                .party("D")
                .build();

        Congress congress2 = Congress.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .state("OH")
                .party("D")
                .build();

        congressRepo.save(congress1);
        congressRepo.save(congress2);

        List<Congress> foundCongresss = congressRepo.findCongressesByStateAndParty("CA", "R").get();

        Assertions.assertThat(foundCongresss.size()).isGreaterThan(0);

    }
}
