package com.android.pokemon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.android.pokemon.model.User;
import com.android.pokemon.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	public User register(User user){
		return userRepository.save(user);
	}
	
	public User logIn(String email, String password) {
		User foundUser = userRepository.findByEmail(email);
		if(foundUser == null) {
			return null;
		}
		
		if(!foundUser.getPassword().equals(password)) {
			return null;
		}
		
		return foundUser;
	}
	
}
