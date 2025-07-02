package com.chartboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//	@Autowired
//    private JwtTokenProvider jwtTokenProvider;
//	@Autowired
//	private CustomUserDetailsService customUserDetailsService;
//	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            
//            .requestMatchers("/memberRegister", "/memberLogin", "/selectUserInfo", "/selectUserName", "/isUserExist", "/changeUserInfo").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//            .requestMatchers("/select15FromGangnamGangDongHospital", "/selectFromGangnamHospital", "/selectFromGangdongHospital", "searchAndFilterHospital").permitAll() 
            
            .requestMatchers("/**").permitAll()

            .anyRequest().authenticated();

        // JWT 필터 등록
//        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
	
//	@Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = 
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
//        return authenticationManagerBuilder.build();
//    }
	
}
