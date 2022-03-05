package com.pfe.ms.authentificationMs.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pfe.ms.authentificationMs.Domain.AppUser;
import com.pfe.ms.authentificationMs.Domain.Roles;
import com.pfe.ms.authentificationMs.Repo.RoleRepo;
import com.pfe.ms.authentificationMs.Repo.UserRepo;



@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService{
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//charger le user authentifier s'il existe dans la base de données et savoir son autorisation
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = userRepo.findBynom(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("user not found");
		}
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		Roles role = user.getUserRole();
		authorities.add(new SimpleGrantedAuthority(role.getNom()));
		//on un spring security objet avec son authorities
		return new User(user.getNom(), user.getPwd(), authorities);
	}
	
	
	//pour crypter le mot passe
	//@Autowired
	//private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public AppUser saveUser(AppUser user) {
    	user.setPwd(passwordEncoder.encode(user.getPwd()));
		return userRepo.save(user);
	}

	@Override
	public Roles saveRole(Roles role) {
		return roleRepo.save(role);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
     AppUser user=userRepo.findBynom(username);
     Roles role=roleRepo.findBynom(rolename);
     user.setUserRole(role);
	}

	@Override
	public AppUser getUser(String username) {
		return userRepo.findBynom(username);
	}

	@Override
	public List<AppUser> getUsers() {
		return userRepo.findAll();
	}
	
	@Override
	public List<Roles> getRoles() {
		return roleRepo.findAll();
	}

	

}
