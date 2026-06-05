package com.denzo.wakil.data.repository

import com.denzo.wakil.Database.UserDao
import com.denzo.wakil.Database.UserEntity
import com.denzo.wakil.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun login(username: String, password: String): UserEntity? {
        return userDao.login(username, password)
    }

    override suspend fun register(user: UserEntity) {
        userDao.insertUser(user)
    }

    override suspend fun getUserByUsername(username: String): UserEntity? {
        return userDao.getUserByUsername(username)
    }
}
