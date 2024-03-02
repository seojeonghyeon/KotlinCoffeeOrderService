package me.justin.kotlincoffeeorderservice.infra.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*


@Slf4j
class LogInterceptor: HandlerInterceptor {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestURI = request.requestURI
        val uuid = UUID.randomUUID().toString()
        request.setAttribute(LOG_ID, uuid)

        log.info("REQUEST [${uuid}][${requestURI}][${handler}]")
        return true
    }

    companion object {
        private val LOG_ID = "logId"
    }

}