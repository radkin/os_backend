package co.inajar.oursponsors.services.user;

import co.inajar.oursponsors.dbOs.entities.User;
import co.inajar.oursponsors.dbOs.entities.user.Preferences;
import co.inajar.oursponsors.dbOs.repos.PreferencesRepo;
import co.inajar.oursponsors.models.user.PreferencesRequest;
import co.inajar.oursponsors.dbOs.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PreferencesRepo preferencesRepo;

    @Override
    public Optional<User> getUserById(Long id) {

        return userRepo.findById(id);
    }

    @Override
    public Optional<User> getUserByApiKey(String apiKey) {
        return userRepo.findByApiKey(apiKey);
    }

    @Override
    public Preferences getPreferencesByUserId(Long id) { return preferencesRepo.findPreferencesByUserId(id); }

    @Override
    public Preferences updateUserPreferences(PreferencesRequest request) {
        var preferences = getPreferencesByUserId(request.getUserId());
        preferences.setMyStateOnly(request.getMyStateOnly());
        preferences.setMyPartyOnly(request.getMyPartyOnly());
        preferences.setTwitterHide(request.getTwitterHide());
        preferences.setFacebookHide(request.getFacebookHide());
        preferences.setYoutubeHide(request.getYoutubehide());
        preferences.setGoogleEntityHide(request.getGoogleEntityHide());
        preferences.setCspanHide(request.getCspanHide());
        preferences.setVoteSmartHide(request.getVoteSmartHide());
        preferences.setGovTrackHide(request.getGovTrackHide());
        preferences.setOpenSecretsHide(request.getOpenSecretsHide());
        preferencesRepo.save(preferences);
        return preferences;
    }
}
