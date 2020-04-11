package com.android.pokemon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.android.pokemon.dto.LoginDTO;
import com.android.pokemon.model.User;
import com.android.pokemon.service.UserService;

@Repository
public class UserController {
	
	@Autowired
	UserService userService;

	@RequestMapping(value = "api/user/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> login(@RequestBody LoginDTO loginDTO) {
		User retVal = userService.logIn(loginDTO.getEmail(), loginDTO.getPassword());
		
		if(retVal == null) {
			return new ResponseEntity<>(HttpStatus.LOCKED);
		}

		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
	
	@RequestMapping(value = "api/user/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> register(@RequestBody User userDTO) {
		User retVal = userService.register(userDTO);
		
		if(retVal == null) {
			return new ResponseEntity<>(HttpStatus.LOCKED);
		}

		return new ResponseEntity<>(retVal, HttpStatus.CREATED);
	}

}
