package com.task.sliidechallenge.data.repos

import com.task.sliidechallenge.core.datasources.remote.UserApiService
import com.task.sliidechallenge.data.models.User
import com.task.sliidechallenge.domain.repos.UserRepository
import retrofit2.Response
import javax.inject.Inject

/**
 * @Author Abdullah Abo El~Makarem on 21/07/2024.
 */
class UserRepositoryImpl @Inject constructor(private val apiService: UserApiService): UserRepository{
    override suspend fun getUsers(): List<User> = apiService.getUsers(page = -1) // Assuming -1 gets the last page

    override suspend fun createUser(user: User): Response<User> = apiService.createUser(user)

    override suspend fun deleteUser(id: Int): Response<Unit> = apiService.deleteUser(id)
}