package com.pfe.ms.authentificationMs.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.Arrays.stream; 
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

//cette classe va filtrer chaque requette quiç vient deu ciennt pour sverifier le token 
public class CustomAutorizationFilter extends OncePerRequestFilter {



	public CustomAutorizationFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//si l'user vient de login on a pas pas besoin de nverifier l'autorisation =>cette requette na pas besoin d'etre intercepter
		if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh") ) {
			filterChain.doFilter(request, response);
		}
		else {
			//recevoir le clé de token du header de la request
			String authorizationHeader = request.getHeader(AUTHORIZATION);
			
			//A chaque fois on envoie une requette avec le token du front on ajoute le mot Bearer s'il est valide donne tout l'autorization qui vient avec le token 
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ") ) {
				try {
					String token = authorizationHeader.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
					//verifier le token 
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(token);
					String username =decodedJWT.getSubject();
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					stream(roles).forEach(role ->{
							authorities.add(new SimpleGrantedAuthority(role));
					});
					
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null ,authorities);
					//Pour dire a spring security voici le user,username,et son role  
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					
					filterChain.doFilter(request, response); 
					
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
				filterChain.doFilter(request, response);
			}
		}
	}

}
