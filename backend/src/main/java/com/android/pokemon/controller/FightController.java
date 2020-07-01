package com.android.pokemon.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.android.pokemon.dto.FightDTO;
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
import com.android.pokemon.utils.Constants;

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
	 * @param fightDTO.user - koji korisnik igra igricu
	 * @param fightDTO.boss - kog bosa pokusava da uhvati
	 * @param fightDTO.caught - da li je boss uspesno uhvacen - samo to ne govori o tome da li je pokemon izgubio
	 * @param fightDTO.pokemonOnMove - sa kojim korisnickim pokemonom trenutno pokusavamo da uhvatimo bossa (ovo se promeni
	 * kada korisnik promeni svog pokemona sa kojim hvata bossa)
	 * @param fightDTO.counterForPokemon - brojac koliko puta je pokusan isti boss, posle najvise 3 puta ne sme vise
	 * @param fightDTO.counterForTurn - broji poteze u tuci
	 * 
	 * @return fightDTO objekat koji predstavlja borbu
	 */
	/*
	 PRVI JSON pre ulaska u borbu, i izabrao je bossa protiv koga se bori i treba mi id korisnika
	 
	{
		"id": null,
		"user": {
			"id": 8
		},
		"boss": {
			"id": 302
		},
		"caught": false,
		"pokemonOnMove": null
	}
	
	Kao odgovor ces dobiti ceo fightDTO objekat koji ces morati da mi prosledjujes nadalje sa izmenom samo na trenutno 
	izabranom pokemonu da znam koji pokemon se trenutno tuce sa bossom (izmena na id i pokemonOnMone.id)
	
	SVAKI SLEDECI DO KRAJA
	
	{
		"id": 4,
		"user": {
			"id": 8
		},
		"boss": {
			"id": 302
		},
		"caught": false,
		"pokemonOnMove": {
			"id": 2
		}
	}
	 
	*/
    @RequestMapping(value = "api/fight", method = RequestMethod.PUT)
    public ResponseEntity<FightDTO> updateFight(@RequestBody FightDTO fightDTO) {
    	
    	if(fightDTO.getId() == null || fightDTO.getId() == 0) {
    		//napravi novi posto je pocetak borbe
    		Boss boss = bossService.findById(fightDTO.getBoss().getId());
    		boss.setFightHealt(setFightHealtBasedOnLevel(Constants.BORDER_FOR_ADDING_HEALT, 
    				boss.getPokemon().getHp(), boss.getLevel()));
    		boss = bossService.save(boss);
    		User user = userService.findOne(fightDTO.getUser().getId());
    		
    		Fight fight = new Fight();
    		fight.setBoss(boss);
    		fight.setCaught(false);
    		fight.setFightStateMove(FightStateMove.POKEMON_ATTACKS);
    		fight.setUser(user);
    		fight.setTimeOfFight(Instant.now());
    		fight.setCounterForPokemon(0);
    		fight.setCounterForTurn(0);
    		
//    		List<UsersPokemons> pokemons = new ArrayList<UsersPokemons>();
//    		for (UsersPokemonsDTO onePokemon : fightDTO.getPokemons()) {
//				UsersPokemons foundPokemon = usersPokemonService.findById(onePokemon.getId());
//				foundPokemon.setFightHealt(foundPokemon.getPokemon().getHp());
//				foundPokemon = usersPokemonService.save(foundPokemon);
//				
//				pokemons.add(foundPokemon);
//			}
//    		
//    		fight.setPokemons(pokemons);
    		Fight savedFight = fightService.save(fight);
    		System.out.println(new FightDTO(savedFight));
    		return new ResponseEntity<>(new FightDTO(savedFight), HttpStatus.OK);
    	}
    	
    	//u borbi si vidi ostalo i skidaj helte
		
		Fight fight = fightService.findById(fightDTO.getId());
		
		if(fight.getPokemonOnMove() == null) {
			//znaci izabran je pokemon za borbu prvi put
			UsersPokemons pokemonOnMove = usersPokemonService.findById(fightDTO.getPokemonOnMove().getId()); 
			fight.setPokemonOnMove(pokemonOnMove);
			fight.getPokemonOnMove().setFightHealt(setFightHealtBasedOnLevel(Constants.BORDER_FOR_ADDING_HEALT, 
					fight.getPokemonOnMove().getPokemon().getHp(), fight.getPokemonOnMove().getLevel()));
			fight.setCounterForPokemon(1);
		}
		else if(fight.getPokemonOnMove().getId() != fightDTO.getPokemonOnMove().getId()){ 
			//ako nije null znaci nije prvi put, sad me zanima da li se pokemon promenio
			UsersPokemons pokemonOnMove = usersPokemonService.findById(fightDTO.getPokemonOnMove().getId()); 
			fight.setPokemonOnMove(pokemonOnMove);
			fight.getPokemonOnMove().setFightHealt(setFightHealtBasedOnLevel(Constants.BORDER_FOR_ADDING_HEALT, 
					fight.getPokemonOnMove().getPokemon().getHp(), fight.getPokemonOnMove().getLevel()));
			fight.setCounterForPokemon(fight.getCounterForPokemon() + 1);
		}

		if(fight.getFightStateMove().equals(FightStateMove.POKEMON_ATTACKS)) { //pokemon napada		
			double damage = this.calculateDMGToDeal(fight.getPokemonOnMove(), 
					fight.getBoss(), FightStateMove.POKEMON_ATTACKS);
			fight.getBoss().setFightHealt(fight.getBoss().getFightHealt() - damage);
			fight.setFightStateMove(FightStateMove.BOSS_ATTACKS);
			
		}
		else { //boss napada
			double damage = this.calculateDMGToDeal(fight.getPokemonOnMove(), 
					fight.getBoss(), FightStateMove.BOSS_ATTACKS);
			fight.getPokemonOnMove().setFightHealt(fight.getPokemonOnMove().getFightHealt() - damage);
			fight.setFightStateMove(FightStateMove.POKEMON_ATTACKS);
		}
		
		//ukoliko je boss spao na 0 uhvacen je
		if(fight.getBoss().getFightHealt() <= 0) {
			fight.setCaught(true);
			usersPokemonService.saveCaughtPokemon(fight.getBoss(), fight.getPokemonOnMove().getUser());
			
			//deo za levelovanje
			UsersPokemons userPokemonToLvl = fight.getPokemonOnMove();
			double experience = userPokemonToLvl.getExperience();
			experience += Constants.EXPERIENCE_FOR_ADDING;
			
			if(experience >= Constants.EXPERIENCE_TO_LEVEL) {
				experience -= Constants.EXPERIENCE_TO_LEVEL;
				
				userPokemonToLvl.setLevel(userPokemonToLvl.getLevel() + 1);
			}
			
			userPokemonToLvl.setExperience(experience);
			usersPokemonService.save(userPokemonToLvl);
		}

		fight.setCounterForTurn(fight.getCounterForTurn() + 1);
		Fight savedFight = fightService.save(fight);
		
		System.out.println(new FightDTO(savedFight));
		return new ResponseEntity<>(new FightDTO(savedFight), HttpStatus.OK);

    }
    
    private double calculateDMGToDeal(UsersPokemons pokemon, Boss boss, FightStateMove fightStateMove) {
    	double level = 1;    //attacker
    	double power = 1;   //attacker
    	double attack = 1;   //attacker
    	double defense = 1;  //defender
    	double modifier = 1; //defender
    	double damage = 0;
    	double factorToDecrease = 1;
    	
    	if(fightStateMove.equals(FightStateMove.POKEMON_ATTACKS)) {
        	level = pokemon.getLevel();    //attacker
        	power = pokemon.getPokemon().getCp();   //attacker
        	attack = pokemon.getPokemon().getAtk();   //attacker
        	defense = boss.getPokemon().getHp();  //defender
        	modifier = 1.0*pokemon.getLevel() / boss.getLevel(); //defender
    	}
    	else {
        	level = boss.getLevel();    //attacker
        	power = boss.getPokemon().getCp();   //attacker
        	attack = boss.getPokemon().getAtk();   //attacker
        	defense = pokemon.getPokemon().getHp();  //defender
        	modifier = (1.0*boss.getLevel() / pokemon.getLevel()) / 5; //defender
    	}
    	
    	damage = (((2 * level / 5 + 2) * power * attack / defense ) /50 + 2) * modifier * factorToDecrease;
    	return Math.round(damage);
    }
    
    private double setFightHealtBasedOnLevel(int paramForBorderToAdd, double healt, int level) {
    	double retHealt = healt;
    	int multiplyConstant = level / paramForBorderToAdd;
    	
    	if(multiplyConstant > 0) {
    		retHealt *= multiplyConstant;
    	}
    	
    	return retHealt;
    }

}
