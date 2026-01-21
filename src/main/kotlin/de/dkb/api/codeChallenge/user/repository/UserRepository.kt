package de.dkb.api.codeChallenge.user.repository

import de.dkb.api.codeChallenge.user.model.User
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, UUID>