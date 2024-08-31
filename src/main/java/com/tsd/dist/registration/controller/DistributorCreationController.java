package com.tsd.dist.registration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tsd.sdk.model.Distributor;
import org.tsd.sdk.resquest.DistributorReq;

import com.tsd.dist.registration.service.DistributorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/tsd/dist")
@CrossOrigin
@Tag(name = "Registration Distributor API", description = "Operations related to registration of distributor")
public class DistributorCreationController {
	
	@Autowired
	DistributorService distributorService;
	
	@GetMapping(path = "/health")
	public ResponseEntity<?> health(){
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping(path = "/register",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Distributor registration", description = "Operations related to registration of distributor")
    public Distributor createUser(@RequestBody DistributorReq distributorReq) {
        return distributorService.saveDistributor(distributorReq);
    }

}