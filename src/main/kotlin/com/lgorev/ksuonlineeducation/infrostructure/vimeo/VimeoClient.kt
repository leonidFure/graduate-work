package com.lgorev.ksuonlineeducation.infrostructure.vimeo

import com.lgorev.ksuonlineeducation.exception.VimeoResponseException
import com.lgorev.ksuonlineeducation.infrostructure.vimeo.model.Video
import com.lgorev.ksuonlineeducation.infrostructure.vimeo.model.VideoPage
import com.lgorev.ksuonlineeducation.infrostructure.vimeo.model.VimeoVideoPageRequestModel
import com.lgorev.ksuonlineeducation.service.VideoRequestModel
import org.apache.http.HttpHeaders.TIMEOUT
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod.*
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


@Service
class VimeoClient {

    @Value("\${vimeo.access_token}")
    private lateinit var vimeoAccessToken: String

    @Value("\${vimeo.url}")
    private lateinit var vimeoUrl: String

    fun getUserVideos(modelVimeo: VimeoVideoPageRequestModel): VideoPage? {
        val url = "${vimeoUrl}/me/videos"
        val params = mutableMapOf(
                "direction" to modelVimeo.direction,
                "page" to modelVimeo.pageNum.toString(),
                "per_page" to modelVimeo.pageSize.toString(),
                "sort" to modelVimeo.sortField
        )
        return get(url, vimeoAccessToken, params)
    }

    fun getVideo(uri: String): Video? {
        val url = "${vimeoUrl}${uri}"
        return get(url, vimeoAccessToken)
    }

    fun uploadVideo() {
        TODO("реализация идет в несколько методов, " +
                "ознакомиться с API https://developer.vimeo.com/api/upload/videos" +
                "реализовть каждый из методов отельно или объеденять пока не понятно")
    }

    fun editVideo(videoUri: String, model: VideoRequestModel): Video? {
        val url = "${vimeoUrl}${videoUri}"
        return patch(url, vimeoAccessToken, model)
    }

    fun deleteVideo(videoUri: String) {
        val url = "${vimeoUrl}${videoUri}"
        delete(url, vimeoAccessToken)
    }


    fun getUserVimeoInfo() {
        TODO("GET https://api.vimeo.com/videos/{video_id}")
    }

    fun startLiveEvent() {
        TODO("GET https://api.vimeo.com/videos/{video_id}")
    }

//    fun createVideo()

    fun getVideoPage(model: VimeoVideoPageRequestModel): VideoPage? {
        val url = "${vimeoUrl}/videos"
        /*
        * Сортировка по дате не работает на стороне клиента
        * */
        val params = mutableMapOf("direction" to model.direction, "page" to model.pageNum.toString(), "per_page" to model.pageSize.toString()
        )
        return get(url, vimeoAccessToken, params)
    }

    private companion object {
        /**
         * GET method for Vimeo API.
         *
         * Retrieves the JSON representation of a resource.
         *
         * @param T the type of response
         * @property url the url of API
         * @property accessToken the token of authorization user in Vimeo
         * @property params the map of params (keys and values). Can be null
         * @throws VimeoResponseException
         * */
        @Throws(VimeoResponseException::class)
        inline fun <reified T : Any> get(url: String, accessToken: String, params: MutableMap<String, String>? = null): T? {
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()
            val token = "bearer $accessToken"
            headers[AUTHORIZATION] = token
            val request = HttpEntity<Unit>(headers)
            var httpUrl = UriComponentsBuilder.fromHttpUrl(url)
            params?.forEach { (k, v) -> httpUrl = httpUrl.queryParam(k, v) }
            try {
                val response = restTemplate.exchange(httpUrl.toUriString(), GET, request, T::class.java)
                if (response.statusCode.isError) throw VimeoResponseException("Ошибка при зарпосе к сервису Vimeo")
                return response.body
            } catch (e: Throwable) {
                throw VimeoResponseException(e.message)
            }
        }

        /**
         * POST method for Vimeo API
         *
         * Adds a new resource to a collection. This method generally requires additional parameters.
         *
         * @param T the type of response
         * @param R the type of body
         * @property url the url of API
         * @property accessToken the token of authorization user in Vimeo
         * @property body the body in http method
         * @throws VimeoResponseException
         * */
        @Throws(VimeoResponseException::class)
        inline fun <R, reified T : Any> post(url: String, accessToken: String, body: R? = null): T? {
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()
            val token = "bearer $accessToken"
            headers[AUTHORIZATION] = token
            val request = HttpEntity<R>(body, headers)
            try {
                val response = restTemplate.exchange(url, POST, request, T::class.java)
                if (response.statusCode.isError) throw VimeoResponseException("Ошибка при зарпосе к сервису Vimeo")
                return response.body
            } catch (e: Throwable) {
                throw VimeoResponseException(e.message)
            }
        }

        /**
         * PUT method for Vimeo API
         *
         * Replaces a resource or associates one resource with another.
         *
         * @param T the type of response
         * @param R the type of body
         * @property url the url of API
         * @property accessToken the token of authorization user in Vimeo
         * @property body the body in http method
         * @throws VimeoResponseException
         * */
        @Throws(VimeoResponseException::class)
        inline fun <R, reified T : Any> put(url: String, accessToken: String, body: R? = null): T? {
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()
            val token = "bearer $accessToken"
            headers[AUTHORIZATION] = token
            val request = HttpEntity<R>(body, headers)
            try {
                val response = restTemplate.exchange(url, PUT, request, T::class.java)
                if (response.statusCode.isError) throw VimeoResponseException("Ошибка при зарпосе к сервису Vimeo")
                return response.body
            } catch (e: Throwable) {
                throw VimeoResponseException(e.message)
            }
        }

        /**
         * PATCH method for Vimeo API
         *
         * Modifies a resource. The body of the request contains the new resource representation.
         *
         * @param T the type of response
         * @param R the type of body
         * @property url the url of API
         * @property accessToken the token of authorization user in Vimeo
         * @property body the body in http method
         * @throws VimeoResponseException
         * */
        @Throws(VimeoResponseException::class)
        inline fun <R, reified T : Any> patch(url: String, accessToken: String, body: R? = null): T? {
            val restTemplate = RestTemplate()
            val requestFactory = HttpComponentsClientHttpRequestFactory()
            restTemplate.requestFactory = requestFactory
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            val token = "bearer $accessToken"
            headers[AUTHORIZATION] = token
            val request = HttpEntity<R>(body, headers)
            try {
                val response = restTemplate.exchange(url, PATCH, request, T::class.java)
                if (response.statusCode.isError) throw VimeoResponseException("Ошибка при зарпосе к сервису Vimeo")
                return response.body
            } catch (e: Throwable) {
                throw VimeoResponseException(e.message)
            }
        }

        /**
         * DELETE method for Vimeo API
         *
         * Deletes a resource or disassociates one resource from another.
         *
         * @property url the url of API
         * @property accessToken the token of authorization user in Vimeo
         * @throws VimeoResponseException
         * */
        @Throws(VimeoResponseException::class)
        fun delete(url: String, accessToken: String) {
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()
            val token = "bearer $accessToken"
            headers[AUTHORIZATION] = token
            val request = HttpEntity<Unit>(headers)
            try {
                val response = restTemplate.exchange(url, DELETE, request, Unit::class.java)
                if (response.statusCode.isError) throw VimeoResponseException("Ошибка при зарпосе к сервису Vimeo")
            } catch (e: Throwable) {
                throw VimeoResponseException(e.message)
            }
        }
    }
}