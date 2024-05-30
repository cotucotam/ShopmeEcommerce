package com.shopme.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;

 
@Configuration
@EnableWebSecurity


public class WebSecurityConfig{   
	@Autowired private CustomerOAuth2UserService oAuth2UserService;
	@Autowired private OAuth2LoginSuccessHandler oauth2LoginHandler;
	@Autowired private DatabaseLoginSuccessHandler databaseLoginHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	@Bean
	public UserDetailsService userDetailsService() {
	// TODO Auto-generated method stub
	return new CustomerUserDetailsService();
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
		
	}
	@Bean
	SecurityFilterChain configureHttp(HttpSecurity http) throws Exception{
		http.authenticationProvider(authenticationProvider());
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/account_details", "/update_account_details", "/cart", "/address_book/**", "/checkout", "/place_order", "/process_paypal_order"
						, "/orders/**").authenticated()
				.anyRequest().permitAll()
				)
		.formLogin(form -> form
				.loginPage("/login")
				.usernameParameter("email")
				.successHandler(databaseLoginHandler)
				.permitAll())
        .oauth2Login(oauth2 -> oauth2
        		.loginPage("/login")
                .userInfoEndpoint(u -> u.userService(oAuth2UserService))
                .successHandler(oauth2LoginHandler))
		.logout(logout -> logout.permitAll())
		.rememberMe(rem-> rem
					.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
					.tokenValiditySeconds(14 * 24 * 60 * 60)
					)
		.sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));	
		return http.build();
	}
	
	@Bean
	WebSecurityCustomizer configureWebSecurity() throws Exception{
		return (web) -> web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**","/fontawesome/**","/webfonts/**");
	}
	
}
