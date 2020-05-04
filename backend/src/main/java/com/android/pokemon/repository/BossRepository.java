package com.android.pokemon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.android.pokemon.model.Boss;

@Repository
public interface BossRepository extends JpaRepository<Boss, Long>{

}
