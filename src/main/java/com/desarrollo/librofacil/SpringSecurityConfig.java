package com.desarrollo.librofacil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.desarrollo.librofacil.auth.LoginAuthSuccessHandler;
import com.desarrollo.librofacil.service.UsuarioDetailService;

@Configuration
public class SpringSecurityConfig {

	@Autowired
	LoginAuthSuccessHandler successHandler;

	@Autowired
	UsuarioDetailService usuarioDetailService;

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
		build.userDetailsService(usuarioDetailService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
				.requestMatchers("/", "/css/**", "/js/**", "/img/**", "/libreria/listarusuarios",
						"/libreria/listarlibros")
				.permitAll()
				.requestMatchers("/libreria/consultarusuario/**", "/libreria/consultarlibro/**").hasAnyRole("USER")
				.requestMatchers("/libreria/nuevousuario/**", "/libreria/agregarusuario/**", "/libreria/eliminarusuario/**",
						"/libreria/modificarusuario/**")	
				.hasAnyRole("ADMIN")
				.requestMatchers("/libreria/nuevolibro/**", "/libreria/agregarlibro/**", "/libreria/eliminarlibro/**",
						"/libreria/modificarlibro/**")
				.hasAnyRole("ADMIN")
				.requestMatchers("/libreria/nuevoprestamo/**", "/libreria/consultarprestamo/**", "/libreria/eliminarprestamo/**", "/libreria/agregarprestamo/**").hasAnyRole("ADMIN")
				.requestMatchers("/libreria/cargarejemplares/**", "/libreria/devolverejemplar/**").hasAnyRole("ADMIN")
				.anyRequest().authenticated()
				.and()
				.formLogin().successHandler(successHandler)
				.loginPage("/login").permitAll()
				.and()
				.logout().permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/error_403");

		return http.build();
	}

}
