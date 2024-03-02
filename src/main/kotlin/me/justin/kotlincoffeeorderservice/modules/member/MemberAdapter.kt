package me.justin.kotlincoffeeorderservice.modules.member

import me.justin.kotlincoffeeorderservice.modules.authority.Authority
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class MemberAdapter(
    val member: Member
): User(
    member.email,
    member.password,
    authorities(member.getAuthorities())
){
    companion object {
        private fun authorities(authorities: MutableSet<Authority>): List<GrantedAuthority> {
            return authorities.map { authority: Authority -> SimpleGrantedAuthority(authority.authorityName) }
        }
    }
}