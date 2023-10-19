package co.inajar.oursponsors.services.preferences;

import co.inajar.oursponsors.dbos.entities.Preferences;
import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.repos.PreferencesRepo;
import co.inajar.oursponsors.models.user.PreferencesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@Transactional
public class PreferencesManagerImpl implements PreferencesManager {

    @Autowired
    private PreferencesRepo preferencesRepo;

    @Override
    public Preferences getPreferencesByUserId(Long id) {
        return preferencesRepo.findPreferencesByUserId(id);
    }

    @Override
    public Preferences updateUserPreferences(PreferencesRequest request, Long id) {
        var preferences = getPreferencesByUserId(id);

        updatePreference(request.getMyStateOnly(), preferences::setMyStateOnly);
        updatePreference(request.getMyPartyOnly(), preferences::setMyPartyOnly);
        updatePreference(request.getTwitterHide(), preferences::setTwitterHide);
        updatePreference(request.getFacebookHide(), preferences::setFacebookHide);
        updatePreference(request.getYoutubehide(), preferences::setYoutubeHide);
        updatePreference(request.getGoogleEntityHide(), preferences::setGoogleEntityHide);
        updatePreference(request.getCspanHide(), preferences::setCspanHide);
        updatePreference(request.getGovTrackHide(), preferences::setGovTrackHide);
        updatePreference(request.getOpenSecretsHide(), preferences::setOpenSecretsHide);

        return preferences;
    }

    private <T> void updatePreference(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    @Override
    public void createUserPreferences(User user) {
        Preferences preferences = new Preferences();
        preferences.setUser(user);
        // default preferences
        preferences.setMyPartyOnly(false);
        preferences.setMyStateOnly(false);
        preferencesRepo.save(preferences);
    }
}
