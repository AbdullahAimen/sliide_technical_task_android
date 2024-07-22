package com.task.sliidechallenge.domain.repos

import com.task.sliidechallenge.data.models.User
import retrofit2.Response

/**
 * @Author Abdullah Abo El~Makarem on 21/07/2024.
 */
interface UserRepository {
    suspend fun getUsers(): List<User>

    suspend fun createUser(user: User): Response<User>

    suspend fun deleteUser(id: Int): Response<Unit>
}