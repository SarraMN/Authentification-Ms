package com.pfe.ms.authentificationMs.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pfe.ms.authentificationMs.filter.CustomAuthentificationFilter;


@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	/*
	 * @Autowired private BCryptPasswordEncoder bCrptPasswordEncoder;
	 */


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//si on veut changer l'url par défaut /login
		
		CustomAuthentificationFilter customAuthentificationFilter = new CustomAuthentificationFilter(authenticationManagerBean());
		  customAuthentificationFilter.setFilterProcessesUrl("/api/login");
		

		http.csrf().disable(); //On doit désactiver le system de tracking avec une cookie de spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//http.authorizeRequests().anyRequest().permitAll();
		
		http.authorizeHttpRequests().antMatchers("/api/login/**", "/api/token/refresh/**" ).permitAll();
		
		//si on garde l'url par défaut
				//http.authorizeHttpRequests().antMatchers("/login").permitAll();
		
		
		//Pour qu'un utilisateur accede au path "/api/user/**" il doit possèder le role Admin
		http.authorizeHttpRequests().antMatchers(HttpMethod.GET,"/api/user/**").hasAnyAuthority("Admin");
		
		
		
		
		http.authorizeRequests().anyRequest().authenticated(); //si on a utilisé des filtres 
		
		//on va passer le filter qu'on a creer (classe) 
		//prend en parametre authentificationManager qu'on va recuper dela  methode " authenticationManagerBean()"
		
		//si on changer l'url "/api/login/**"on doit passer le nv filtre
		http.addFilter(customAuthentificationFilter);   //au lieu de http.addFilter(new CustomAuthentificationFilter(authenticationManagerBean()));
		
		http.addFilterBefore(new CustomAuthentificationFilter(authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class);

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	
	///
	@Bean 
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	public SecurityConfig() {
		// TODO Auto-generated constructor stub
	}
	

}
