package com.auth.service.domain.repository;

import com.auth.service.domain.entity.User;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    void deleteByLogin(String login);
}
