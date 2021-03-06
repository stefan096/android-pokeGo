package com.android.pokemon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.android.pokemon.dto.LoginDTO;
import com.android.pokemon.dto.UserDTO;
import com.android.pokemon.model.User;
import com.android.pokemon.service.UserService;
import com.android.pokemon.utils.CheckValidity;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;

	@RequestMapping(value = "api/user/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> login(@RequestBody LoginDTO loginDTO) {
		User retVal = userService.logIn(loginDTO.getEmail(), loginDTO.getPassword());

		if(retVal == null) {
			return new ResponseEntity<>(HttpStatus.LOCKED);
		}
		
		return new ResponseEntity<>(new UserDTO(retVal), HttpStatus.OK);
	}
	
	@RequestMapping(value = "api/user/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> register(@RequestBody User userDTO) {
		
		if(!CheckValidity.nullOrEmpty(userDTO.getEmail())) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); //406
		}
		
		if(!CheckValidity.nullOrEmpty(userDTO.getLastName())) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); //406
		}
		
		if(!CheckValidity.nullOrEmpty(userDTO.getName())) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); //406
		}
		
		if(!CheckValidity.nullOrEmpty(userDTO.getPassword())) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); //406
		}
		
		User uniqueUser = userService.findByEmail(userDTO.getEmail());
		
		if(uniqueUser != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //400
		}

		User retVal = userService.register(userDTO);
		
		if(retVal == null) {
			return new ResponseEntity<>(HttpStatus.LOCKED);  //423
		}

		return new ResponseEntity<>(new UserDTO(retVal), HttpStatus.CREATED);
	}

	
	@RequestMapping(value = "api/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
		User retVal = userService.findOne(id);
		
		if(retVal == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new UserDTO(retVal), HttpStatus.OK);
	}



	@RequestMapping(value = "api/user/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> editUser(@RequestBody User userDTO) {
		
		if(!CheckValidity.nullOrEmpty(userDTO.getName())) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); //406
		}
		
		if(!CheckValidity.nullOrEmpty(userDTO.getLastName())) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); //406
		}
		
		User retVal = userService.findOne(userDTO.getId());
		
		if(retVal == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		retVal.setLastName(userDTO.getLastName());
		retVal.setName(userDTO.getName());
		User savedUser = userService.register(retVal);
		
		return new ResponseEntity<>(new UserDTO(savedUser), HttpStatus.OK);
	}
	
}
