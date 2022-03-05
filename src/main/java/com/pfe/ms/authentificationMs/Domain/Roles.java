package com.pfe.ms.authentificationMs.Domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Roles {
	
	@Id @GeneratedValue(strategy =GenerationType.AUTO)
	private Long id;
    private String nom;
    
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,mappedBy="UserRole")
    Set<AppUser> User= new HashSet<AppUser>();
	
    
    public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}

	@JsonIgnore
	public Set<AppUser> getUser() {
		return User;
	}


	public Roles(Long id, String nom, Set<AppUser> user) {
		super();
		this.id = id;
		this.nom = nom;
		User = user;
	}


	public void setUser(Set<AppUser> user) {
		User = user;
	}


	public Roles() {
		// TODO Auto-generated constructor stub
	}

}
