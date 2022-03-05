package com.pfe.ms.authentificationMs.Services;

import java.util.List;

import com.pfe.ms.authentificationMs.Domain.AppUser;
import com.pfe.ms.authentificationMs.Domain.Roles;

public interface UserService {

	//ajouter role et user dans la bd 
	AppUser saveUser(AppUser user);
	Roles saveRole(Roles role);
	//il faut pas avoir 2 user dans la base de données avec le meme nom
	void addRoleToUser(String username,String rolename);
	
	AppUser getUser(String username);
	List<AppUser> getUsers();
	List<Roles> getRoles();

}
