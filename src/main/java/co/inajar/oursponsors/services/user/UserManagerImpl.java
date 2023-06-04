package co.inajar.oursponsors.services.user;

import co.inajar.oursponsors.dbOs.entities.User;
import co.inajar.oursponsors.dbOs.entities.user.Preferences;
import co.inajar.oursponsors.dbOs.repos.PreferencesRepo;
import co.inajar.oursponsors.models.user.PreferencesRequest;
import co.inajar.oursponsors.dbOs.repos.UserRepo;
import co.inajar.oursponsors.models.user.UserRequest;
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
        return userRepo.findUserByApiKey(apiKey);
    }

    @Override
    public Preferences getPreferencesByUserId(Long id) { return preferencesRepo.findPreferencesByUserId(id); }

    @Override
    public Preferences updateUserPreferences(PreferencesRequest request) {
        var preferences = getPreferencesByUserId(request.getUserId());

        if (request.getMyStateOnly() != null) preferences.setMyStateOnly(request.getMyStateOnly());
        if (request.getMyPartyOnly() != null) preferences.setMyPartyOnly(request.getMyPartyOnly());
        if (request.getTwitterHide() != null) preferences.setTwitterHide(request.getTwitterHide());
        if (request.getFacebookHide() != null) preferences.setFacebookHide(request.getFacebookHide());
        if (request.getYoutubehide() != null) preferences.setYoutubeHide(request.getYoutubehide());
        if (request.getGoogleEntityHide() != null) preferences.setGoogleEntityHide(request.getGoogleEntityHide());
        if (request.getCspanHide() != null) preferences.setCspanHide(request.getCspanHide());
        if (request.getGovTrackHide() !=null) preferences.setGovTrackHide(request.getGovTrackHide());
        if (request.getOpenSecretsHide() != null) preferences.setOpenSecretsHide(request.getOpenSecretsHide());
        return preferences;
    }

    // ToDo: createUser, updateUser with password encode and decode
        /*
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String pwd = bcryptPasswordEncoder.encode("password");
        */

    @Override
    public User updateUser(UserRequest request, User user) {
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getParty() != null) user.setParty(request.getParty());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getState() != null) user.setState(request.getState());
        return userRepo.save(user);
    }
}
