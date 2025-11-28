package com.idat.Requerimiento_01.dto;


public class CitaRequest {
	
    private String especialidad;

    private String fecha; // formato: "2025-11-20T11:00"

    private String motivo;

    
    
	public CitaRequest() {
		super();
	}



	public CitaRequest(String especialidad, String fecha, String motivo) {
		super();
		this.especialidad = especialidad;
		this.fecha = fecha;
		this.motivo = motivo;
	}



	public String getEspecialidad() {return especialidad;}
	public void setEspecialidad(String especialidad) {this.especialidad = especialidad;}

	public String getFecha() {return fecha;}
	public void setFecha(String fecha) {this.fecha = fecha;}

	public String getMotivo() {return motivo;}
	public void setMotivo(String motivo) {this.motivo = motivo;}

}
