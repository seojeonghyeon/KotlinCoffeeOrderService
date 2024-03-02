package me.justin.kotlincoffeeorderservice.infra.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

/**
 * JWT Access Deny Handler
 * Triggered when accreditation fails after checking certification.
 */

@Component
class JwtAccesssDeniedHandler: AccessDeniedHandler{
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        response?.sendError(HttpServletResponse.SC_FORBIDDEN)
    }

}