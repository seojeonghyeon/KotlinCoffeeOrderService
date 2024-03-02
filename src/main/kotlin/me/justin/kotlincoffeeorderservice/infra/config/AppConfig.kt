package me.justin.kotlincoffeeorderservice.infra.config

import jakarta.annotation.PostConstruct
import me.justin.kotlincoffeeorderservice.infra.auditor.AuditorAwareImpl
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.Date
import java.util.TimeZone

@Configuration
class AppConfig {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder{
        return BCryptPasswordEncoder()
    }

    @PostConstruct
    fun started(): Unit{
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
        log.info("현재 시각 : ${Date()}")
    }

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAwareImpl()
    }
}