package com.ryanpmartz.booktrackr.service;

import com.codahale.metrics.annotation.Timed;
import com.ryanpmartz.booktrackr.domain.User;
import com.ryanpmartz.booktrackr.repository.UserRepository;
import com.ryanpmartz.booktrackr.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Timed
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Timed
    @Transactional
    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        userRoleRepository.save(savedUser.getRoles());

        return savedUser;
    }
}
