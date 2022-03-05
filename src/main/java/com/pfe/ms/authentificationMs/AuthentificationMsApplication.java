package com.pfe.ms.authentificationMs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pfe.ms.authentificationMs.Domain.AppUser;
import com.pfe.ms.authentificationMs.Domain.Roles;
import com.pfe.ms.authentificationMs.Services.UserService;


@SpringBootApplication
public class AuthentificationMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthentificationMsApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(UserService userservice) {
		return args ->{
			userservice.saveRole(new Roles(null,"Admin", null));
			userservice.saveRole(new Roles(null,"Professeur", null));
			userservice.saveRole(new Roles(null,"Candidat", null));
		
			userservice.saveUser(new AppUser(null,"sarra", "sarra","khaznadar", 25075032, "sarahmannai@gmail.com",null));
			userservice.saveUser(new AppUser(null,"sirine", "sirine","khaznadar", 52075032, "sirine@gmail.com",null));
			userservice.addRoleToUser("sarra","Admin");
			userservice.addRoleToUser("sirine","Candidat");



		};
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
