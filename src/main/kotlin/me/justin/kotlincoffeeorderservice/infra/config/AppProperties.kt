package me.justin.kotlincoffeeorderservice.infra.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("app")
data class AppProperties(private val host: String? = null)