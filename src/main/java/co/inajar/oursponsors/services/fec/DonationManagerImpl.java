package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.repos.fec.DonationRepo;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.models.fec.MiniDonationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DonationManagerImpl implements DonationManager {

    @Autowired
    private DonationRepo donationRepo;

    @Override
    public Donation mapFecDonorToDonation(FecCommitteeDonor donor, Sponsor sponsor, String ppId) {
        var newDonation = new Donation();
        newDonation.setDateOfDonation(LocalDate.parse(donor.getContributionReceiptDate()));
        BigDecimal donation = new BigDecimal(donor.getContributionReceiptAmount());
        newDonation.setAmount(donation);
        newDonation.setSponsor(sponsor);
        newDonation.setPpId(ppId);
        return donationRepo.save(newDonation);
    }

    @Override
    public List<MiniDonationResponse> mapDonationResponses(List<Sponsor> sponsors) {
        return sponsors.parallelStream()
                .flatMap(sponsor -> sponsor.getDonations().stream())
                .map(MiniDonationResponse::new)
                .toList();
    }
}
