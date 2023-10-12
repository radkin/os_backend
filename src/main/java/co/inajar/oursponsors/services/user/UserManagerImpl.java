package co.inajar.oursponsors.services.user;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.repos.UserRepo;
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

    @Override
    public Optional<User> getUserById(Long id) {

        return userRepo.findById(id);
    }

    @Override
    public Optional<User> getUserByApiKey(String apiKey) {
        return userRepo.findUserByApiKey(apiKey);
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
        updateUserField(request::getGoogleUid, user::setGoogleUid);
        updateUserField(request::getIsLoggedIn, user::setIsLoggedIn);
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
    public Optional<User> getUserByGoogleUid(String googleUid) {
        return userRepo.findUserByGoogleUid(googleUid);
    }

    // ToDo: createUser, updateUser with password encode and decode
        /*
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String pwd = bcryptPasswordEncoder.encode("password");
        */


}
