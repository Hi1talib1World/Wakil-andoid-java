package com.denzo.wakil.domain.repository

import com.denzo.wakil.Database.UserEntity

interface UserRepository {
    suspend fun login(username: String, password: String): UserEntity?
    suspend fun register(user: UserEntity)
    suspend fun getUserByUsername(username: String): UserEntity?
}
