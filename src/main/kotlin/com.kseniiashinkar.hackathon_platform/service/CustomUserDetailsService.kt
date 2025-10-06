package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.entity.User
import com.kseniiashinkar.hackathon_platform.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("Пользователь не найден: $email") }

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            getAuthorities()
        )
    }

    private fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_USER"))
    }
}