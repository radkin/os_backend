package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.repos.PreferenceRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PreferenceRepoTests {

    @Autowired
    private PreferenceRepo preferenceRepo;

    @Test
    public void PreferencesRepo_SaveAll_ReturnsSavedPreferences() {


    }
}
