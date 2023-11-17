package co.inajar.oursponsors.helpers;

import co.inajar.oursponsors.dbos.entities.candidates.Sector;

import java.util.ArrayList;
import java.util.List;

public class SectorGenerator {

    public static void main(String[] args) {
        List<Sector> generatedSectors = generateSectors();
        for (Sector sector : generatedSectors) {
            System.out.println("Sector Name: " + sector.getSectorName() + ", Total: " + sector.getTotal());
        }
    }

    public static List<Sector> generateSectors() {
        List<Sector> sectors = new ArrayList<>();
        String sectorName = "tech";
        String cid = "N00001234";

        for (int i = 1; i <= 11; i++) {
            int total = 1000 - (i * 100); // Vary the total in each iteration
            Sector sector = Sector.builder()
                    .sectorName(sectorName)
                    .total(total)
                    .cid(cid)
                    .build();
            sectors.add(sector);
        }

        return sectors;
    }
}
