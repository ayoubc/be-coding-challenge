package de.dkb.api.codeChallenge.user.service

import de.dkb.api.codeChallenge.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun getAllUsers() = userRepository.findAll()
}