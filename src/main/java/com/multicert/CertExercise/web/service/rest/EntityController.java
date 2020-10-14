package com.multicert.CertExercise.web.service.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multicert.CertExercise.bsl.services.IEntityService;
import com.multicert.CertExercise.dao.models.EntityData;

@RestController
public class EntityController {
	
	@Autowired
	private IEntityService entityService;
	
	@GetMapping("/entities2")
	List<EntityData> listEntities(){
		return entityService.listEntities();
	}
}
