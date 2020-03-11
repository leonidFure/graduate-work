package com.lgorev.ksuonlineeducation.domain.common

data class PageResponseModel<E>(
        val content: MutableSet<E>,
        val totalCount: Long
)

inline fun <E, R> PageResponseModel<E>.map(transform: (E) -> R): PageResponseModel<R> {
    val content = this.content.map(transform).toMutableSet()
    return PageResponseModel(content, totalCount)
}