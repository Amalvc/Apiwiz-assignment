package com.amal.apiwiz.Security;
import com.amal.apiwiz.Security.Jwt.AuthTokenFilter;
import com.amal.apiwiz.Security.Jwt.JwtAccessDeniedHandler;
import com.amal.apiwiz.Security.Jwt.JwtAuthEntryPoint;
import com.amal.apiwiz.Security.UserSecurity.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for web security settings.
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
    private final UserDetailService userDetailService;
    private final AuthTokenFilter authTokenFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * Bean for configuring password encoder.
     *
     * @return - PasswordEncoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Uses BCrypt password encoder
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for configuring DaoAuthenticationProvider.
     *
     * @return - DaoAuthenticationProvider bean
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider(); // Creates a new DaoAuthenticationProvider
        authProvider.setUserDetailsService(userDetailService); // Sets the UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // Sets the password encoder
        return authProvider; // Returns the configured DaoAuthenticationProvider
    }

    /**
     * Bean for providing AuthenticationManager.
     *
     * @param authConfig - AuthenticationConfiguration object
     * @return - AuthenticationManager bean
     * @throws Exception - If an error occurs while retrieving the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager(); // Retrieves the AuthenticationManager from AuthenticationConfiguration
    }

    /**
     * Bean for configuring the SecurityFilterChain.
     *
     * @param http - HttpSecurity object
     * @return - SecurityFilterChain bean
     * @throws Exception - If an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint)
                                              .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("api/auth/login","api/auth/signup")
                        .permitAll()
                        .requestMatchers("api/auth/assign-user/**","/api/tasks/**").authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        log.info("security filter chain");
        return http.build();
    }
}