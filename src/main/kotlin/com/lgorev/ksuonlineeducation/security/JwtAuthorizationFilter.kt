package com.lgorev.ksuonlineeducation.security


import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.lang.IllegalArgumentException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(authenticationManager: AuthenticationManager) :
        BasicAuthenticationFilter(authenticationManager) {

    private val log = LoggerFactory.getLogger(JwtAuthorizationFilter::class.java)

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  chain: FilterChain) {
        val authentication = getAuthentication(request)
        authentication?.let {
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(TOKEN_HEADER)
        if (token != null && token.isNotEmpty() && token.startsWith(TOKEN_PREFIX)) {
            try {
                val parsedToken = Jwts
                        .parser()
                        .setSigningKey(JWT_SECRET.toByteArray())
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                val username = parsedToken.body.subject
                val roles = parsedToken.body[ROLES_CLAIM]
                        .toString()
                        .replace("([\\[\\]])".toRegex(), "")
                        .split(", ")
                val userIdClaim = parsedToken.body[USER_ID_CLAIM]
                val userId = userIdClaim?.toString()
                val grantedAuthorities = roles.map { SimpleGrantedAuthority(it) }
                val tokenCredentialContainer = TokenCredentialContainer(UUID.fromString(userId), token)
                return UsernamePasswordAuthenticationToken(username, tokenCredentialContainer, grantedAuthorities)
            } catch (e: ExpiredJwtException) {
                log.warn("Request to parse expired JWT : $token failed : ${e.message}")
            } catch (e: UnsupportedJwtException) {
                log.warn("Request to parse unsupported JWT : $token failed : ${e.message}")
            } catch (e: MalformedJwtException) {
                log.warn("Request to parse JWT with invalid signature : $token failed : ${e.message}")
            } catch (e: IllegalArgumentException) {
                log.warn("Request to parse empty or null JWT : $token failed : ${e.message}")
            }
        }
        return null
    }
}