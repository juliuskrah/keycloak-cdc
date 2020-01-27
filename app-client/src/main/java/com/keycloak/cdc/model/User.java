package com.keycloak.cdc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class User {

	private UUID id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private List<Role> role = new ArrayList<Role>();
	
}
