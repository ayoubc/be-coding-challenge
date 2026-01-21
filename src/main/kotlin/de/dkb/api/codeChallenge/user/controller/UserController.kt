package de.dkb.api.codeChallenge.user.controller


import de.dkb.api.codeChallenge.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users")
    fun getAllUsers() = userService.getAllUsers()
}