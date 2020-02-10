package com.lgorev.ksuonlineeducation.api

    import com.lgorev.ksuonlineeducation.domain.theme.ThemeRequestModel
import com.lgorev.ksuonlineeducation.domain.theme.ThemeRequestPageModel
import com.lgorev.ksuonlineeducation.service.ThemeService
    import org.springframework.http.ResponseEntity
    import org.springframework.http.ResponseEntity.*
    import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/themes")
class ThemeController (private val themeService: ThemeService) {
    @GetMapping
    fun getById(@RequestParam id: UUID) = ok(themeService.getThemeById(id))

    @PostMapping("page")
    fun getPage(@RequestBody model: ThemeRequestPageModel) = ok(themeService.getThemePage(model))

    @PostMapping
    fun add(@RequestBody model: ThemeRequestModel) = ok(themeService.addTheme(model))

    @PutMapping
    fun update(@RequestBody model: ThemeRequestModel) = ok(themeService.updateTheme(model))

    @DeleteMapping
    fun delete(@RequestParam id: UUID) = ok(themeService.deleteTheme(id))
}