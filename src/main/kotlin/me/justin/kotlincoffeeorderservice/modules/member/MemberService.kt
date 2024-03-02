package me.justin.kotlincoffeeorderservice.modules.member

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService (
    private val memberRepository: MemberRepository,
    private val eventPublisher: ApplicationEventPublisher,
): UserDetailsService {
    private val log = LoggerFactory.getLogger(this.javaClass)
    override fun loadUserByUsername(emailOrMemberId: String): UserDetails {
        var member: Member? = memberRepository.findByEmail(emailOrMemberId).orElse(null)
        if(member == null) {
            member = memberRepository.findById(emailOrMemberId).orElse(null)
        }
        if(member == null) {
            throw UsernameNotFoundException(emailOrMemberId);
        }
        return MemberAdapter(member)
    }

    @Transactional
    fun register(email: String, encryptedPwd: String, simpleEncryptedPwd: String): String{
        val member: Member = Member.createUserMember(email, encryptedPwd, simpleEncryptedPwd)
        val saveMember: Member = memberRepository.save(member)
//        eventPublisher.publishEvent(MemberCreatedEvent(saveMember))
        return saveMember.getId()
    }

    fun hasEmail(email: String): Boolean {
        return memberRepository.findByEmail(email).isPresent
    }

    fun findByEmail(email: String): Member {
        return memberRepository.findByEmail(email).orElse(null)
    }


}