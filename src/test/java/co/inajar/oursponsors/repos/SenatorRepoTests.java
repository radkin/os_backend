package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.helpers.FiftyCharacterGenerator;
import co.inajar.oursponsors.helpers.NameGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SenatorRepoTests {

    @Autowired
    private SenatorRepo senatorRepo;

    @Test
    public void SenatorRepo_FindSenatorById_ReturnsSenator() {

        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");

        Senator senator = Senator.builder()
                .firstName(name[0])
                .lastName(name[1])
                .build();

        senatorRepo.save(senator);

        Senator foundSenator = senatorRepo.findSenatorById(senator.getId()).get();

        Assertions.assertThat(foundSenator.getId()).isEqualTo(senator.getId());

    }

    @Test
    public void SenatorRepo_FindFirstSenatorByProPublicaId_ReturnsOneSenator() {

        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");
        String proPublicaId = FiftyCharacterGenerator.generateToken();

        Senator senator = Senator.builder()
                .firstName(name[0])
                .lastName(name[1])
                .proPublicaId(proPublicaId)
                .build();

        senatorRepo.save(senator);

        Senator foundSenator = senatorRepo.findFirstSenatorByProPublicaId(senator.getProPublicaId()).get();

        Assertions.assertThat(foundSenator.getProPublicaId()).isEqualTo(senator.getProPublicaId());

    }

    @Test
    public void SenatorRepo_FindSenatorsByState_ReturnsSenators() {

        String randomName1 = NameGenerator.generateRandomName();
        String[] name1 = randomName1.split(" ");


        String randomName2 = NameGenerator.generateRandomName();
        String[] name2 = randomName2.split(" ");


        Senator senator1 = Senator.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .state("CA")
                .build();

        Senator senator2 = Senator.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .state("CA")
                .build();

        senatorRepo.save(senator1);
        senatorRepo.save(senator2);

        List<Senator> foundSenators = senatorRepo.findSenatorsByState("CA").get();

        Assertions.assertThat(foundSenators.size()).isGreaterThan(0);

    }

    @Test
    public void SenatorRepo_FindSenatorsByParty_ReturnsSenators() {

        String randomName1 = NameGenerator.generateRandomName();
        String[] name1 = randomName1.split(" ");


        String randomName2 = NameGenerator.generateRandomName();
        String[] name2 = randomName2.split(" ");


        Senator senator1 = Senator.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .party("R")
                .build();

        Senator senator2 = Senator.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .party("R")
                .build();

        senatorRepo.save(senator1);
        senatorRepo.save(senator2);

        List<Senator> foundSenators = senatorRepo.findSenatorsByParty("R").get();

        Assertions.assertThat(foundSenators.size()).isGreaterThan(0);

    }


    @Test
    public void SenatorRepo_FindSenatorsByStateAndParty_ReturnsSenators() {

        String randomName1 = NameGenerator.generateRandomName();
        String[] name1 = randomName1.split(" ");


        String randomName2 = NameGenerator.generateRandomName();
        String[] name2 = randomName2.split(" ");


        Senator senator1 = Senator.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .state("OH")
                .party("D")
                .build();

        Senator senator2 = Senator.builder()
                .firstName(name1[0])
                .lastName(name1[1])
                .state("OH")
                .party("D")
                .build();

        senatorRepo.save(senator1);
        senatorRepo.save(senator2);

        List<Senator> foundSenators = senatorRepo.findSenatorsByStateAndParty("OH", "D").get();

        Assertions.assertThat(foundSenators.size()).isGreaterThan(0);

    }
}