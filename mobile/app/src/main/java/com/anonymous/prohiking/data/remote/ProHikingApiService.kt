package com.anonymous.prohiking.data.remote

import com.anonymous.prohiking.BuildConfig
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.R
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
import retrofit2.http.POST
import java.net.CookieManager
import java.net.CookiePolicy
import java.nio.charset.Charset
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BuildConfig.PROHIKING_API_URL)
    .client(newHttpClient())
    .build()

interface ProHikingApiService {
    @POST("api/user/register")
    suspend fun registerUser(@Body payload: RegisterPayload): UserInfo?

    @POST("api/user/login")
    suspend fun loginUser(@Body payload: LoginPayload): UserInfo?

    @POST("api/user/logout")
    suspend fun logoutUser(): String?
}

object ProHikingApi {
    val retrofitService : ProHikingApiService by lazy {
        retrofit.create(ProHikingApiService::class.java)
    }
}

fun newHttpClient(): OkHttpClient {
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
