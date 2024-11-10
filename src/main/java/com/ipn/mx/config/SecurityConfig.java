package com.ipn.mx.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ipn.mx.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private AuthenticationProvider authProvider;
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        		.cors()
            	.and()
        	.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/**", "/files/images/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sessionManagemer ->
            	sessionManagemer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .logout(logout -> logout.logoutUrl("/auth/logout")
	            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Permitir todos los orígenes
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Métodos permitidos
	    configuration.setAllowedHeaders(List.of("*")); // Cabeceras permitidas
	    configuration.setAllowCredentials(true); // Permitir credenciales si es necesario
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration); // Aplicar configuración a todas las rutas
	    return source;
	}
	
	
}
