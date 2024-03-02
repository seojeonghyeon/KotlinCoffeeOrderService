package me.justin.kotlincoffeeorderservice.infra.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException

@Slf4j
@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
): GenericFilterBean() {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain
    ) {
        val httpServletRequest: HttpServletRequest = servletRequest as HttpServletRequest
        val token: String = jwtTokenProvider.getToken(httpServletRequest)
        val requestURI = httpServletRequest.requestURI

        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            val authentication: Authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
            log.debug("Spring Security Context : Save Authentication Info '${authentication.name}', URI : ${requestURI}")
        } else {
            log.debug("No valid JWT token, URI : ${requestURI}")
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }
}