package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.group.GroupRequestModel
import com.lgorev.ksuonlineeducation.domain.group.GroupResponseModel
import com.lgorev.ksuonlineeducation.domain.user.Role
import com.lgorev.ksuonlineeducation.domain.user.Role.STUDENT
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.group.*
import com.lgorev.ksuonlineeducation.repository.trainingdirection.TrainingDirectionRepository
import com.lgorev.ksuonlineeducation.repository.user.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Service
@Transactional
class GroupService(private val groupRepository: GroupRepository,
                   private val trainingDirectionRepository: TrainingDirectionRepository,
                   private val userGroupRepository: UserGroupRepository,
                   private val userRepository: UserRepository) {

    @Throws(UniqueConstraintException::class)
    fun addGroup(model: GroupRequestModel): GroupResponseModel {
        groupRepository.findByName(model.name)?.let {
            throw UniqueConstraintException(message = "Группа ${model.name} уже существует")
        }
        return groupRepository.save(model.toEntity()).toModel()
    }

    @Throws(UniqueConstraintException::class, NotFoundException::class)
    fun updateGroup(model: GroupRequestModel): GroupResponseModel {
        groupRepository.findByName(model.name)?.let { group ->
            if (group.id != model.id)
                throw UniqueConstraintException(message = "Группа ${model.name} уже существует")
        }
        groupRepository.findByIdOrNull(model.id)?.let { group ->
            if (!trainingDirectionRepository.existsById(model.trainingDirectionId))
                throw NotFoundException("Направление не найдено")

            group.name = model.name
            group.trainingDirectionId = model.trainingDirectionId
            return group.toModel()
        }
        throw NotFoundException(message = "Группа не найдена")
    }

    @Throws(NotFoundException::class)
    fun getGroupById(id: UUID): GroupResponseModel {
        groupRepository.findByIdOrNull(id)?.let {
            return it.toModel()
        }
        throw NotFoundException("Группа не найдена")
    }

    fun getGroupPage(pageable: Pageable) = groupRepository.findAll(pageable).map { it.toModel() }

    fun deleteGroup(id: UUID) = groupRepository.deleteById(id)

    fun addStudentToGroup(studentId: UUID, groupId: UUID) {
        if (!groupRepository.existsById(groupId))
            throw NotFoundException("Группа не найдена")

        userRepository.findByIdOrNull(studentId)?.let { user ->
            val roles = user.roles.map { it.userRoleId.role }
                if (STUDENT !in roles) throw BadRequestException("Данный пользователь не является студентом")

            userGroupRepository.save(UserGroupEntity(UserGroupId(studentId, groupId)))
        }
        throw NotFoundException("Ученик не найден не найден")
    }

    fun removeStudentFromGroup(studentId: UUID, groupId: UUID) {
        if (!groupRepository.existsById(groupId))
            throw NotFoundException("Группа не найдена")
        if(!userRepository.existsById(studentId))
            throw NotFoundException("Ученик не найден не найден")
        userGroupRepository.deleteById(UserGroupId(studentId, groupId))
    }
}

private fun GroupRequestModel.toEntity() = GroupEntity(id, name, trainingDirectionId)

private fun GroupEntity.toModel() = GroupResponseModel(id, name, trainingDirectionId)