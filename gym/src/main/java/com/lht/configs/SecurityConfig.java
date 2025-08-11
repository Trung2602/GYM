/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
        http
                // Tắt CSRF (trong trường hợp bạn không dùng) và cấu hình CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // Sử dụng cú pháp lambda mới, gọn gàng hơn

                // Cấu hình phân quyền truy cập
                .authorizeHttpRequests(requests -> requests
                // Cho phép truy cập các tài nguyên tĩnh và trang đăng nhập
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                // Cho phép truy cập API đăng nhập nếu bạn có một API riêng
                .requestMatchers("/api/login").permitAll()
                // Mọi yêu cầu khác đều phải được xác thực
                .anyRequest().authenticated())
                // Cấu hình form đăng nhập
                .formLogin(form -> form
                .loginPage("/login") // Trang đăng nhập tùy chỉnh
                .loginProcessingUrl("/login") // URL xử lý form đăng nhập
                .defaultSuccessUrl("/", true) // URL sau khi đăng nhập thành công
                .failureUrl("/login?error=true") // URL khi đăng nhập thất bại
                .permitAll()) // Cho phép mọi người truy cập các URL liên quan đến formLogin

                // Cấu hình đăng xuất
                .logout(logout -> logout
                .logoutUrl("/logout") // URL xử lý đăng xuất
                .logoutSuccessUrl("/login") // URL sau khi đăng xuất thành công
                .deleteCookies("JSESSIONID") // Xóa cookie phiên làm việc
                .clearAuthentication(true) // Xóa thông tin xác thực
                .permitAll()); // Cho phép mọi người truy cập URL đăng xuất

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
