package me.justin.kotlincoffeeorderservice.modules.authority

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import me.justin.kotlincoffeeorderservice.modules.member.Member

@Entity
data class Authority protected constructor(
    var authorityName: String
) {
    @Id @GeneratedValue @Column(name = "authority_id")
    private var id: Long? = null
    @ManyToMany
    @JoinTable(
        name = "member_authorities",
        joinColumns = [JoinColumn(name = "authority_id")],
        inverseJoinColumns = [JoinColumn(name = "member_id")]
    )
    private var members: MutableSet<Member> = HashSet()

    fun setMember(member: Member): Unit{
        this.members.add(member)
        member.getAuthorities().add(this)
    }

    fun getMembers(): MutableSet<Member> {
        return members
    }

    companion object {
        fun createAuthority(role: String): Authority{
            return Authority(role)
        }
    }
}
