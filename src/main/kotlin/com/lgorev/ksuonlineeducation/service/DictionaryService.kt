package com.lgorev.ksuonlineeducation.service

import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service

@Service
@PropertySource(value = ["classpath:dictionary.properties"], encoding = "Windows-1251")
class DictionaryService(private val env: Environment) {

    fun <E> getDictionary(values: Array<E>) = values.map { it to env[it.toString().toLowerCase()] }.toMap()
}