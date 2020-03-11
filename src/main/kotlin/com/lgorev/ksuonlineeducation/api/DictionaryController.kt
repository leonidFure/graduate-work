package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.course.CourseStatus
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramStatus
import com.lgorev.ksuonlineeducation.domain.lesson.LessonStatus
import com.lgorev.ksuonlineeducation.domain.subject.SubjectType
import com.lgorev.ksuonlineeducation.domain.timetable.TimetableType
import com.lgorev.ksuonlineeducation.service.DictionaryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/dict")
class DictionaryController (private val dictionaryService: DictionaryService) {

    @GetMapping("course")
    fun getCourseDictionary() = dictionaryService.getDictionary(CourseStatus.values())

    @GetMapping("education-program")
    fun getEducationProgramDictionary() = dictionaryService.getDictionary(EducationProgramStatus.values())

    @GetMapping("lesson")
    fun getLessonDictionary() = dictionaryService.getDictionary(LessonStatus.values())

    @GetMapping("subject")
    fun getSubjectDictionary() = dictionaryService.getDictionary(SubjectType.values())

    @GetMapping("timetable")
    fun getTimetableDictionary() = dictionaryService.getDictionary(TimetableType.values())

}