package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.teacher.TeacherModel
import com.lgorev.ksuonlineeducation.domain.user.*
import com.lgorev.ksuonlineeducation.exception.AuthException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.teacher.TeacherEntity
import com.lgorev.ksuonlineeducation.repository.user.UserRepository
import com.lgorev.ksuonlineeducation.repository.user.UserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(email: String?) = userRepository.findByEmail(email ?: "")?.toUserModel()

    @Throws(NotFoundException::class)
    fun getUserById(id: UUID): UserResponseModel {
        userRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Пользователь не найден")
    }

    fun loadPage(pageable: Pageable) = userRepository.findAll(pageable).map { it.toModel() }

    @Throws(NotFoundException::class)
    fun updateUser(model: UserRequestModel): UserResponseModel {
        userRepository.findByIdOrNull(model.id)?.let { user ->
            user.firstName = model.firstName
            user.lastName = model.lastName
            user.patronymic = model.patronymic
            return user.toModel()
        }
        throw NotFoundException("Пользователь не найден")
    }

    @Throws(NotFoundException::class)
    fun setUserNotActive(id: UUID) {
        val user = userRepository.findByIdOrNull(id)
        if (user != null) user.isActive = false
        else throw NotFoundException("Пользователь не найден")
    }

    @Throws(AuthException::class)
    fun updatePassword(model: PasswordModel) {
        userRepository.findByIdOrNull(model.id)?.let { user ->
            user.password = BCrypt.hashpw(model.password, BCrypt.gensalt(12))
        }
        throw NotFoundException("Пользователь не найден")
    }
    fun existUserById(id: UUID) = userRepository.existsById(id)
}


fun UserEntity.toModel() = UserResponseModel(id, firstName, lastName, patronymic, email, roles.map { it.userRoleId.role }.toMutableSet(), teacher?.toModel())

private fun TeacherEntity.toModel() = TeacherModel(startWorkDate, info)

private fun UserEntity.toUserModel() = UserModel(id, firstName, lastName, patronymic, email, password, roles.map { it.userRoleId.role }.toMutableList())
