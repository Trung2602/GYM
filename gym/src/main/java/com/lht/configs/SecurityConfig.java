/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lht.jwt.JwtFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
 *
 * @author admin
 */
@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.lht.controllers",
    "com.lht.repository",
    "com.lht.service"
})
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(0)
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests
                -> requests
                        // Cho phép truy cập các tài nguyên tĩnh
                        .requestMatchers("/login", "/css/**", "/images/logo_transparent_white.png", "/js/**").permitAll()
                        .requestMatchers("/").hasRole("ADMIN")
                        // Cho phép truy cập API đăng nhập nếu bạn có một API riêng
                        .requestMatchers("/api/**").permitAll()
                        // ========== ACCOUNT ==========
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/account/me").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/register-customer").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/verify-otp").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/verify-password").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/account/update").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/change-password").authenticated()
                        // ========== CUSTOMER ==========
                        .requestMatchers(HttpMethod.GET, "/api/customer/{id}").hasRole("CUSTOMER")
                        // ========== FACILITY ==========
                        .requestMatchers(HttpMethod.GET, "/api/facilities-all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/facility/**").permitAll()
                        // ========== PLAN ==========
                        .requestMatchers(HttpMethod.GET, "/api/plans-all").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/plans-filter").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/plan/**").hasRole("CUSTOMER")
                        // ========== SHIFT ==========
                        .requestMatchers(HttpMethod.GET, "/api/shifts-all").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/shifts-filter").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/shift/**").hasRole("STAFF")
                        // ========== CUSTOMER SCHEDULE ==========
                        .requestMatchers(HttpMethod.GET, "/api/customer-schedules-all/**").hasAnyRole("CUSTOMER", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/customer-schedules-filter").hasAnyRole("CUSTOMER", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/customer-schedule/**").hasAnyRole("CUSTOMER", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/customer-schedule-update").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/customer-schedule-delete/**").hasRole("CUSTOMER")
                        // ========== PAY CUSTOMER ==========
                        .requestMatchers(HttpMethod.GET, "/api/pay-customers-all/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/pay-customer-filter").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/pay-customer/**").hasRole("CUSTOMER")
                        // ========== STAFF ==========
                        .requestMatchers(HttpMethod.GET, "/api/staffs-all").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/working-staff").hasRole("STAFF")
                        // ========== STAFF TYPE ==========
                        .requestMatchers(HttpMethod.GET, "/api/staff-type-all").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/staff-type/**").hasRole("STAFF")
                        // ========== STAFF SCHEDULE ==========
                        .requestMatchers(HttpMethod.GET, "/api/staff-schedules-all/**").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/staff-schedules-filter").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/staff-schedule/**").hasRole("STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/staff-schedule-update").hasRole("STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/staff-schedule-delete/**").hasRole("STAFF")
                        // ========== STAFF DAY OFF ==========
                        .requestMatchers(HttpMethod.GET, "/api/staff-day-offs-all/**").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/staff-day-offs-filter").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/staff-day-off/**").hasRole("STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/staff-day-off-update").hasRole("STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/staff-day-off-delete/**").hasRole("STAFF")
                        // ========== SALARY ==========
                        .requestMatchers(HttpMethod.GET, "/api/salaries-all/**").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/salaries-filter").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/salary/**").hasRole("STAFF")
                        // ========== PAYMENT ========== 
                        .requestMatchers(HttpMethod.GET, "/api/payment/create").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/payment/return").hasRole("CUSTOMER")
                        // ========== DEFAULT ==========
                        .anyRequest().authenticated())
                // Cấu hình form đăng nhập
                .formLogin(form -> form.loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    // Nếu là ADMIN thì cho vào trang chủ
                    if (authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/");
                    } else {
                        // Nếu không phải ADMIN thì logout và trả về lỗi "forbidden"
                        request.getSession().invalidate();
                        response.sendRedirect("/login?error=forbidden");
                    }
                })
                .failureHandler((request, response, exception) -> {
                    // Sai username/password
                    response.sendRedirect("/login?error=bad_credentials");
                })
                .permitAll()
                )
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutSuccessUrl("/login?logout=true").permitAll());
        return http.build();
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary
                = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "dxgc9wwrd",
                        "api_key", "167655672385223",
                        "api_secret", "M9MjBJTbL-Kv5_mr8u8QEGpxgo4",
                        "secure", true));
        return cloudinary;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000/")); // frontend origin: cho phép port truy cập api
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true); // Nếu dùng cookie

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
