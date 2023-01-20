package co.inajar.oursponsors.services;

import co.inajar.oursponsors.dbOs.entities.User;
import co.inajar.oursponsors.dbOs.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserManagerImpl implements UserManager{

    @Autowired
    private UserRepo userRepo;

    @Override
    public Optional<User> getUserById(Long id) {

        return userRepo.findById(id);
    }

    @Override
    public Optional<User> getUserByApiKey(String apiKey) {
        return userRepo.findByApiKey(apiKey);
    }

}
