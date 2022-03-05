package com.pfe.ms.authentificationMs.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity 
public class AppUser {

	
	@Id @GeneratedValue(strategy =GenerationType.AUTO)
	private Long id;
    private String nom;
    private String pwd;
    private String adresse;
    private long numerotell;
    private String email;
    

    
    @ManyToOne
    private Roles UserRole;
    
	public AppUser() {
		// TODO Auto-generated constructor stub
	}

	public AppUser(Long id, String nom, String pwd, String adresse, long numerotell, String email,
			com.pfe.ms.authentificationMs.Domain.Roles userRole) {
		super();
		this.id = id;
		
		this.nom = nom;
		this.pwd = pwd;
		this.adresse = adresse;
		this.numerotell = numerotell;
		this.email = email;
		UserRole = userRole;
	}

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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public long getNumerotell() {
		return numerotell;
	}

	public void setNumerotell(long numerotell) {
		this.numerotell = numerotell;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Roles getUserRole() {
		return UserRole;
	}

	public void setUserRole(Roles userRole) {
		UserRole = userRole;
	}

}
