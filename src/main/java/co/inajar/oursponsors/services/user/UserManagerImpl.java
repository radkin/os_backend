package co.inajar.oursponsors.services.user;

import co.inajar.oursponsors.dbOs.entities.User;
import co.inajar.oursponsors.dbOs.entities.user.Preferences;
import co.inajar.oursponsors.dbOs.repos.PreferencesRepo;
import co.inajar.oursponsors.dbOs.repos.UserRepo;
import co.inajar.oursponsors.services.user.UserManager;
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

}
