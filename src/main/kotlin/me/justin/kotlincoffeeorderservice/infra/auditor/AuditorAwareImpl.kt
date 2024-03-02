package me.justin.kotlincoffeeorderservice.infra.auditor


import me.justin.kotlincoffeeorderservice.modules.member.Member
import me.justin.kotlincoffeeorderservice.modules.member.MemberAdapter
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class AuditorAwareImpl :AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        if(null != authentication && authentication.isAuthenticated) {
            return getAuthentication(authentication)
        }
        return Optional.empty()
    }

    private fun getAuthentication(authentication: Authentication): Optional<String> {
        val memberAdapter: MemberAdapter = authentication.principal as MemberAdapter
        val member: Member = memberAdapter.member
        return Optional.ofNullable(member.id)
    }
}
