package me.justin.kotlincoffeeorderservice.infra.config

import me.justin.kotlincoffeeorderservice.infra.auth.JwtAccesssDeniedHandler
import me.justin.kotlincoffeeorderservice.infra.auth.JwtAuthenticationEntryPoint
import me.justin.kotlincoffeeorderservice.infra.auth.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.CorsFilter

@Profile(value = ["test","local","dev","stg","prd"])
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class WebSecurityConfig (
        private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
        private val jwtAccessDeniedHandler: JwtAccesssDeniedHandler,
        private val corsFilter: CorsFilter,
        private val jwtAuthenticationFilter: JwtAuthenticationFilter
    ) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain{
        http
            .csrf().disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)
            .and()
            .headers()
            .frameOptions().sameOrigin()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .requestMatchers("/h2-console/**", "/favicon.ico", "/error", "/image/**", "/file/**", "/css/**", "/resources/**").permitAll()
            .requestMatchers("/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**", "/swagger/**").permitAll()
            .requestMatchers("/docs/*", "/v2/api-docs", "/v3/api-docs/**").permitAll()
            .requestMatchers("/api/order/login", "/api/order/members/add", "/api/order/menus/**", "/api/order/categories/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}