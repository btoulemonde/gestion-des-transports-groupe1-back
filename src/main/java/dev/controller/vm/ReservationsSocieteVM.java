package dev.controller.vm;

import java.time.LocalDateTime;

import dev.domain.ReservationsSociete;
import dev.domain.VehiculeSociete;

public class ReservationsSocieteVM {

	private LocalDateTime date;
	private LocalDateTime dateRetour;
	private CollegueVM collegue;
	private CollegueVM chauffeur;
	private VehiculeSociete vehicule;
	private Boolean avecChauffeur;
	
	public ReservationsSocieteVM(ReservationsSociete resaSociete) {
        this.date = resaSociete.getDate();
        this.dateRetour = resaSociete.getDateRetour();
        this.collegue = new CollegueVM(resaSociete.getCollegue());
        if(resaSociete.getChauffeur() != null) {
        	this.chauffeur = new CollegueVM(resaSociete.getChauffeur());
        }
        this.vehicule = resaSociete.getVehicules(); 
        this.avecChauffeur = resaSociete.getAvecChauffeur();
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public LocalDateTime getDateRetour() {
		return dateRetour;
	}
	public void setDateRetour(LocalDateTime dateRetour) {
		this.dateRetour = dateRetour;
	}
	
	public CollegueVM getCollegue() {
		return collegue;
	}

	public void setCollegue(CollegueVM collegue) {
		this.collegue = collegue;
	}

	public CollegueVM getChauffeur() {
		return chauffeur;
	}

	public void setChauffeur(CollegueVM chauffeur) {
		this.chauffeur = chauffeur;
	}

	public VehiculeSociete getVehicule() {
		return vehicule;
	}
	public void setVehicule(VehiculeSociete vehicule) {
		this.vehicule = vehicule;
	}

	public Boolean getAvecChauffeur() {
		return avecChauffeur;
	}

	public void setAvecChauffeur(Boolean avecChauffeur) {
		this.avecChauffeur = avecChauffeur;
	}
}
