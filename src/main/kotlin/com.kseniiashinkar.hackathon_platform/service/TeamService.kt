package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.entity.Team

interface TeamService {
    fun findAll(): List<Team>
    fun findById(id: Long): Team?
    fun save(team: Team): Team
    fun deleteById(id: Long)
    fun findByEventId(eventId: Long): List<Team>
    fun findByInviteCode(inviteCode: String): Team?
    fun searchByName(name: String): List<Team>
    fun hasAvailableSlots(teamId: Long): Boolean
    fun getMemberCount(teamId: Long): Int
}