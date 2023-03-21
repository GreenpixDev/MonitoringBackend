package ru.greenpix.monitoring.configuration

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.greenpix.monitoring.api.BridgeApi
import ru.greenpix.monitoring.api.PingerApi
import ru.greenpix.monitoring.api.RecaptchaApi
import ru.greenpix.monitoring.properties.BackendsProperties

@Configuration
class RetrofitConfig(
    private val backendsProperties: BackendsProperties
) {

    @Bean
    fun bridgeRetrofit(): Retrofit
            = Retrofit.Builder()
        .baseUrl(backendsProperties.bridgeUrl)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        ))
        .build()

    @Bean
    fun pingerRetrofit(): Retrofit
            = Retrofit.Builder()
        .baseUrl(backendsProperties.pingerUrl)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
            ))
        .build()

    @Bean
    fun googleRetrofit(): Retrofit
            = Retrofit.Builder()
        .baseUrl("https://www.google.com")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
            ))
        .build()

    //@Bean
    fun bridgeApi(): BridgeApi = bridgeRetrofit().create()

    //@Bean
    fun pingerApi(): PingerApi = pingerRetrofit().create()

    //@Bean
    fun recaptchaApi(): RecaptchaApi = googleRetrofit().create()

}