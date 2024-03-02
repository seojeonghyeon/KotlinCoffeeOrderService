package me.justin.kotlincoffeeorderservice.modules.member

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import me.justin.kotlincoffeeorderservice.modules.authority.Authority
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.domain.Persistable
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.HashSet

@Entity @Table(name = "members")
data class Member protected constructor(
    @Id @Column(name = "member_id")
    val id: String,
    val memberName: String,
    @Column(unique = true)
    val email: String,
    val password: String,
    val simplePassword: String, //with Refresh
    var point: Int,
    var disabled: Boolean,
): Persistable<String>{
    @CreationTimestamp
    private val createdDate: ZonedDateTime? = null
    @UpdateTimestamp
    private val updatedDate: ZonedDateTime? = null
    @ManyToMany(mappedBy = "members", cascade = [CascadeType.ALL])
    private val authorities: MutableSet<Authority> = HashSet()
//    @JsonIgnore
//    @OneToMany(mappedBy = "member")
//    private val orders: MutableList<Order> = ArrayList()
//    @JsonIgnore
//    @OneToMany(mappedBy = "member")
//    private val points: MutableList<Point> = ArrayList()

    fun setAuthorities(authority: Authority): Unit{
        this.authorities.add(authority)
        authority.getMembers().add(this)
    }

    fun getAuthorities(): MutableSet<Authority>{
        return authorities
    }

    fun inducePoint(point: Int): Int {
        this.point += point
        return this.point
    }

    fun deductPoint(point: Int): Unit {
        this.point -= point
    }

    override fun getId(): String {
        return id;
    }

    override fun isNew(): Boolean {
        return createdDate == null
    }

    companion object {
        fun createUserMember(
            email: String,
            encryptedPwd: String,
            simpleEncryptedPwd: String
        ): Member{
            val authority: Authority = Authority.createAuthority("USER")
            return createMember(authority, email, encryptedPwd, simpleEncryptedPwd)
        }

        fun createManagerMember(
            email: String,
            encryptedPwd: String,
            simpleEncryptedPwd: String
        ): Member{
            val authority: Authority = Authority.createAuthority("MANAGER")
            return createMember(authority, email, encryptedPwd, simpleEncryptedPwd)
        }

        fun createAdminMember(
            email: String,
            encryptedPwd: String,
            simpleEncryptedPwd: String
        ): Member{
            val authority: Authority = Authority.createAuthority("ADMIN")
            return createMember(authority, email, encryptedPwd, simpleEncryptedPwd)
        }

        private fun createMember(
            authority: Authority,
            email: String,
            encryptedPwd: String,
            simplePassword: String
        ): Member {
            val member = Member(
                UUID.randomUUID().toString(),
                AdjectiveWord.getWordOne()+" "+AnimalWord.getWordOne(),
                email,
                encryptedPwd,
                simplePassword,
                0,
                false
            )
            member.setAuthorities(authority)
            authority.setMember(member)
            return member
        }
    }

}