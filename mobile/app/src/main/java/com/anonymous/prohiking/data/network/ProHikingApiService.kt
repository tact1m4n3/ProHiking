package com.anonymous.prohiking.data.network

import com.anonymous.prohiking.BuildConfig
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.R
import com.anonymous.prohiking.data.model.Point
import com.anonymous.prohiking.data.model.Trail
import com.anonymous.prohiking.data.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.CookieManager
import java.net.CookiePolicy
import java.nio.charset.Charset
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

interface ProHikingApiService {
    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User
    @POST("api/user/register")
    suspend fun registerUser(@Body payload: RegisterPayload): User
    @POST("api/user/login")
    suspend fun loginUser(@Body payload: LoginPayload): User
    @POST("api/user/logout")
    suspend fun logoutUser(): String
    @GET("api/trails/{id}")
    suspend fun getTrailById(@Path("id") id: Int): Trail
    @GET("api/trails/{id}/path")
    suspend fun getTrailPath(@Path("id") id: Int): List<Point>
    @GET("api/trails/search")
    suspend fun searchTrails(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("name") name: String,
        @Query("length") length: String,
        @Query("center") center: String,
        @Query("radius") radius: Double,
    ): List<Trail>
}

fun initProHikingApiService(): ProHikingApiService {
    @OptIn(ExperimentalSerializationApi::class)
    val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.PROHIKING_API_URL)
        .client(newHttpClient())
        .build()

    val proHikingApiService: ProHikingApiService by lazy {
        retrofit.create(ProHikingApiService::class.java)
    }

    return proHikingApiService
}

private fun newHttpClient(): OkHttpClient {
    val certificateFactory = CertificateFactory.getInstance("X.509")
    val serverCertInputStream = ProHikingApplication.instance.resources.openRawResource(R.raw.server_cert)
    val serverCert = certificateFactory.generateCertificate(serverCertInputStream)

    val clientKeyString = String(ProHikingApplication.instance.resources.openRawResource(R.raw.client_key).readBytes(), Charset.defaultCharset())
    val clientCertString = String(ProHikingApplication.instance.resources.openRawResource(R.raw.client_cert).readBytes(), Charset.defaultCharset())

    val clientCert = HeldCertificate.decode(clientCertString + clientKeyString)

    val handshakeCertificates = HandshakeCertificates.Builder()
        .addTrustedCertificate(serverCert as X509Certificate)
        .heldCertificate(clientCert)
        .build()

    val cookieManager = CookieManager()
    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    val cookieJar = JavaNetCookieJar(cookieManager)

    return OkHttpClient.Builder()
        .sslSocketFactory(handshakeCertificates.sslSocketFactory(), handshakeCertificates.trustManager)
        .cookieJar(cookieJar)
        .build()
}