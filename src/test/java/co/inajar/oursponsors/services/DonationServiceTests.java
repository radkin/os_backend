package co.inajar.oursponsors.services;

import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.repos.fec.DonationRepo;
import co.inajar.oursponsors.helpers.NameGenerator;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.services.fec.DonationManagerImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DonationServiceTests {

    @Mock
    private DonationRepo donationRepo;

    @InjectMocks
    private DonationManagerImpl donationManager;

    @Test
    public void DonationManager_MapFecDonorToDonation_ReturnsDonation() {

        var PpId = "999";

        FecCommitteeDonor fecCommitteeDonor = FecCommitteeDonor.builder()
                .contributionReceiptDate("2020-01-20")
                .contributionReceiptAmount("1000")
                .build();

        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");

        Sponsor sponsor = Sponsor.builder()
                        .contributorAggregateYtd(BigDecimal.valueOf(1))
                        .contributorFirstName(name[0])
                        .contributorLastName(name[1])
                        .build();

        Donation donation = Donation.builder()
                .sponsor(sponsor)
                .amount(BigDecimal.valueOf(10))
                .ppId(PpId)
                .build();

        when(donationRepo.save(Mockito.any(Donation.class))).thenReturn(donation);
        Donation savedDonation = donationManager.mapFecDonorToDonation(fecCommitteeDonor, sponsor, PpId);
        Assertions.assertThat(savedDonation).isNotNull();
    }

}
