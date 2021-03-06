package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.common.map
import com.lgorev.ksuonlineeducation.domain.lesson.*
import com.lgorev.ksuonlineeducation.domain.theme.ThemeResponseModel
import com.lgorev.ksuonlineeducation.domain.timetable.TimetableResponseModel
import com.lgorev.ksuonlineeducation.domain.timetable.TimetableType
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.lesson.LessonEntity
import com.lgorev.ksuonlineeducation.repository.lesson.LessonLogEntity
import com.lgorev.ksuonlineeducation.repository.lesson.LessonRepository
import com.lgorev.ksuonlineeducation.repository.theme.ThemeEntity
import com.lgorev.ksuonlineeducation.util.filter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import java.util.*

@Service
@Transactional
class LessonService(private val lessonRepository: LessonRepository) {

    @Autowired
    private lateinit var timetableService: TimetableService

    @Autowired
    private lateinit var courseService: CourseService

    @Autowired
    private lateinit var lessonLogService: LessonLogService

    @Autowired
    private lateinit var lessonsThemesService: LessonsThemesService

    @Autowired
    private lateinit var themeService: ThemeService

    @Throws(NotFoundException::class)
    fun getLessonById(id: UUID): LessonResponseModel {
        lessonRepository.findByIdOrNull(id)?.let { lesson ->
            val lessonsThemes = lessonsThemesService.getLessonsThemesByLessonId(lesson.id)
            val themeIds = lessonsThemes.map { it.lessonsThemesId.themesId }.toMutableSet()
            val themes = themeService.getThemesByIds(themeIds).map { it.toModel() }.toMutableSet()
            val timetableById = timetableService.getTimetableById(lesson.timetableId)
            val model = lesson.toModel(themes, timetableById)
            val now = LocalDateTime.now()
            model.isLiveEventAvailable = now.isAfter(model.startTime) && now.isBefore(model.endTime)
            return model
        }
        throw BadRequestException("Занятие не найдено")
    }

    fun existsLessonById(id: UUID) = lessonRepository.existsById(id)

    fun getLessonPage(model: LessonRequestPageModel): PageResponseModel<LessonResponseModel> {
        return if (model.themeIds.isNotEmpty()) {
            val lessonsThemesIds = lessonsThemesService.getLessonsThemesByThemeIds(model.themeIds)
            val ids = lessonsThemesIds.map { it.lessonsThemesId.lessonId }.toMutableSet()
            val lessons = lessonRepository.findLessonPage(model, ids).map { it.toModel() }
            val lessonsIds = lessons.map { it.id }.content
            val timetableIds = lessons.content.map { it.timetableId }.distinct()
            val timetables = timetableService.getTimetablesByIds(timetableIds)
            val lessonsThemes = lessonsThemesService.getLessonsThemesByLessonIds(lessonsIds)
            val themes = themeService.getThemesByIds(lessonsThemes.map { it.lessonsThemesId.themesId }.toMutableSet())
            lessons.map {
                val find = timetables.find { timetable -> timetable.id == it.timetableId }
                LessonResponseModel(it.id, it.courseId, it.timetableId, it.date, it.status,
                        themes.filter { theme ->
                            lessonsThemes.filter { ls ->
                                ls.lessonsThemesId.lessonId == it.id
                            }.map { ls ->
                                ls.lessonsThemesId.themesId
                            }.contains(theme.id)
                        }.map { themeEntity ->
                            themeEntity.toModel()
                        }.toMutableSet(),
                        LocalDateTime.of(it.date, find?.startTime),
                        LocalDateTime.of(it.date, find?.endTime),
                        videoUri = it.videoUri)
            }
        } else {
            val lessons = lessonRepository.findLessonPage(model, null).map { it.toModel() }
            val lessonsIds = lessons.map { it.id }.content
            val timetableIds = lessons.content.map { it.timetableId }.distinct()
            val timetables = timetableService.getTimetablesByIds(timetableIds)
            val lessonsThemes = lessonsThemesService.getLessonsThemesByLessonIds(lessonsIds)
            val themes = themeService.getThemesByIds(lessonsThemes.map { it.lessonsThemesId.themesId }.toMutableSet())
            lessons.map {
                val find = timetables.find { timetable -> timetable.id == it.timetableId }
                LessonResponseModel(it.id, it.courseId, it.timetableId, it.date, it.status,
                        themes.filter { theme ->
                            lessonsThemes.filter { ls ->
                                ls.lessonsThemesId.lessonId == it.id
                            }.map { ls ->
                                ls.lessonsThemesId.themesId
                            }.contains(theme.id)
                        }.map { themeEntity ->
                            themeEntity.toModel()
                        }.toMutableSet(),
                        LocalDateTime.of(it.date, find?.startTime),
                        LocalDateTime.of(it.date, find?.endTime),
                        videoUri = it.videoUri)

            }
        }
    }

    fun getLessonList(model: LessonRequestListModel): MutableList<LessonResponseModel> {
        val lessons = lessonRepository.findAllByCourseIdInAndDateBetween(model.courseIds, model.dateFrom, model.dateTo)
        val timetableIds = lessons.map { it.timetableId }.distinct()
        val timetables = timetableService.getTimetablesByIds(timetableIds)
        val lessonsIds = lessons.map { it.id }.toMutableSet()
        val lessonsThemes = lessonsThemesService.getLessonsThemesByLessonIds(lessonsIds)
        val themes = themeService.getThemesByIds(lessonsThemes.map { it.lessonsThemesId.themesId }.toMutableSet())
        return lessons.map {
            val find = timetables.find { timetable -> timetable.id == it.timetableId }
            LessonResponseModel(it.id, it.courseId, it.timetableId, it.date, it.status,
                    themes.filter { theme ->
                        lessonsThemes.filter { ls ->
                            ls.lessonsThemesId.lessonId == it.id
                        }.map { ls ->
                            ls.lessonsThemesId.themesId
                        }.contains(theme.id)
                    }.map { themeEntity ->
                        themeEntity.toModel()
                    }.toMutableSet(),
                    LocalDateTime.of(it.date, find?.startTime),
                    LocalDateTime.of(it.date, find?.endTime),
                    it.videoUri
            )
        }.toMutableList()
    }

    fun getLessonListByCourseId(id: UUID) = lessonRepository.findAllByCourseId(id).map { it.toModel() }

    @Throws(NotFoundException::class, BadRequestException::class)
    fun addLesson(model: LessonRequestModel): LessonResponseModel {
        if (!courseService.existCourseById(model.courseId))
            throw BadRequestException("Курс не найден")

        val timetable = timetableService.getTimetableByIdOrNull(model.timetableId)
        if (timetable != null) {
            if (timetable.courseId != model.courseId)
                throw BadRequestException("Курс занятия и расписания не совпадают")
        } else {
            throw BadRequestException("Расписание не найдено")
        }

        val lesson = lessonRepository.save(model.toEntity()).toModel()

        val lessonLog = LessonLogModel(model.id, LocalDateTime.now(), null, LessonStatus.LESSON_CREATED)
        lessonLogService.addLessonLog(lessonLog)

        return lesson
    }

    fun updateLesson(model: LessonRequestModel): LessonResponseModel {
        lessonRepository.findByIdOrNull(model.id)?.let { lesson ->
            val oldStatus = lesson.status
            lesson.date = model.date
            lesson.status = model.status
            val lessonLog = LessonLogModel(model.id, LocalDateTime.now(), oldStatus, lesson.status)
            lessonLogService.addLessonLog(lessonLog)
            return lesson.toModel()
        }
        throw BadRequestException("Занятие не найдено")
    }

    fun deleteLesson(id: UUID) {
        if (lessonRepository.existsById(id))
            lessonRepository.deleteById(id)
    }

    fun addLessonsForCourse(timetables: List<TimetableResponseModel>) {
        val lessons = mutableSetOf<LessonEntity>()
        val courseId = timetables.first().courseId
        val course = courseService.getCourseById(courseId, null)
        val courseRange = (course.startDate..course.endDate)
        val weekFields = WeekFields.of(Locale.getDefault())
        timetables.forEach { t ->
            lessons.addAll(courseRange
                    .filter { day ->
                        (t.type == TimetableType.EVEN && day[weekFields.weekOfWeekBasedYear()] % 2 == 0 ||
                                t.type == TimetableType.ODD && day[weekFields.weekOfWeekBasedYear()] % 2 != 0 ||
                                t.type == TimetableType.EVERY_WEEK) && day.dayOfWeek == t.dayOfWeek
                    }
                    .map { day -> LessonEntity(UUID.randomUUID(), courseId, t.id, day, LessonStatus.LESSON_CREATED, null) }
            )
        }

        val lessonEntities = lessonRepository.saveAll(lessons)
        val lessonsLog = lessonEntities.map { l -> LessonLogEntity(UUID.randomUUID(), l.id, LocalDateTime.now(), null, LessonStatus.LESSON_CREATED) }

        lessonLogService.addLessonsLog(lessonsLog)
    }

    fun updateLessonsForCourse(timetables: List<TimetableResponseModel>) {
        val courseId = timetables.first().courseId
        lessonRepository.deleteAllByCourseId(courseId)
        addLessonsForCourse(timetables)
    }

    fun setVideoUriToVideo(id: UUID, videoUri: String) {
        lessonRepository.findByIdOrNull(id)?.let { it.videoUri = videoUri }
    }
}

private fun LessonRequestModel.toEntity() = LessonEntity(id, courseId, timetableId, date, status, videoUri)
private fun LessonEntity.toModel() = LessonResponseModel(id, courseId, timetableId, date, status, videoUri = videoUri)
private fun LessonEntity.toModel(themes: MutableSet<ThemeResponseModel>) = LessonResponseModel(id, courseId, timetableId, date, status, videoUri = videoUri, themes = themes)
private fun LessonEntity.toModel(themes: MutableSet<ThemeResponseModel>, timetable: TimetableResponseModel) = LessonResponseModel(id, courseId, timetableId, date, status, startTime = LocalDateTime.of(date, timetable.startTime), endTime = LocalDateTime.of(date, timetable.endTime), videoUri = videoUri, themes = themes)
private fun ThemeEntity.toModel() = ThemeResponseModel(id, parentThemeId, number, educationProgramId, name, description)

