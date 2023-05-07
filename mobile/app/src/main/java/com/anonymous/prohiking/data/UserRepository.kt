package com.anonymous.prohiking.data

import com.anonymous.prohiking.data.model.User
import com.anonymous.prohiking.data.network.LoginPayload
import com.anonymous.prohiking.data.network.ProHikingApiService
import com.anonymous.prohiking.data.utils.ResultWrapper
import com.anonymous.prohiking.data.network.RegisterPayload
import com.anonymous.prohiking.data.network.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface UserRepository {
    suspend fun getUserById(id: Int): ResultWrapper<User>

    suspend fun registerUser(username: String, email: String, password: String): ResultWrapper<User>

    suspend fun loginUser(username: String, password: String): ResultWrapper<User>

    suspend fun logoutUser(): ResultWrapper<String>
}

class UserRepositoryImpl(private val proHikingApiService: ProHikingApiService, private val dispatcher: CoroutineDispatcher = Dispatchers.IO): UserRepository {
    override suspend fun getUserById(id: Int): ResultWrapper<User> {
        return safeApiCall(dispatcher) { proHikingApiService.getUserById(id) }
    }

    override suspend fun registerUser(username: String, email: String, password: String): ResultWrapper<User> {
        return safeApiCall(dispatcher) { proHikingApiService.registerUser(RegisterPayload(username, email, password)) }
    }

    override suspend fun loginUser(username: String, password: String): ResultWrapper<User> {
        return safeApiCall(dispatcher) { proHikingApiService.loginUser(LoginPayload(username, password)) }
    }

    override suspend fun logoutUser(): ResultWrapper<String> {
        return safeApiCall(dispatcher) { proHikingApiService.logoutUser() }
    }
}