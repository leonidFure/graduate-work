package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.lesson.*
import com.lgorev.ksuonlineeducation.domain.timetable.TimetableResponseModel
import com.lgorev.ksuonlineeducation.domain.timetable.TimetableType
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.lesson.LessonEntity
import com.lgorev.ksuonlineeducation.repository.lesson.LessonLogEntity
import com.lgorev.ksuonlineeducation.repository.lesson.LessonRepository
import com.lgorev.ksuonlineeducation.util.filter
import org.apache.tomcat.jni.Local
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

    @Throws(NotFoundException::class)
    fun getLessonById(id: UUID): LessonResponseModel {
        lessonRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Занятие не найдено")
    }

    fun getLessonPage(model: LessonRequestPageModel) = lessonRepository.findLessonPage(model)

    @Throws(NotFoundException::class, BadRequestException::class)
    fun addLesson(model: LessonRequestModel): LessonResponseModel {
        if (!courseService.existCourseById(model.courseId))
            throw NotFoundException("Курс не найден")

        val timetable = timetableService.getTimetableByIdOrNull(model.timetableId)
        if (timetable != null) {
            if (timetable.courseId != model.courseId)
                throw BadRequestException("Курс занятия и расписания не совпадают")
        } else {
            throw NotFoundException("Расписание не найдено")
        }

        val lesson = lessonRepository.save(model.toEntity()).toModel()

        val lessonLog = LessonLogModel(model.id, LocalDateTime.now(), null, LessonStatus.CREATED)
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
        throw NotFoundException("Занятие не найдено")
    }

    fun deleteLesson(id: UUID) {
        if (lessonRepository.existsById(id))
            lessonRepository.deleteById(id)
    }

    fun addLessonsForCourse(timetables: List<TimetableResponseModel>) {
        val lessons = mutableSetOf<LessonEntity>()
        val courseId = timetables.first().courseId
        val course = courseService.getCourseById(courseId)
        val courseRange = (course.startDate..course.endDate)
        val weekFields = WeekFields.of(Locale.getDefault())
        timetables.forEach { t ->
            lessons.addAll(courseRange
                    .filter { day ->
                        (t.type == TimetableType.EVEN && day[weekFields.weekOfWeekBasedYear()] % 2 == 0 ||
                                t.type == TimetableType.ODD && day[weekFields.weekOfWeekBasedYear()] % 2 != 0 ||
                                t.type == TimetableType.EVERY_WEEK) && day.dayOfWeek == t.dayOfWeek
                    }
                    .map { day -> LessonEntity(UUID.randomUUID(), courseId, t.id, day, LessonStatus.CREATED) }
            )
        }

        val lessonEntities = lessonRepository.saveAll(lessons)
        val lessonsLog = lessonEntities.map { l -> LessonLogEntity(UUID.randomUUID(), l.id, LocalDateTime.now(), null, LessonStatus.CREATED) }

        lessonLogService.addLessonsLog(lessonsLog)
    }

    fun updateLessonsForCourse(timetables: List<TimetableResponseModel>) {
        val courseId = timetables.first().courseId
        lessonRepository.deleteAllByCourseId(courseId)
        addLessonsForCourse(timetables)
    }
}

private fun LessonRequestModel.toEntity() = LessonEntity(id, courseId, timetableId, date, status)
private fun LessonEntity.toModel() = LessonResponseModel(id, courseId, timetableId, date, status)
