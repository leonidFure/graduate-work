package com.lgorev.ksuonlineeducation.config

import com.google.common.base.Predicates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
            .select()
            .paths(Predicates.not(PathSelectors.regex("/error")))
            .build()
            .apiInfo(apiInfo())
            .securitySchemes(arrayListOf(ApiKey("apiKey", "Authorization", "header")))
            .securityContexts(arrayListOf(securityContext()))

    private fun apiInfo() = ApiInfoBuilder()
            .title("Graduate work")
            .description("The REST API for graduate work.")
            .termsOfServiceUrl("")
            .version("1.0.0")
            .build()

    private fun securityContext(): SecurityContext? {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/.*"))
                .forPaths(
                        Predicates.and(
                                Predicates.not(PathSelectors.regex("/api/auth/reg.*")),
                                Predicates.not(PathSelectors.regex("/api/auth/log.*"))
                        )
                )
                .build()
    }

    private fun defaultAuth(): List<SecurityReference?>? {
        val authorizationScope = AuthorizationScope("apiKey", "accessEverything")
        val authorizationScopes: Array<AuthorizationScope?> = arrayOfNulls(1)
        authorizationScopes[0] = authorizationScope
        return listOf(SecurityReference("apiKey", authorizationScopes))
    }
}


