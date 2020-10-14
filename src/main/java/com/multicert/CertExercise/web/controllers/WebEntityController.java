package com.multicert.CertExercise.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@RequestMapping(value = "/entities/add", method = RequestMethod.POST)
	public String addEntity(@RequestParam("name") String name, 
					@RequestParam("nif") String nif, 
					@RequestParam("countryCode") String countryCode, 
					@RequestParam("entityType") String entityType, 
					@RequestParam("companyName") String companyName, 
					Model model) {
		System.out.println("Received addEntity. Name = " + name + ", entityType: " + entityType);
		List<EntityData> entities = new ArrayList<EntityData>();
		try {
			entityService.addEntity(name, nif, countryCode, entityType, companyName);
		} catch(Exception e) {	//Todo specify?
			model.addAttribute("errorMessage", "Error adding client, invalid arguments");
		}
		entities = (List<EntityData>) entityService.listEntities();
		model.addAttribute("entities", entities);
		return "entities";
	}
}
