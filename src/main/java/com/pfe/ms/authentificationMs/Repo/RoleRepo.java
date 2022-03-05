package com.pfe.ms.authentificationMs.Repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfe.ms.authentificationMs.Domain.Roles;

@Repository
public interface RoleRepo extends JpaRepository<Roles, Long>{

	Roles findBynom(String name);
}
