package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.video.CreateVideoModel
import com.lgorev.ksuonlineeducation.domain.video.Upload
import com.lgorev.ksuonlineeducation.domain.video.VideoPageRequestModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.exception.VideoResponseException
import com.lgorev.ksuonlineeducation.infrostructure.vimeo.VimeoClient
import com.lgorev.ksuonlineeducation.infrostructure.vimeo.model.Video
import com.lgorev.ksuonlineeducation.infrostructure.vimeo.model.VideoPage
import com.lgorev.ksuonlineeducation.infrostructure.vimeo.model.VimeoVideoPageRequestModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class VideoService {
    @Autowired
    private lateinit var vimeoClient: VimeoClient

    @Autowired
    private lateinit var lessonService: LessonService


    @Throws(NotFoundException::class)
    fun getVideoByLessonId(id: UUID): Video {
        val lesson = lessonService.getLessonById(id)
        if (lesson.videoUri == null)
            throw NotFoundException("У данного занятия отсутвуют видео материалы")
        return vimeoClient.getVideo(lesson.videoUri) ?: throw NotFoundException("Видео не найдено")
    }

    @Throws(NotFoundException::class)
    fun getVideoPage(model: VideoPageRequestModel): VideoPage {
        var videoUrisString: String? = null
        if (model.courseId != null) {
            val lessons = lessonService.getLessonListByCourseId(model.courseId)
            videoUrisString = lessons.map { it.videoUri }.joinToString(",")
        }
        if (videoUrisString == null) throw NotFoundException("Видео материалы не найдены")
        val pageRequest = VimeoVideoPageRequestModel(model.pageNum, model.pageSize, videoUrisString)
        return vimeoClient.getVideoPage(pageRequest) ?: throw NotFoundException("Видео материалы не найдены")
    }

    fun updateVideoInfo(lessonId: UUID, model: VideoRequestModel): Video {
        val lesson = lessonService.getLessonById(lessonId)
        if (lesson.videoUri == null) throw VideoResponseException()
        return vimeoClient.editVideo(lesson.videoUri, model) ?: throw VideoResponseException()
    }

    fun deleteVideo(lessonId: UUID) {
        val lesson = lessonService.getLessonById(lessonId)
        if (lesson.videoUri == null) throw VideoResponseException()
        vimeoClient.deleteVideo(lesson.videoUri)
    }

    @Throws(VideoResponseException::class)
    fun uploadVideo(file: MultipartFile, lessonId: UUID) {
        val requestModel = CreateVideoModel(Upload(size = file.size, redirectUri = "http://localhost:8080/static/success"))
        val video = vimeoClient.createVideo(requestModel) ?: throw VideoResponseException()
        if (video.upload.uploadLink == null) throw VideoResponseException()
        vimeoClient.uploadVideo(video.upload.uploadLink, file)
        if (video.uri == null) throw VideoResponseException()
        lessonService.setVideoUriToVideo(lessonId, video.uri)
    }
}

