package me.justin.kotlincoffeeorderservice.infra.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.apache.logging.log4j.util.Strings
import org.springframework.core.env.Environment
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*
import org.springframework.security.core.Authentication

@Component
class JwtTokenProvider (
    private val env: Environment,
    private val userDetailsService: UserDetailsService,
){
    @PostConstruct
    protected fun init() {
        secretKey = env.getProperty("spring.security.jwt.secret")
        tokenExpirationTime = env.getProperty("spring.security.jwt.token_expiration_time")?.toLong()
    }

    fun createToken (memberId: String): String{
        return Jwts.builder()
            .setSubject(memberId)
            .setExpiration(Date(System.currentTimeMillis()+ tokenExpirationTime!!))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(getUserId(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUserId(token: String): String{
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).body.subject
    }

    fun getToken(request: HttpServletRequest): String {
        val headers: Enumeration<String> = request.getHeaders(AUTHORIZATION)
        while (headers.hasMoreElements()) {
            val value: String = headers.nextElement()
            if(value.toLowerCase().startsWith(BEARER.toLowerCase())) {
                return value.substring(BEARER.length).trim()
            }
        }
        return Strings.EMPTY
    }

    fun validateToken(token: String): Boolean {
        try {
            val claims: Jws<Claims> = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            return false
        }
    }

    companion object {
        private val AUTHORIZATION: String = "Authorization"
        private val BEARER: String = "Bearer"
        private var secretKey: String? = null
        private var tokenExpirationTime: Long? = null
    }
}