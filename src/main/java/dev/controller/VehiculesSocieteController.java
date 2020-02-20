package dev.controller;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.controller.dto.VehiculesSocieteDTO;
import dev.controller.dto.VehiculesSocieteFiltreDTO;
import dev.controller.vm.VehiculeSocieteVM;
import dev.service.VehiculesSocieteService;

/***
 * controller get et post pour les vehicules de societe
 * - lister les vehicules de societe
 * - filtrer les vehicules de societe par immatriculation et marque
 * @author audrey
 *
 */

@RestController
@RequestMapping(value = "vehiculesSociete")
public class VehiculesSocieteController {

	private VehiculesSocieteService vehiculesSocieteService;

	public VehiculesSocieteController(VehiculesSocieteService vehiculesSocieteService) {
		super();
		this.vehiculesSocieteService = vehiculesSocieteService;
	}
	
	/**
	 * GET vehiculesSociete : méthode qui récupere les véhicules de société 
	 * 
	 * @return liste de vehicules de societe (VehiculeSocieteVM)
	 */
	@GetMapping()
	public List<VehiculeSocieteVM> listerVehiculesSociete() {
		return this.vehiculesSocieteService.listerVehiculesSociete();
	}
	
	/**
	 * POST /vehiculesSociete
	 * 
	 * @param vehiculeDTO : données json
	
	 * @return liste des vehicules filtrés
	 */
	@PostMapping
	public List<VehiculeSocieteVM> chercherVehiculeAvecFiltre(
			@RequestBody VehiculesSocieteFiltreDTO vehiculeDTO) {
		return this.vehiculesSocieteService.chercherVehiculeAvecFiltre(vehiculeDTO);
	}
	
	@PostMapping(value="/creer")
	public ResponseEntity<?> creerVehiculeSociete(@RequestBody @Valid VehiculesSocieteDTO vehiculePost) {
		
		this.vehiculesSocieteService.creerVehiculeSociete(vehiculePost);
		return ResponseEntity.status(HttpStatus.CREATED).body("Véhicule ajouté en base de données");
	}
	
	@ExceptionHandler(value = { EntityExistsException.class })
	public ResponseEntity<String> VehiculePresent(EntityExistsException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Véhicule de société déjà existant");
	}
	
}
