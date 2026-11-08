package com.example.demoApiVersao23082026.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Libera requisições PREFLIGHT (OPTIONS) do navegador
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/certificados/validar/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/eventos/imagens/*").permitAll()

                        // Rotas públicas
                        .requestMatchers(
                                "/usuarios/criar",
                                "/auth/login",
                                "/eventos/publicos",
                                "/planos/ativos"
                        ).permitAll()

                        // Apenas contas FUNCIONARIO podem acessar estas rotas.
                        // Cada endpoint também valida as permissões individuais.
                        .requestMatchers("/funcionario/**").hasRole("FUNCIONARIO")

                        // Rotas do próprio usuário
                        .requestMatchers(
                                HttpMethod.GET,
                                "/usuarios/me"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/usuarios/me",
                                "/usuarios/me/senha"
                        ).authenticated()

                        // Relatórios consolidados são exclusivos de administradores.
                        .requestMatchers(HttpMethod.GET, "/relatorios/eventos").hasRole("ADMIN")

                        // Rotas administrativas
                        .requestMatchers(
                                "/admin/**",
                                "/historico-admin/**",
                                "/usuarios/todos",
                                "/pagamentos/todos"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/usuarios/{id}"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/usuarios/{id}"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/usuarios/{id}"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite requisições vindas de qualquer origem (inclusive file://)
        configuration.setAllowedOriginPatterns(List.of("*"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}