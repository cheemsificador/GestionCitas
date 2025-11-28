package com.idat.Requerimiento_01.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;


@Entity
@Table(name = "citas")
public class Cita {
	
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUsuario;

    @Column(nullable = false)
    private String especialidad;

    @Column(nullable = false)
    private String fecha;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Column(nullable = false)
    private String estado; // PROGRAMADA, CANCELADA
	
    
    
    public Cita() {
		super();
	}



	public Cita(Long id, Long idUsuario, String especialidad, String fecha, String motivo, String estado) {
		super();
		this.id = id;
		this.idUsuario = idUsuario;
		this.especialidad = especialidad;
		this.fecha = fecha;
		this.motivo = motivo;
		this.estado = estado;
	}



	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public Long getIdUsuario() {return idUsuario;}
	public void setIdUsuario(Long idUsuario) {this.idUsuario = idUsuario;}

	public String getEspecialidad() {return especialidad;}
	public void setEspecialidad(String especialidad) {this.especialidad = especialidad;}

	public String getFecha() {return fecha;}
	public void setFecha(String fecha) {this.fecha = fecha;}

	public String getMotivo() {return motivo;}
	public void setMotivo(String motivo) {this.motivo = motivo;}

	public String getEstado() {return estado;}
	public void setEstado(String estado) {this.estado = estado;}

}
