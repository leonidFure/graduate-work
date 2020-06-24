package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.common.forEach
import com.lgorev.ksuonlineeducation.domain.common.map
import com.lgorev.ksuonlineeducation.domain.user.*
import com.lgorev.ksuonlineeducation.exception.AuthException
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.faculty.TeachersFacultiesEntity
import com.lgorev.ksuonlineeducation.repository.faculty.TeachersFacultiesId
import com.lgorev.ksuonlineeducation.repository.user.UserRepository
import com.lgorev.ksuonlineeducation.repository.user.UserEntity
import com.lgorev.ksuonlineeducation.util.getRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import java.util.*

@Service
@Transactional
class UserService(private val userRepository: UserRepository) : UserDetailsService {

    @Autowired
    private lateinit var coursesTeacherService: CoursesTeachersService

    @Autowired
    private lateinit var coursesSubscriptionService: CourseSubscriptionService

    @Autowired
    private lateinit var teachersFacultiesService: TeachersFacultiesService

    @Autowired
    private lateinit var facultiesService: FacultyService

    override fun loadUserByUsername(email: String?) = userRepository.findByEmail(email ?: "")?.toUserModel()

    @Throws(NotFoundException::class)
    fun getUserById(id: UUID): UserResponseModel {
        userRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw BadRequestException("Пользователь не найден")
    }

    fun getUsersByIds(ids: MutableSet<UUID>) = userRepository.findAllById(ids).map { it.toModel() }
    fun getUsersByIds2(ids: MutableSet<UUID?>) = userRepository.findAllById(ids).map { it.toModel() }

    @Throws(NotFoundException::class)
    fun updateUser(model: UserRequestModel): UserResponseModel {
        userRepository.findByIdOrNull(model.id)?.let { user ->
            user.firstName = model.firstName
            user.lastName = model.lastName
            user.patronymic = model.patronymic
            user.startWorkDate = model.startWorkDate
            user.info = model.info
            return user.toModel()
        }
        throw BadRequestException("Пользователь не найден")
    }

    @Throws(BadRequestException::class)
    fun saveUser(model: UserRequestModel): UserResponseModel {
        if (userRepository.existsByEmail(model.email))
            throw BadRequestException("Пользователь с логином \"${model.email}\" уже существует")
        val response = userRepository.save(model.toUserEntity()).toModel()
        if (model.facultiesIds != null) {
            val list = model.facultiesIds.map { TeachersFacultiesEntity(TeachersFacultiesId(response.id, it)) }
            teachersFacultiesService.saveAll(list)
        }
        return response
    }

    @Throws(BadRequestException::class)
    fun setUserNotActive(id: UUID) {
        val user = userRepository.findByIdOrNull(id)
        if (user != null) user.isActive = false
        else throw BadRequestException("Пользователь не найден")
    }

    @Throws(AuthException::class)
    fun updatePassword(model: PasswordModel, id: UUID) {
        val user = userRepository.findByIdOrNull(id)
        if (user != null) {
            if (BCrypt.checkpw(model.oldPassword, user.password)) {
                user.password = BCrypt.hashpw(model.password, BCrypt.gensalt(12))
            } else throw BadRequestException("Вы указали не верный пароль")
        } else throw BadRequestException("Пользователь не найден")
    }

    fun existUserById(id: UUID) = userRepository.existsById(id)

    fun getAllUsers() = userRepository.findAllByRole(Role.TEACHER).map { it.toModel() }

    fun getCourseTeachers(courseId: UUID): List<UserResponseModel> {
        val coursesTeachers = coursesTeacherService.getCoursesTeachersByCourseId(courseId)
        val list = coursesTeachers.map { it.teacherId }
        return userRepository.findAllById(list).map { it.toModel() }
    }

    fun getNotCourseTeachers(courseId: UUID): List<UserResponseModel> {
        val coursesTeachers = coursesTeacherService.getCoursesTeachersByCourseId(courseId)
        val list = coursesTeachers.map { it.teacherId }
        return if (list.isEmpty()) userRepository.findAllByRole(Role.TEACHER).map { it.toModel() }
        else userRepository.findAllByRoleAndIdNotIn(Role.TEACHER, list).map { it.toModel() }
    }

    fun getCourseSubs(courseId: UUID): List<UserResponseModel> {
        val coursesTeachers = coursesSubscriptionService.getByCourseId(courseId)
        val list = coursesTeachers.map { it.id.userId }
        return userRepository.findAllById(list).map { it.toModel() }
    }

    fun getPage(model: UserPageRequestModel, principal: Principal): PageResponseModel<UserResponseModel> {

        val page = when {
            model.courseIdForTeacher != null -> {
                val coursesTeachers = coursesTeacherService.getCoursesTeachersByCourseId(model.courseIdForTeacher)
                val teachersIds = coursesTeachers.map { it.teacherId }
                model.ids = teachersIds.toMutableSet()
                userRepository.getPage(model, principal).map { user -> user.toModel() }
            }
            model.courseIdForSubscription != null -> {
                val coursesSubscription = coursesSubscriptionService.getByCourseId(model.courseIdForSubscription)
                val teachersIds = coursesSubscription.map { it.id.courseId }
                model.ids = teachersIds.toMutableSet()
                userRepository.getPage(model, principal).map { user -> user.toModel() }
            }
            else -> {
                userRepository.getPage(model, principal).map { user -> user.toModel() }
            }
        }
        val ids = page.content.map { it.id }.toMutableSet()

        val teachersFaculties = teachersFacultiesService.getTeachersFacultiesByTeacherIds(ids)
        val facultiesIds = teachersFaculties.map { it.facultyId }.toMutableSet()
        val faculties = facultiesService.getFacultyListByIds(facultiesIds)

        page.forEach { user ->
            run {
                val currentFacultiesIds = teachersFaculties
                        .filter { it.teacherId == user.id }
                        .map { it.facultyId }
                val currentFaculties = faculties
                        .filter { it.id in currentFacultiesIds }
                        .toMutableSet()
                user.faculties = currentFaculties
            }
        }
        return page
    }

    fun existsTeacherById(id: UUID) = userRepository.existsByIdAndRole(id, Role.TEACHER)

    fun setImageId(userId: UUID, imageId: UUID) {
        userRepository.findByIdOrNull(userId)?.let { it.imageId = imageId }
    }

}


fun UserEntity.toModel() = UserResponseModel(id, firstName, lastName, patronymic, email, role, "/api/files/users?id=${id}", startWorkDate, info, imageId, registrationDate = registrationDate)


private fun UserEntity.toUserModel() = UserModel(id, firstName, lastName, patronymic, email, password, role)
