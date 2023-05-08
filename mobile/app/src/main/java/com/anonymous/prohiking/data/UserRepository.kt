package com.anonymous.prohiking.data

import com.anonymous.prohiking.data.model.User
import com.anonymous.prohiking.data.network.LoginPayload
import com.anonymous.prohiking.data.network.ProHikingApiService
import com.anonymous.prohiking.data.utils.ResultWrapper
import com.anonymous.prohiking.data.network.RegisterPayload
import com.anonymous.prohiking.data.network.utils.enforceLogin
import com.anonymous.prohiking.data.network.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UserRepository(
    private val proHikingApiService: ProHikingApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getUserById(id: Int): ResultWrapper<User> {
        return enforceLogin { safeApiCall(dispatcher) { proHikingApiService.getUserById(id) } }
    }

    suspend fun registerUser(username: String, email: String, password: String): ResultWrapper<User> {
        return safeApiCall(dispatcher) { proHikingApiService.registerUser(RegisterPayload(username, email, password)) }
    }

    suspend fun loginUser(username: String, password: String): ResultWrapper<User> {
        return safeApiCall(dispatcher) { proHikingApiService.loginUser(LoginPayload(username, password)) }
    }

    suspend fun logoutUser(): ResultWrapper<String> {
        return safeApiCall(dispatcher) { proHikingApiService.logoutUser() }
    }
}