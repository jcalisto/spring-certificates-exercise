package com.multicert.CertExercise.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.multicert.CertExercise.bsl.services.IEntityService;
import com.multicert.CertExercise.dao.models.EntityData;

@Controller
public class WebEntityController {
	
	@Autowired
	private IEntityService entityService;

	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/entities")
	public String listEntities(Model model) {
		List<EntityData> entities;
		try {
			entities = (List<EntityData>) entityService.listEntities();
		} catch(Exception e) {	//Todo specify?
			System.out.println("listEntities Exception: " + e.getMessage());
			entities = new ArrayList<EntityData>();
			model.addAttribute("errorMessage", "Can't fetch Entities.");
		}
		model.addAttribute("entities", entities);
		return "entities";
	}
	
	@PostMapping("/entities/add")
	public String addEntity(@RequestParam("name") String name, 
					@RequestParam("nif") String nif, 
					@RequestParam("countryCode") String countryCode, 
					@RequestParam("entityType") String entityType, 
					@RequestParam("companyName") String companyName, 
					Model model, RedirectAttributes redirectAttributes) {
		try {
			entityService.addEntity(name, nif, countryCode, entityType, companyName);
		} catch(Exception e) {	//Todo specify?
			redirectAttributes.addAttribute("errorMessage", "Error adding client, invalid arguments");
		}
		return "redirect:/entities";
	}
	
	@GetMapping("/entities/{id}/details")
	public String detailEntity(@PathVariable(value="id") Long id, Model model) {
		Optional<EntityData> wEntity = entityService.getEntityById(id);
		if(wEntity.isPresent()) {
			model.addAttribute("entity", wEntity.get());
			model.addAttribute("requestedCertificates", wEntity.get().getRequestedCertificates());
			model.addAttribute("signedCertificates", wEntity.get().getSignedCertificates());
		} else {
			model.addAttribute("errorMessage", "Entity with ID=" + id + " not found.");
		}
		return "entity_detailed";
	}
	


	
}


