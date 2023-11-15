package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.campaigns.Committee;
import co.inajar.oursponsors.dbos.repos.CommitteeRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommitteeRepoTests {

    @Autowired
    private CommitteeRepo committeeRepo;

    @Test
    public void CommitteeRepo_SaveAll_ReturnsSavedCommittee() {

        Committee committee = Committee.builder()
                .fecCommitteeId("C00476564")
                .build();

        Committee savedCommittee = committeeRepo.save(committee);

        Assertions.assertThat(savedCommittee).isNotNull();
        Assertions.assertThat(savedCommittee.getId()).isGreaterThan(0);
    }

    @Test
    public void CommitteeRepo_GetAll_ReturnsMoreThanOneCommittee() {

        Committee committee1 = Committee.builder()
                .fecCommitteeId("C00476564")
                .build();

        Committee committee2 = Committee.builder()
                .fecCommitteeId("C00476511")
                .build();

        committeeRepo.save(committee1);
        committeeRepo.save(committee2);

        List<Committee> committeeList = committeeRepo.findAll();

        Assertions.assertThat(committeeList).isNotNull();
        Assertions.assertThat(committeeList.size()).isGreaterThan(0);
    }

    @Test
    public void CommitteeRepo_FindById_ReturnsCommittee() {

        Committee committee = Committee.builder()
                .fecCommitteeId("C00476564")
                .build();

        committeeRepo.save(committee);

        Committee foundCommittee = committeeRepo.findById(committee.getId()).get();

        Assertions.assertThat(foundCommittee).isNotNull();
    }

}