package com.ryanpmartz.booktrackr.service

import com.codahale.metrics.annotation.Timed
import com.ryanpmartz.booktrackr.domain.User
import com.ryanpmartz.booktrackr.domain.UserRole
import com.ryanpmartz.booktrackr.repository.UserRepository
import com.ryanpmartz.booktrackr.repository.UserRoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class UserService @Autowired
constructor(private val userRepository: UserRepository, private val userRoleRepository: UserRoleRepository) {

    @Timed
    @Transactional(readOnly = true)
    open fun getUserByEmail(email: String): Optional<User> {
        return Optional.ofNullable(userRepository.findByEmail(email))
    }

    @Timed
    @Transactional
    open fun createUser(user: User): User {
        val savedUser = userRepository.save(user)
        userRoleRepository.save<UserRole>(savedUser.roles)

        return savedUser
    }
}
