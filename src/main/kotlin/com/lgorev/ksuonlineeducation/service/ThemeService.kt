package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.theme.ThemeRequestModel
import com.lgorev.ksuonlineeducation.domain.theme.ThemeRequestPageModel
import com.lgorev.ksuonlineeducation.domain.theme.ThemeResponseModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.theme.ThemeEntity
import com.lgorev.ksuonlineeducation.repository.theme.ThemeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ThemeService(private val themeRepository: ThemeRepository) {

    @Autowired
    private lateinit var educationProgramService: EducationProgramService
    @Autowired
    private lateinit var lessonsThemesService: LessonsThemesService

    @Throws(NotFoundException::class)
    fun getThemeById(id: UUID): ThemeResponseModel {
        themeRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Тема не найдена")
    }

    fun getThemePage(model: ThemeRequestPageModel): Page<ThemeResponseModel> {
        return if(model.lessonsIds.isNotEmpty()) {
            val lessonsThemesIds = lessonsThemesService.getLessonsThemesByLessonIds(model.lessonsIds)
            val ids = lessonsThemesIds.map { it.lessonsThemesId.themesId }.toMutableSet()
            themeRepository.findPage(model, ids).map { it.toModel() }
        } else themeRepository.findPage(model, null).map { it.toModel() }
    }

    @Throws(NotFoundException::class)
    fun addTheme(model: ThemeRequestModel): ThemeEntity {
        model.parentThemeId?.let { parentThemeId ->
            if (!themeRepository.existsById(parentThemeId))
                throw NotFoundException("Основная тема не найдена")
        }

        if (!educationProgramService.existEducationProgramById(model.educationProgramId))
            throw NotFoundException("Прогрмма обучения не найдена")

        return themeRepository.save(model.toEntity())
    }

    @Throws(NotFoundException::class)
    fun updateTheme(model: ThemeRequestModel): ThemeResponseModel {
        model.parentThemeId?.let { parentThemeId ->
            if (!themeRepository.existsById(parentThemeId))
                throw NotFoundException("Основная тема не найдена")
        }

        if (!educationProgramService.existEducationProgramById(model.educationProgramId))
            throw NotFoundException("Прогрмма обучения не найдена")

        themeRepository.findByIdOrNull(model.id)?.let { theme ->
            theme.number = model.number
            theme.name = model.name
            theme.description = model.description
            theme.parentThemeId = model.parentThemeId
            theme.educationProgramId = model.educationProgramId
            return theme.toModel()
        }
        throw NotFoundException("Тема не найдена")
    }

    fun deleteTheme(id: UUID) {
        if (themeRepository.existsById(id))
            themeRepository.deleteById(id)
    }
}

private fun ThemeEntity.toModel() = ThemeResponseModel(id, parentThemeId, number, educationProgramId, name, description)
private fun ThemeRequestModel.toEntity() = ThemeEntity(id, parentThemeId, number, educationProgramId, name, description)