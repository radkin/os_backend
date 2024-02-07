package co.inajar.oursponsors.services;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.SponsorCongressRepo;
import co.inajar.oursponsors.dbos.repos.SponsorSenatorsRepo;
import co.inajar.oursponsors.dbos.repos.fec.SponsorRepo;
import co.inajar.oursponsors.dbos.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbos.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.services.fec.SponsorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SponsorServiceTests {

    @InjectMocks
    private SponsorImpl sponsorsService;

    @Mock
    private SenatorRepo senatorRepo;

    @Mock
    private CongressRepo congressRepo;

    @Mock
    private SponsorRepo sponsorRepo;

    @Mock
    private SponsorSenatorsRepo sponsorSenatorsRepo;

    @Mock
    private SponsorCongressRepo sponsorCongressRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMapFecDonorToSponsorWithSenator() {
        // Arrange
        FecCommitteeDonor donor = new FecCommitteeDonor();
        donor.setContributionReceiptAmount("100.00");
        donor.setContributionReceiptDate("2023-10-11");
        donor.setContributorAggregateYtd("500.00");
        // ... Set other donor properties

        Long osId = 1L;
        String chamber = "senator";

        Senator senator = new Senator();
        senator.setId(osId);

        when(senatorRepo.getById(osId)).thenReturn(senator);
        when(sponsorRepo.save(any(Sponsor.class))).thenReturn(new Sponsor());

        // Act
        Sponsor result = sponsorsService.mapFecDonorToSponsor(donor, osId, chamber);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContributionReceiptAmount()).isEqualTo(new BigDecimal("100.00"));
        assertThat(result.getContributionReceiptDate()).isEqualTo(LocalDate.of(2023, 10, 11).toString());
        assertThat(result.getContributorAggregateYtd()).isEqualTo(new BigDecimal("500.00"));
        // ... Add more assertions for other properties
    }

    @Test
    public void testMapFecDonorToSponsorWithCongress() {
        // Arrange
        FecCommitteeDonor donor = new FecCommitteeDonor();
        donor.setContributionReceiptAmount("200.00");
        donor.setContributionReceiptDate("2023-10-12");
        donor.setContributorAggregateYtd("800.00");
        // ... Set other donor properties

        Long osId = 2L;
        String chamber = "congress";

        Congress congress = new Congress();
        congress.setId(osId);

        when(congressRepo.getById(osId)).thenReturn(congress);
        when(sponsorRepo.save(any(Sponsor.class))).thenReturn(new Sponsor());

        // Act
        Sponsor result = sponsorsService.mapFecDonorToSponsor(donor, osId, chamber);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContributionReceiptAmount()).isEqualTo(new BigDecimal("200.00"));
        assertThat(result.getContributionReceiptDate()).isEqualTo(LocalDate.of(2023, 10, 12).toString());
        assertThat(result.getContributorAggregateYtd()).isEqualTo(new BigDecimal("800.00"));
        // ... Add more assertions for other properties
    }

}