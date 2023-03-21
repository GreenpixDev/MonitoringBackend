package ru.greenpix.monitoring.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono
import ru.greenpix.monitoring.properties.BCryptProperties
import ru.greenpix.monitoring.security.AuthenticationManager
import ru.greenpix.monitoring.security.SecurityContextRepository


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(
    private val authenticationManager: AuthenticationManager,
    private val securityContextRepository: SecurityContextRepository,
    private val bCryptProperties: BCryptProperties
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain
    = http
        .exceptionHandling()
        .authenticationEntryPoint { exchange, _ ->
            Mono.fromRunnable { exchange.response.statusCode = HttpStatus.UNAUTHORIZED }
        }
        .accessDeniedHandler { exchange, _ ->
            Mono.fromRunnable { exchange.response.statusCode = HttpStatus.FORBIDDEN }
        }
        .and()
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers(
            "/api/*/servers",
            "/api/*/versions",
            "/api/*/users/signin",
            "/api/*/users/signup",
            "/api/*/users/restore",
            "/api/*/verify/email",
            "/api/*/verify/restore",
            "/swagger",
            "/webjars/**",
            "/v3/api-docs/**"
        ).permitAll()
        .anyExchange().authenticated()
        .and()
        .build()

    @Bean
    fun corsConfiguration(): CorsConfigurationSource? {
        val corsConfig = CorsConfiguration()
        corsConfig.applyPermitDefaultValues()
        corsConfig.addAllowedHeader("*")
        corsConfig.addAllowedMethod("*")
        corsConfig.addAllowedOrigin("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return source
    }

    @Bean
    fun bcryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder(
        BCryptPasswordEncoder.BCryptVersion.valueOf("\$${bCryptProperties.version.uppercase()}"),
        bCryptProperties.strength
    )
}