package dev.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.repository.ReservationsSocieteRepo;
import dev.repository.VehiculeSocieteRepo;
import dev.controller.dto.VehiculesSocieteDTO;
import dev.controller.dto.VehiculesSocieteFiltreDTO;
import dev.controller.vm.VehiculeSocieteVM;
import dev.domain.ReservationsSociete;
import dev.domain.Statut;
import dev.domain.VehiculeSociete;
import dev.exception.VehiculeNonTrouveException;
import dev.exception.VehiculeTrouveException;

/**
 * Classe de service pour les méthodes utilisés par la classe
 * VehiculesSocieteController
 * - lister les véhicules de société
 * - filtrer les vehicules de societe par immatriculation ou par marque
 * - créer un véhicule de société
 * - mettre a jour un vehicule de société
 * - supprimer un véhicule de société
 */

@Service
public class VehiculesSocieteService {

	private VehiculeSocieteRepo vehiculesSocieteRepo;
	private ReservationsSocieteRepo reservationsSocieteRepo;
	
	/**
	 * @param vehiculesSocieteRepo
	 * @param reservationsSocieteRepo
	 */
	public VehiculesSocieteService(VehiculeSocieteRepo vehiculesSocieteRepo,
			ReservationsSocieteRepo reservationsSocieteRepo) {
		super();
		this.vehiculesSocieteRepo = vehiculesSocieteRepo;
		this.reservationsSocieteRepo = reservationsSocieteRepo;
	}

	public List<VehiculeSocieteVM> listerVehiculesSociete(){

		return this.vehiculesSocieteRepo.findAll().stream().map(VehiculeSocieteVM::new)
				.collect(Collectors.toList());
	}
	
	public List<VehiculeSocieteVM> chercherVehiculeAvecFiltre(VehiculesSocieteFiltreDTO vehiculeFiltrePost) {
		return this.vehiculesSocieteRepo.findByImmatriculationOrMarque(vehiculeFiltrePost.getImmatriculation(), vehiculeFiltrePost.getMarque())
				.stream().map(VehiculeSocieteVM::new)
				.collect(Collectors.toList());
	}
	
	public void creerVehiculeSociete (VehiculesSocieteDTO vehiculeDTOPost) throws VehiculeTrouveException {
		
		if(this.vehiculesSocieteRepo.findByImmatriculationExist(vehiculeDTOPost.getImmatriculation())){
			throw new VehiculeTrouveException("");
		}
		
		VehiculeSociete vehiculeNewPost = new VehiculeSociete();
		
		vehiculeNewPost.setImmatriculation(vehiculeDTOPost.getImmatriculation());
		vehiculeNewPost.setMarque(vehiculeDTOPost.getMarque());
		vehiculeNewPost.setModele(vehiculeDTOPost.getModele());
		vehiculeNewPost.setCategorie(vehiculeDTOPost.getCategorie());
		
		if(vehiculeDTOPost.getStatut()!= null) {
			vehiculeNewPost.setStatut(vehiculeDTOPost.getStatut());
		}
		else {
			vehiculeNewPost.setStatut(Statut.EN_SERVICE);
		}
		
		vehiculeNewPost.setUrlPhoto(vehiculeDTOPost.getUrlPhoto());
		
		this.vehiculesSocieteRepo.save(vehiculeNewPost);
	
	}
	
	public void updaterVehiculeSociete(Long idVehicule, VehiculesSocieteDTO vehiculeDTOPost) throws VehiculeTrouveException, VehiculeNonTrouveException {
		
		VehiculeSociete vehiculeEdit = this.vehiculesSocieteRepo.findById(idVehicule).orElseThrow(() -> new VehiculeNonTrouveException(""));
		
		if(!vehiculeEdit.getImmatriculation().equals(vehiculeDTOPost.getImmatriculation())) {
			if(this.vehiculesSocieteRepo.findByImmatriculationExist(vehiculeDTOPost.getImmatriculation())){
				throw new VehiculeTrouveException("");
			}
		}
		
		if(vehiculeDTOPost.getImmatriculation()!= null) {
			vehiculeEdit.setImmatriculation(vehiculeDTOPost.getImmatriculation());
		}
		
		if(vehiculeDTOPost.getMarque()!= null) {
			vehiculeEdit.setMarque(vehiculeDTOPost.getMarque());
		}
		
		if(vehiculeDTOPost.getModele()!= null) {
			vehiculeEdit.setModele(vehiculeDTOPost.getModele());
		}
		
		if(vehiculeDTOPost.getCategorie()!= null) {
			vehiculeEdit.setCategorie(vehiculeDTOPost.getCategorie());
		}
		
		if(vehiculeDTOPost.getStatut()!= null) {
			vehiculeEdit.setStatut(vehiculeDTOPost.getStatut());
		}
		
		if(vehiculeDTOPost.getUrlPhoto()!= null) {
			vehiculeEdit.setUrlPhoto(vehiculeDTOPost.getUrlPhoto());
		}
		this.vehiculesSocieteRepo.save(vehiculeEdit);
	}
	
	public void supprimerVehiculeSociete(Long idVehicule) throws VehiculeNonTrouveException {
		
		VehiculeSociete vehicule = this.vehiculesSocieteRepo.findById(idVehicule).orElseThrow(() -> new VehiculeNonTrouveException(""));
		
		
		List<ReservationsSociete> listReservationsSociete = this.reservationsSocieteRepo.findReservationsByVehicules(vehicule);
		
		listReservationsSociete.forEach(resa->{
			//System.out.println(resa.getCollegue().getEmail());
			//System.out.println(resa.getDate());
			//envoi mail pour annuler les reservations en cours
			//supprimer les reservations prévues avec ce véhicule
			this.reservationsSocieteRepo.delete(resa);
		});
			
		this.vehiculesSocieteRepo.deleteById(idVehicule);
	}
}
