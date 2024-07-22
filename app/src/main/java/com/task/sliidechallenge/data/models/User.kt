package com.task.sliidechallenge.data.models

/**
 * @Author Abdullah Abo El~Makarem on 21/07/2024.
 */
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val creationDate:String = "" // no creation date returned from backend.
)
