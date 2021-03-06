package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.exception.LoginException
import com.lgorev.ksuonlineeducation.domain.common.TokenResponseModel
import com.lgorev.ksuonlineeducation.domain.user.Role
import com.lgorev.ksuonlineeducation.domain.user.UserLoginModel
import com.lgorev.ksuonlineeducation.domain.user.UserRequestModel
import com.lgorev.ksuonlineeducation.exception.AuthException
import com.lgorev.ksuonlineeducation.repository.session.SessionEntity
import com.lgorev.ksuonlineeducation.repository.user.UserRepository
import com.lgorev.ksuonlineeducation.repository.user.UserEntity
import com.lgorev.ksuonlineeducation.security.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class AuthService(private val userRepository: UserRepository) {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var sessionService: SessionService

    @Throws(AuthException::class)
    fun login(model: UserLoginModel): TokenResponseModel {
        userService.loadUserByUsername(model.email)?.let { user ->
            if (BCrypt.checkpw(model.password, user.password)) {
                val sessionId = UUID.randomUUID()
                val accessToken = generateAccessToken(user.id, user.username, user.role, sessionId)
                val refreshToken = generateRefreshToken(user.id, user.username, sessionId)
                val session = SessionEntity(
                        sessionId,
                        user.id,
                        LocalDateTime.now().plusSeconds(REFRESH_TOKEN_LIFETIME_SECONDS)
                )
                sessionService.addSession(session)
                return TokenResponseModel(
                        accessToken,
                        refreshToken,
                        user.id,
                        user.role,
                        LocalDateTime.now().plusSeconds(ACCESS_TOKEN_LIFETIME_SECONDS),
                        LocalDateTime.now().plusSeconds(REFRESH_TOKEN_LIFETIME_SECONDS)
                )
            } else throw AuthException("Неверный пароль")
        }
        throw AuthException("Пользователь с логином \"${model.email}\" ненайден")
    }

    @Throws(LoginException::class)
    fun register(model: UserRequestModel): TokenResponseModel {
        userRepository.findByEmail(model.email)?.let {
            throw LoginException("Пользователь с логином \"${model.email}\" уже существует")
        }
        val newUser = userRepository.save(model.toUserEntity())
        val sessionId = UUID.randomUUID()
        val accessToken = generateAccessToken(newUser.id, newUser.email, newUser.role, sessionId)
        val refreshToken = generateRefreshToken(newUser.id, newUser.email, sessionId)
        val session = SessionEntity(
                sessionId,
                newUser.id,
                LocalDateTime.now().plusSeconds(REFRESH_TOKEN_LIFETIME_SECONDS)
        )
        sessionService.addSession(session)
        return TokenResponseModel(
                accessToken,
                refreshToken,
                newUser.id,
                newUser.role,
                LocalDateTime.now().plusSeconds(ACCESS_TOKEN_LIFETIME_SECONDS),
                LocalDateTime.now().plusSeconds(REFRESH_TOKEN_LIFETIME_SECONDS)
        )
    }

    @Throws(AuthException::class)
    fun refresh(login: String, sessionId: UUID): TokenResponseModel {
        userService.loadUserByUsername(login)?.let { user ->
            val accessToken = generateAccessToken(user.id, user.username, user.role, sessionId)
            val refreshToken = generateRefreshToken(user.id, user.username, sessionId)
            val refreshTokenExpirationTime = LocalDateTime.now().plusSeconds(REFRESH_TOKEN_LIFETIME_SECONDS)
            sessionService.updateSession(sessionId, refreshTokenExpirationTime)
            return TokenResponseModel(
                    accessToken,
                    refreshToken,
                    user.id,
                    user.role,
                    LocalDateTime.now().plusSeconds(ACCESS_TOKEN_LIFETIME_SECONDS),
                    LocalDateTime.now().plusSeconds(REFRESH_TOKEN_LIFETIME_SECONDS)
            )
        }
        throw AuthException("Пользователь с логином \"${login}\" ненайден")
    }

    fun logout(sessionId: UUID) {
        sessionService.deleteSession(sessionId)
    }


    private fun generateAccessToken(userId: UUID, email: String, role: Role, sessionId: UUID): String? {
        return Jwts
                .builder()
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.toByteArray()), SignatureAlgorithm.HS512)
                .setHeaderParam(TOKEN_TYPE_HEADER, TOKEN_TYPE)
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(email)
                .setExpiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_LIFETIME_SECONDS * 1000))
                .claim(ROLES_CLAIM, role.toString())
                .claim(SESSION_ID_CLAIM, sessionId.toString())
                .claim(USER_ID_CLAIM, userId)
                .compact()
    }

    private fun generateRefreshToken(userId: UUID, email: String, sessionId: UUID): String? {
        return Jwts
                .builder()
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.toByteArray()), SignatureAlgorithm.HS512)
                .setHeaderParam(TOKEN_TYPE_HEADER, TOKEN_TYPE)
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(email)
                .setExpiration(Date(System.currentTimeMillis() + REFRESH_TOKEN_LIFETIME_SECONDS * 1000))
                .claim(ROLES_CLAIM, listOf(REFRESH_ROLE).toString())
                .claim(SESSION_ID_CLAIM, sessionId.toString())
                .claim(USER_ID_CLAIM, userId)
                .compact()
    }
}

fun UserRequestModel.toUserEntity(): UserEntity {
    val userId = UUID.randomUUID()
    return UserEntity(
            userId,
            firstName,
            lastName,
            patronymic,
            email,
            BCrypt.hashpw(password, BCrypt.gensalt(12)),
            true,
            LocalDate.now(),
            role,
            startWorkDate,
            info,
            null
    )
}

