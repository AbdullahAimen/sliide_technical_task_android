package com.task.sliidechallenge.core.datasources.remote

import com.task.sliidechallenge.data.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException

/**
 * @Author Abdullah Abo El~Makarem on 21/07/2024.
 */
interface UserApiService {

    @GET("/public/v2/users")
    suspend fun getUsers(@Query("page") page: Int): List<User>

    @POST("/public/v2/users")
    suspend fun createUser(@Body user: User): Response<User>

    @DELETE("/public/v2/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
}