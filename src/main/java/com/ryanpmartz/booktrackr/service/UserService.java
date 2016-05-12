package com.ryanpmartz.booktrackr.service;

import com.codahale.metrics.annotation.Timed;
import com.ryanpmartz.booktrackr.domain.User;
import com.ryanpmartz.booktrackr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Timed
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Timed
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
