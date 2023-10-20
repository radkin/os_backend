package co.inajar.oursponsors.services;

import co.inajar.oursponsors.dbos.entities.campaigns.Committee;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.CommitteeRepo;
import co.inajar.oursponsors.services.fec.CommitteeApiImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommitteeApiServiceTests {

    @Mock
    private CommitteeRepo committeeRepo;

    @InjectMocks
    private CommitteeApiImpl committeeApiManager;


    @Test
    public void CommitteeApiManager_CreateSenatorCommittee_ReturnsSenatorCommittee() {

        String fecCommitteeId = "fakeFecCID";

        Committee committee = Committee.builder()
                .ppId("fakePpId")
                .fecCommitteeId(fecCommitteeId)
                .twoYearTransactionPeriod(2020)
                .build();

        Senator senator = Senator.builder()
                .proPublicaId("fakeProPublicaID")
                .nextElection("2024")
                .build();

        when(committeeRepo.save(Mockito.any(Committee.class))).thenReturn(committee);
        Committee savedCommittee = committeeApiManager.createSenatorCommittee(senator, fecCommitteeId);
        Assertions.assertThat(savedCommittee).isNotNull();
        Assertions.assertThat(savedCommittee.getFecCommitteeId()).isEqualTo(fecCommitteeId);
    }

    @Test
    public void CommitteeManager_CreateCongressCommittee_ReturnsCongressCommittee() {
        String fecCommitteeId = "fakeFecCID";

        Committee committee = Committee.builder()
                .ppId("fakePpId")
                .fecCommitteeId(fecCommitteeId)
                .twoYearTransactionPeriod(2020)
                .build();

        Congress congress = Congress.builder()
                .proPublicaId("fakeProPublicaID")
                .nextElection("2023")
                .build();

        when(committeeRepo.save(Mockito.any(Committee.class))).thenReturn(committee);
        Committee savedCommittee = committeeApiManager.createCongressCommittee(congress, fecCommitteeId);
        Assertions.assertThat(savedCommittee).isNotNull();
        Assertions.assertThat(savedCommittee.getFecCommitteeId()).isEqualTo(fecCommitteeId);
    }

    @Test void CommitteeManager_GetCommittees_ReturnsListOfCommittees() {

        String fecCommitteeId = "fakeFecCID";

        Committee committee1 = Committee.builder()
                .ppId("fakePpId1")
                .fecCommitteeId(fecCommitteeId)
                .twoYearTransactionPeriod(2020)
                .build();

        Committee committee2 = Committee.builder()
                .ppId("fakePpId2")
                .fecCommitteeId(fecCommitteeId)
                .twoYearTransactionPeriod(2020)
                .build();

        Congress congress = Congress.builder()
                .proPublicaId("fakeProPublicaID")
                .nextElection("2023")
                .build();

        when(committeeRepo.save(Mockito.any(Committee.class))).thenReturn(committee1);
        Committee savedCommittee1 = committeeApiManager.createCongressCommittee(congress, fecCommitteeId);

        when(committeeRepo.save(Mockito.any(Committee.class))).thenReturn(committee2);
        Committee savedCommittee2 = committeeApiManager.createCongressCommittee(congress, fecCommitteeId);

        var committeesList = new ArrayList<Committee>();
        committeesList.add(savedCommittee1);
        committeesList.add(savedCommittee2);

        Assertions.assertThat(committeesList.size()).isGreaterThan(0);
    }

}
