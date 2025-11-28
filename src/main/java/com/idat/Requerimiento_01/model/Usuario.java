package com.idat.Requerimiento_01.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    
    
	public Usuario() {
		super();
	}



	public Usuario(Long id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}



	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}

	public String getPassword() {return password;}

	public void setPassword(String password) {this.password = password;}
     
    
}


