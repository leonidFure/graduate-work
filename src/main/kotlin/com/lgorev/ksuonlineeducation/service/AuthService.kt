package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.exception.LoginException
import com.lgorev.ksuonlineeducation.domain.common.TokenResponseModel
import com.lgorev.ksuonlineeducation.domain.user.Role
import com.lgorev.ksuonlineeducation.domain.user.UserLoginModel
import com.lgorev.ksuonlineeducation.domain.user.UserRequestModel
import com.lgorev.ksuonlineeducation.exception.AuthException
import com.lgorev.ksuonlineeducation.repository.user.UserRepository
import com.lgorev.ksuonlineeducation.repository.user.UserEntity
import com.lgorev.ksuonlineeducation.repository.user.UserRoleEntity
import com.lgorev.ksuonlineeducation.repository.user.UserRoleId
import com.lgorev.ksuonlineeducation.security.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(private val userRepository: UserRepository,
                  private val userService: UserService) {

    @Throws(AuthException::class)
    fun login(model: UserLoginModel): TokenResponseModel {
        userService.loadUserByUsername(model.email)?.let {
            if (BCrypt.checkpw(model.password, it.password)) {
                return TokenResponseModel(
                        generateToken(it.id, it.username, it.roles),
                        it.roles,
                        LocalDateTime.now().plusSeconds(ACCESS_TOKEN_LIFETIME_SECONDS)
                )
            } else throw AuthException("Неверный пароль")
        }
        throw AuthException("Пользователь с логином \"${model.email}\" ненайден")
    }

    @Throws(LoginException::class)
    fun register(model: UserRequestModel) {
        userRepository.findByEmail(model.email)?.let {
            throw LoginException("Пользователь с логином \"${model.email}\" уже существует")
        }
        userRepository.save(model.asUserEntity())
    }

    private fun generateToken(userId: UUID, email: String, role: MutableList<Role>): String? {
        return Jwts
                .builder()
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.toByteArray()), SignatureAlgorithm.HS512)
                .setHeaderParam(TOKEN_TYPE_HEADER, TOKEN_TYPE)
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(email)
                .setExpiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_LIFETIME_SECONDS * 1000))
                .claim(ROLES_CLAIM, role.toString())
                .claim(USER_ID_CLAIM, userId)
                .compact()
    }
}

private fun UserRequestModel.asUserEntity(): UserEntity {
    val userId = UUID.randomUUID()
    return UserEntity(
            userId,
            firstName,
            lastName,
            patronymic,
            gender,
            email,
            BCrypt.hashpw(password, BCrypt.gensalt(12)),
            true,
            LocalDate.now(),
            roles.map { UserRoleEntity(UserRoleId(userId, it)) }.toMutableSet()
    )
}

