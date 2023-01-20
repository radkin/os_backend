package co.inajar.oursponsors.services;

import co.inajar.oursponsors.dbOs.entities.User;

import java.util.Optional;

public interface UserManager {
    Optional<User> getUserById(Long id);
    Optional<User> getUserByApiKey(String apiKey);
}
