package com.android.pokemon.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.android.pokemon.model.Pokemon;
import com.android.pokemon.service.PokemonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PokemonController {
	
	@Autowired
	PokemonService pokemonService;

	@RequestMapping(value = "api/pokemon", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<Pokemon>> getAllPokemons() {
		List<Pokemon> pokemons = pokemonService.findAll();
		
		return new ResponseEntity<>(pokemons, HttpStatus.OK);
	}

	@RequestMapping(value = "api/pokemon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Pokemon> create(@RequestBody Pokemon pokemonDTO) {
		Pokemon retVal = pokemonService.save(pokemonDTO);

		return new ResponseEntity<>(retVal, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "api/pokemon/all", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createAll(@RequestBody List<Pokemon> pokemons) {
		pokemonService.saveAll(pokemons);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "api/pokemon/all/string", method = RequestMethod.POST)
	public ResponseEntity<Void> createAll(@RequestBody String tempValue) {
		//System.out.println(tempValue);
		String tempValue2 = tempValue.replace("\\", "");
		
		ObjectMapper mapper = new ObjectMapper();
		String toConvert = tempValue2.substring(1, tempValue2.length()-1);
				
		// 2. convert JSON array to List of objects
		List<Pokemon> pokemons = new ArrayList<Pokemon>();
		try {
			pokemons = Arrays.asList(mapper.readValue(toConvert, Pokemon[].class));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		pokemonService.saveAll(pokemons);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "api/pokemon/proba", method = RequestMethod.GET)
	public ResponseEntity<Void> createAllProba() {
		System.out.println("USAOO GET");
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
