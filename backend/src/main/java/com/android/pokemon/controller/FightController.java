package com.android.pokemon.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.android.pokemon.dto.FightDTO;
import com.android.pokemon.dto.UsersPokemonsDTO;
import com.android.pokemon.enums.FightStateMove;
import com.android.pokemon.model.Boss;
import com.android.pokemon.model.Fight;
import com.android.pokemon.model.User;
import com.android.pokemon.model.UsersPokemons;
import com.android.pokemon.service.BossService;
import com.android.pokemon.service.FightService;
import com.android.pokemon.service.PokemonService;
import com.android.pokemon.service.UserService;
import com.android.pokemon.service.UsersPokemonsService;

@Controller
public class FightController {
	
	@Autowired
	BossService bossService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PokemonService pokemonService;
	
	@Autowired
	UsersPokemonsService usersPokemonService;
	
	@Autowired
	FightService fightService;
	
	/**
	 * 
	 * @param fightDTO
	 * @return
	 */
    @RequestMapping(value = "api/fight", method = RequestMethod.PUT)
    public ResponseEntity<FightDTO> updateFight(@RequestBody FightDTO fightDTO) {
    	
    	if(fightDTO.getId() == null || fightDTO.getId() == 0) {
    		//napravi novi posto je pocetak borbe
    		Boss boss = bossService.findById(fightDTO.getId());
    		boss.setFightHealt(boss.getPokemon().getHp());
    		boss = bossService.save(boss);
    		User user = userService.findOne(fightDTO.getUser().getId());
    		
    		Fight fight = new Fight();
    		fight.setBoss(boss);
    		fight.setCaught(false);
    		fight.setFightStateMove(FightStateMove.POKEMON_ATTACKS);
    		fight.setUser(user);
    		fight.setTimeOfFight(Instant.now());
    		
    		List<UsersPokemons> pokemons = new ArrayList<UsersPokemons>();
    		for (UsersPokemonsDTO onePokemon : fightDTO.getPokemons()) {
				UsersPokemons foundPokemon = usersPokemonService.findById(onePokemon.getId());
				foundPokemon.setFightHealt(foundPokemon.getPokemon().getHp());
				foundPokemon = usersPokemonService.save(foundPokemon);
				
				pokemons.add(foundPokemon);
			}
    		
    		fight.setPokemons(pokemons);
    		Fight savedFight = fightService.save(fight);
    		
    		return new ResponseEntity<>(new FightDTO(savedFight), HttpStatus.OK);
    	}
    	
    	//u borbi si vidi ostalo i skidaj helte
		
		Fight fight = fightService.findById(fightDTO.getId());
		
		if(fight.getPokemonOnMove() == null) {
			//znaci izabran je pokemon za borbu prvi put
			UsersPokemons pokemonOnMove = usersPokemonService.findById(fightDTO.getPokemonOnMove().getId()); 
			fight.setPokemonOnMove(pokemonOnMove);
			fight.getPokemonOnMove().setFightHealt(pokemonOnMove.getPokemon().getHp());
		}
		else if(fight.getPokemonOnMove().getId() != fightDTO.getPokemonOnMove().getId()){ 
			//ako nije null znaci nije prvi put, sad me zanima da li se pokemon promenio
			UsersPokemons pokemonOnMove = usersPokemonService.findById(fightDTO.getPokemonOnMove().getId()); 
			fight.setPokemonOnMove(pokemonOnMove);
			fight.getPokemonOnMove().setFightHealt(pokemonOnMove.getPokemon().getHp());
		}

		if(fight.getFightStateMove().equals(FightStateMove.POKEMON_ATTACKS)) { //pokemon napada		
			fight.setFightStateMove(FightStateMove.BOSS_ATTACKS);
			double damage = this.calculateDMGToDeal();
			fight.getBoss().setFightHealt(fight.getBoss().getFightHealt() - damage);
			
		}
		else { //boss napada
			fight.setFightStateMove(FightStateMove.POKEMON_ATTACKS);
			double damage = this.calculateDMGToDeal();
			fight.getPokemonOnMove().setFightHealt(fight.getPokemonOnMove().getFightHealt() - damage);
		}
		
		//ukoliko je boss spao na 0 uhvacen je
		if(fight.getBoss().getFightHealt() <= 0) {
			fight.setCaught(true);
		}
		
		
		Fight savedFight = fightService.save(fight);
		
		return new ResponseEntity<>(new FightDTO(savedFight), HttpStatus.OK);

    }
    
    private double calculateDMGToDeal() {
    	return 10.0;
    }

}
