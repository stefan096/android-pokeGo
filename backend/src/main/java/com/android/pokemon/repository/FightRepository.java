package com.android.pokemon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.android.pokemon.model.Fight;

@Repository
public interface FightRepository extends JpaRepository<Fight, Long> {

}
