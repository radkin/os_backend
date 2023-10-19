package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.candidates.Sector;
import co.inajar.oursponsors.dbos.repos.opensecrets.SectorRepo;
import co.inajar.oursponsors.helpers.SectorGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SectorRepoTests {

    @Autowired
    private SectorRepo sectorRepo;

    @Test
    public void SectorRepo_FindTop10SectorsByCidOrderByTotalDesc_ReturnsListOfSectors() {

        List<Sector> sectors = SectorGenerator.generateSectors();
        var firstSector = sectors.stream().findFirst();

        sectorRepo.saveAll(sectors);

        List<Sector> foundSectors = sectorRepo.findTop10SectorsByCidOrderByTotalDesc(firstSector.get().getCid()).get();

        // make sure the smallest sector is not in our list of the top 10
        Sector smallestSector = findSectorWithSmallestTotal(sectors);

        Assertions.assertThat(foundSectors.size()).isGreaterThan(0);
        Assertions.assertThat(foundSectors).doesNotContain(smallestSector);
    }

    private Sector findSectorWithSmallestTotal(List<Sector> sectors) {
        if (sectors.isEmpty()) {
            return null; // Return null if the list is empty
        }

        Sector smallestSector = sectors.get(0); // Initialize with the first sector
        for (Sector sector : sectors) {
            if (sector.getTotal() < smallestSector.getTotal()) {
                smallestSector = sector; // Update if a smaller total is found
            }
        }

        return smallestSector;
    }
}
