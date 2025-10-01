package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.entity.TeamMember
import com.kseniiashinkar.hackathon_platform.entity.TeamRole

interface TeamMemberService {
    fun addMemberToTeam(userId: Long, teamId: Long, role: TeamRole): TeamMember
    fun removeMemberFromTeam(userId: Long, teamId: Long)
    fun findMembersByTeam(teamId: Long): List<TeamMember>
    fun findTeamsByUser(userId: Long): List<TeamMember>
    fun updateMemberRole(userId: Long, teamId: Long, newRole: TeamRole): TeamMember
    fun isUserInTeam(userId: Long, teamId: Long): Boolean
}