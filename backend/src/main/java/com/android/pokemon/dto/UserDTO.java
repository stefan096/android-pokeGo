package com.android.pokemon.dto;

import com.android.pokemon.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
	private Long id;
    private String name;
    private String lastName;
    private String email;
    
    public UserDTO(User user) {
    	this.id = user.getId();
    	this.lastName = user.getLastName();
    	this.name = user.getName();
    	this.email = user.getEmail();
    }
}
