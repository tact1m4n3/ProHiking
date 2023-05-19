package com.anonymous.prohiking.data

import android.content.Context
import com.anonymous.prohiking.data.model.User
import com.anonymous.prohiking.data.network.LoginPayload
import com.anonymous.prohiking.data.network.ProHikingApiService
import com.anonymous.prohiking.data.utils.Result
import com.anonymous.prohiking.data.network.RegisterPayload
import com.anonymous.prohiking.data.network.utils.enforceLogin
import com.anonymous.prohiking.data.network.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface UserRepository {
    suspend fun getUserById(id: Int): Result<User>
    suspend fun registerUser(username: String, email: String, password: String): Result<User>
    suspend fun loginUser(username: String, password: String): Result<User>
    suspend fun logoutUser(): Result<String>
    suspend fun deleteUser(): Result<String>
}

class DefaultUserRepository(
    private val context: Context,
    private val proHikingApiService: ProHikingApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): UserRepository {
    override suspend fun getUserById(id: Int): Result<User> {
        return enforceLogin(context) { safeApiCall(dispatcher) { proHikingApiService.getUserById(id) } }
    }

    override suspend fun registerUser(username: String, email: String, password: String): Result<User> {
        return safeApiCall(dispatcher) { proHikingApiService.registerUser(RegisterPayload(username, email, password)) }
    }

    override suspend fun loginUser(username: String, password: String): Result<User> {
        return safeApiCall(dispatcher) { proHikingApiService.loginUser(LoginPayload(username, password)) }
    }

    override suspend fun logoutUser(): Result<String> {
        return enforceLogin(context) { safeApiCall(dispatcher) { proHikingApiService.logoutUser() } }
    }

    override suspend fun deleteUser(): Result<String> {
        return enforceLogin(context) { safeApiCall(dispatcher) { proHikingApiService.deleteUser() } }
    }
}