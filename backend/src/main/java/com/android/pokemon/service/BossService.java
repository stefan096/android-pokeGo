package com.android.pokemon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.android.pokemon.model.Boss;
import com.android.pokemon.repository.BossRepository;

@Service
public class BossService {
	
	@Autowired
	BossRepository bossRepository;
	
	public List<Boss> findAll(){
		return bossRepository.findAll();
	}
	
	public Boss findById(Long id){
		return bossRepository.findById(id).get();
	}
	
	public Boss save(Boss boss){
		return bossRepository.save(boss);
	}
	
	public void deleteAll() {
		List<Boss> bosses = bossRepository.findAll();
		bosses.forEach(boss -> {
			bossRepository.delete(boss);
		});
	}

}

