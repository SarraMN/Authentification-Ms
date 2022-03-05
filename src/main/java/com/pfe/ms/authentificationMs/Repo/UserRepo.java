package com.pfe.ms.authentificationMs.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfe.ms.authentificationMs.Domain.AppUser;

@Repository
public interface UserRepo extends JpaRepository<AppUser, Long>{

	AppUser findBynom(String login);
	
}
