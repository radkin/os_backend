package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.SponsorCongress;
import co.inajar.oursponsors.dbos.entities.SponsorSenator;
import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.SponsorCongressRepo;
import co.inajar.oursponsors.dbos.repos.SponsorSenatorsRepo;
import co.inajar.oursponsors.dbos.repos.fec.SponsorRepo;
import co.inajar.oursponsors.dbos.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbos.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.models.fec.SponsorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SponsorManagerImpl implements SponsorManager {

    @Autowired
    DonationManager donationManager;

    @Autowired
    SponsorRepo sponsorRepo;
    @Autowired
    SenatorRepo senatorRepo;
    @Autowired
    CongressRepo congressRepo;
    @Autowired
    SponsorSenatorsRepo sponsorSenatorsRepo;
    @Autowired
    SponsorCongressRepo sponsorCongressRepo;
    private static final String CREATING_DONATIONS = "creating new donations {}";
    private final Logger logger = LoggerFactory.getLogger(SponsorManagerImpl.class);
    private static final String INVALID_CHAMBER_NAME = "Invalid chamber please use either senator or congress";

    @Override
    public List<Sponsor> getSponsors(SponsorRequest data) {
        if (data.getChamber().equals("senator")) {
            Optional<Senator> senator = senatorRepo.findById(data.getOsId());
            if (senator.isPresent()) {
                Optional<List<SponsorSenator>> possibleSponsorSenators = sponsorSenatorsRepo.findSponsorsBySenatorId(senator.get().getId());
                if (possibleSponsorSenators.isPresent()) {
                    var sponsorSenators = possibleSponsorSenators.get().parallelStream()
                            .map(SponsorSenator::getSponsor)
                            .toList();
                    List<Long> sponsorIds = sponsorSenators.parallelStream()
                            .map(Sponsor::getId)
                            .toList();
                    return gatherRelatedSponsors(sponsorIds);
                }
            }
        } else if (data.getChamber().equals("congress")) {
            Optional<Congress> congress = congressRepo.findById(data.getOsId());
            if (congress.isPresent()) {
                Optional<List<SponsorCongress>> possibleSponsorCongress = sponsorCongressRepo.findSponsorsByCongressId(congress.get().getId());
                if (possibleSponsorCongress.isPresent()) {
                    var sponsorCongress = possibleSponsorCongress.get().parallelStream()
                            .map(SponsorCongress::getSponsor)
                            .toList();
                    List<Long> sponsorIds = sponsorCongress.parallelStream()
                            .map(Sponsor::getId)
                            .toList();
                    return gatherRelatedSponsors(sponsorIds);
                }
            }
        } else {
            logger.error("Unable to process request. Chamber and ID are required");
        }
        return Collections.emptyList();
    }

    private List<Sponsor> gatherRelatedSponsors(List<Long> sponsorIds) {
        var sponsors = sponsorIds.parallelStream()
                .map(this::getSponsorById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return sponsors.stream()
                .sorted(Comparator.comparing(Sponsor::getContributorAggregateYtd).reversed())
                .toList();
    }

    private Optional<Sponsor> getSponsorById(Long id) {
        return sponsorRepo.findById(id);
    }

    @Override
    public Optional<Sponsor> getSponsorByName(String name) {
        return Optional.ofNullable(sponsorRepo.findByContributorName(name));
    }

    @Override
    public Sponsor mapFecDonorToSponsor(FecCommitteeDonor donor, Long osId, String chamber) {
        var newSponsor = new Sponsor();
        var receiptAmount = new BigDecimal(donor.getContributionReceiptAmount());
        newSponsor.setContributionReceiptAmount(receiptAmount);
        newSponsor.setContributionReceiptDate(donor.getContributionReceiptDate());
        var aggregateAmount = new BigDecimal(donor.getContributorAggregateYtd());
        newSponsor.setContributorAggregateYtd(aggregateAmount);
        newSponsor.setContributorCity(donor.getContributorCity());
        newSponsor.setContributorEmployer(donor.getContributorEmployer());
        newSponsor.setContributorFirstName(donor.getContributorFirstName());
        newSponsor.setContributorLastName(donor.getContributorLastName());
        newSponsor.setContributorMiddleName(donor.getContributorMiddleName());
        newSponsor.setContributorName(donor.getContributorName());
        newSponsor.setContributorOccupation(donor.getContributorOccupation());
        newSponsor.setContributorState(donor.getContributorState());
        newSponsor.setContributorStreet1(donor.getContributorStreet1());
        newSponsor.setContributorStreet2(donor.getContributorStreet2());
        newSponsor.setContributorZip(donor.getContributorZip());

        if (chamber.equals("senator")) {
            Senator senator = senatorRepo.getById(osId);
            // add new sponsor
            sponsorRepo.save(newSponsor);
            // add sponsorSenator ManyToMany
            SponsorSenator sponsorSenator = new SponsorSenator();
            sponsorSenator.setSponsor(newSponsor);
            sponsorSenator.setSenator(senator);
            sponsorSenatorsRepo.save(sponsorSenator);
        } else if (chamber.equals("congress")) {
            Congress congress = congressRepo.getById(osId);
            // add new sponsor
            sponsorRepo.save(newSponsor);
            // add sponsorCongress ManyToMany
            SponsorCongress sponsorCongress = new SponsorCongress();
            sponsorCongress.setSponsor(newSponsor);
            sponsorCongress.setCongress(congress);
            sponsorCongressRepo.save(sponsorCongress);
        } else {
            logger.info(INVALID_CHAMBER_NAME);
        }
        return newSponsor;
    }

    @Override
    public List<Sponsor> processDonorsAndGetNewSponsors(String chamber, Long osId, String proPublicaId, Map<String, List<FecCommitteeDonor>> cmteFecDonors) {
        List<Sponsor> newSponsors = new ArrayList<>();
        List<Donation> donations = new ArrayList<>();
        cmteFecDonors.forEach((cmte, donorList) -> donorList.forEach(d -> {
            Optional<Sponsor> possibleExistingSponsor = getSponsorByName(d.getContributorName());
            Sponsor sponsor;
            if (possibleExistingSponsor.isEmpty()) {
                sponsor = mapFecDonorToSponsor(d, osId, chamber);
                newSponsors.add(sponsor);
            } else {
                BigDecimal possibleExistingSponsorYtd = possibleExistingSponsor.get().getContributorAggregateYtd();
                BigDecimal newSponsorYtd = new BigDecimal(d.getContributorAggregateYtd());
                sponsor = possibleExistingSponsor.get();
                if (newSponsorYtd.compareTo(possibleExistingSponsorYtd) > 0) {
                    sponsor.setContributorAggregateYtd(possibleExistingSponsorYtd);
                }
            }
            donations.add(donationManager.mapFecDonorToDonation(d, sponsor, proPublicaId));
            logger.info(CREATING_DONATIONS, donations);
        }));
        return newSponsors;
    }

}
