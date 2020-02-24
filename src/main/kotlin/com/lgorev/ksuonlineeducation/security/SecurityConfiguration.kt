package com.lgorev.ksuonlineeducation.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@PropertySource("classpath:application.properties")
class SecurityConfiguration : WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity) {
        http
                .cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .addFilter(JwtAuthorizationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().oauth2Login()
                .clientRegistrationRepository(clientRegistrationRepository())
                .authorizedClientService(authorizedClientService())
    }

    @Bean
    fun authorizedClientService(): OAuth2AuthorizedClientService? {
        return InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository())
    }

    @Bean
    fun clientRegistrationRepository(): InMemoryClientRegistrationRepository {
        val registration = CommonOAuth2Provider.GOOGLE
                .getBuilder("google")
                .clientId("543027232730-oqfcom96nmpp2o5dobuehq9vvuc983tv.apps.googleusercontent.com")
                .clientSecret("fSyk1sMblxDy8wRCKqJ7aRC4")
                .build()
        return InMemoryClientRegistrationRepository(listOf(registration))
    }



}