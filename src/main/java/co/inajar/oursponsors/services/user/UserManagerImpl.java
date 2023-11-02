package co.inajar.oursponsors.services.user;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.entities.Preferences;
import co.inajar.oursponsors.dbos.repos.PreferenceRepo;
import co.inajar.oursponsors.dbos.repos.UserRepo;
import co.inajar.oursponsors.models.user.PreferencesRequest;
import co.inajar.oursponsors.models.user.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
@Transactional
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PreferenceRepo preferenceRepo;

    @Override
    public Optional<User> getUserById(Long id) {

        return userRepo.findById(id);
    }

    @Override
    public Optional<User> getUserByApiKey(String apiKey) {
        return userRepo.findUserByApiKey(apiKey);
    }

    @Override
    public Preferences getPreferencesByUserId(Long id) {
        return preferenceRepo.findPreferencesByUserId(id);
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
    public User createOrUpdateUser(UserRequest request, User user) {
        updateUserField(request::getFirstName, user::setFirstName);
        updateUserField(request::getLastName, user::setLastName);
        updateUserField(request::getParty, user::setParty);
        updateUserFieldIfNull(request::getEmail, user::getEmail, user::setEmail);
        updateUserField(request::getGender, user::setGender);
        updateUserField(request::getState, user::setState);
        updateUserField(request::getIsEnabled, user::setIsEnabled);
        updateUserField(request::getIsLoggedIn, user::setIsLoggedIn);
        updateUserField(request::getInajarApiKey, user::setApiKey);
        updateUserField(request::getName, user::setName);
        return userRepo.save(user);
    }

    private <T> void updateUserField(Supplier<T> getter, Consumer<T> setter) {
        T value = getter.get();
        if (value != null) {
            setter.accept(value);
        }
    }

    private <T> void updateUserFieldIfNull(Supplier<T> getter, Supplier<T> nullChecker, Consumer<T> setter) {
        T value = getter.get();
        if (value != null && nullChecker.get() == null) {
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
        preferenceRepo.save(preferences);
    }
}
