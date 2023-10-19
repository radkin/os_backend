package co.inajar.oursponsors.services;

import co.inajar.oursponsors.dbos.repos.PreferencesRepo;
import co.inajar.oursponsors.services.preferences.PreferencesManagerImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PreferencesServiceTests {

    @Mock
    private PreferencesRepo preferencesRepo;

    @InjectMocks
    private PreferencesManagerImpl preferencesManager;

    // need to backfill preferences repo tests.

//    @Test
//    public void PreferncesManager_UpdateUserPreferences_ReturnsPreferences() {
//
//    }

}
