package com.pfe.ms.authentificationMs.controller;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.sql.Date;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.ms.authentificationMs.Domain.AppUser;
import com.pfe.ms.authentificationMs.Domain.Roles;
import com.pfe.ms.authentificationMs.Services.UserService;

    
	@RestController
	@RequestMapping("/api")
	public class UserController {

		@Autowired
		private UserService userService;
		
		
		@PostMapping("/signup")
		public AppUser create(@RequestBody AppUser user) {
			return userService.saveUser(user);
		}
		
		@GetMapping("/users")
		public List<AppUser> getAll(){
		return userService.getUsers();
		}
		
		@GetMapping("/roles")
		public List<Roles> getAllRoles(){
		return userService.getRoles();
		}
		
		/*
		 * @GetMapping("/users") public ResponseEntity<List<AppUser>> getAll(){ return
		 * ResponseEntity.ok().body(userService.getUsers()); }
		 */
		
		@PostMapping("/users/add")
		public AppUser add(@RequestBody AppUser user){
		return userService.saveUser(user);
		}
		

		@PostMapping("/roles/add")
		public Roles add(@RequestBody Roles role){
		return userService.saveRole(role);
		}
		
		//on va envoyer à travers le header après le mot Bearer le access token 
		@GetMapping("/token/refresh")
		public void RefreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException{
			String authorizationHeader = request.getHeader(AUTHORIZATION);
			
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				
				try {
					String refresh_Token = authorizationHeader.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
					//verifier le token 
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(refresh_Token);
					String username = decodedJWT.getSubject();
					AppUser user = userService.getUser(username);
					
					String access_Token = JWT.create()
							.withSubject(user.getNom())
							.withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000 ))
							.withIssuer(request.getRequestURL().toString())
							.sign(algorithm);
							
					
							//Pour retourner les 2 tokens dans body sous format JSON au lieu dans le head 
							Map<String, String> tokens = new HashMap<>();
							tokens.put("access_Token", access_Token);
							tokens.put("refresh_Token", refresh_Token);
							response.setContentType(MediaType.APPLICATION_JSON_VALUE);
							new ObjectMapper().writeValue(response.getOutputStream(), tokens);
									
				} catch (Exception e) {
					response.setHeader("error", e.getMessage());
					response.setStatus(FORBIDDEN.value());
					//response.sendError(FORBIDDEN.value());
					
					Map<String, String> error = new HashMap<>();
					error.put("error_message", e.getMessage());
					
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), error);
				}
				
			}else {
				throw new RuntimeException("Refresh token is missing");
			}
		}
	   

}
