package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.common.map
import com.lgorev.ksuonlineeducation.domain.user.*
import com.lgorev.ksuonlineeducation.exception.AuthException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.user.UserRepository
import com.lgorev.ksuonlineeducation.repository.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    lateinit var coursesTeacherService: CoursesTeachersService

    @Autowired
    lateinit var coursesSubscriptionService: CourseSubscriptionService

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

    fun setUserPhotoExists(id: UUID) {
        userRepository.findByIdOrNull(id)?.let { user ->
            user.photoExists = true
        }
    }

    fun getPage(model: UserPageRequestModel): PageResponseModel<UserResponseModel> {
        return when {
            model.courseIdForTeacher != null -> {
                val coursesTeachers = coursesTeacherService.getCoursesTeachersByCourseId(model.courseIdForTeacher)
                val teachersIds = coursesTeachers.map { it.teacherId }
                model.ids = teachersIds.toMutableSet()
                userRepository.getPage(model).map { user -> user.toModel() }
            }
            model.courseIdForSubscription != null -> {
                val coursesSubscription = coursesSubscriptionService.getByCourseId(model.courseIdForSubscription)
                val teachersIds = coursesSubscription.map { it.id.courseId }
                model.ids = teachersIds.toMutableSet()
                userRepository.getPage(model).map { user -> user.toModel() }
            }
            else -> {
                userRepository.getPage(model).map { user -> user.toModel() }
            }
        }
    }

    fun existsTeacherById(id: UUID) = userRepository.existsByIdAndRole(id, Role.TEACHER)
}


fun UserEntity.toModel() = UserResponseModel(id, firstName, lastName, patronymic, email, role, "/api/files/avatar/open?id=${id}", startWorkDate, info)

private fun UserEntity.toUserModel() = UserModel(id, firstName, lastName, patronymic, email, password, role)
