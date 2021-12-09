package org.itxtech.pams.repo;

import org.itxtech.pams.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByToken(String token);
}
