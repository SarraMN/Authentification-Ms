package com.pfe.ms.authentificationMs.filter;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthentificationFilter extends UsernamePasswordAuthenticationFilter{


	
	private final AuthenticationManager authenticationManager;
	
	
	public CustomAuthentificationFilter (AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException {
		String username = request.getParameter("nom");
		String password = request.getParameter("pwd");
		
		//ajouter les information lié au utilisateur authetifier au Token
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		
		
		//appeler le authentificaationManger pour authentifier l'utilisateur 
		return authenticationManager.authenticate(authenticationToken);
	}

	
	
	//si l'authentification avec succée cette méthode est appelée on doit donner le token au utilisaire après son authentification
	//on va télécharger u_ne librerie pour faciliter le travail==> dépendance
	//chercher dans le google autho0 java jwt maven et ajouter dans pom file comme dépendance
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentification) throws IOException, ServletException {
		
		//get l'utilisateur qui s'était authentifier avec succé 
		User user = (User)authentification.getPrincipal();//User de spring security 
		
		//get des informations de user pour crer JWT
		//algorithme pour enregistrer le token et le refresh token 
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
		
		//dans notre application le user name est unique c'est pour cela on a passer getUserName
		//on doit ajuster le temps de Token 10 min 
		//withClaim => les permissions
		
		String access_Token = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000 ))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);
				
		//refresh token pour 30 min
				String refresh_Token = JWT.create().withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + 30*60*1000 ))
						.withIssuer(request.getRequestURL().toString())
						.sign(algorithm);		
				/*
				 * response.setHeader("access_Token", access_Token);
				 * response.setHeader("refresh_Token", refresh_Token);
				 */
				//Pour retourner les 2 tokens dans body sous format JSON au lieu dans le head 
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_Token", access_Token);
				tokens.put("refresh_Token", refresh_Token);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
		
	}



}
