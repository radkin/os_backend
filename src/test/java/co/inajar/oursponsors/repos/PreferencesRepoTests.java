package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.repos.PreferencesRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PreferencesRepoTests {

    @Autowired
    private PreferencesRepo preferencesRepo;

    @Test
    public void PreferencesRepo_SaveAll_ReturnsSavedPreferences() {


    }
}
