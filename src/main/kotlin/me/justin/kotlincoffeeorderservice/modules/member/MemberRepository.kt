package me.justin.kotlincoffeeorderservice.modules.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface MemberRepository: JpaRepository<Member, String>{
    fun findByEmail(email: String): Optional<Member>
}
