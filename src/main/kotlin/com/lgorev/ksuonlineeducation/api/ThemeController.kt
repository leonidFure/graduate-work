package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.theme.ThemeLessonModel
import com.lgorev.ksuonlineeducation.domain.theme.ThemeRequestModel
import com.lgorev.ksuonlineeducation.domain.theme.ThemeRequestPageModel
import com.lgorev.ksuonlineeducation.service.ThemeService
import org.springframework.http.ResponseEntity.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/themes")
class ThemeController(private val themeService: ThemeService) {
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getById(@RequestParam id: UUID) = ok(themeService.getThemeById(id))

    @GetMapping("list")
    @PreAuthorize("isAuthenticated()")
    fun getByEPId(@RequestParam id: UUID) = ok(themeService.getThemesByEPId(id))

    @GetMapping("list/lesson")
    @PreAuthorize("isAuthenticated()")
    fun getByEPId(@RequestParam educationProgramId: UUID, @RequestParam lessonId: UUID) = ok(themeService.getThemesByEPId(educationProgramId, lessonId))

    @PostMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(@RequestBody model: ThemeRequestPageModel) = ok(themeService.getThemePage(model))

    @PostMapping("lesson/add")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun addToLesson(@RequestBody model: ThemeLessonModel) = themeService.addThemeToLesson(model)

    @PostMapping("lesson/delete")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun deleteFromLesson(@RequestBody model: ThemeLessonModel) = themeService.deleteThemeFromLesson(model)

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: ThemeRequestModel) = ok(themeService.addTheme(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: ThemeRequestModel) = ok(themeService.updateTheme(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(themeService.deleteTheme(id))
}