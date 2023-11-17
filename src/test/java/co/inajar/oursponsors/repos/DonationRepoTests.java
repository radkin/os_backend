package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import co.inajar.oursponsors.dbos.repos.fec.DonationRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;


@SpringBootTest
public class DonationRepoTests {

    @Autowired
    private DonationRepo donationRepo;

    @Test
    public void DonationRepo_SaveAll_ReturnsSavedDonation() {

        Donation donation = Donation.builder()
                .amount(BigDecimal.valueOf(100))
                .build();

        Donation savedDonation = donationRepo.save(donation);

        Assertions.assertThat(savedDonation).isNotNull();
        Assertions.assertThat(savedDonation.getId()).isGreaterThan(0);
    }

    @Test
    public void DonationRepo_GetAll_ReturnsMoreThanOneDonation() {

        Donation donation1 = Donation.builder()
                .amount(BigDecimal.valueOf(100))
                .build();

        Donation donation2 = Donation.builder()
                .amount(BigDecimal.valueOf(500))
                .build();

        donationRepo.save(donation1);
        donationRepo.save(donation2);

        List<Donation> donationList = donationRepo.findAll();

        Assertions.assertThat(donationList).isNotNull();
        Assertions.assertThat(donationList.size()).isGreaterThan(0);
    }

    @Test
    public void DonationRepo_FindById_ReturnsDonation() {

        Donation donation = Donation.builder()
                .amount(BigDecimal.valueOf(100))
                .build();

        donationRepo.save(donation);

        Donation foundDonation = donationRepo.findById(donation.getId()).get();

        Assertions.assertThat(foundDonation).isNotNull();
    }

}