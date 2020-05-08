package com.android.pokemon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.android.pokemon.model.Fight;
import com.android.pokemon.repository.FightRepository;

@Service
public class FightService {
	
	@Autowired
	FightRepository fightRepository;
	
	public List<Fight> findAll(){
		return fightRepository.findAll();
	}
	
	public Fight findById(Long id){
		return fightRepository.findById(id).get();
	}
	
	public Fight save(Fight fight){
		return fightRepository.save(fight);
	}

	public void deleteAll() {
		List<Fight> fights = fightRepository.findAll();
		fights.forEach(fight -> {
			fightRepository.delete(fight);
		});
	}
	

}
