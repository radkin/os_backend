package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.models.fec.FecCommitteeDonor;

import java.util.List;

public interface CommitteesApiManager {

    List<FecCommitteeDonor> getFecCommitteeDonors(String committeeId, Integer twoYearTransactionPeriod);
}
