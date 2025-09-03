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
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/").hasRole("ADMIN")
                        // Cho phép truy cập API đăng nhập nếu bạn có một API riêng
                        .requestMatchers("/api/**").permitAll()
                        // ========== ACCOUNT ==========
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/account/me").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/register-customer").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/verify-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/account/update").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/change-password").permitAll()
                        // ========== FACILITY ==========
                        .requestMatchers(HttpMethod.GET, "/api/facilities-all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/facility/**").permitAll()
                        // ========== PLAN ==========
                        .requestMatchers(HttpMethod.GET, "/api/plans-all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/plans-filter").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/plan/**").permitAll()
                        // ========== SHIFT ==========
                        .requestMatchers(HttpMethod.GET, "/api/shifts-all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shifts-filter").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shift/**").permitAll()
                        // ========== CUSTOMER SCHEDULE ==========
                        .requestMatchers(HttpMethod.GET, "/api/customer-schedules-all/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer-schedules-filter").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customer-schedule/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/customer-schedule-update").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/customer-schedule-delete/**").permitAll()
                        // ========== PAY CUSTOMER ==========
                        .requestMatchers(HttpMethod.GET, "/api/pay-customers-all/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/pay-customer-filter").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/pay-customer/**").permitAll()
                        // ========== STAFF ==========
                        .requestMatchers(HttpMethod.GET, "/api/staffs-all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/working-staff").permitAll()
                        // ========== STAFF TYPE ==========
                        .requestMatchers(HttpMethod.GET, "/api/staff-type-all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/staff-type/**").permitAll()
                        // ========== STAFF SCHEDULE ==========
                        .requestMatchers(HttpMethod.GET, "/api/staff-schedules-all/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/staff-schedules-filter").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/staff-schedule/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/staff-schedule-update").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/staff-schedule-delete/**").permitAll()
                        // ========== STAFF DAY OFF ==========
                        .requestMatchers(HttpMethod.GET, "/api/staff-day-offs-all/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/staff-day-offs-filter").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/staff-day-off/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/staff-day-off-update").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/staff-day-off-delete/**").permitAll()
                        // ========== SALARY ==========
                        .requestMatchers(HttpMethod.GET, "/api/salaries-all/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/salaries-filter").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/salary/**").permitAll()
                        // ========== PAYMENT ==========
                        .requestMatchers(HttpMethod.GET, "/api/payment/create").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/payment/return").permitAll()
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
